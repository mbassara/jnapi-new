package pl.mbassara.jnapi.features.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.mbassara.jnapi.features.parser.AntlrBasedParserFactory;
import pl.mbassara.jnapi.features.parser.SubtitlesParser;
import pl.mbassara.jnapi.features.parser.SubtitlesParsingException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.mbassara.jnapi.model.Format.MicroDVD;
import static pl.mbassara.jnapi.model.SubtitlesBuilder.subtitles;

class MicroDVDParserTest {

    private static final String INPUT = """
            {1}{2}First subtitle - first line.
            {100}{123}Second subtitle - first line.|Second subtitle - second line.
            {1000}{2000}Third subtitle - first line.
            """;

    private static final String INPUT_EMPTY_SUBTITLE = """
            {1}{2}
            {100}{123}Second subtitle.
            """;

    private static final String INPUT_INCORRECT_FRAME_NUMBER = """
            {1}{-2}First subtitle.
            {100}{123}Second subtitle.
            """;

    private static final String INPUT_INCORRECT_FRAME_FORMAT = """
            {1}[2]First subtitle.
            {100}{123}Second subtitle.
            """;

    private final SubtitlesParser parser = new AntlrBasedParserFactory().create(MicroDVD);

    @Test
    void shouldParseMicroDVDInput() {
        // given
        var expectedResult = subtitles()
                .withFrameBasedSubtitle(
                        1, 2,
                        "First subtitle - first line."
                )
                .withFrameBasedSubtitle(
                        100, 123,
                        "Second subtitle - first line.",
                        "Second subtitle - second line."
                )
                .withFrameBasedSubtitle(
                        1000, 2000,
                        "Third subtitle - first line."
                )
                .build();

        // when
        var result = parser.parse(INPUT);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("incorrectInput")
    void shouldFailOnIncorrectInput(String input) {
        // when
        var exception = catchThrowable(() -> parser.parse(input));

        // then
        assertThat(exception)
                .isInstanceOf(SubtitlesParsingException.class)
                .hasMessageContaining(MicroDVD.name());
    }

    private static Stream<Arguments> incorrectInput() {
        return Stream.of(
                Arguments.of(INPUT_EMPTY_SUBTITLE),
                Arguments.of(INPUT_INCORRECT_FRAME_NUMBER),
                Arguments.of(INPUT_INCORRECT_FRAME_FORMAT),
                Arguments.of("")
        );
    }

}