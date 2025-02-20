package pl.mbassara.jnapi.model;

import java.util.ArrayList;
import java.util.List;

public record Subtitles(List<Subtitle> subtitles) {
    public Subtitles() {
        this(new ArrayList<>());
    }

    public void add(Subtitle subtitle) {
        this.subtitles.add(subtitle);
    }
}
