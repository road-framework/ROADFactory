// $ANTLR : "ep.g" -> "EventPatternParser.java"$

package au.edu.swin.ict.serendip.grammar.ep;

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

    public final void expression() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST expression_AST = null;

	try { // for error handling
	    xorpattern();
	    astFactory.addASTChild(currentAST, returnAST);
	    expression_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_0);
	}
	returnAST = expression_AST;
    }

    public final void xorpattern() throws RecognitionException,
	    TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST xorpattern_AST = null;

	try { // for error handling
	    orpattern();
	    astFactory.addASTChild(currentAST, returnAST);
	    {
		_loop142: do {
		    if ((LA(1) == XOR)) {
			AST tmp1_AST = null;
			tmp1_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(XOR);
			orpattern();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop142;
		    }

		} while (true);
	    }
	    xorpattern_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_0);
	}
	returnAST = xorpattern_AST;
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
		_loop145: do {
		    if ((LA(1) == OR)) {
			AST tmp2_AST = null;
			tmp2_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp2_AST);
			match(OR);
			andpattern();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop145;
		    }

		} while (true);
	    }
	    orpattern_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_1);
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
		_loop148: do {
		    if ((LA(1) == AND)) {
			AST tmp3_AST = null;
			tmp3_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp3_AST);
			match(AND);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
		    } else {
			break _loop148;
		    }

		} while (true);
	    }
	    andpattern_AST = (AST) currentAST.root;
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_2);
	}
	returnAST = andpattern_AST;
    }

    public final void atom() throws RecognitionException, TokenStreamException {

	returnAST = null;
	ASTPair currentAST = new ASTPair();
	AST atom_AST = null;

	try { // for error handling
	    switch (LA(1)) {
	    case LPAREN: {
		{
		    match(LPAREN);
		    expression();
		    astFactory.addASTChild(currentAST, returnAST);
		    match(RPAREN);
		}
		atom_AST = (AST) currentAST.root;
		break;
	    }
	    case WORD: {
		AST tmp6_AST = null;
		tmp6_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp6_AST);
		match(WORD);
		atom_AST = (AST) currentAST.root;
		break;
	    }
	    default: {
		throw new NoViableAltException(LT(1), getFilename());
	    }
	    }
	} catch (RecognitionException ex) {
	    reportError(ex);
	    recover(ex, _tokenSet_3);
	}
	returnAST = atom_AST;
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>",
	    "NULL_TREE_LOOKAHEAD", "XOR", "OR", "AND", "LPAREN", "RPAREN",
	    "WORD", "WS", "SEMI", "DIGITX" };

    protected void buildTokenTypeASTClassMap() {
	tokenTypeToASTClassMap = null;
    };

    private static final long[] mk_tokenSet_0() {
	long[] data = { 256L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
	long[] data = { 272L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
	long[] data = { 304L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
	long[] data = { 368L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

}
