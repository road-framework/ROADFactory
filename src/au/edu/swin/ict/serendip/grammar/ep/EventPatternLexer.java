// $ANTLR : "ep.g" -> "EventPatternLexer.java"$

package au.edu.swin.ict.serendip.grammar.ep;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class EventPatternLexer extends antlr.CharScanner implements
	EventPatternParserTokenTypes, TokenStream {
    public EventPatternLexer(InputStream in) {
	this(new ByteBuffer(in));
    }

    public EventPatternLexer(Reader in) {
	this(new CharBuffer(in));
    }

    public EventPatternLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
    }

    public EventPatternLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
    }

    public Token nextToken() throws TokenStreamException {
	Token theRetToken = null;
	tryAgain: for (;;) {
	    Token _token = null;
	    int _ttype = Token.INVALID_TYPE;
	    resetText();
	    try { // for char stream error handling
		try { // for lexical error handling
		    switch (LA(1)) {
		    case '\t':
		    case '\n':
		    case '\r':
		    case ' ': {
			mWS(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '(': {
			mLPAREN(true);
			theRetToken = _returnToken;
			break;
		    }
		    case ')': {
			mRPAREN(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '*': {
			mAND(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '|': {
			mOR(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '^': {
			mXOR(true);
			theRetToken = _returnToken;
			break;
		    }
		    case ';': {
			mSEMI(true);
			theRetToken = _returnToken;
			break;
		    }
		    default:
			if ((LA(1) == '0' || LA(1) == '1')) {
			    mDIGITX(true);
			    theRetToken = _returnToken;
			} else if ((_tokenSet_0.member(LA(1)))) {
			    mWORD(true);
			    theRetToken = _returnToken;
			} else {
			    if (LA(1) == EOF_CHAR) {
				uponEOF();
				_returnToken = makeToken(Token.EOF_TYPE);
			    } else {
				throw new NoViableAltForCharException(
					(char) LA(1), getFilename(), getLine(),
					getColumn());
			    }
			}
		    }
		    if (_returnToken == null)
			continue tryAgain; // found SKIP token
		    _ttype = _returnToken.getType();
		    _ttype = testLiteralsTable(_ttype);
		    _returnToken.setType(_ttype);
		    return _returnToken;
		} catch (RecognitionException e) {
		    throw new TokenStreamRecognitionException(e);
		}
	    } catch (CharStreamException cse) {
		if (cse instanceof CharStreamIOException) {
		    throw new TokenStreamIOException(
			    ((CharStreamIOException) cse).io);
		} else {
		    throw new TokenStreamException(cse.getMessage());
		}
	    }
	}
    }

    public final void mWS(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = WS;
	int _saveIndex;

	{
	    switch (LA(1)) {
	    case ' ': {
		match(' ');
		break;
	    }
	    case '\t': {
		match('\t');
		break;
	    }
	    case '\n': {
		match('\n');
		break;
	    }
	    case '\r': {
		match('\r');
		break;
	    }
	    default: {
		throw new NoViableAltForCharException((char) LA(1),
			getFilename(), getLine(), getColumn());
	    }
	    }
	}
	_ttype = Token.SKIP;
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mLPAREN(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = LPAREN;
	int _saveIndex;

	match('(');
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mRPAREN(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = RPAREN;
	int _saveIndex;

	match(')');
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mAND(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = AND;
	int _saveIndex;

	match("*");
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mOR(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = OR;
	int _saveIndex;

	match("|");
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mXOR(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = XOR;
	int _saveIndex;

	match("^");
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mSEMI(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = SEMI;
	int _saveIndex;

	match(';');
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mDIGITX(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = DIGITX;
	int _saveIndex;

	switch (LA(1)) {
	case '0': {
	    match('0');
	    break;
	}
	case '1': {
	    match('1');
	    break;
	}
	default: {
	    throw new NoViableAltForCharException((char) LA(1), getFilename(),
		    getLine(), getColumn());
	}
	}
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mWORD(boolean _createToken) throws RecognitionException,
	    CharStreamException, TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = WORD;
	int _saveIndex;

	{
	    int _cnt162 = 0;
	    _loop162: do {
		switch (LA(1)) {
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z': {
		    matchRange('a', 'z');
		    break;
		}
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z': {
		    matchRange('A', 'Z');
		    break;
		}
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9': {
		    matchRange('0', '9');
		    break;
		}
		case '.': {
		    match('.');
		    break;
		}
		case '[': {
		    match('[');
		    break;
		}
		case ']': {
		    match(']');
		    break;
		}
		default: {
		    if (_cnt162 >= 1) {
			break _loop162;
		    } else {
			throw new NoViableAltForCharException((char) LA(1),
				getFilename(), getLine(), getColumn());
		    }
		}
		}
		_cnt162++;
	    } while (true);
	}
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    private static final long[] mk_tokenSet_0() {
	long[] data = { 288019269919178752L, 576460744518795262L, 0L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

}
