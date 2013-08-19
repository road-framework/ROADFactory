package au.edu.swin.ict.serendip.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

public class CommonFileReader {
    public static StringBuffer readFileContents(String fileName) {
	BufferedReader reader = null;
	StringBuffer contents = new StringBuffer();
	try {
	    reader = new BufferedReader(new FileReader(new File(fileName)));
	    String text = null;
	    while ((text = reader.readLine()) != null) {
		contents.append(text).append(
			System.getProperty("line.separator"));
	    }
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e2) {
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	} finally {
	    try {
		if (reader != null) {
		    reader.close();
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
	return contents;
    }
}
