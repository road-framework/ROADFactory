package au.edu.swin.ict.serendip.verficiation;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.petrinet.PetriNet;

import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.composition.view.ProcessView;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.epc.EPCAnalyzer;
import au.edu.swin.ict.serendip.petrinet.PetriNetBuilder;
import au.edu.swin.ict.serendip.romeo.RomeoPNVerify;
import au.edu.swin.ict.serendip.tctl.TCTLWriter;
import au.edu.swin.ict.road.xml.bindings.*;
import au.edu.swin.ict.serendip.util.CompositionUtil;

public class PNVerification implements SerendipVerification {
    static Logger logger = Logger.getLogger(PNVerification.class);
    RomeoPNVerify pnv = null;

    public PNVerification() {
	super();

	try {
	    pnv = new RomeoPNVerify(
		    ModelProviderFactory
			    .getProperty(Constants.SERENDIP_ROMEO_EXE_PROP));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    // DONE
    public VerificationResult verify(ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException {

	Composition comp = modelProviderFactory.getEngine().getComposition();

	List<ProcessDefinitionType> processDefinitionTypeList = comp
		.getComposite().getSmcBinding().getProcessDefinitions()
		.getProcessDefinition();
	for (ProcessDefinitionType pdt : processDefinitionTypeList) {
	    VerificationResult processResult = this.verifyProcessDef(
		    modelProviderFactory, pdt.getId());
	    if (!processResult.isValid()) {
		// If FAILURE, we need to return. No more verifications.
		return processResult;
	    }
	}

	// If all are successful we are good!
	return new VerificationResult(true, "", null, null);
    }

    @Override
    public VerificationResult verifyProcessDef(
	    ModelProviderFactory modelProviderFactory, String definitionId)
	    throws SerendipVerificationException {
	ConfigurableEPC epc = null;
	VerificationResult result = null;
	ProcessView pv = null;

	Composition comp = modelProviderFactory.getEngine().getComposition();

	// Get process EPC
	try {
	    pv = modelProviderFactory.getProcessView(definitionId, null);
	} catch (SerendipException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	    throw new SerendipVerificationException(
		    "Cannot get the views for process " + definitionId + " >"
			    + e1.getMessage());
	}

	epc = pv.getViewAsEPC();

	// Get constraints for given process
	ConstraintType[] constraints = CompositionUtil.getAllConstriantsForPD(
		definitionId, true, comp);

	// Now we verifythe process as an EPC
	result = this.verifyEPC(definitionId, modelProviderFactory, epc,
		constraints);

	return result;
    }

    @Override
    public VerificationResult verifyBehavior(
	    ModelProviderFactory modelProviderFactory, String behaviorId)
	    throws SerendipVerificationException {
	ConfigurableEPC epc = null;
	VerificationResult result = null;

	Composition comp = modelProviderFactory.getEngine().getComposition();

	// Get the behavior term. Well.. we re-use method
	// getBehaviorTermsForBTIDs. but it returns MAXIMUM 1
	BehaviorTermType[] bts = CompositionUtil.getBehaviorTermsForBTIDs(
		new String[] { behaviorId }, comp);
	// If the bt cannot be found, return null
	if (bts.length < 1) {
	    return null;
	}

	epc = null;// TODO we need to get the behavior term object to get the
		   // EPC
	// Get constraints for given BT
	ConstraintType[] constraints = CompositionUtil
		.getAllConstriantsForBTs(new BehaviorTermType[] { bts[0] });

	// Now we verify the process as an EPC
	result = this.verifyEPC(behaviorId, modelProviderFactory, epc,
		constraints);

	return result;

    }

    @Override
    public VerificationResult verifyProcessInstance(ProcessInstance pi,
	    ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException {
	ConfigurableEPC epc;
	try {
	    epc = pi.getCurrentProcessView().getViewAsEPC();
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    throw new SerendipVerificationException(e.getMessage());
	}
	ConstraintType[] constraints = pi.getAllConstriants();
	VerificationResult result = this.verifyEPC(pi.getId(),
		modelProviderFactory, epc, constraints);
	return result;
    }

    // The most important method usually hidden ;-)
    // This will do the necessary verification
    private VerificationResult verifyEPC(String id,
	    ModelProviderFactory modelProviderFactory, ConfigurableEPC epc,
	    ConstraintType[] constraints) throws SerendipVerificationException {
	PetriNet pn = null;
	try {
	    pn = PetriNetBuilder.epcToPetriNet(epc);
	} catch (SerendipException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	    throw new SerendipVerificationException(
		    "Cannot build petrinet for process " + id + " >"
			    + e1.getMessage());
	}

	// Now we have a petri net. Write it to a file that is being accepted by
	// the Romeo
	String fileNamePN = modelProviderFactory.getTempFileDirectory() + "/"
		+ id + Constants.SERENDIP_PETRINET_FILE_EXT;// Should be the
							    // name of the
							    // process e.g.
							    // [composite_name]/
	String initEventIds[] = new EPCAnalyzer(epc).getInitEvents();
	try {
	    // Write petri net file
	    PetriNetBuilder.writeToFile(pn, fileNamePN, initEventIds);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    throw new SerendipVerificationException(
		    "Cannot write petrinet to for process " + id + " > "
			    + e.getMessage());
	}
	// Having the petri net written, now its time to write the constraint
	// (TCTL) files

	for (int j = 0; j < constraints.length; j++) {
	    String constraintId = constraints[j].getId();
	    String constraintExpr = constraints[j].getExpression();
	    String lang = null; // TODO get @langauge
	    // Consider only the well-defined and enabled=true constraints
	    if ((null == constraintExpr)
		    || (false == constraints[j].isEnabled())) {
		// If the constraint does not contain an expression continue
		continue;
	    }
	    try {
		String fileNameTCTL = modelProviderFactory
			.getTempFileDirectory()
			+ "/"
			+ constraintId
			+ Constants.SERENDIP_TCTL_FILE_EXT;
		// Why pass petrinet too?
		// Note that in here we need to get the node identifiers of the
		// petrinet. So we have to pass the petrinet as well.
		TCTLWriter.writeTCTL(fileNameTCTL, constraintExpr, pn);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		throw new SerendipVerificationException(e.getMessage());
	    }

	}

	// TODO contruct the properytNames array
	Vector<String> constrintsIdVec = new Vector<String>();
	for (int i = 0; i < constraints.length; i++) {
	    constrintsIdVec.add(constraints[i].getId());// The id name is used
							// to write the
							// constraint
	}
	String[] constraintsArr = new String[constrintsIdVec.size()];// Array
								     // for
								     // the
								     // verifyAll
								     // method

	try {
	    String tempFileLocation = modelProviderFactory
		    .getTempFileDirectory();
	    VerificationResult result = this.pnv.verifyAll(tempFileLocation,
		    id, constrintsIdVec.toArray(constraintsArr));
	    return result;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SerendipVerificationException(e.getMessage());
	}

    }

}
