package au.edu.swin.ict.serendip.composition.view;

import java.util.Vector;

import org.processmining.analysis.epc.EPCSoundnessChecker;

import org.processmining.converting.epc2transitionsystem.EpcToTransitionSystem;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.plugin.ProvidedObject;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.core.SerendipException;

public class ProcessView extends SerendipView {
    Vector<String> constraintsVec = new Vector<String>();

    public ProcessView() {
	super();
    }

    public ProcessView(String id, Vector<BehaviorTerm> btVec) {
	super(id, btVec);

    }

    /**
     * Returns an HTML result via ProM
     * 
     * @see EPCSoundnessChecker
     * @return
     */
    public static String checkSoundness(ConfigurableEPC epc) {

	EPCSoundnessChecker soundChecker = new EPCSoundnessChecker();
	EpcToTransitionSystem converter = new EpcToTransitionSystem();
	String result = soundChecker
		.checkSoundness(converter.convert(epc), epc);

	return result;
    }

    public void addConstraint(String constraint) {
	this.constraintsVec.add(constraint);
    }

    @Override
    public String toString() {
	return super.toString();
    }

}
