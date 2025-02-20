package pl.mbassara.jnapi.features.printer;

import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.Subtitle;
import pl.mbassara.jnapi.model.Subtitles;

import static pl.mbassara.jnapi.common.TimestampUtils.printMPL2Timestamp;

class MPL2Printer implements SubtitlesPrinter {

    @Override
    public String print(Subtitles subtitles, OptionalFps fps) {
        var builder = new StringBuilder();
        subtitles.subtitles()
                .forEach(s -> append(builder, s, fps));
        return builder.toString();
    }

    private void append(StringBuilder builder, Subtitle subtitle, OptionalFps fps) {
        var timeBased = subtitle.asTimeBased(fps);

        builder.append("[")
                .append(printMPL2Timestamp(timeBased.startTime()))
                .append("][")
                .append(printMPL2Timestamp(timeBased.endTime()))
                .append("]")
                .append(String.join("|", timeBased.lines()))
                .append("\n");
    }
}
