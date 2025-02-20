package pl.mbassara.jnapi.common;

import lombok.experimental.UtilityClass;
import pl.mbassara.jnapi.model.OptionalFps;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimestampUtils {

    private static final DateTimeFormatter SUB_RIP_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    public static LocalTime parseSubRipTimestamp(String timestamp) {
        return LocalTime.parse(timestamp, SUB_RIP_TIMESTAMP_FORMAT);
    }

    public static String printSubRipTimestamp(LocalTime timestamp) {
        return timestamp.format(SUB_RIP_TIMESTAMP_FORMAT);
    }

    public static LocalTime parseMPL2Timestamp(long tenthsOfSecond) {
        return LocalTime.ofNanoOfDay(tenthsOfSecond * 100 * 1000 * 1000);
    }

    public static String printMPL2Timestamp(LocalTime timestamp) {
        return Long.toString(timestamp.toNanoOfDay() / 100 / 1000 / 1000);
    }

    public static LocalTime frameToTimestamp(int frame, OptionalFps optionalFps) {
        double fps = optionalFps.get()
                .orElseThrow(() -> new IllegalStateException("FPS is required to convert frame-based subtitles to time-based"));

        double seconds = frame / fps;
        long nanos = (long) (seconds * 1000 * 1000 * 1000);

        return LocalTime.ofNanoOfDay(nanos);
    }

    public static int timestampToFrame(LocalTime timestamp, OptionalFps optionalFps) {
        double fps = optionalFps.get()
                .orElseThrow(() -> new IllegalStateException("FPS is required to convert time-based subtitles to frame-based"));

        long nanos = timestamp.toNanoOfDay();
        double seconds = nanos / 1000. / 1000. / 1000.;

        return (int) Math.floor(seconds * fps);
    }
}
