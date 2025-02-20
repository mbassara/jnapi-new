grammar MicroDVD;

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
    : '{' n1=number '}' '{' n2=number '}' subtitleLines NEWLINE*
      { $sub = new FrameBasedSubtitle($n1.value, $n2.value, $subtitleLines.lines); }
    ;

number returns [int value]
    : NUMBER
      { $value = Integer.parseInt($NUMBER.text); }
    ;

subtitleLines returns [List<String> lines = new ArrayList()]
    : ( lineText '|'? { $lines.add($lineText.text); } )+
    ;

lineText
    : (TEXT_CHAR | NUMBER | '}' | '{')+
    ;

// Lexer rules
NUMBER          : DIGIT+;
NEWLINE         : [\r\n]+;
TEXT_CHAR       : ~[\r\n];

fragment DIGIT  : [0-9];
