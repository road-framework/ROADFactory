package au.edu.swin.ict.serendip.core.mgmt.scripting;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import antlr.CommonAST;
import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * The parser to parse scripts. Credit Alex Miller for his article at
 * http://tech
 * .puredanger.com/2007/01/13/implementing-a-scripting-language-with-antlr
 * -part-1-lexer/
 * 
 * @author Malinda
 * 
 */
public class ScriptParser {
    private static Logger log = Logger.getLogger(ScriptParser.class.getName());

    public static Script parseScript(String scriptText)
	    throws RecognitionException, TokenStreamException {
	// Reader reader = new BufferedReader(new
	// FileReader("E://ROAD/workspace/myantlr/src/kau/serscript/test.script"));
	Reader reader = new StringReader(scriptText);
	SerLexer lexer = new SerLexer(reader);
	SerParser parser = new SerParser(lexer);

	parser.script();
	CommonAST t = (CommonAST) parser.getAST();

	// Traverse the tree created by the parser
	SerWalker walker = new SerWalker();
	Script script = walker.script(t);
	return script;
    }

    public static Script parseBlock(String blockText)
	    throws RecognitionException, TokenStreamException {
	// We temporary build a script+block to parse the command
	String scriptText = blockText;
	Script script = parseScript(scriptText);
	return script;
    }

    public static void printScript(Script script) {
	List<Block> blocks = script.getBlocks();
	for (Block b : blocks) {
	    log.debug("-" + b.getName());
	    List<Command> cmds = b.getCommands();
	    for (Command c : cmds) {
		log.debug("--" + c.getName());
		Set keys = c.getProperties().keySet();
		for (Object k : keys) {

		    log.debug("---" + k + " " + c.getProperty((String) k));
		}
	    }
	}
    }

    public static void main(String[] args) {
	System.out.println("Begin");
	String str = " job1 { "
		+ "updatePropertyOfTask pid=p001 taskeId=t1 property=preEP value=\"[E1] AND [E2]\";"
		+ "" + " }";
	try {
	    Script script = parseScript(str);
	    printScript(script);
	} catch (RecognitionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TokenStreamException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
