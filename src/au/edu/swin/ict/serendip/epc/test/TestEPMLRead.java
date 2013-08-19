package au.edu.swin.ict.serendip.epc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCFunction;
import org.processmining.mining.epcmining.EPCResult;

import au.edu.swin.ict.serendip.epc.EPMLReader;

public class TestEPMLRead {
    private static Logger log = Logger.getLogger(TestEPMLRead.class.getName());

    public static void main(String[] args) {
	EPCResult result = null;
	File file = new File("sample/epml/1.epml");
	try {
	    result = EPMLReader.readFile(file);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	ConfigurableEPC epc = result.getEPC();
	System.out.println(epc.getIdentifier());
	ArrayList<EPCFunction> funcs = epc.getFunctions();
	for (int i = 0; i < funcs.size(); i++) {
	    System.out.println(funcs.get(i).getIdentifier() + " "
		    + funcs.get(i).getId());
	}
    }
}
