// $ANTLR : "ser.g" -> "SerWalker.java"$

package au.edu.swin.ict.serendip.core.mgmt.scripting;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class SerWalker extends antlr.TreeParser implements SerLexerTokenTypes {

    /**
     * Helper method to remove quotes from a quoted string.
     * 
     * @param value
     *            The value to remove quotes from
     * @return The value with surrounding quotes (if any) removed
     */
    private String removeQuotes(String value) {
	if (value != null && value.startsWith("\"")) {
	    return value.substring(1, value.length() - 1);
	}
	return value;
    }

    public SerWalker() {
	tokenNames = _tokenNames;
    }

    /**
     * Look for SCRIPT node with child BLOCK ASTs and return a Script object
     * 
     * @return Script object, never null
     */
    public final Script script(AST _t) throws RecognitionException {
	Script script;

	AST script_AST_in = (_t == ASTNULL) ? null : (AST) _t;

	script = new Script();

	try { // for error handling
	    AST __t196 = _t;
	    AST tmp1_AST_in = (AST) _t;
	    match(_t, SCRIPT);
	    _t = _t.getFirstChild();
	    {
		_loop198: do {
		    if (_t == null)
			_t = ASTNULL;
		    if ((_t.getType() == BLOCK)) {
			block(_t, script);
			_t = _retTree;
		    } else {
			break _loop198;
		    }

		} while (true);
	    }
	    _t = __t196;
	    _t = _t.getNextSibling();
	} catch (RecognitionException ex) {
	    reportError(ex);
	    if (_t != null) {
		_t = _t.getNextSibling();
	    }
	}
	_retTree = _t;
	return script;
    }

    /**
     * Look for BLOCK node with child COMMAND ASTs and add any block found to
     * the blocks list.
     * 
     * @param blocks
     *            The blocks being collected
     */
    public final void block(AST _t, Script script) throws RecognitionException {

	AST block_AST_in = (_t == ASTNULL) ? null : (AST) _t;
	AST b = null;

	Block block = null;

	try { // for error handling
	    AST __t200 = _t;
	    AST tmp2_AST_in = (AST) _t;
	    match(_t, BLOCK);
	    _t = _t.getFirstChild();
	    {
		b = (AST) _t;
		match(_t, STRING);
		_t = _t.getNextSibling();

		block = new Block(removeQuotes(b.getText()));

		{
		    _loop203: do {
			if (_t == null)
			    _t = ASTNULL;
			if ((_t.getType() == COMMAND)) {
			    command(_t, block);
			    _t = _retTree;
			} else {
			    break _loop203;
			}

		    } while (true);
		}
	    }
	    _t = __t200;
	    _t = _t.getNextSibling();
	    script.addBlock(block);
	} catch (RecognitionException ex) {
	    reportError(ex);
	    if (_t != null) {
		_t = _t.getNextSibling();
	    }
	}
	_retTree = _t;
    }

    /**
     * Look for COMMAND node with child command name and properties and add the
     * command info to the block.
     * 
     * @param block
     *            The block being parsed
     */
    public final void command(AST _t, Block block) throws RecognitionException {

	AST command_AST_in = (_t == ASTNULL) ? null : (AST) _t;
	AST c = null;

	Command command = null;

	try { // for error handling
	    AST __t205 = _t;
	    AST tmp3_AST_in = (AST) _t;
	    match(_t, COMMAND);
	    _t = _t.getFirstChild();
	    {
		c = (AST) _t;
		match(_t, STRING);
		_t = _t.getNextSibling();

		command = new Command(removeQuotes(c.getText()));
		block.addCommand(command);

		{
		    _loop208: do {
			if (_t == null)
			    _t = ASTNULL;
			if ((_t.getType() == PROPERTY)) {
			    property(_t, command);
			    _t = _retTree;
			} else {
			    break _loop208;
			}

		    } while (true);
		}
	    }
	    _t = __t205;
	    _t = _t.getNextSibling();
	} catch (RecognitionException ex) {
	    reportError(ex);
	    if (_t != null) {
		_t = _t.getNextSibling();
	    }
	}
	_retTree = _t;
    }

    /**
     * Look for PROPERTY node with child (optional) key and value and add the
     * property to the current CommandInfo.
     * 
     * @param command
     *            The current command
     */
    public final void property(AST _t, Command command)
	    throws RecognitionException {

	AST property_AST_in = (_t == ASTNULL) ? null : (AST) _t;
	AST p1 = null;
	AST p2 = null;

	try { // for error handling
	    AST __t210 = _t;
	    AST tmp4_AST_in = (AST) _t;
	    match(_t, PROPERTY);
	    _t = _t.getFirstChild();
	    {
		p1 = (AST) _t;
		match(_t, STRING);
		_t = _t.getNextSibling();
		{
		    if (_t == null)
			_t = ASTNULL;
		    switch (_t.getType()) {
		    case STRING: {
			p2 = (AST) _t;
			match(_t, STRING);
			_t = _t.getNextSibling();
			break;
		    }
		    case 3: {
			break;
		    }
		    default: {
			throw new NoViableAltException(_t);
		    }
		    }
		}

		if (p2 == null) {
		    command.addProperty(removeQuotes(p1.getText()));
		} else {
		    command.addProperty(removeQuotes(p1.getText()),
			    removeQuotes(p2.getText()));
		}

	    }
	    _t = __t210;
	    _t = _t.getNextSibling();
	} catch (RecognitionException ex) {
	    reportError(ex);
	    if (_t != null) {
		_t = _t.getNextSibling();
	    }
	}
	_retTree = _t;
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>",
	    "NULL_TREE_LOOKAHEAD", "STRING", "WS", "LINE_COMMENT", "LBRACE",
	    "RBRACE", "EQUALS", "SEMI", "SCRIPT", "BLOCK", "COMMAND",
	    "PROPERTY" };

}
