package pl.mbassara.jnapi.model;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class OptionalFps {

    private static final OptionalFps EMPTY = new OptionalFps(null);

    private final Double fps;

    public static OptionalFps of(Double fps) {
        return new OptionalFps(fps);
    }

    public static OptionalFps ofNullable(String fps) {
        if (fps == null) {
            return empty();
        }
        return of(Double.parseDouble(fps));
    }

    public static OptionalFps empty() {
        return EMPTY;
    }

    public Optional<Double> get() {
        return Optional.ofNullable(fps);
    }
}
