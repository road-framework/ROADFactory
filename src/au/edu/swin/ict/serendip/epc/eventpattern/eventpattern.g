header {
    package au.edu.swin.ict.serendip.epml.eventpattern;
}


class EventPatternParser extends Parser;
options {
    buildAST = true;   // uses CommonAST by default
}

orpattern:   andpattern (OR^ andpattern)* 
    ;

andpattern:   atom (AND^ atom)*
    ;

atom:   WORD
    ;
    
    class EventPatternLexer extends Lexer;

WS	:	(' '
	|	'\t'
	|	'\n'
	|	'\r')
		{ _ttype = Token.SKIP; }
	;

LPAREN:	'('
	;

RPAREN:	')'
	;

AND:	"AND"
	;

OR:	"OR"
	;

SEMI:	';'
	;

INT	:	('0'..'9')+
	;
	
WORD : ('a'..'z' | 'A'..'Z' |'0'..'9' |'.'|'['|']')+ 
     ;
