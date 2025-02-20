package pl.mbassara.jnapi.features.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.model.Subtitles;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GenericSubtitlesParser implements SubtitlesParser {

    private final AntlrBasedParserFactory parserFactory;

    @Override
    public Subtitles parse(String input) {
        return Stream.of(Format.values())
                .map(parserFactory::create)
                .flatMap(parser -> parser.tryParse(input))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot parse subtitles"));
    }
}
