// $ANTLR : "ser.g" -> "SerParser.java"$

package au.edu.swin.ict.serendip.core.mgmt.scripting;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class SerParser extends antlr.LLkParser implements SerLexerTokenTypes {

    protected SerParser(TokenBuffer tokenBuf, int k) {
	super(tokenBuf, k);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public SerParser(TokenBuffer tokenBuf) {
	this(tokenBuf, 2);
    }

    protected SerParser(TokenStream lexer, int k) {
	super(lexer, k);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public SerParser(TokenStream lexer) {
	this(lexer, 2);
    }

    public SerParser(ParserSharedInputState state) {
	super(state, 2);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    /**
     * Parse a script, which consists of 0 or more blocks. The AST produced will
     * contain an imaginary SCRIPT node at the root with child block ASTs.
     */
    public final void script() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST script_AST = null;

	try { // for error handling
	    {
		_loop183: do {
		    if ((LA(1) == STRING)) {
			block();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop183;
		    }

		} while (true);
	    }
	    script_AST = (AST) currentAST.root;
	    script_AST = (AST) astFactory.make((new ASTArray(2)).add(
		    astFactory.create(SCRIPT, "SCRIPT")).add(script_AST));
	    currentAST.root = script_AST;
	    currentAST.child = script_AST != null
		    && script_AST.getFirstChild() != null ? script_AST
		    .getFirstChild() : script_AST;
	    currentAST.advanceChildToEnd();
	    script_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_0);
	}
	returnAST = script_AST;
    }

    /**
     * Parse a block, which consists of a block name then 0 or more commands in
     * { }. The AST produced will contain an imaginary BLOCK node at the root
     * with children which are the block name followed by an AST for each child
     * command. The { } are not included in the AST.
     */
    public final void block() throws RecognitionException, TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST block_AST = null;

	try { // for error handling
	    {
		AST tmp5_AST = null;
		tmp5_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp5_AST);
		match(STRING);
		match(LBRACE);
		{
		    _loop187: do {
			if ((LA(1) == STRING)) {
			    command();
			    astFactory.addASTChild(currentAST, returnAST);
			} else {
			    break _loop187;
			}

		    } while (true);
		}
		match(RBRACE);
	    }
	    block_AST = (AST) currentAST.root;
	    block_AST = (AST) astFactory.make((new ASTArray(2)).add(
		    astFactory.create(BLOCK, "BLOCK")).add(block_AST));
	    currentAST.root = block_AST;
	    currentAST.child = block_AST != null
		    && block_AST.getFirstChild() != null ? block_AST
		    .getFirstChild() : block_AST;
	    currentAST.advanceChildToEnd();
	    block_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_1);
	}
	returnAST = block_AST;
    }

    /**
     * Parse a command, which consists of a command name, followed by 0 or more
     * command properties and terminated with a semicolon.
     */
    public final void command() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST command_AST = null;

	try { // for error handling
	    {
		AST tmp8_AST = null;
		tmp8_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp8_AST);
		match(STRING);
		{
		    _loop191: do {
			if ((LA(1) == STRING)) {
			    property();
			    astFactory.addASTChild(currentAST, returnAST);
			} else {
			    break _loop191;
			}

		    } while (true);
		}
		match(SEMI);
	    }
	    command_AST = (AST) currentAST.root;
	    command_AST = (AST) astFactory.make((new ASTArray(2)).add(
		    astFactory.create(COMMAND, "COMMAND")).add(command_AST));
	    currentAST.root = command_AST;
	    currentAST.child = command_AST != null
		    && command_AST.getFirstChild() != null ? command_AST
		    .getFirstChild() : command_AST;
	    currentAST.advanceChildToEnd();
	    command_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_2);
	}
	returnAST = command_AST;
    }

    /**
     * Parse a property definition, which consists either of an unkeyed value or
     * as a key-value pair. The AST produced will contain an imaginary PROPERTY
     * token node at the root with either one child (for a non-keyed value) or
     * two children if there is a key and a value. The = is not included in the
     * AST.
     */
    public final void property() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST property_AST = null;

	try { // for error handling
	    {
		{
		    if ((LA(1) == STRING) && (LA(2) == EQUALS)) {
			AST tmp10_AST = null;
			tmp10_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp10_AST);
			match(STRING);
			match(EQUALS);
		    } else if ((LA(1) == STRING)
			    && (LA(2) == STRING || LA(2) == SEMI)) {
		    } else {
			throw new NoViableAltException(LT(1), getFilename());
		    }

		}
		AST tmp12_AST = null;
		tmp12_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp12_AST);
		match(STRING);
	    }
	    property_AST = (AST) currentAST.root;
	    property_AST = (AST) astFactory.make((new ASTArray(2)).add(
		    astFactory.create(PROPERTY, "PROPERTY")).add(property_AST));
	    currentAST.root = property_AST;
	    currentAST.child = property_AST != null
		    && property_AST.getFirstChild() != null ? property_AST
		    .getFirstChild() : property_AST;
	    currentAST.advanceChildToEnd();
	    property_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_3);
	}
	returnAST = property_AST;
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>",
	    "NULL_TREE_LOOKAHEAD", "STRING", "WS", "LINE_COMMENT", "LBRACE",
	    "RBRACE", "EQUALS", "SEMI", "SCRIPT", "BLOCK", "COMMAND",
	    "PROPERTY" };

    protected void buildTokenTypeASTClassMap() {
	tokenTypeToASTClassMap = null;
    };

    private static final long[] mk_tokenSet_0() {
	long[] data = { 2L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
	long[] data = { 18L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
	long[] data = { 272L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
	long[] data = { 1040L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

}
