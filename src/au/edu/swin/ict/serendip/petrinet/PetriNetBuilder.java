package au.edu.swin.ict.serendip.petrinet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.processmining.converting.EPCToPetriNetConverterPlugin;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.algorithms.EPCToPetriNetConverter;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;

import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.SerendipException;

public class PetriNetBuilder {
    static Logger log = Logger.getLogger(PetriNetBuilder.class);

    public static PetriNet epcToPetriNet(ConfigurableEPC epc)
	    throws SerendipException {
	PetriNet pn = null;
	if (null == epc) {
	    throw new SerendipException("No model is available");
	}

	log.debug("Converting EPC: " + epc.getIdentifier());
	// Conversion
	// EPCToPetriNetConverterPlugin epcToPNplugin = new
	// EPCToPetriNetConverterPlugin();
	// pn = epcToPNplugin.convert(epc);

	pn = EPCToPetriNetConverter.convert(epc, new HashMap(), null, null);

	// DEBUG
	Iterator<EPCEvent> events = epc.getEvents().iterator();
	while (events.hasNext()) {
	    log.debug(">" + events.next().getIdentifier());
	}

	Iterator<Place> places = pn.getPlaces().iterator();
	while (places.hasNext()) {
	    log.debug("]" + places.next().getIdentifier());
	}
	// end DEBUG
	if (null == pn) {
	    throw new SerendipException(
		    "Cannot get the equivalent petrinet of " + epc.getId());
	}
	pn.setName(epc.getIdentifier());

	return pn;
    }

    public static void writeToFile(PetriNet pn, String fileName,
	    String[] initPlaces) throws IOException {
	FileWriter fw = new FileWriter(fileName, false);
	BufferedWriter bw = new BufferedWriter(fw);
	PetriNetWriter.write(pn, bw, fileName, initPlaces);
	bw.close();
	log.debug("Petrinet written to " + fileName);

    }
}
