package pl.mbassara.jnapi.features.parser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mbassara.jnapi.application.BaseSpringBootTest;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.model.FrameBasedSubtitle;
import pl.mbassara.jnapi.model.TimeBasedSubtitle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.mbassara.jnapi.model.Format.*;

class GenericSubtitlesParserIT extends BaseSpringBootTest {

    private static final int EXPECTED_NUMBER_OF_SUBTITLES = 23;
    private static final int EXPECTED_NUMBER_OF_SUBTITLE_LINES = 25;

    @Autowired
    private GenericSubtitlesParser parser;

    @Test
    void shouldParseMicroDVD() {
        // given
        var microDvdInput = readClasspathResource(sourceSubtitlesClasspathLocation(MicroDVD));

        // when
        var subtitles = parser.parse(microDvdInput);

        // then
        assertThat(subtitles.subtitles())
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLES)
                .allMatch(FrameBasedSubtitle.class::isInstance)
                .map(FrameBasedSubtitle.class::cast)
                .flatMap(FrameBasedSubtitle::lines)
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLE_LINES);
    }

    @Test
    void shouldParseMPL2() {
        // given
        var mpl2Input = readClasspathResource(sourceSubtitlesClasspathLocation(MPL2));

        // when
        var subtitles = parser.parse(mpl2Input);

        // then
        assertThat(subtitles.subtitles())
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLES)
                .allMatch(TimeBasedSubtitle.class::isInstance)
                .map(TimeBasedSubtitle.class::cast)
                .flatMap(TimeBasedSubtitle::lines)
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLE_LINES);
    }

    @Test
    void shouldParseSubRip() {
        // given
        var subRipInput = readClasspathResource(sourceSubtitlesClasspathLocation(SubRip));

        // when
        var subtitles = parser.parse(subRipInput);

        // then
        assertThat(subtitles.subtitles())
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLES)
                .allMatch(TimeBasedSubtitle.class::isInstance)
                .map(TimeBasedSubtitle.class::cast)
                .flatMap(TimeBasedSubtitle::lines)
                .hasSize(EXPECTED_NUMBER_OF_SUBTITLE_LINES);
    }

    @Test
    void shouldThrowExceptionOnIncorrectInput() {
        // when
        var exception = catchException(() -> parser.parse("incorrect"));

        // then
        assertThat(exception)
                .hasMessage("Cannot parse subtitles");
    }

    private String sourceSubtitlesClasspathLocation(Format format) {
        return "subtitles/source/" + format + ".txt";
    }

}