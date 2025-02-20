package pl.mbassara.jnapi.model;

public sealed interface Subtitle permits FrameBasedSubtitle, TimeBasedSubtitle {

    FrameBasedSubtitle asFrameBased(OptionalFps fps);

    TimeBasedSubtitle asTimeBased(OptionalFps fps);
}