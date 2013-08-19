package au.edu.swin.ict.road.roadtest.filemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * The LogWriter which writes the message history to external files
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 */
public class FileManager {
    private static Logger log = Logger.getLogger(FileManager.class.getName());
    private PrintWriter fileWriter;
    private HashMap<String, String> fileContents;

    /**
     * This function lookup in the folder for all the xml files which represents
     * the message content which is read and kept in memory as hashmap of
     * <location,filecontent>
     * 
     * @param folderPath
     *            <code>String</code> location of folder
     */
    public void readFolder(String folderPath) {
	fileContents = new HashMap<String, String>();
	File folder = new File(folderPath);
	File[] listOfFiles = folder.listFiles();
	if (listOfFiles != null) {
	    for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isFile()) {
		    String fileName = listOfFiles[i].getName();
		    fileContents.put(folderPath + File.separator + fileName,
			    readFile(folderPath + File.separator + fileName));
		}
	    }
	}
    }

    /**
     * This function reads the file content for specific file which is done only
     * once when system is loading
     * 
     * @param fileName
     *            <code>String</code> location of the file
     * @return
     */
    private String readFile(String fileName) {
	try {
	    BufferedReader buffR = new BufferedReader(new FileReader(fileName));
	    String s, finalString = "";
	    while ((s = buffR.readLine()) != null) {
		finalString = finalString + s
			+ System.getProperty("line.separator");
	    }
	    return finalString;
	} catch (IOException e) {
	    log.debug("Exception: " + e.getMessage());
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * This function retuns the entire collection of filelocation to filecontent
     * 
     * @return <code>HashMap</code> map containing file location as keys and
     *         content of file as values
     */
    public HashMap<String, String> getFileContents() {
	return this.fileContents;
    }

    /**
     * Function retuns the location of file loaded in memory
     * 
     * @return <code>String</code> location of file
     */
    public String[] getFileLocations() {
	String[] keys = (String[]) (fileContents.keySet()
		.toArray(new String[fileContents.size()]));
	return keys;
    }

    /**
     * Function retuns the content of file loaded in memory
     * 
     * @param fileKey
     *            <code>String</code> key of file location specified in hashmap
     * @return <code>String</code> content of file
     */
    public String getFileContent(String fileKey) {
	return (String) this.fileContents.get(fileKey);
    }

    /**
     * Functionity to create new filewriter and check if file exist or not if
     * file exist it returns 'FILEEXIST' or else retuns 'FILECREATED'
     * 
     * @param filename
     *            <code>String</code> location of the file
     * @return <code>String</code> if file exist it returns 'FILEEXIST' or else
     *         retuns 'FILECREATED'
     */
    public String createFileWriter(String filename) {
	File file = new File(filename);
	try {
	    boolean exist = file.createNewFile();
	    if (!exist) {
		fileWriter = new PrintWriter(new FileWriter(filename));
		return "FILEEXIST";
	    } else
		fileWriter = new PrintWriter(new FileWriter(filename, true));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return "FILECREATED";
    }

    /**
     * Appends the content to file
     * 
     * @param linesToWrite
     *            <code>String</code> lines that needs to be saved in file
     * @return <code>boolean</code> if written succesfully
     */
    public boolean writeLinesToFile(Object[] linesToWrite) {
	try {
	    for (int i = 0; i < linesToWrite.length; i++) {
		fileWriter.println(linesToWrite[i].toString());
		fileWriter.println(System.getProperty("line.separator"));
	    }
	    fileWriter.flush();
	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
	    if (fileWriter != null)
		fileWriter.close();
	}
	return false;
    }

}
