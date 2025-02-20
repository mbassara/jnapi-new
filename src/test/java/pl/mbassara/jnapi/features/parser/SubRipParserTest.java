package pl.mbassara.jnapi.features.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.mbassara.jnapi.features.parser.AntlrBasedParserFactory;
import pl.mbassara.jnapi.features.parser.SubtitlesParser;
import pl.mbassara.jnapi.features.parser.SubtitlesParsingException;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.mbassara.jnapi.model.Format.SubRip;
import static pl.mbassara.jnapi.model.SubtitlesBuilder.subtitles;

class SubRipParserTest {

    private static final String INPUT = """
            1
            00:00:01,000 --> 00:00:04,000
            First subtitle - first line.
            
            2
            00:01:00,000 --> 00:01:04,000
            Second subtitle - first line.
            Second subtitle - second line.
            
            3
            01:02:03,123 --> 01:02:06,100
            Third subtitle - first line.
            """;

    private static final String INPUT_EMPTY_SUBTITLE = """
            1
            00:00:01,000 --> 00:00:04,000
            
            2
            00:01:00,000 --> 00:01:04,000
            Second subtitle.
            """;

    private static final String INPUT_MISSING_NUMBER = """
            1
            00:00:01,000 --> 00:00:04,000
            First subtitle.
            
            00:01:00,000 --> 00:01:04,000
            Second subtitle.
            """;

    private static final String INPUT_INCORRECT_TIMESTAMP = """
            1
            00:00:01,000 --> 00:00:04,000
            First subtitle.
            
            2
            00:01 --> 00:01:04,000
            Second subtitle.
            """;

    private final SubtitlesParser parser = new AntlrBasedParserFactory().create(SubRip);

    @Test
    void shouldParseSubRipInput() {
        // given
        var expectedResult = subtitles()
                .withTimeBasedSubtitle(
                        LocalTime.of(0, 0, 1),
                        LocalTime.of(0, 0, 4),
                        "First subtitle - first line."
                )
                .withTimeBasedSubtitle(
                        LocalTime.of(0, 1, 0),
                        LocalTime.of(0, 1, 4),
                        "Second subtitle - first line.",
                        "Second subtitle - second line."
                )
                .withTimeBasedSubtitle(
                        LocalTime.of(1, 2, 3, 123000000),
                        LocalTime.of(1, 2, 6, 100000000),
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
                .hasMessageContaining(SubRip.name());
    }

    private static Stream<Arguments> incorrectInput() {
        return Stream.of(
                Arguments.of(INPUT_EMPTY_SUBTITLE),
                Arguments.of(INPUT_MISSING_NUMBER),
                Arguments.of(INPUT_INCORRECT_TIMESTAMP),
                Arguments.of("")
        );
    }

}