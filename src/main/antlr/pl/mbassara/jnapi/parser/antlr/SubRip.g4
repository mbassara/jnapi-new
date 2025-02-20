grammar SubRip;

@header {
    package pl.mbassara.jnapi.parser.antlr;

    import pl.mbassara.jnapi.model.*;
    import static pl.mbassara.jnapi.common.TimestampUtils.*;
}

// Parser rules
subtitles returns [Subtitles subs = new Subtitles()]
    : ( subtitle { $subs.add($subtitle.sub); } )+ EOF
    ;

subtitle returns [Subtitle sub]
    : NUMBER NEWLINE
      t1=TIMESTAMP ' --> ' t2=TIMESTAMP NEWLINE
      subtitleLines
      NEWLINE*
      { $sub = new TimeBasedSubtitle(parseSubRipTimestamp($t1.text), parseSubRipTimestamp($t2.text), $subtitleLines.lines); }
    ;

subtitleLines returns [List<String> lines = new ArrayList()]
    : ( lineText NEWLINE* { $lines.add($lineText.text); } )+
    ;

lineText
    : (TEXT_CHAR | NUMBER)+
    ;

// Lexer rules
TIMESTAMP       : DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ',' DIGIT DIGIT DIGIT;
NUMBER          : DIGIT+;
NEWLINE         : [\r\n]+;
TEXT_CHAR       : ~[\r\n];

fragment DIGIT  : [0-9];
