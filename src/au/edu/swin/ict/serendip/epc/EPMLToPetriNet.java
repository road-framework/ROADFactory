package au.edu.swin.ict.serendip.epc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.mining.epcmining.EPCResult;
import org.processmining.mining.petrinetmining.PetriNetResult;

public class EPMLToPetriNet {
    PetriNet pn = null;

    public EPMLToPetriNet(InputStream input) throws Exception {
	SerendipEPMLImport importplugin = new SerendipEPMLImport();
	EPCResult epcResult = null;

	if (null == input) {
	    throw new Exception("Input stream is null");
	}

	epcResult = (EPCResult) importplugin.importFile(input);

	ConfigurableEPC epc = epcResult.getEPC();

	// EPCToPetriNetConverterPlugin epc2pn = new
	// EPCToPetriNetConverterPlugin();
	SerendipPetriNetConverterPlugin epc2pn = new SerendipPetriNetConverterPlugin();// Use
										       // the
										       // alternative

	ProvidedObject epcObject = new ProvidedObject("EPC",
		new Object[] { epc });
	PetriNetResult pnr = (PetriNetResult) epc2pn.convert(epcObject);

	this.pn = pnr.getPetriNet();

    }

    // get method

    public static void main(String args[]) {
	File file = new File("samples/Lager.epml");

	try {

	    FileInputStream stream = new FileInputStream(file);

	    EPMLToPetriNet e2p = new EPMLToPetriNet(stream);

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
