package pl.mbassara.jnapi.features.parser;

import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.model.Format;
import pl.mbassara.jnapi.parser.antlr.*;

@Component
class AntlrBasedParserFactory {

    SubtitlesParser create(Format format) {
        return switch (format) {
            case MicroDVD -> new AntlrBasedParser<>(MicroDVDLexer::new, MicroDVDParser::new, p -> p.subtitles().subs);
            case MPL2 -> new AntlrBasedParser<>(MPL2Lexer::new, MPL2Parser::new, p -> p.subtitles().subs);
            case SubRip -> new AntlrBasedParser<>(SubRipLexer::new, SubRipParser::new, p -> p.subtitles().subs);
        };
    }
}
