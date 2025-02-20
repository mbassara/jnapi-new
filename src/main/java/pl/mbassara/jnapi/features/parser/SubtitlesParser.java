package pl.mbassara.jnapi.features.parser;

import pl.mbassara.jnapi.model.Subtitles;

import java.util.stream.Stream;

interface SubtitlesParser {

    Subtitles parse(String input);

    default Stream<Subtitles> tryParse(String input) {
        try {
            return Stream.ofNullable(parse(input));
        } catch (SubtitlesParsingException e) {
            return Stream.empty();
        }
    }
}
