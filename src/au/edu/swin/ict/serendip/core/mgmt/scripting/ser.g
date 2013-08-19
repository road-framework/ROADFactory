/* ############################## L E X E R ############################ */
/**
 * Modified based on the original grammer by Alex Miller. 
 * Credit for his article at
 * http://tech.puredanger.com/2007/01/13/implementing-a-scripting-language-with-antlr-part-1-lexer/
 * @author Malinda
 * 
 */
	header {
   	 package au.edu.swin.ict.serendip.core.mgmt.scripting;
	}
class SerLexer extends Lexer;

	options { 
		k=1; 	// Only 1 lookahead character required
	}
	
	// Define string values - either simple unquoted or complex quoted
	STRING : ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|':')+ 
			| ('"' (~'"')* '"');
	
	// Ignore all whitespace
	WS     :
	    (' ' 
	    | '\t' 
	    | '\r' '\n' { newline(); } 
	    | '\n'      { newline(); }
	    ) 
	    { $setType(Token.SKIP); } ;
	
	// Single-line comment
	LINE_COMMENT : '#' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? 
		{ $setType(Token.SKIP); newline(); } ;
	
	// Punctuation     
	LBRACE : '{';
	RBRACE : '}';
	EQUALS : '=';
	SEMI : ';';


/* ############################## P A R S E R ############################ */

class SerParser extends Parser;

	options { 
		buildAST=true; 		// Automatically build the AST while parsing
		k=2; 				// Need lookahead of two for props without keys (to check for the =)
	}
	
	tokens {
		SCRIPT;				// Imaginary token inserted at the root of the script
		BLOCK;				// Imaginary token inserted at the root of a block
		COMMAND;			// Imaginary token inserted at the root of a command
		PROPERTY;			// Imaginary token inserted at the root of a property
	}
	
	/**
	 * Parse a script, which consists of 0 or more blocks.  The AST produced will contain an imaginary
	 * SCRIPT node at the root with child block ASTs.
	 */
	script : (block)*
		{#script = #([SCRIPT, "SCRIPT"], #script);};		// Magic to insert the imgainary SCRIPT node
	
	/**
	 * Parse a block, which consists of a block name then 0 or more commands in { }.  The AST produced
	 * will contain an imaginary BLOCK node at the root with children which are the block name followed 
	 * by an AST for each child command.  The { } are not included in the AST.
	 */
	block : (STRING LBRACE! (command)* RBRACE!)
		{#block = #([BLOCK, "BLOCK"], #block);};

	/**
	 * Parse a command, which consists of a command name, followed by 0 or more command properties and 
	 * terminated with a semicolon.  
	 */
	command : (STRING (property)* SEMI!)
		{#command = #([COMMAND, "COMMAND"], #command);};
	
	/**
	 * Parse a property definition, which consists either of an unkeyed value
	 * or as a key-value pair.  The AST produced will contain an imaginary PROPERTY token node at the root
	 * with either one child (for a non-keyed value) or two children if there is a key and a value.
	 * The = is not included in the AST.
	 */
	property : ( (STRING EQUALS!)? STRING)
		{#property = #([PROPERTY, "PROPERTY"], #property);};


/* ############################## T R E E  W A L K E R ############################ */

class SerWalker extends TreeParser;

{
	/**
	 * Helper method to remove quotes from a quoted string.
	 * @param value The value to remove quotes from
	 * @return The value with surrounding quotes (if any) removed
	 */
    private String removeQuotes(String value) {
        if(value != null && value.startsWith("\"")) {
            return value.substring(1, value.length()-1);
        }
        return value;
    }
}

	/**
	 * Look for SCRIPT node with child BLOCK ASTs and return a Script object
	 * @return Script object, never null
	 */
	script returns [Script script]
	{
		script = new Script();
	}
		:	#( SCRIPT (block[script] )* );
		
	/**
	 * Look for BLOCK node with child COMMAND ASTs and add any block found to the blocks list.
	 * @param blocks The blocks being collected
	 */
	block [Script script] 
	{
		Block block = null;
	}
		:	#( 	BLOCK
				 (
					b:STRING 
					{ 
						block = new Block(removeQuotes(b.getText())); 
					} 
					(command[block])*
				)
			)
			{ script.addBlock(block); }
		;
	
	/**
	 * Look for COMMAND node with child command name and properties and add the command
	 * info to the block.
	 * @param block The block being parsed
	 */
	command [Block block]
	{
		Command command = null;
	}
		:	#( 	COMMAND
				(
					c:STRING 
					{ 
						command = new Command(removeQuotes(c.getText())); 
						block.addCommand(command);
					}
					(property[command])*
				)
			);
	
	/** 
	 * Look for PROPERTY node with child (optional) key and value and add the property 
	 * to the current CommandInfo.
	 * @param command The current command
	 */
	property [Command command] 
		: 	#( PROPERTY
				(
					p1:STRING (p2:STRING)?
					{
						if(p2 == null) {
							command.addProperty(removeQuotes(p1.getText()));
						} else {
							command.addProperty(removeQuotes(p1.getText()), removeQuotes(p2.getText()));
						}
					}
				)
			);
	