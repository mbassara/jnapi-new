package pl.mbassara.jnapi.model;

import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SubtitlesBuilder {

    private final List<Subtitle> subtitles = new ArrayList<>();

    public static SubtitlesBuilder subtitles() {
        return new SubtitlesBuilder();
    }

    public SubtitlesBuilder withTimeBasedSubtitle(LocalTime startTime, LocalTime endTime, String... lines) {
        subtitles.add(new TimeBasedSubtitle(startTime, endTime, List.of(lines)));
        return this;
    }

    public SubtitlesBuilder withFrameBasedSubtitle(int startFrame, int endFrame, String... lines) {
        subtitles.add(new FrameBasedSubtitle(startFrame, endFrame, List.of(lines)));
        return this;
    }

    public Subtitles build() {
        return new Subtitles(subtitles);
    }
}