package pl.mbassara.jnapi.features.printer;

import pl.mbassara.jnapi.model.OptionalFps;
import pl.mbassara.jnapi.model.Subtitle;
import pl.mbassara.jnapi.model.Subtitles;

import static pl.mbassara.jnapi.common.TimestampUtils.printSubRipTimestamp;

class SubRipPrinter implements SubtitlesPrinter {

    @Override
    public String print(Subtitles subtitles, OptionalFps fps) {
        var subs = subtitles.subtitles();
        var builder = new StringBuilder();

        for (var i = 0; i < subs.size(); i++) {
            builder.append(i + 1).append("\n");
            append(builder, subs.get(i), fps);
        }
        return builder.toString();
    }

    private void append(StringBuilder builder, Subtitle subtitle, OptionalFps fps) {
        var timeBased = subtitle.asTimeBased(fps);

        builder.append(printSubRipTimestamp(timeBased.startTime()))
                .append(" --> ")
                .append(printSubRipTimestamp(timeBased.endTime()))
                .append("\n");

        timeBased.lines()
                .forEach(line -> builder.append(line).append("\n"));

        builder.append("\n");
    }
}
