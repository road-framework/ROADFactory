// $ANTLR : "ser.g" -> "SerLexer.java"$

package au.edu.swin.ict.serendip.core.mgmt.scripting;

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

public class SerLexer extends antlr.CharScanner implements SerLexerTokenTypes,
	TokenStream {
    public SerLexer(InputStream in) {
	this(new ByteBuffer(in));
    }

    public SerLexer(Reader in) {
	this(new CharBuffer(in));
    }

    public SerLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
    }

    public SerLexer(LexerSharedInputState state) {
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
		    case '"':
		    case '0':
		    case '1':
		    case '2':
		    case '3':
		    case '4':
		    case '5':
		    case '6':
		    case '7':
		    case '8':
		    case '9':
		    case ':':
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
		    case 'Z':
		    case '_':
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
			mSTRING(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '\t':
		    case '\n':
		    case '\r':
		    case ' ': {
			mWS(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '#': {
			mLINE_COMMENT(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '{': {
			mLBRACE(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '}': {
			mRBRACE(true);
			theRetToken = _returnToken;
			break;
		    }
		    case '=': {
			mEQUALS(true);
			theRetToken = _returnToken;
			break;
		    }
		    case ';': {
			mSEMI(true);
			theRetToken = _returnToken;
			break;
		    }
		    default: {
			if (LA(1) == EOF_CHAR) {
			    uponEOF();
			    _returnToken = makeToken(Token.EOF_TYPE);
			} else {
			    throw new NoViableAltForCharException((char) LA(1),
				    getFilename(), getLine(), getColumn());
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

    public final void mSTRING(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = STRING;
	int _saveIndex;

	switch (LA(1)) {
	case '0':
	case '1':
	case '2':
	case '3':
	case '4':
	case '5':
	case '6':
	case '7':
	case '8':
	case '9':
	case ':':
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
	case 'Z':
	case '_':
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
	    {
		int _cnt165 = 0;
		_loop165: do {
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
		    case '_': {
			match('_');
			break;
		    }
		    case ':': {
			match(':');
			break;
		    }
		    default: {
			if (_cnt165 >= 1) {
			    break _loop165;
			} else {
			    throw new NoViableAltForCharException((char) LA(1),
				    getFilename(), getLine(), getColumn());
			}
		    }
		    }
		    _cnt165++;
		} while (true);
	    }
	    break;
	}
	case '"': {
	    {
		match('"');
		{
		    _loop168: do {
			if ((_tokenSet_0.member(LA(1)))) {
			    matchNot('"');
			} else {
			    break _loop168;
			}

		    } while (true);
		}
		match('"');
	    }
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
	    case '\r': {
		match('\r');
		match('\n');
		newline();
		break;
	    }
	    case '\n': {
		match('\n');
		newline();
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

    public final void mLINE_COMMENT(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = LINE_COMMENT;
	int _saveIndex;

	match('#');
	{
	    _loop174: do {
		if ((_tokenSet_1.member(LA(1)))) {
		    {
			match(_tokenSet_1);
		    }
		} else {
		    break _loop174;
		}

	    } while (true);
	}
	{
	    switch (LA(1)) {
	    case '\n': {
		match('\n');
		break;
	    }
	    case '\r': {
		match('\r');
		{
		    if ((LA(1) == '\n')) {
			match('\n');
		    } else {
		    }

		}
		break;
	    }
	    default: {
	    }
	    }
	}
	_ttype = Token.SKIP;
	newline();
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mLBRACE(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = LBRACE;
	int _saveIndex;

	match('{');
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mRBRACE(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = RBRACE;
	int _saveIndex;

	match('}');
	if (_createToken && _token == null && _ttype != Token.SKIP) {
	    _token = makeToken(_ttype);
	    _token.setText(new String(text.getBuffer(), _begin, text.length()
		    - _begin));
	}
	_returnToken = _token;
    }

    public final void mEQUALS(boolean _createToken)
	    throws RecognitionException, CharStreamException,
	    TokenStreamException {
	int _ttype;
	Token _token = null;
	int _begin = text.length();
	_ttype = EQUALS;
	int _saveIndex;

	match('=');
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

    private static final long[] mk_tokenSet_0() {
	long[] data = { -17179869185L, -1L, 0L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
	long[] data = { -9217L, -1L, 0L, 0L };
	return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

}
