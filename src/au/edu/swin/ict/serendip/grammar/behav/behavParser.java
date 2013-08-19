// $ANTLR 3.4 E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g 2011-09-05 22:03:41

package au.edu.swin.ict.serendip.grammar.behav;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class behavParser extends Parser {
    public static final String[] tokenNames = new String[] { "<invalid>",
	    "<EOR>", "<DOWN>", "<UP>", "AND", "BEHAVIOR", "BTNAME", "COMMENT",
	    "EXTENDS", "LBRACK", "LINE_COMMENT", "LPAREN", "OR", "POST", "PP",
	    "PRE", "RBRACK", "ROLE", "RPAREN", "SCRIPT", "SEMI", "STRING",
	    "TASK", "TASKNAME", "WORD", "WS", "XOR", "'Behavior'", "'Task'",
	    "'extends'", "'post'", "'pp'", "'pre'", "'role'" };

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
    public Parser[] getDelegates() {
	return new Parser[] {};
    }

    // delegators

    public behavParser(TokenStream input) {
	this(input, new RecognizerSharedState());
    }

    public behavParser(TokenStream input, RecognizerSharedState state) {
	super(input, state);
    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
	this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
	return adaptor;
    }

    public String[] getTokenNames() {
	return behavParser.tokenNames;
    }

    public String getGrammarFileName() {
	return "E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g";
    }

    public static class script_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "script"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:1:
    // script : ( ( bterm )+ ) EOF -> ^( '_Script' ( bterm )+ ) ;
    public final behavParser.script_return script() throws RecognitionException {
	behavParser.script_return retval = new behavParser.script_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token EOF2 = null;
	behavParser.bterm_return bterm1 = null;

	Object EOF2_tree = null;
	RewriteRuleTokenStream stream_EOF = new RewriteRuleTokenStream(adaptor,
		"token EOF");
	RewriteRuleSubtreeStream stream_bterm = new RewriteRuleSubtreeStream(
		adaptor, "rule bterm");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:7:
	    // ( ( ( bterm )+ ) EOF -> ^( '_Script' ( bterm )+ ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:9:
	    // ( ( bterm )+ ) EOF
	    {
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:9:
		// ( ( bterm )+ )
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:10:
		// ( bterm )+
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:10:
		    // ( bterm )+
		    int cnt1 = 0;
		    loop1: do {
			int alt1 = 2;
			int LA1_0 = input.LA(1);

			if ((LA1_0 == 27)) {
			    alt1 = 1;
			}

			switch (alt1) {
			case 1:
			// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:31:11:
			// bterm
			{
			    pushFollow(FOLLOW_bterm_in_script149);
			    bterm1 = bterm();

			    state._fsp--;

			    stream_bterm.add(bterm1.getTree());

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

		EOF2 = (Token) match(input, EOF, FOLLOW_EOF_in_script155);
		stream_EOF.add(EOF2);

		// AST REWRITE
		// elements: bterm, SCRIPT
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 32:3: -> ^( '_Script' ( bterm )+ )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:32:6:
		    // ^( '_Script' ( bterm )+ )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(SCRIPT, "SCRIPT"),
				root_1);

			if (!(stream_bterm.hasNext())) {
			    throw new RewriteEarlyExitException();
			}
			while (stream_bterm.hasNext()) {
			    adaptor.addChild(root_1, stream_bterm.nextTree());

			}
			stream_bterm.reset();

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "script"

    public static class bterm_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "bterm"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:1:
    // bterm : btId ( btExtends )? '{' ( task )+ '}' ';' -> ^( '_Behavior' btId
    // ( btExtends )? ( task )+ ) ;
    public final behavParser.bterm_return bterm() throws RecognitionException {
	behavParser.bterm_return retval = new behavParser.bterm_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token char_literal5 = null;
	Token char_literal7 = null;
	Token char_literal8 = null;
	behavParser.btId_return btId3 = null;

	behavParser.btExtends_return btExtends4 = null;

	behavParser.task_return task6 = null;

	Object char_literal5_tree = null;
	Object char_literal7_tree = null;
	Object char_literal8_tree = null;
	RewriteRuleTokenStream stream_RBRACK = new RewriteRuleTokenStream(
		adaptor, "token RBRACK");
	RewriteRuleTokenStream stream_LBRACK = new RewriteRuleTokenStream(
		adaptor, "token LBRACK");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_btId = new RewriteRuleSubtreeStream(
		adaptor, "rule btId");
	RewriteRuleSubtreeStream stream_btExtends = new RewriteRuleSubtreeStream(
		adaptor, "rule btExtends");
	RewriteRuleSubtreeStream stream_task = new RewriteRuleSubtreeStream(
		adaptor, "rule task");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:6:
	    // ( btId ( btExtends )? '{' ( task )+ '}' ';' -> ^( '_Behavior'
	    // btId ( btExtends )? ( task )+ ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:9:
	    // btId ( btExtends )? '{' ( task )+ '}' ';'
	    {
		pushFollow(FOLLOW_btId_in_bterm179);
		btId3 = btId();

		state._fsp--;

		stream_btId.add(btId3.getTree());

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:14:
		// ( btExtends )?
		int alt2 = 2;
		int LA2_0 = input.LA(1);

		if ((LA2_0 == 29)) {
		    alt2 = 1;
		}
		switch (alt2) {
		case 1:
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:15:
		// btExtends
		{
		    pushFollow(FOLLOW_btExtends_in_bterm182);
		    btExtends4 = btExtends();

		    state._fsp--;

		    stream_btExtends.add(btExtends4.getTree());

		}
		    break;

		}

		char_literal5 = (Token) match(input, LBRACK,
			FOLLOW_LBRACK_in_bterm187);
		stream_LBRACK.add(char_literal5);

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:34:
		// ( task )+
		int cnt3 = 0;
		loop3: do {
		    int alt3 = 2;
		    int LA3_0 = input.LA(1);

		    if ((LA3_0 == 28)) {
			alt3 = 1;
		    }

		    switch (alt3) {
		    case 1:
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:34:35:
		    // task
		    {
			pushFollow(FOLLOW_task_in_bterm192);
			task6 = task();

			state._fsp--;

			stream_task.add(task6.getTree());

		    }
			break;

		    default:
			if (cnt3 >= 1)
			    break loop3;
			EarlyExitException eee = new EarlyExitException(3,
				input);
			throw eee;
		    }
		    cnt3++;
		} while (true);

		char_literal7 = (Token) match(input, RBRACK,
			FOLLOW_RBRACK_in_bterm198);
		stream_RBRACK.add(char_literal7);

		char_literal8 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_bterm200);
		stream_SEMI.add(char_literal8);

		// AST REWRITE
		// elements: task, btId, btExtends, BEHAVIOR
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 35:3: -> ^( '_Behavior' btId ( btExtends )? ( task )+ )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:35:6:
		    // ^( '_Behavior' btId ( btExtends )? ( task )+ )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(BEHAVIOR, "BEHAVIOR"),
				root_1);

			adaptor.addChild(root_1, stream_btId.nextTree());

			// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:35:25:
			// ( btExtends )?
			if (stream_btExtends.hasNext()) {
			    adaptor.addChild(root_1,
				    stream_btExtends.nextTree());

			}
			stream_btExtends.reset();

			if (!(stream_task.hasNext())) {
			    throw new RewriteEarlyExitException();
			}
			while (stream_task.hasNext()) {
			    adaptor.addChild(root_1, stream_task.nextTree());

			}
			stream_task.reset();

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "bterm"

    public static class btId_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "btId"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:38:1: btId
    // : 'Behavior' btName -> ^( '_btName' btName ) ;
    public final behavParser.btId_return btId() throws RecognitionException {
	behavParser.btId_return retval = new behavParser.btId_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal9 = null;
	behavParser.btName_return btName10 = null;

	Object string_literal9_tree = null;
	RewriteRuleTokenStream stream_27 = new RewriteRuleTokenStream(adaptor,
		"token 27");
	RewriteRuleSubtreeStream stream_btName = new RewriteRuleSubtreeStream(
		adaptor, "rule btName");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:38:6:
	    // ( 'Behavior' btName -> ^( '_btName' btName ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:38:8:
	    // 'Behavior' btName
	    {
		string_literal9 = (Token) match(input, 27, FOLLOW_27_in_btId233);
		stream_27.add(string_literal9);

		pushFollow(FOLLOW_btName_in_btId235);
		btName10 = btName();

		state._fsp--;

		stream_btName.add(btName10.getTree());

		// AST REWRITE
		// elements: btName, BTNAME
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 39:3: -> ^( '_btName' btName )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:39:6:
		    // ^( '_btName' btName )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(BTNAME, "BTNAME"),
				root_1);

			adaptor.addChild(root_1, stream_btName.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "btId"

    public static class btExtends_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "btExtends"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:41:1:
    // btExtends : 'extends' extendBtId -> ^( '_extends' extendBtId ) ;
    public final behavParser.btExtends_return btExtends()
	    throws RecognitionException {
	behavParser.btExtends_return retval = new behavParser.btExtends_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal11 = null;
	behavParser.extendBtId_return extendBtId12 = null;

	Object string_literal11_tree = null;
	RewriteRuleTokenStream stream_29 = new RewriteRuleTokenStream(adaptor,
		"token 29");
	RewriteRuleSubtreeStream stream_extendBtId = new RewriteRuleSubtreeStream(
		adaptor, "rule extendBtId");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:41:11:
	    // ( 'extends' extendBtId -> ^( '_extends' extendBtId ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:41:13:
	    // 'extends' extendBtId
	    {
		string_literal11 = (Token) match(input, 29,
			FOLLOW_29_in_btExtends255);
		stream_29.add(string_literal11);

		pushFollow(FOLLOW_extendBtId_in_btExtends257);
		extendBtId12 = extendBtId();

		state._fsp--;

		stream_extendBtId.add(extendBtId12.getTree());

		// AST REWRITE
		// elements: EXTENDS, extendBtId
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 42:3: -> ^( '_extends' extendBtId )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:42:6:
		    // ^( '_extends' extendBtId )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(EXTENDS, "EXTENDS"),
				root_1);

			adaptor.addChild(root_1, stream_extendBtId.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "btExtends"

    public static class task_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "task"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:45:1: task
    // : taskId '{' pre post ( pp )? role '}' ';' -> ^( '_Task' taskId pre post
    // pp role ) ;
    public final behavParser.task_return task() throws RecognitionException {
	behavParser.task_return retval = new behavParser.task_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token char_literal14 = null;
	Token char_literal19 = null;
	Token char_literal20 = null;
	behavParser.taskId_return taskId13 = null;

	behavParser.pre_return pre15 = null;

	behavParser.post_return post16 = null;

	behavParser.pp_return pp17 = null;

	behavParser.role_return role18 = null;

	Object char_literal14_tree = null;
	Object char_literal19_tree = null;
	Object char_literal20_tree = null;
	RewriteRuleTokenStream stream_RBRACK = new RewriteRuleTokenStream(
		adaptor, "token RBRACK");
	RewriteRuleTokenStream stream_LBRACK = new RewriteRuleTokenStream(
		adaptor, "token LBRACK");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_taskId = new RewriteRuleSubtreeStream(
		adaptor, "rule taskId");
	RewriteRuleSubtreeStream stream_post = new RewriteRuleSubtreeStream(
		adaptor, "rule post");
	RewriteRuleSubtreeStream stream_pre = new RewriteRuleSubtreeStream(
		adaptor, "rule pre");
	RewriteRuleSubtreeStream stream_role = new RewriteRuleSubtreeStream(
		adaptor, "rule role");
	RewriteRuleSubtreeStream stream_pp = new RewriteRuleSubtreeStream(
		adaptor, "rule pp");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:45:5:
	    // ( taskId '{' pre post ( pp )? role '}' ';' -> ^( '_Task' taskId
	    // pre post pp role ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:45:7:
	    // taskId '{' pre post ( pp )? role '}' ';'
	    {
		pushFollow(FOLLOW_taskId_in_task279);
		taskId13 = taskId();

		state._fsp--;

		stream_taskId.add(taskId13.getTree());

		char_literal14 = (Token) match(input, LBRACK,
			FOLLOW_LBRACK_in_task283);
		stream_LBRACK.add(char_literal14);

		pushFollow(FOLLOW_pre_in_task285);
		pre15 = pre();

		state._fsp--;

		stream_pre.add(pre15.getTree());

		pushFollow(FOLLOW_post_in_task287);
		post16 = post();

		state._fsp--;

		stream_post.add(post16.getTree());

		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:45:30:
		// ( pp )?
		int alt4 = 2;
		int LA4_0 = input.LA(1);

		if ((LA4_0 == 31)) {
		    alt4 = 1;
		}
		switch (alt4) {
		case 1:
		// E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:45:30:
		// pp
		{
		    pushFollow(FOLLOW_pp_in_task290);
		    pp17 = pp();

		    state._fsp--;

		    stream_pp.add(pp17.getTree());

		}
		    break;

		}

		pushFollow(FOLLOW_role_in_task293);
		role18 = role();

		state._fsp--;

		stream_role.add(role18.getTree());

		char_literal19 = (Token) match(input, RBRACK,
			FOLLOW_RBRACK_in_task295);
		stream_RBRACK.add(char_literal19);

		char_literal20 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_task298);
		stream_SEMI.add(char_literal20);

		// AST REWRITE
		// elements: post, role, pp, pre, taskId, TASK
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 46:3: -> ^( '_Task' taskId pre post pp role )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:46:6:
		    // ^( '_Task' taskId pre post pp role )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(TASK, "TASK"), root_1);

			adaptor.addChild(root_1, stream_taskId.nextTree());

			adaptor.addChild(root_1, stream_pre.nextTree());

			adaptor.addChild(root_1, stream_post.nextTree());

			adaptor.addChild(root_1, stream_pp.nextTree());

			adaptor.addChild(root_1, stream_role.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "task"

    public static class taskId_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "taskId"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:48:1:
    // taskId : 'Task' taskName -> ^( '_taskName' taskName ) ;
    public final behavParser.taskId_return taskId() throws RecognitionException {
	behavParser.taskId_return retval = new behavParser.taskId_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal21 = null;
	behavParser.taskName_return taskName22 = null;

	Object string_literal21_tree = null;
	RewriteRuleTokenStream stream_28 = new RewriteRuleTokenStream(adaptor,
		"token 28");
	RewriteRuleSubtreeStream stream_taskName = new RewriteRuleSubtreeStream(
		adaptor, "rule taskName");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:48:8:
	    // ( 'Task' taskName -> ^( '_taskName' taskName ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:48:11:
	    // 'Task' taskName
	    {
		string_literal21 = (Token) match(input, 28,
			FOLLOW_28_in_taskId338);
		stream_28.add(string_literal21);

		pushFollow(FOLLOW_taskName_in_taskId340);
		taskName22 = taskName();

		state._fsp--;

		stream_taskName.add(taskName22.getTree());

		// AST REWRITE
		// elements: TASKNAME, taskName
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 49:3: -> ^( '_taskName' taskName )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:49:6:
		    // ^( '_taskName' taskName )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(TASKNAME, "TASKNAME"),
				root_1);

			adaptor.addChild(root_1, stream_taskName.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "taskId"

    public static class pre_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "pre"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:53:1: pre
    // : 'pre' ePattern ';' -> ^( '_pre' ePattern ) ;
    public final behavParser.pre_return pre() throws RecognitionException {
	behavParser.pre_return retval = new behavParser.pre_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal23 = null;
	Token char_literal25 = null;
	behavParser.ePattern_return ePattern24 = null;

	Object string_literal23_tree = null;
	Object char_literal25_tree = null;
	RewriteRuleTokenStream stream_32 = new RewriteRuleTokenStream(adaptor,
		"token 32");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_ePattern = new RewriteRuleSubtreeStream(
		adaptor, "rule ePattern");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:53:4:
	    // ( 'pre' ePattern ';' -> ^( '_pre' ePattern ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:53:6:
	    // 'pre' ePattern ';'
	    {
		string_literal23 = (Token) match(input, 32, FOLLOW_32_in_pre362);
		stream_32.add(string_literal23);

		pushFollow(FOLLOW_ePattern_in_pre366);
		ePattern24 = ePattern();

		state._fsp--;

		stream_ePattern.add(ePattern24.getTree());

		char_literal25 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_pre369);
		stream_SEMI.add(char_literal25);

		// AST REWRITE
		// elements: ePattern, PRE
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 54:3: -> ^( '_pre' ePattern )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:54:6:
		    // ^( '_pre' ePattern )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(PRE, "PRE"), root_1);

			adaptor.addChild(root_1, stream_ePattern.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "pre"

    public static class post_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "post"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:56:1: post
    // : 'post' ePattern ';' -> ^( '_post' ePattern ) ;
    public final behavParser.post_return post() throws RecognitionException {
	behavParser.post_return retval = new behavParser.post_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal26 = null;
	Token char_literal28 = null;
	behavParser.ePattern_return ePattern27 = null;

	Object string_literal26_tree = null;
	Object char_literal28_tree = null;
	RewriteRuleTokenStream stream_30 = new RewriteRuleTokenStream(adaptor,
		"token 30");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_ePattern = new RewriteRuleSubtreeStream(
		adaptor, "rule ePattern");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:56:5:
	    // ( 'post' ePattern ';' -> ^( '_post' ePattern ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:56:7:
	    // 'post' ePattern ';'
	    {
		string_literal26 = (Token) match(input, 30,
			FOLLOW_30_in_post390);
		stream_30.add(string_literal26);

		pushFollow(FOLLOW_ePattern_in_post394);
		ePattern27 = ePattern();

		state._fsp--;

		stream_ePattern.add(ePattern27.getTree());

		char_literal28 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_post398);
		stream_SEMI.add(char_literal28);

		// AST REWRITE
		// elements: ePattern, POST
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 57:3: -> ^( '_post' ePattern )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:57:6:
		    // ^( '_post' ePattern )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(POST, "POST"), root_1);

			adaptor.addChild(root_1, stream_ePattern.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "post"

    public static class pp_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "pp"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:59:1: pp :
    // 'pp' ppVal ';' -> ^( '_pp' ppVal ) ;
    public final behavParser.pp_return pp() throws RecognitionException {
	behavParser.pp_return retval = new behavParser.pp_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal29 = null;
	Token char_literal31 = null;
	behavParser.ppVal_return ppVal30 = null;

	Object string_literal29_tree = null;
	Object char_literal31_tree = null;
	RewriteRuleTokenStream stream_31 = new RewriteRuleTokenStream(adaptor,
		"token 31");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_ppVal = new RewriteRuleSubtreeStream(
		adaptor, "rule ppVal");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:59:3:
	    // ( 'pp' ppVal ';' -> ^( '_pp' ppVal ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:59:6:
	    // 'pp' ppVal ';'
	    {
		string_literal29 = (Token) match(input, 31, FOLLOW_31_in_pp420);
		stream_31.add(string_literal29);

		pushFollow(FOLLOW_ppVal_in_pp422);
		ppVal30 = ppVal();

		state._fsp--;

		stream_ppVal.add(ppVal30.getTree());

		char_literal31 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_pp425);
		stream_SEMI.add(char_literal31);

		// AST REWRITE
		// elements: ppVal, PP
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 60:3: -> ^( '_pp' ppVal )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:60:6:
		    // ^( '_pp' ppVal )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(PP, "PP"), root_1);

			adaptor.addChild(root_1, stream_ppVal.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "pp"

    public static class role_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "role"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:62:1: role
    // : 'role' rObligVal ';' -> ^( '_role' rObligVal ) ;
    public final behavParser.role_return role() throws RecognitionException {
	behavParser.role_return retval = new behavParser.role_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token string_literal32 = null;
	Token char_literal34 = null;
	behavParser.rObligVal_return rObligVal33 = null;

	Object string_literal32_tree = null;
	Object char_literal34_tree = null;
	RewriteRuleTokenStream stream_33 = new RewriteRuleTokenStream(adaptor,
		"token 33");
	RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(
		adaptor, "token SEMI");
	RewriteRuleSubtreeStream stream_rObligVal = new RewriteRuleSubtreeStream(
		adaptor, "rule rObligVal");
	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:62:5:
	    // ( 'role' rObligVal ';' -> ^( '_role' rObligVal ) )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:62:7:
	    // 'role' rObligVal ';'
	    {
		string_literal32 = (Token) match(input, 33,
			FOLLOW_33_in_role446);
		stream_33.add(string_literal32);

		pushFollow(FOLLOW_rObligVal_in_role450);
		rObligVal33 = rObligVal();

		state._fsp--;

		stream_rObligVal.add(rObligVal33.getTree());

		char_literal34 = (Token) match(input, SEMI,
			FOLLOW_SEMI_in_role453);
		stream_SEMI.add(char_literal34);

		// AST REWRITE
		// elements: rObligVal, ROLE
		// token labels:
		// rule labels: retval
		// token list labels:
		// rule list labels:
		// wildcard labels:
		retval.tree = root_0;
		RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
			adaptor, "rule retval", retval != null ? retval.tree
				: null);

		root_0 = (Object) adaptor.nil();
		// 63:3: -> ^( '_role' rObligVal )
		{
		    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:63:6:
		    // ^( '_role' rObligVal )
		    {
			Object root_1 = (Object) adaptor.nil();
			root_1 = (Object) adaptor.becomeRoot(
				(Object) adaptor.create(ROLE, "ROLE"), root_1);

			adaptor.addChild(root_1, stream_rObligVal.nextTree());

			adaptor.addChild(root_0, root_1);
		    }

		}

		retval.tree = root_0;

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "role"

    public static class ePattern_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "ePattern"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:66:1:
    // ePattern : STRING ;
    public final behavParser.ePattern_return ePattern()
	    throws RecognitionException {
	behavParser.ePattern_return retval = new behavParser.ePattern_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token STRING35 = null;

	Object STRING35_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:66:10:
	    // ( STRING )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:66:12:
	    // STRING
	    {
		root_0 = (Object) adaptor.nil();

		STRING35 = (Token) match(input, STRING,
			FOLLOW_STRING_in_ePattern476);
		STRING35_tree = (Object) adaptor.create(STRING35);
		adaptor.addChild(root_0, STRING35_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "ePattern"

    public static class btName_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "btName"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:68:1:
    // btName : WORD ;
    public final behavParser.btName_return btName() throws RecognitionException {
	behavParser.btName_return retval = new behavParser.btName_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD36 = null;

	Object WORD36_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:68:7:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:68:9:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD36 = (Token) match(input, WORD, FOLLOW_WORD_in_btName484);
		WORD36_tree = (Object) adaptor.create(WORD36);
		adaptor.addChild(root_0, WORD36_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "btName"

    public static class extendBtId_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "extendBtId"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:69:1:
    // extendBtId : WORD ;
    public final behavParser.extendBtId_return extendBtId()
	    throws RecognitionException {
	behavParser.extendBtId_return retval = new behavParser.extendBtId_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD37 = null;

	Object WORD37_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:69:12:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:69:14:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD37 = (Token) match(input, WORD,
			FOLLOW_WORD_in_extendBtId493);
		WORD37_tree = (Object) adaptor.create(WORD37);
		adaptor.addChild(root_0, WORD37_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "extendBtId"

    public static class taskName_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "taskName"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:70:1:
    // taskName : WORD ;
    public final behavParser.taskName_return taskName()
	    throws RecognitionException {
	behavParser.taskName_return retval = new behavParser.taskName_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD38 = null;

	Object WORD38_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:70:10:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:70:12:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD38 = (Token) match(input, WORD, FOLLOW_WORD_in_taskName501);
		WORD38_tree = (Object) adaptor.create(WORD38);
		adaptor.addChild(root_0, WORD38_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "taskName"

    public static class eppreVal_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "eppreVal"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:72:1:
    // eppreVal : WORD ;
    public final behavParser.eppreVal_return eppreVal()
	    throws RecognitionException {
	behavParser.eppreVal_return retval = new behavParser.eppreVal_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD39 = null;

	Object WORD39_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:72:9:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:72:11:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD39 = (Token) match(input, WORD, FOLLOW_WORD_in_eppreVal509);
		WORD39_tree = (Object) adaptor.create(WORD39);
		adaptor.addChild(root_0, WORD39_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "eppreVal"

    public static class eppostVal_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "eppostVal"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:73:1:
    // eppostVal : WORD ;
    public final behavParser.eppostVal_return eppostVal()
	    throws RecognitionException {
	behavParser.eppostVal_return retval = new behavParser.eppostVal_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD40 = null;

	Object WORD40_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:73:10:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:73:12:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD40 = (Token) match(input, WORD, FOLLOW_WORD_in_eppostVal516);
		WORD40_tree = (Object) adaptor.create(WORD40);
		adaptor.addChild(root_0, WORD40_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "eppostVal"

    public static class ppVal_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "ppVal"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:74:1:
    // ppVal : WORD ;
    public final behavParser.ppVal_return ppVal() throws RecognitionException {
	behavParser.ppVal_return retval = new behavParser.ppVal_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD41 = null;

	Object WORD41_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:74:6:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:74:8:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD41 = (Token) match(input, WORD, FOLLOW_WORD_in_ppVal523);
		WORD41_tree = (Object) adaptor.create(WORD41);
		adaptor.addChild(root_0, WORD41_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "ppVal"

    public static class rObligVal_return extends ParserRuleReturnScope {
	Object tree;

	public Object getTree() {
	    return tree;
	}
    };

    // $ANTLR start "rObligVal"
    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:75:1:
    // rObligVal : WORD ;
    public final behavParser.rObligVal_return rObligVal()
	    throws RecognitionException {
	behavParser.rObligVal_return retval = new behavParser.rObligVal_return();
	retval.start = input.LT(1);

	Object root_0 = null;

	Token WORD42 = null;

	Object WORD42_tree = null;

	try {
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:75:10:
	    // ( WORD )
	    // E:\\ROAD\\workspaceIndigo\\BehavScripting\\src\\behav\\behav.g:75:12:
	    // WORD
	    {
		root_0 = (Object) adaptor.nil();

		WORD42 = (Token) match(input, WORD, FOLLOW_WORD_in_rObligVal530);
		WORD42_tree = (Object) adaptor.create(WORD42);
		adaptor.addChild(root_0, WORD42_tree);

	    }

	    retval.stop = input.LT(-1);

	    retval.tree = (Object) adaptor.rulePostProcessing(root_0);
	    adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

	} catch (RecognitionException re) {
	    reportError(re);
	    recover(input, re);
	    retval.tree = (Object) adaptor.errorNode(input, retval.start,
		    input.LT(-1), re);

	}

	finally {
	    // do for sure before leaving
	}
	return retval;
    }

    // $ANTLR end "rObligVal"

    // Delegated rules

    public static final BitSet FOLLOW_bterm_in_script149 = new BitSet(
	    new long[] { 0x0000000008000000L });
    public static final BitSet FOLLOW_EOF_in_script155 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_btId_in_bterm179 = new BitSet(
	    new long[] { 0x0000000020000200L });
    public static final BitSet FOLLOW_btExtends_in_bterm182 = new BitSet(
	    new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_LBRACK_in_bterm187 = new BitSet(
	    new long[] { 0x0000000010000000L });
    public static final BitSet FOLLOW_task_in_bterm192 = new BitSet(
	    new long[] { 0x0000000010010000L });
    public static final BitSet FOLLOW_RBRACK_in_bterm198 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_bterm200 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_btId233 = new BitSet(
	    new long[] { 0x0000000001000000L });
    public static final BitSet FOLLOW_btName_in_btId235 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_29_in_btExtends255 = new BitSet(
	    new long[] { 0x0000000001000000L });
    public static final BitSet FOLLOW_extendBtId_in_btExtends257 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_taskId_in_task279 = new BitSet(
	    new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_LBRACK_in_task283 = new BitSet(
	    new long[] { 0x0000000100000000L });
    public static final BitSet FOLLOW_pre_in_task285 = new BitSet(
	    new long[] { 0x0000000040000000L });
    public static final BitSet FOLLOW_post_in_task287 = new BitSet(
	    new long[] { 0x0000000280000000L });
    public static final BitSet FOLLOW_pp_in_task290 = new BitSet(
	    new long[] { 0x0000000200000000L });
    public static final BitSet FOLLOW_role_in_task293 = new BitSet(
	    new long[] { 0x0000000000010000L });
    public static final BitSet FOLLOW_RBRACK_in_task295 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_task298 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_28_in_taskId338 = new BitSet(
	    new long[] { 0x0000000001000000L });
    public static final BitSet FOLLOW_taskName_in_taskId340 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_32_in_pre362 = new BitSet(
	    new long[] { 0x0000000000200000L });
    public static final BitSet FOLLOW_ePattern_in_pre366 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_pre369 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_30_in_post390 = new BitSet(
	    new long[] { 0x0000000000200000L });
    public static final BitSet FOLLOW_ePattern_in_post394 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_post398 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_31_in_pp420 = new BitSet(
	    new long[] { 0x0000000001000000L });
    public static final BitSet FOLLOW_ppVal_in_pp422 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_pp425 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_33_in_role446 = new BitSet(
	    new long[] { 0x0000000001000000L });
    public static final BitSet FOLLOW_rObligVal_in_role450 = new BitSet(
	    new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_SEMI_in_role453 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_in_ePattern476 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_btName484 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_extendBtId493 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_taskName501 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_eppreVal509 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_eppostVal516 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_ppVal523 = new BitSet(
	    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_WORD_in_rObligVal530 = new BitSet(
	    new long[] { 0x0000000000000002L });

}