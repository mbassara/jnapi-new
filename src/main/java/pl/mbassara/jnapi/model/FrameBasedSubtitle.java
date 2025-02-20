package pl.mbassara.jnapi.model;

import java.util.List;

import static pl.mbassara.jnapi.common.TimestampUtils.frameToTimestamp;

public record FrameBasedSubtitle(int startFrame,
                                 int endFrame,
                                 List<String> lines) implements Subtitle {
    @Override
    public FrameBasedSubtitle asFrameBased(OptionalFps fps) {
        return this;
    }

    @Override
    public TimeBasedSubtitle asTimeBased(OptionalFps fps) {
        return new TimeBasedSubtitle(
                frameToTimestamp(startFrame, fps),
                frameToTimestamp(endFrame, fps),
                lines
        );
    }
}