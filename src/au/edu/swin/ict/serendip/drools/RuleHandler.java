package au.edu.swin.ict.serendip.drools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RuleHandler {
    private static final String EXT = ".drl";

    public static void createEmptyRules(String rulelocation, String[] btIds)
	    throws IOException {
	// TODO: Create empty epml files for the given btIds array. Later users
	// can load them using the editor and define the interactions
	for (int i = 0; i < btIds.length; i++) {
	    createEmptyRule(rulelocation, btIds[i]);

	}
    }

    public static void createEmptyRule(String epmllocation, String btId)
	    throws IOException {
	// Create a file for each behavior id
	File file = new File(epmllocation + "/" + btId + EXT);
	if (file.exists()) {

	    return;
	}
	FileWriter fstream = new FileWriter(file.getAbsolutePath());
	BufferedWriter out = new BufferedWriter(fstream);
	out.write("#created by Serendip\n"
		+ "#Please write how to evaluate messages in this rule \n"
		+ "package _serendib_\n" + "rule \"A stand alone rule\"\n"
		+ "when\n" + "#conditions\n" + "then\n" + "#actions\n"
		+ "end\"");
	out.close();
    }
}
