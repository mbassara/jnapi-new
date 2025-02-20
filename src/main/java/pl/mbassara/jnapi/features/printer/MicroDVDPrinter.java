package pl.mbassara.jnapi.features.printer;

import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.Subtitle;
import pl.mbassara.jnapi.model.Subtitles;

class MicroDVDPrinter implements SubtitlesPrinter {

    @Override
    public String print(Subtitles subtitles, OptionalFps fps) {
        var builder = new StringBuilder();
        subtitles.subtitles()
                .forEach(s -> append(builder, s, fps));
        return builder.toString();
    }

    private void append(StringBuilder builder, Subtitle subtitle, OptionalFps fps) {
        var frameBased = subtitle.asFrameBased(fps);

        builder.append("{")
                .append(frameBased.startFrame())
                .append("}{")
                .append(frameBased.endFrame())
                .append("}")
                .append(String.join("|", frameBased.lines()))
                .append("\n");
    }
}
