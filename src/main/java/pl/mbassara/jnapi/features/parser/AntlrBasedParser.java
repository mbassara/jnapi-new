package pl.mbassara.jnapi.features.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import pl.mbassara.jnapi.model.Subtitles;

import java.util.function.Function;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class AntlrBasedParser<L extends Lexer, P extends Parser> implements SubtitlesParser {

    private final Function<CharStream, L> lexerCreator;
    private final Function<TokenStream, P> parserCreator;
    private final Function<P, Subtitles> parsingMethod;

    @Override
    public Subtitles parse(String input) {
        var lexer = lexerCreator.apply(CharStreams.fromString(input));
        var parser = parserCreator.apply(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());

        return parse(parser);
    }

    private Subtitles parse(P parser) {
        log.info(message("Parsing subtitles", parser));

        try {
            var subtitles = parsingMethod.apply(parser);
            log.info(message("Successfully parsed subtitles", parser));
            return subtitles;
        } catch (RuntimeException e) {
            var msg = message("Failed to parse subtitles", parser);
            log.info(msg);
            throw new SubtitlesParsingException(msg, e);
        }
    }

    private String message(String message, P parser) {
        return format("%s with %s", message, parser.getClass().getSimpleName());
    }
}
