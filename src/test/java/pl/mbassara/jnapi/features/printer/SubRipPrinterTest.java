package pl.mbassara.jnapi.features.printer;

import org.junit.jupiter.api.Test;
import pl.mbassara.jnapi.model.OptionalFps;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.mbassara.jnapi.model.SubtitlesBuilder.subtitles;

class SubRipPrinterTest {

    private final SubtitlesPrinter printer = new SubRipPrinter();

    @Test
    void shouldPrint_timeBasedSubtitles_withoutFps() {
        // given
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
        var output = printer.print(subtitles, OptionalFps.empty());

        // then
        assertThat(output).isEqualTo("""
                1
                00:00:01,000 --> 00:00:02,000
                First subtitle.
                
                2
                00:01:00,000 --> 00:01:01,500
                Second subtitle - first line.
                Second subtitle - second line.
                
                """);
    }

    @Test
    void shouldPrint_frameBasedSubtitles_withFps() {
        // given
        var fps = 5.0;
        var subtitles = subtitles()
                .withFrameBasedSubtitle(
                        10, 20,
                        "First subtitle."
                )
                .withFrameBasedSubtitle(
                        30, 45,
                        "Second subtitle - first line.",
                        "Second subtitle - second line."
                )
                .build();

        // when
        var output = printer.print(subtitles, OptionalFps.of(fps));

        // then
        assertThat(output).isEqualTo("""
                1
                00:00:02,000 --> 00:00:04,000
                First subtitle.
                
                2
                00:00:06,000 --> 00:00:09,000
                Second subtitle - first line.
                Second subtitle - second line.
                
                """);
    }

    @Test
    void shouldThrowExceptionFor_frameBasedSubtitles_withoutFps() {
        // given
        var subtitles = subtitles()
                .withFrameBasedSubtitle(
                        10, 20,
                        "First subtitle."
                )
                .build();

        // when
        var exception = catchException(() -> printer.print(subtitles, OptionalFps.empty()));

        // then
        assertThat(exception)
                .hasMessage("FPS is required to convert frame-based subtitles to time-based");
    }
}