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
import static pl.mbassara.jnapi.model.Format.MPL2;
import static pl.mbassara.jnapi.model.SubtitlesBuilder.subtitles;

class MPL2ParserTest {

    private static final String INPUT = """
            [10][40]First subtitle - first line.
            [600][640]Second subtitle - first line.|Second subtitle - second line.
            [36615][36650]Third subtitle - first line.
            """;

    private static final String INPUT_EMPTY_SUBTITLE = """
            [1000][1020]
            [2000][2020]Second subtitle.
            """;

    private static final String INPUT_NEGATIVE_TIMESTAMP = """
            {1000}{-1}First subtitle.
            [2000][2020]Second subtitle.
            """;

    private static final String INPUT_INCORRECT_TIMESTAMP_FORMAT = """
            {1000}[1020]First subtitle.
            [2000][2020]Second subtitle.
            """;

    private final SubtitlesParser parser = new AntlrBasedParserFactory().create(MPL2);

    @Test
    void shouldParseMPL2Input() {
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
                        LocalTime.of(1, 1, 1, 500000000),
                        LocalTime.of(1, 1, 5),
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
                .hasMessageContaining(MPL2.name());
    }

    private static Stream<Arguments> incorrectInput() {
        return Stream.of(
                Arguments.of(INPUT_EMPTY_SUBTITLE),
                Arguments.of(INPUT_NEGATIVE_TIMESTAMP),
                Arguments.of(INPUT_INCORRECT_TIMESTAMP_FORMAT),
                Arguments.of("")
        );
    }

}