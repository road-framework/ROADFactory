grammar behav;

options {
  language=Java;
  output= AST;
}

tokens{
  SCRIPT = '_Script';
  BEHAVIOR = '_Behavior';
  BTNAME = '_btName' ;
  EXTENDS = '_extends' ;
  TASK = '_Task' ;
  TASKNAME = '_taskName';
  PRE ='_pre' ;
  POST ='_post';
  PP ='_pp' ;
  ROLE ='_role' ;
  
}

@header {
  package au.edu.swin.ict.serendip.grammar.behav;
}

@lexer::header {
  package au.edu.swin.ict.serendip.grammar.behav; 
}

 
script: ((bterm)+)  EOF
  -> ^('_Script' (bterm)+ )
  ;
bterm:  btId (btExtends)?  '{'   (task)+   '}' ';'
  -> ^('_Behavior' btId (btExtends)? (task)+)
  ;
  
btId : 'Behavior' btName
  -> ^('_btName' btName)
  ;
btExtends : 'extends' extendBtId 
  -> ^('_extends' extendBtId)
  ;
 
task: taskId   '{' pre post  pp? role '}'  ';'   
  -> ^('_Task' taskId   pre post pp role    ) 
  ; 
taskId :  'Task' taskName 
  -> ^('_taskName' taskName)
  ;
//taskBody:  '{' pre post  pp role '}'  ';'   
//  ;
pre: 'pre'   ePattern  ';' 
  -> ^('_pre' ePattern )
  ;
post: 'post'   ePattern   ';' 
  -> ^('_post' ePattern )
  ;
pp:  'pp' ppVal  ';' 
  -> ^('_pp' ppVal )
  ;
role: 'role'   rObligVal  ';' 
  -> ^('_role' rObligVal )
  ;

ePattern : STRING ;

btName: WORD ; 
extendBtId : WORD ;
taskName : WORD ;

eppreVal: WORD ;
eppostVal: WORD ;
ppVal: WORD ;
rObligVal: WORD ;
 

LPAREN:  '('   ;

RPAREN: ')'    ;

AND:  '*'  ;

OR: '|'   ;
  
XOR: '^'   ;


WORD : ('a'..'z' | 'A'..'Z' |'0'..'9' |'.' )+ 
     ;
 
 // Define string values - either simple unquoted or complex quoted
STRING : ('a'..'z'|'A'..'Z'|'0'..'9'|'_' | LPAREN| RPAREN | AND | OR | XOR)+ 
      | ('"' (~'"')* '"')
      ;


COMMENT : '/*' .* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT  : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;

LBRACK: '{' 
  ;

RBRACK: '}'  
  ;


SEMI: ';'
  ;

//NEWLINE:'\r'? '\n' ;

WS: ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+   { $channel = HIDDEN; } ;