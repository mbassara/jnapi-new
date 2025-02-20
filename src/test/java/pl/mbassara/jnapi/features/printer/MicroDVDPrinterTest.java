package pl.mbassara.jnapi.features.printer;

import org.junit.jupiter.api.Test;
import pl.mbassara.jnapi.model.OptionalFps;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.mbassara.jnapi.model.SubtitlesBuilder.subtitles;

class MicroDVDPrinterTest {

    private final SubtitlesPrinter printer = new MicroDVDPrinter();

    @Test
    void shouldPrint_frameBasedSubtitles_withoutFps() {
        // given
        var subtitles = subtitles()
                .withFrameBasedSubtitle(
                        1, 2,
                        "First subtitle."
                )
                .withFrameBasedSubtitle(
                        3, 4,
                        "Second subtitle - first line.",
                        "Second subtitle - second line."
                )
                .build();

        // when
        var output = printer.print(subtitles, OptionalFps.empty());

        // then
        assertThat(output).isEqualTo("""
                {1}{2}First subtitle.
                {3}{4}Second subtitle - first line.|Second subtitle - second line.
                """);
    }

    @Test
    void shouldPrint_timeBasedSubtitles_withFps() {
        // given
        var fps = 10.0;

        var subtitles = subtitles()
                .withTimeBasedSubtitle(
                        LocalTime.of(0, 0, 1),
                        LocalTime.of(0, 0, 2),
                        "First subtitle."
                )
                .withTimeBasedSubtitle(
                        LocalTime.of(0, 1, 0),
                        LocalTime.of(0, 1, 1, 500000000),
                        "Second subtitle - first line.",
                        "Second subtitle - second line."
                )
                .build();

        // when
        var output = printer.print(subtitles, OptionalFps.of(fps));

        // then
        assertThat(output).isEqualTo("""
                {10}{20}First subtitle.
                {600}{615}Second subtitle - first line.|Second subtitle - second line.
                """);
    }

    @Test
    void shouldThrowExceptionFor_timeBasedSubtitles_withoutFps() {
        // given
        var subtitles = subtitles()
                .withTimeBasedSubtitle(
                        LocalTime.of(0, 0, 1),
                        LocalTime.of(0, 0, 2),
                        "First subtitle."
                )
                .build();

        // when
        var exception = catchException(() -> printer.print(subtitles, OptionalFps.empty()));

        // then
        assertThat(exception)
                .hasMessage("FPS is required to convert time-based subtitles to frame-based");
    }
}