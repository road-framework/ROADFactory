package au.edu.swin.ict.serendip.grammar.ep;

import java.io.StringReader;
import org.apache.log4j.Logger;
import java.util.Vector;
import antlr.CommonAST;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import au.edu.swin.ict.road.composite.MessageDeliverer;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventCloud;

/**
 * This is the new implementation for the event pattern recognizer. I have used
 * my own grammar to parse event patterns. The advantages of this method
 * compared to previuos (jboolexpression library) 1. We can support XOR
 * connector 2. We do not need [] brackets for event ids
 * 
 * @author Malinda
 * 
 */
public class EventPatternRecognizer implements EventPatternParserTokenTypes {
    // get the logger
    private static Logger log = Logger.getLogger(EventPatternRecognizer.class
	    .getName());

    public static AST patternToAST(String pattern) throws RecognitionException,
	    TokenStreamException {
	String expr = pattern;
	EventPatternLexer lexer = new EventPatternLexer(new StringReader(expr));
	EventPatternParser parser = new EventPatternParser(lexer);

	parser.expression();
	CommonAST t = (CommonAST) parser.getAST();

	AST tree = parser.getAST();
	return tree;
    }

    public static boolean evaluate(AST ast, String pId, final EventCloud eCloud)
	    throws Exception {

	if (null == ast) {
	    throw new Exception("Null AST");
	} else {

	    log.debug(ast.getText());
	}

	switch (ast.getType()) {
	case EventPatternRecognizer.OR:
	    AST orChild = ast.getFirstChild();
	    AST orSib = orChild.getNextSibling();
	    if (orSib == null) {
		return evaluate(orChild, pId, eCloud);
	    } else {
		return evaluate(orChild, pId, eCloud)
			|| evaluate(orSib, pId, eCloud); // x || y
	    }

	    // break;
	case EventPatternRecognizer.AND:
	    AST andChild = ast.getFirstChild();
	    AST andSib = andChild.getNextSibling();
	    if (andSib == null) {
		return evaluate(andChild, pId, eCloud);
	    } else {
		return evaluate(andChild, pId, eCloud)
			&& evaluate(andSib, pId, eCloud);
	    }
	    // break;
	case EventPatternRecognizer.XOR:
	    AST xorChild = ast.getFirstChild();
	    AST xorSib = xorChild.getNextSibling();
	    if (xorSib == null) {
		return evaluate(xorChild, pId, eCloud);
	    } else {
		return evaluate(xorChild, pId, eCloud)
			^ evaluate(xorSib, pId, eCloud);
	    }
	    // break;
	case EventPatternRecognizer.WORD:
	    String eventId = ast.getText().trim();
	    if (eCloud.isEventRecorded(eventId, pId)) {// Check if the event is
						       // recorded
		return true;
	    } else {
		return false;
	    }
	    // break;

	default:
	    throw new Exception("Unknown token" + ast.getText());
	}

    }

    /**
     * Checks of the given event pattern is matched or not, based on the
     * triggered events.
     * 
     * @param eventPattern
     * @param pId
     * @param eCloud
     * @return
     * @throws SerendipException
     */
    public static boolean isPatternMatched(String eventPattern, String pId,
	    final EventCloud eCloud) throws SerendipException {
	AST ast = null;
	try {
	    ast = patternToAST(eventPattern);
	    if (null == ast) {
		throw new SerendipException("Cannot parse the expression. "
			+ eventPattern);
	    }
	    return evaluate(ast, pId, eCloud);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    throw new SerendipException("Pattern matching failed. "
		    + e.getMessage());
	}
    }

    public static boolean evaluateBool(AST ast) throws Exception {

	if (null == ast) {
	    throw new Exception("Null AST");
	} else {
	    log.debug(ast.getText());
	}

	switch (ast.getType()) {
	case EventPatternRecognizer.OR:
	    AST orChild = ast.getFirstChild();
	    AST orSib = orChild.getNextSibling();
	    if (orSib == null) {
		return evaluateBool(orChild);
	    } else {
		return evaluateBool(orChild) || evaluateBool(orSib); // x || y
	    }

	    // break;
	case EventPatternRecognizer.AND:
	    AST andChild = ast.getFirstChild();
	    AST andSib = andChild.getNextSibling();
	    if (andSib == null) {
		return evaluateBool(andChild);
	    } else {
		return evaluateBool(andChild) && evaluateBool(andSib);
	    }
	    // break;
	case EventPatternRecognizer.XOR:
	    AST xorChild = ast.getFirstChild();
	    AST xorSib = xorChild.getNextSibling();
	    if (xorSib == null) {
		return evaluateBool(xorChild);
	    } else {
		return evaluateBool(xorChild) ^ evaluateBool(xorSib);
	    }
	    // break;
	case EventPatternRecognizer.DIGITX:
	    String digit = ast.getText();
	    if (digit.trim().equals("0")) {
		return false;
	    } else if (digit.trim().equals("1")) {
		return true;
	    } else {
		throw new Exception("Cannot parse" + digit);
	    }
	    // break;

	default:
	    throw new Exception("Unknown token" + ast.getText());
	}// eof switch
    }

    public static void evaluateExpression(String expression)
	    throws RecognitionException, TokenStreamException, Exception {
	log.debug(expression + "=" + evaluateBool(patternToAST(expression)));
    }

    public static void main(String[] args) throws RecognitionException,
	    TokenStreamException, Exception {
	// String[] ops = {"^", "|", "*"};
	// for(String op: ops){
	// evaluateExpression("1 "+op+" 1");
	// evaluateExpression("1 "+op+" 0");
	// evaluateExpression("0 "+op+" 1");
	// evaluateExpression("0 "+op+" 0");
	// log.debug("");
	// }

	// Some extreme testings
	log.debug("Checking basic expressions");
	evaluateExpression("1 * 1");
	// evaluateExpression("0");
	//
	// evaluateExpression("(0)");
	// evaluateExpression("(1)");
	//
	// evaluateExpression(" (0 * 0) | 1 ) ");
	// evaluateExpression(" 0 * (0 | 1) ");
    }

}
