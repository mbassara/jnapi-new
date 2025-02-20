package pl.mbassara.jnapi.features.printer;

import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.Subtitles;

public interface SubtitlesPrinter {
    String print(Subtitles subtitles, OptionalFps fps);
}
