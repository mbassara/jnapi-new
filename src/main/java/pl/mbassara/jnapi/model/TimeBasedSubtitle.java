package pl.mbassara.jnapi.model;

import java.time.LocalTime;
import java.util.List;

import static pl.mbassara.jnapi.common.TimestampUtils.timestampToFrame;

public record TimeBasedSubtitle(LocalTime startTime,
                                LocalTime endTime,
                                List<String> lines) implements Subtitle {
    @Override
    public FrameBasedSubtitle asFrameBased(OptionalFps fps) {
        return new FrameBasedSubtitle(
                timestampToFrame(startTime, fps),
                timestampToFrame(endTime, fps),
                lines
        );
    }

    @Override
    public TimeBasedSubtitle asTimeBased(OptionalFps fps) {
        return this;
    }
}