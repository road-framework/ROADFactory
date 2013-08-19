// $ANTLR : "eventpattern.g" -> "EventPatternParser.java"$

package au.edu.swin.ict.serendip.epc.eventpattern;

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

public class EventPatternParser extends antlr.LLkParser implements
	EventPatternParserTokenTypes {

    protected EventPatternParser(TokenBuffer tokenBuf, int k) {
	super(tokenBuf, k);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public EventPatternParser(TokenBuffer tokenBuf) {
	this(tokenBuf, 1);
    }

    protected EventPatternParser(TokenStream lexer, int k) {
	super(lexer, k);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public EventPatternParser(TokenStream lexer) {
	this(lexer, 1);
    }

    public EventPatternParser(ParserSharedInputState state) {
	super(state, 1);
	tokenNames = _tokenNames;
	buildTokenTypeASTClassMap();
	astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public final void orpattern() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST orpattern_AST = null;

	try { // for error handling
	    andpattern();
	    astFactory.addASTChild(currentAST, returnAST);
	    {
		_loop76: do {
		    if ((LA(1) == OR)) {
			AST tmp1_AST = null;
			tmp1_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(OR);
			andpattern();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop76;
		    }

		} while (true);
	    }
	    orpattern_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_0);
	}
	returnAST = orpattern_AST;
    }

    public final void andpattern() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST andpattern_AST = null;

	try { // for error handling
	    atom();
	    astFactory.addASTChild(currentAST, returnAST);
	    {
		_loop79: do {
		    if ((LA(1) == AND)) {
			AST tmp2_AST = null;
			tmp2_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp2_AST);
			match(AND);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop79;
		    }

		} while (true);
	    }
	    andpattern_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_1);
	}
	returnAST = andpattern_AST;
    }

    public final void atom() throws RecognitionException, TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST atom_AST = null;

	try { // for error handling
	    AST tmp3_AST = null;
	    tmp3_AST = astFactory.create(LT(1));
	    astFactory.addASTChild(currentAST, tmp3_AST);
	    match(WORD);
	    atom_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_2);
	}
	returnAST = atom_AST;
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>",
	    "NULL_TREE_LOOKAHEAD", "OR", "AND", "WORD", "WS", "LPAREN",
	    "RPAREN", "SEMI", "INT" };

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
	long[] data = { 50L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

}
