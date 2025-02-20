package pl.mbassara.jnapi.application;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mbassara.jnapi.features.downloader.napiprojekt.NapiprojektApiMock;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.NapiprojektResultDtoBuilder;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.StatusDto;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.mbassara.jnapi.model.Format.*;

class SubtitlesServiceE2ETest extends BaseSpringBootTest {

    private static final SubtitlesLanguage SUBTITLES_LANGUAGE = SubtitlesLanguage.PL;
    private static final String MOVIE_FILE_NAME = "MOVIE_FILE_NAME";
    private static final String MOVIE_FILE_CONTENT = "MOVIE_FILE_CONTENT";
    private static final String SUBTITLES_ID = "12b9a6347c3359911c1d5de9b62694a9";
    private static final String TITLE = "TITLE";
    private static final OptionalFps FPS = OptionalFps.of(30.0);

    @Autowired
    private NapiprojektApiMock napiprojektApiMock;

    @Autowired
    private SubtitlesService subtitlesService;

    @ParameterizedTest
    @MethodSource("formatsProvider")
    void shouldDownloadSubtitles(Format sourceFormat, Format targetFormat) {
        // given
        stubNapiprojektResponse(sourceFormat);

        var movieFile = tempFile(MOVIE_FILE_NAME, MOVIE_FILE_CONTENT);

        // when
        subtitlesService.downloadSubtitles(movieFile, FPS, SUBTITLES_LANGUAGE, targetFormat, UTF_8);

        // then
        var expectedSubtitlesContent = readClasspathResource(expectedSubtitlesClasspathLocation(sourceFormat, targetFormat));
        var targetSubtitlesFile = targetSubtitlesFile(movieFile, targetFormat);

        assertThat(targetSubtitlesFile).hasContent(expectedSubtitlesContent);
    }

    static Stream<Arguments> formatsProvider() {
        return Stream.of(Format.values()).flatMap(
                fromFormat -> Stream.of(Format.values())
                        .map(toFormat -> Arguments.of(fromFormat, toFormat))
        );
    }

    @Test
    void shouldNotRequireFps_whenUsingTwoTimeBasedFormats() {
        // given
        var sourceFormat = SubRip;
        var targetFormat = MPL2;
        var fps = OptionalFps.empty();

        stubNapiprojektResponse(sourceFormat);

        var movieFile = tempFile(MOVIE_FILE_NAME, MOVIE_FILE_CONTENT);

        // when
        subtitlesService.downloadSubtitles(movieFile, fps, SUBTITLES_LANGUAGE, targetFormat, UTF_8);

        // then
        var expectedSubtitlesContent = readClasspathResource(expectedSubtitlesClasspathLocation(sourceFormat, targetFormat));
        var targetSubtitlesFile = targetSubtitlesFile(movieFile, targetFormat);

        assertThat(targetSubtitlesFile).hasContent(expectedSubtitlesContent);
    }

    @Test
    @SuppressWarnings("UnnecessaryLocalVariable")
    void shouldRequireFps_whenConvertingTimeBasedSubtitlesToFrameBased() {
        // given
        var sourceFormat = SubRip;
        var targetFormat = MicroDVD;
        var fps = OptionalFps.empty();

        stubNapiprojektResponse(sourceFormat);

        var movieFile = tempFile(MOVIE_FILE_NAME, MOVIE_FILE_CONTENT);

        // when
        var exception = catchException(
                () -> subtitlesService.downloadSubtitles(movieFile, fps, SUBTITLES_LANGUAGE, targetFormat, UTF_8)
        );

        // then
        assertThat(exception).hasMessageContaining("FPS is required");
    }

    private void stubNapiprojektResponse(Format sourceFormat) {
        var sourceSubtitlesContent = readClasspathResource(sourceSubtitlesClasspathLocation(sourceFormat));
        var responseDto = NapiprojektResultDtoBuilder
                .withTitle(TITLE)
                .withStatus(StatusDto.success)
                .withBase64Content(sourceSubtitlesContent);

        napiprojektApiMock.stubSubtitles(SUBTITLES_ID, SUBTITLES_LANGUAGE, responseDto);
    }

    private String sourceSubtitlesClasspathLocation(Format format) {
        return "subtitles/source/" + format + ".txt";
    }

    private String expectedSubtitlesClasspathLocation(Format sourceFormat, Format targetFormat) {
        return "subtitles/expected/" + sourceFormat + "_" + targetFormat + ".txt";
    }

    private Path targetSubtitlesFile(Path movieFile, Format targetFormat) {
        var baseName = FilenameUtils.getBaseName(movieFile.toFile().getName());
        var extension = switch (targetFormat) {
            case MicroDVD, MPL2 -> ".txt";
            case SubRip -> ".srt";
        };

        return movieFile.resolveSibling(baseName + extension);
    }

}
