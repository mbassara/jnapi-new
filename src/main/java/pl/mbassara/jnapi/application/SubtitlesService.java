package pl.mbassara.jnapi.application;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.common.CharsetDetector;
import pl.mbassara.jnapi.features.downloader.SubtitlesDownloader;
import pl.mbassara.jnapi.features.parser.GenericSubtitlesParser;
import pl.mbassara.jnapi.features.printer.SubtitlesPrinterFactory;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
class SubtitlesService {

    private final SubtitlesDownloader subtitlesDownloader;
    private final CharsetDetector charsetDetector;
    private final GenericSubtitlesParser parser;
    private final SubtitlesPrinterFactory printerFactory;

    @SneakyThrows
    void downloadSubtitles(Path movieFile, OptionalFps fps, SubtitlesLanguage language, Format targetFormat, Charset targetCharset) {
        var rawSubtitles = subtitlesDownloader.downloadSubtitles(movieFile, language);
        var targetPath = resolveTargetPath(movieFile, targetFormat);

        var text = charsetDetector.readWithDetectedCharset(rawSubtitles.subtitles());
        var parsedSubtitles = parser.parse(text);

        log.info("Printing subtitles in {} format", targetFormat);
        var printer = printerFactory.create(targetFormat);
        var formattedSubtitles = printer.print(parsedSubtitles, fps);

        Files.writeString(targetPath, formattedSubtitles, targetCharset);
        log.info("Subtitles saved to {}", targetPath);
    }

    private Path resolveTargetPath(Path movieFile, Format format) {
        var baseName = FilenameUtils.getBaseName(movieFile.toAbsolutePath().toString());
        var extension = getFileExtension(format);

        return movieFile.resolveSibling(baseName + extension);
    }

    private String getFileExtension(Format format) {
        return switch (format) {
            case MicroDVD, MPL2 -> ".txt";
            case SubRip -> ".srt";
        };
    }
}
