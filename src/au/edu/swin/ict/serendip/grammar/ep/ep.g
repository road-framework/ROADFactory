header {
    package au.edu.swin.ict.serendip.grammar.ep;
}


class EventPatternParser extends Parser;
options {
    buildAST = true;   // uses CommonAST by default
}

expression :    xorpattern  
	; 

xorpattern : orpattern (XOR^ orpattern)*
	;
 
orpattern:   andpattern (OR^ andpattern)* 
    ; 
     
andpattern:   atom (AND^ atom)*
    ;
    
atom:      (LPAREN! expression RPAREN!) | WORD
    ;

    

class EventPatternLexer extends Lexer;

WS	:	(' '
	|	'\t'
	|	'\n'
	|	'\r')
		{ _ttype = Token.SKIP; }
	;

LPAREN:	'('//{ _ttype = Token.SKIP; }
	;

RPAREN:	')'//{ _ttype = Token.SKIP; }
	;

AND:	"*"
	;

OR:	"|" 
	;
	
XOR: "^"
	;
	
SEMI:	';'
	;
	
DIGITX: '0'|'1'
	;
	
WORD : ('a'..'z' | 'A'..'Z' |'0'..'9' |'.'|'['|']')+ 
     ;
