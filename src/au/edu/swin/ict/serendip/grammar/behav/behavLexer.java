// $ANTLR 3.4 E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g 2011-09-05 22:03:41

package au.edu.swin.ict.serendip.grammar.behav;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class behavLexer extends Lexer {
    public static final int EOF = -1;
    public static final int T__27 = 27;
    public static final int T__28 = 28;
    public static final int T__29 = 29;
    public static final int T__30 = 30;
    public static final int T__31 = 31;
    public static final int T__32 = 32;
    public static final int T__33 = 33;
    public static final int AND = 4;
    public static final int BEHAVIOR = 5;
    public static final int BTNAME = 6;
    public static final int COMMENT = 7;
    public static final int EXTENDS = 8;
    public static final int LBRACK = 9;
    public static final int LINE_COMMENT = 10;
    public static final int LPAREN = 11;
    public static final int OR = 12;
    public static final int POST = 13;
    public static final int PP = 14;
    public static final int PRE = 15;
    public static final int RBRACK = 16;
    public static final int ROLE = 17;
    public static final int RPAREN = 18;
    public static final int SCRIPT = 19;
    public static final int SEMI = 20;
    public static final int STRING = 21;
    public static final int TASK = 22;
    public static final int TASKNAME = 23;
    public static final int WORD = 24;
    public static final int WS = 25;
    public static final int XOR = 26;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
	return new Lexer[] {};
    }

    public behavLexer() {
    }

    public behavLexer(CharStream input) {
	this(input, new RecognizerSharedState());
    }

    public behavLexer(CharStream input, RecognizerSharedState state) {
	super(input, state);
    }

    public String getGrammarFileName() {
	return "E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g";
    }

    // $ANTLR start "BEHAVIOR"
    public final void mBEHAVIOR() throws RecognitionException {
	try {
	    int _type = BEHAVIOR;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:11:10:
	    // ( '_Behavior' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:11:12:
	    // '_Behavior'
	    {
		match("_Behavior");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "BEHAVIOR"

    // $ANTLR start "BTNAME"
    public final void mBTNAME() throws RecognitionException {
	try {
	    int _type = BTNAME;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:12:8:
	    // ( '_btName' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:12:10:
	    // '_btName'
	    {
		match("_btName");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "BTNAME"

    // $ANTLR start "EXTENDS"
    public final void mEXTENDS() throws RecognitionException {
	try {
	    int _type = EXTENDS;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:13:9:
	    // ( '_extends' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:13:11:
	    // '_extends'
	    {
		match("_extends");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "EXTENDS"

    // $ANTLR start "POST"
    public final void mPOST() throws RecognitionException {
	try {
	    int _type = POST;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:14:6:
	    // ( '_post' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:14:8:
	    // '_post'
	    {
		match("_post");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "POST"

    // $ANTLR start "PP"
    public final void mPP() throws RecognitionException {
	try {
	    int _type = PP;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:15:4:
	    // ( '_pp' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:15:6:
	    // '_pp'
	    {
		match("_pp");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "PP"

    // $ANTLR start "PRE"
    public final void mPRE() throws RecognitionException {
	try {
	    int _type = PRE;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:16:5:
	    // ( '_pre' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:16:7:
	    // '_pre'
	    {
		match("_pre");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "PRE"

    // $ANTLR start "ROLE"
    public final void mROLE() throws RecognitionException {
	try {
	    int _type = ROLE;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:17:6:
	    // ( '_role' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:17:8:
	    // '_role'
	    {
		match("_role");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "ROLE"

    // $ANTLR start "SCRIPT"
    public final void mSCRIPT() throws RecognitionException {
	try {
	    int _type = SCRIPT;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:18:8:
	    // ( '_Script' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:18:10:
	    // '_Script'
	    {
		match("_Script");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "SCRIPT"

    // $ANTLR start "TASK"
    public final void mTASK() throws RecognitionException {
	try {
	    int _type = TASK;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:19:6:
	    // ( '_Task' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:19:8:
	    // '_Task'
	    {
		match("_Task");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "TASK"

    // $ANTLR start "TASKNAME"
    public final void mTASKNAME() throws RecognitionException {
	try {
	    int _type = TASKNAME;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:20:10:
	    // ( '_taskName' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:20:12:
	    // '_taskName'
	    {
		match("_taskName");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "TASKNAME"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
	try {
	    int _type = T__27;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:21:7:
	    // ( 'Behavior' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:21:9:
	    // 'Behavior'
	    {
		match("Behavior");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
	try {
	    int _type = T__28;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:22:7:
	    // ( 'Task' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:22:9:
	    // 'Task'
	    {
		match("Task");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
	try {
	    int _type = T__29;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:23:7:
	    // ( 'extends' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:23:9:
	    // 'extends'
	    {
		match("extends");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
	try {
	    int _type = T__30;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:24:7:
	    // ( 'post' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:24:9:
	    // 'post'
	    {
		match("post");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
	try {
	    int _type = T__31;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:25:7:
	    // ( 'pp' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:25:9:
	    // 'pp'
	    {
		match("pp");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
	try {
	    int _type = T__32;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:26:7:
	    // ( 'pre' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:26:9:
	    // 'pre'
	    {
		match("pre");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
	try {
	    int _type = T__33;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:27:7:
	    // ( 'role' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:27:9:
	    // 'role'
	    {
		match("role");

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "T__33"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
	try {
	    int _type = LPAREN;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:78:7:
	    // ( '(' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:78:10:
	    // '('
	    {
		match('(');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
	try {
	    int _type = RPAREN;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:80:7:
	    // ( ')' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:80:9:
	    // ')'
	    {
		match(')');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "RPAREN"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
	try {
	    int _type = AND;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:82:4:
	    // ( '*' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:82:7:
	    // '*'
	    {
		match('*');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
	try {
	    int _type = OR;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:84:3:
	    // ( '|' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:84:5:
	    // '|'
	    {
		match('|');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "OR"

    // $ANTLR start "XOR"
    public final void mXOR() throws RecognitionException {
	try {
	    int _type = XOR;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:86:4:
	    // ( '^' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:86:6:
	    // '^'
	    {
		match('^');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "XOR"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
	try {
	    int _type = WORD;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:89:6:
	    // ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '.' )+ )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:89:8:
	    // ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '.' )+
	    {
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:89:8:
		// ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '.' )+
		int cnt1 = 0;
		loop1: do {
		    int alt1 = 2;
		    int LA1_0 = input.LA(1);

		    if ((LA1_0 == '.' || (LA1_0 >= '0' && LA1_0 <= '9')
			    || (LA1_0 >= 'A' && LA1_0 <= 'Z') || (LA1_0 >= 'a' && LA1_0 <= 'z'))) {
			alt1 = 1;
		    }

		    switch (alt1) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:
		    {
			if (input.LA(1) == '.'
				|| (input.LA(1) >= '0' && input.LA(1) <= '9')
				|| (input.LA(1) >= 'A' && input.LA(1) <= 'Z')
				|| (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
			    input.consume();
			} else {
			    MismatchedSetException mse = new MismatchedSetException(
				    null, input);
			    recover(mse);
			    throw mse;
			}

		    }
			break;

		    default:
			if (cnt1 >= 1)
			    break loop1;
			EarlyExitException eee = new EarlyExitException(1,
				input);
			throw eee;
		    }
		    cnt1++;
		} while (true);

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "WORD"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
	try {
	    int _type = STRING;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:93:8:
	    // ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | LPAREN | RPAREN
	    // | AND | OR | XOR )+ | ( '\"' (~ '\"' )* '\"' ) )
	    int alt4 = 2;
	    int LA4_0 = input.LA(1);

	    if (((LA4_0 >= '(' && LA4_0 <= '*')
		    || (LA4_0 >= '0' && LA4_0 <= '9')
		    || (LA4_0 >= 'A' && LA4_0 <= 'Z')
		    || (LA4_0 >= '^' && LA4_0 <= '_')
		    || (LA4_0 >= 'a' && LA4_0 <= 'z') || LA4_0 == '|')) {
		alt4 = 1;
	    } else if ((LA4_0 == '\"')) {
		alt4 = 2;
	    } else {
		NoViableAltException nvae = new NoViableAltException("", 4, 0,
			input);

		throw nvae;

	    }
	    switch (alt4) {
	    case 1:
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:93:12:
	    // ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | LPAREN | RPAREN |
	    // AND | OR | XOR )+
	    {
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:93:12:
		// ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | LPAREN |
		// RPAREN | AND | OR | XOR )+
		int cnt2 = 0;
		loop2: do {
		    int alt2 = 2;
		    int LA2_0 = input.LA(1);

		    if (((LA2_0 >= '(' && LA2_0 <= '*')
			    || (LA2_0 >= '0' && LA2_0 <= '9')
			    || (LA2_0 >= 'A' && LA2_0 <= 'Z')
			    || (LA2_0 >= '^' && LA2_0 <= '_')
			    || (LA2_0 >= 'a' && LA2_0 <= 'z') || LA2_0 == '|')) {
			alt2 = 1;
		    }

		    switch (alt2) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:
		    {
			if ((input.LA(1) >= '(' && input.LA(1) <= '*')
				|| (input.LA(1) >= '0' && input.LA(1) <= '9')
				|| (input.LA(1) >= 'A' && input.LA(1) <= 'Z')
				|| (input.LA(1) >= '^' && input.LA(1) <= '_')
				|| (input.LA(1) >= 'a' && input.LA(1) <= 'z')
				|| input.LA(1) == '|') {
			    input.consume();
			} else {
			    MismatchedSetException mse = new MismatchedSetException(
				    null, input);
			    recover(mse);
			    throw mse;
			}

		    }
			break;

		    default:
			if (cnt2 >= 1)
			    break loop2;
			EarlyExitException eee = new EarlyExitException(2,
				input);
			throw eee;
		    }
		    cnt2++;
		} while (true);

	    }
		break;
	    case 2:
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:94:10:
	    // ( '\"' (~ '\"' )* '\"' )
	    {
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:94:10:
		// ( '\"' (~ '\"' )* '\"' )
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:94:11:
		// '\"' (~ '\"' )* '\"'
		{
		    match('\"');

		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:94:15:
		    // (~ '\"' )*
		    loop3: do {
			int alt3 = 2;
			int LA3_0 = input.LA(1);

			if (((LA3_0 >= '\u0000' && LA3_0 <= '!') || (LA3_0 >= '#' && LA3_0 <= '\uFFFF'))) {
			    alt3 = 1;
			}

			switch (alt3) {
			case 1:
			// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:
			{
			    if ((input.LA(1) >= '\u0000' && input.LA(1) <= '!')
				    || (input.LA(1) >= '#' && input.LA(1) <= '\uFFFF')) {
				input.consume();
			    } else {
				MismatchedSetException mse = new MismatchedSetException(
					null, input);
				recover(mse);
				throw mse;
			    }

			}
			    break;

			default:
			    break loop3;
			}
		    } while (true);

		    match('\"');

		}

	    }
		break;

	    }
	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "STRING"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
	try {
	    int _type = COMMENT;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:99:9:
	    // ( '/*' ( . )* '*/' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:99:11:
	    // '/*' ( . )* '*/'
	    {
		match("/*");

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:99:16:
		// ( . )*
		loop5: do {
		    int alt5 = 2;
		    int LA5_0 = input.LA(1);

		    if ((LA5_0 == '*')) {
			int LA5_1 = input.LA(2);

			if ((LA5_1 == '/')) {
			    alt5 = 2;
			} else if (((LA5_1 >= '\u0000' && LA5_1 <= '.') || (LA5_1 >= '0' && LA5_1 <= '\uFFFF'))) {
			    alt5 = 1;
			}

		    } else if (((LA5_0 >= '\u0000' && LA5_0 <= ')') || (LA5_0 >= '+' && LA5_0 <= '\uFFFF'))) {
			alt5 = 1;
		    }

		    switch (alt5) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:99:16:
		    // .
		    {
			matchAny();

		    }
			break;

		    default:
			break loop5;
		    }
		} while (true);

		match("*/");

		_channel = HIDDEN;

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "COMMENT"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
	try {
	    int _type = LINE_COMMENT;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:102:15:
	    // ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:102:17:
	    // '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
	    {
		match("//");

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:102:22:
		// (~ ( '\\n' | '\\r' ) )*
		loop6: do {
		    int alt6 = 2;
		    int LA6_0 = input.LA(1);

		    if (((LA6_0 >= '\u0000' && LA6_0 <= '\t')
			    || (LA6_0 >= '\u000B' && LA6_0 <= '\f') || (LA6_0 >= '\u000E' && LA6_0 <= '\uFFFF'))) {
			alt6 = 1;
		    }

		    switch (alt6) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:
		    {
			if ((input.LA(1) >= '\u0000' && input.LA(1) <= '\t')
				|| (input.LA(1) >= '\u000B' && input.LA(1) <= '\f')
				|| (input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF')) {
			    input.consume();
			} else {
			    MismatchedSetException mse = new MismatchedSetException(
				    null, input);
			    recover(mse);
			    throw mse;
			}

		    }
			break;

		    default:
			break loop6;
		    }
		} while (true);

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:102:36:
		// ( '\\r' )?
		int alt7 = 2;
		int LA7_0 = input.LA(1);

		if ((LA7_0 == '\r')) {
		    alt7 = 1;
		}
		switch (alt7) {
		case 1:
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:102:36:
		// '\\r'
		{
		    match('\r');

		}
		    break;

		}

		match('\n');

		_channel = HIDDEN;

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "LINE_COMMENT"

    // $ANTLR start "LBRACK"
    public final void mLBRACK() throws RecognitionException {
	try {
	    int _type = LBRACK;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:105:7:
	    // ( '{' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:105:9:
	    // '{'
	    {
		match('{');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "LBRACK"

    // $ANTLR start "RBRACK"
    public final void mRBRACK() throws RecognitionException {
	try {
	    int _type = RBRACK;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:108:7:
	    // ( '}' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:108:9:
	    // '}'
	    {
		match('}');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "RBRACK"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
	try {
	    int _type = SEMI;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:112:5:
	    // ( ';' )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:112:7:
	    // ';'
	    {
		match(';');

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "SEMI"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
	try {
	    int _type = WS;
	    int _channel = DEFAULT_TOKEN_CHANNEL;
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:117:3:
	    // ( ( '\\t' | ' ' | '\\r' | '\\n' | '\ ' )+ )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:117:5:
	    // ( '\\t' | ' ' | '\\r' | '\\n' | '\ ' )+
	    {
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:117:5:
		// ( '\\t' | ' ' | '\\r' | '\\n' | '\ ' )+
		int cnt8 = 0;
		loop8: do {
		    int alt8 = 2;
		    int LA8_0 = input.LA(1);

		    if (((LA8_0 >= '\t' && LA8_0 <= '\n')
			    || (LA8_0 >= '\f' && LA8_0 <= '\r') || LA8_0 == ' ')) {
			alt8 = 1;
		    }

		    switch (alt8) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:
		    {
			if ((input.LA(1) >= '\t' && input.LA(1) <= '\n')
				|| (input.LA(1) >= '\f' && input.LA(1) <= '\r')
				|| input.LA(1) == ' ') {
			    input.consume();
			} else {
			    MismatchedSetException mse = new MismatchedSetException(
				    null, input);
			    recover(mse);
			    throw mse;
			}

		    }
			break;

		    default:
			if (cnt8 >= 1)
			    break loop8;
			EarlyExitException eee = new EarlyExitException(8,
				input);
			throw eee;
		    }
		    cnt8++;
		} while (true);

		_channel = HIDDEN;

	    }

	    state.type = _type;
	    state.channel = _channel;
	} finally {
	    // do for sure before leaving
	}
    }

    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:8: (
	// BEHAVIOR | BTNAME | EXTENDS | POST | PP | PRE | ROLE | SCRIPT | TASK
	// | TASKNAME | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 |
	// LPAREN | RPAREN | AND | OR | XOR | WORD | STRING | COMMENT |
	// LINE_COMMENT | LBRACK | RBRACK | SEMI | WS )
	int alt9 = 30;
	alt9 = dfa9.predict(input);
	switch (alt9) {
	case 1:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:10:
	// BEHAVIOR
	{
	    mBEHAVIOR();

	}
	    break;
	case 2:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:19:
	// BTNAME
	{
	    mBTNAME();

	}
	    break;
	case 3:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:26:
	// EXTENDS
	{
	    mEXTENDS();

	}
	    break;
	case 4:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:34:
	// POST
	{
	    mPOST();

	}
	    break;
	case 5:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:39:
	// PP
	{
	    mPP();

	}
	    break;
	case 6:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:42:
	// PRE
	{
	    mPRE();

	}
	    break;
	case 7:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:46:
	// ROLE
	{
	    mROLE();

	}
	    break;
	case 8:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:51:
	// SCRIPT
	{
	    mSCRIPT();

	}
	    break;
	case 9:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:58:
	// TASK
	{
	    mTASK();

	}
	    break;
	case 10:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:63:
	// TASKNAME
	{
	    mTASKNAME();

	}
	    break;
	case 11:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:72:
	// T__27
	{
	    mT__27();

	}
	    break;
	case 12:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:78:
	// T__28
	{
	    mT__28();

	}
	    break;
	case 13:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:84:
	// T__29
	{
	    mT__29();

	}
	    break;
	case 14:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:90:
	// T__30
	{
	    mT__30();

	}
	    break;
	case 15:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:96:
	// T__31
	{
	    mT__31();

	}
	    break;
	case 16:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:102:
	// T__32
	{
	    mT__32();

	}
	    break;
	case 17:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:108:
	// T__33
	{
	    mT__33();

	}
	    break;
	case 18:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:114:
	// LPAREN
	{
	    mLPAREN();

	}
	    break;
	case 19:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:121:
	// RPAREN
	{
	    mRPAREN();

	}
	    break;
	case 20:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:128:
	// AND
	{
	    mAND();

	}
	    break;
	case 21:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:132:
	// OR
	{
	    mOR();

	}
	    break;
	case 22:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:135:
	// XOR
	{
	    mXOR();

	}
	    break;
	case 23:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:139:
	// WORD
	{
	    mWORD();

	}
	    break;
	case 24:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:144:
	// STRING
	{
	    mSTRING();

	}
	    break;
	case 25:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:151:
	// COMMENT
	{
	    mCOMMENT();

	}
	    break;
	case 26:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:159:
	// LINE_COMMENT
	{
	    mLINE_COMMENT();

	}
	    break;
	case 27:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:172:
	// LBRACK
	{
	    mLBRACK();

	}
	    break;
	case 28:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:179:
	// RBRACK
	{
	    mRBRACK();

	}
	    break;
	case 29:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:186:
	// SEMI
	{
	    mSEMI();

	}
	    break;
	case 30:
	// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:1:191:
	// WS
	{
	    mWS();

	}
	    break;

	}

    }

    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS = "\1\uffff\1\16\5\15\1\43\1\44\1\45\1\46\1\47\1\15\7\uffff\10\16\4"
	    + "\15\1\70\2\15\7\uffff\4\16\1\77\5\16\4\15\1\uffff\1\111\1\15\4\16"
	    + "\1\uffff\1\117\4\16\1\15\1\125\1\15\1\127\1\uffff\1\130\3\16\1\134"
	    + "\1\uffff\1\135\1\16\1\137\1\16\1\15\1\uffff\1\15\2\uffff\3\16\2"
	    + "\uffff\1\16\1\uffff\1\16\2\15\1\16\1\153\1\16\1\155\1\16\1\15\1"
	    + "\160\1\16\1\uffff\1\162\1\uffff\1\16\1\164\1\uffff\1\165\1\uffff"
	    + "\1\166\3\uffff";
    static final String DFA9_eofS = "\167\uffff";
    static final String DFA9_minS = "\1\11\1\102\13\50\2\uffff\1\52\4\uffff\1\145\1\164\1\170\2\157\1"
	    + "\143\2\141\7\50\7\uffff\1\150\1\116\1\164\1\163\1\50\1\145\1\154"
	    + "\1\162\2\163\4\50\1\uffff\2\50\2\141\1\145\1\164\1\uffff\1\50\1"
	    + "\145\1\151\2\153\4\50\1\uffff\1\50\1\166\1\155\1\156\1\50\1\uffff"
	    + "\1\50\1\160\1\50\1\116\1\50\1\uffff\1\50\2\uffff\1\151\1\145\1\144"
	    + "\2\uffff\1\164\1\uffff\1\141\2\50\1\157\1\50\1\163\1\50\1\155\2"
	    + "\50\1\162\1\uffff\1\50\1\uffff\1\145\1\50\1\uffff\1\50\1\uffff\1"
	    + "\50\3\uffff";
    static final String DFA9_maxS = "\1\175\1\164\13\174\2\uffff\1\57\4\uffff\1\145\1\164\1\170\1\162"
	    + "\1\157\1\143\2\141\7\174\7\uffff\1\150\1\116\1\164\1\163\1\174\1"
	    + "\145\1\154\1\162\2\163\4\174\1\uffff\2\174\2\141\1\145\1\164\1\uffff"
	    + "\1\174\1\145\1\151\2\153\4\174\1\uffff\1\174\1\166\1\155\1\156\1"
	    + "\174\1\uffff\1\174\1\160\1\174\1\116\1\174\1\uffff\1\174\2\uffff"
	    + "\1\151\1\145\1\144\2\uffff\1\164\1\uffff\1\141\2\174\1\157\1\174"
	    + "\1\163\1\174\1\155\2\174\1\162\1\uffff\1\174\1\uffff\1\145\1\174"
	    + "\1\uffff\1\174\1\uffff\1\174\3\uffff";
    static final String DFA9_acceptS = "\15\uffff\1\27\1\30\1\uffff\1\33\1\34\1\35\1\36\17\uffff\1\22\1"
	    + "\23\1\24\1\25\1\26\1\31\1\32\16\uffff\1\17\6\uffff\1\5\11\uffff"
	    + "\1\20\5\uffff\1\6\5\uffff\1\14\1\uffff\1\16\1\21\3\uffff\1\4\1\7"
	    + "\1\uffff\1\11\13\uffff\1\2\1\uffff\1\10\2\uffff\1\15\1\uffff\1\3"
	    + "\1\uffff\1\13\1\1\1\12";
    static final String DFA9_specialS = "\167\uffff}>";
    static final String[] DFA9_transitionS = {
	    "\2\23\1\uffff\2\23\22\uffff\1\23\1\uffff\1\16\5\uffff\1\7\1"
		    + "\10\1\11\3\uffff\1\15\1\17\12\14\1\uffff\1\22\5\uffff\1\14\1"
		    + "\2\21\14\1\3\6\14\3\uffff\1\13\1\1\1\uffff\4\14\1\4\12\14\1"
		    + "\5\1\14\1\6\10\14\1\20\1\12\1\21",
	    "\1\24\20\uffff\1\31\1\32\15\uffff\1\25\2\uffff\1\26\12\uffff"
		    + "\1\27\1\uffff\1\30\1\uffff\1\33",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\4\14"
		    + "\1\34\25\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\1\35"
		    + "\31\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\27\14"
		    + "\1\36\2\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\16\14"
		    + "\1\37\1\40\1\14\1\41\10\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\16\14"
		    + "\1\42\13\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\32\14"
		    + "\1\uffff\1\16",
	    "",
	    "",
	    "\1\50\4\uffff\1\51",
	    "",
	    "",
	    "",
	    "",
	    "\1\52",
	    "\1\53",
	    "\1\54",
	    "\1\55\1\56\1\uffff\1\57",
	    "\1\60",
	    "\1\61",
	    "\1\62",
	    "\1\63",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\7\14"
		    + "\1\64\22\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\22\14"
		    + "\1\65\7\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\23\14"
		    + "\1\66\6\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\22\14"
		    + "\1\67\7\14\1\uffff\1\16",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\4\14"
		    + "\1\71\25\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\13\14"
		    + "\1\72\16\14\1\uffff\1\16",
	    "",
	    "",
	    "",
	    "",
	    "",
	    "",
	    "",
	    "\1\73",
	    "\1\74",
	    "\1\75",
	    "\1\76",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\100",
	    "\1\101",
	    "\1\102",
	    "\1\103",
	    "\1\104",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\1\105"
		    + "\31\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\12\14"
		    + "\1\106\17\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\4\14"
		    + "\1\107\25\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\23\14"
		    + "\1\110\6\14\1\uffff\1\16",
	    "",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\4\14"
		    + "\1\112\25\14\1\uffff\1\16",
	    "\1\113",
	    "\1\114",
	    "\1\115",
	    "\1\116",
	    "",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\120",
	    "\1\121",
	    "\1\122",
	    "\1\123",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\25\14"
		    + "\1\124\4\14\1\uffff\1\16",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\15\14"
		    + "\1\126\14\14\1\uffff\1\16",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "\1\131",
	    "\1\132",
	    "\1\133",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\136",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\140",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\10\14"
		    + "\1\141\21\14\1\uffff\1\16",
	    "",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\3\14"
		    + "\1\142\26\14\1\uffff\1\16",
	    "",
	    "",
	    "\1\143",
	    "\1\144",
	    "\1\145",
	    "",
	    "",
	    "\1\146",
	    "",
	    "\1\147",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\16\14"
		    + "\1\150\13\14\1\uffff\1\16",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\22\14"
		    + "\1\151\7\14\1\uffff\1\16",
	    "\1\152",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\154",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "\1\156",
	    "\3\16\5\uffff\12\14\7\uffff\32\14\3\uffff\2\16\1\uffff\21\14"
		    + "\1\157\10\14\1\uffff\1\16",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "\1\161",
	    "",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "",
	    "\1\163",
	    "\3\16\3\uffff\1\15\1\uffff\12\14\7\uffff\32\14\3\uffff\2\16"
		    + "\1\uffff\32\14\1\uffff\1\16",
	    "",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16",
	    "",
	    "\3\16\5\uffff\12\16\7\uffff\32\16\3\uffff\2\16\1\uffff\32\16"
		    + "\1\uffff\1\16", "", "", "" };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA
	    .unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA
	    .unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
	int numStates = DFA9_transitionS.length;
	DFA9_transition = new short[numStates][];
	for (int i = 0; i < numStates; i++) {
	    DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
	}
    }

    class DFA9 extends DFA {

	public DFA9(BaseRecognizer recognizer) {
	    this.recognizer = recognizer;
	    this.decisionNumber = 9;
	    this.eot = DFA9_eot;
	    this.eof = DFA9_eof;
	    this.min = DFA9_min;
	    this.max = DFA9_max;
	    this.accept = DFA9_accept;
	    this.special = DFA9_special;
	    this.transition = DFA9_transition;
	}

	public String getDescription() {
	    return "1:1: Tokens : ( BEHAVIOR | BTNAME | EXTENDS | POST | PP | PRE | ROLE | SCRIPT | TASK | TASKNAME | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | LPAREN | RPAREN | AND | OR | XOR | WORD | STRING | COMMENT | LINE_COMMENT | LBRACK | RBRACK | SEMI | WS );";
	}
    }

}