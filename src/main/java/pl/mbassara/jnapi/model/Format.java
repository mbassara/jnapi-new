package pl.mbassara.jnapi.model;

import java.util.stream.Stream;

public enum Format {
    MicroDVD, MPL2, SubRip;

    public static Format valueOfIgnoreCase(String value) {
        return Stream.of(values())
                .filter(f -> f.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown format: " + value));
    }
}
