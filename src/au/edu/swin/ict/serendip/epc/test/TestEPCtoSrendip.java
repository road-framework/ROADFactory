package au.edu.swin.ict.serendip.epc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.mining.epcmining.EPCResult;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.epc.EPCToSerendip;
import au.edu.swin.ict.serendip.epc.EPMLReader;

public class TestEPCtoSrendip {
    private static Logger log = Logger.getLogger(TestEPCtoSrendip.class
	    .getName());

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
	log.debug(" EPC Id" + epc.getIdentifier());

	EPCToSerendip.convrtEPCToSerendip(null, null, epc, null);
    }
}
