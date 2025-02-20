package pl.mbassara.jnapi.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static pl.mbassara.jnapi.model.Format.SubRip;
import static pl.mbassara.jnapi.model.SubtitlesLanguage.PL;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
@Command(name = "jnapi", mixinStandardHelpOptions = true)
class ApplicationRunner implements CommandLineRunner, Runnable {

    @Option(names = {"-p", "--path"}, description = "Movie path", required = true)
    private String path;

    @Option(names = {"-f", "--fps"}, description = "Frames per second (you can use MediaInfo to get fps of a movie file). If subtitles format conversion will not be required you can skip the fps parameter.")
    private String fps;

    @Option(names = {"-l", "--language"}, description = "Language od the subtitles to download. Possible values: PL (default), EN.")
    private String language;

    @Option(names = {"--format"}, description = "Target format of the subtitles. Possible values: MicroDVD, MPL2, SubRip (default).")
    private String format;

    @Option(names = {"-c", "--charset"}, description = "Target charset of the subtitles (default UTF-8)")
    private String charset;

    private final SubtitlesService converter;

    @Override
    public void run() {
        try {
            var path = Path.of(this.path);
            var fps = OptionalFps.ofNullable(this.fps);
            var language = getOrElse(this.language, SubtitlesLanguage::valueOf, PL);
            var format = getOrElse(this.format, Format::valueOfIgnoreCase, SubRip);
            var charset = getOrElse(this.charset, Charset::forName, UTF_8);

            converter.downloadSubtitles(path, fps, language, format, charset);
        } catch (Exception e) {
            logError(e);
        }
    }

    @Override
    public void run(String... args) {
        new CommandLine(this).execute(args);
    }

    private <T> T getOrElse(String nullable, Function<String, T> mapper, T defaultValue) {
        return Optional.ofNullable(nullable).map(mapper).orElse(defaultValue);
    }

    private void logError(Exception e) {
        var message = format("Cannot download subtitles. %s: %s", e.getClass().getSimpleName(), e.getMessage());
        log.error(message);
        log.debug(message, e);
    }
}
