package au.edu.swin.ict.serendip.epc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.processmining.analysis.epcmerge.EPCMergeMethod;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCConnector;
import org.processmining.framework.models.epcpack.EPCEdge;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;
import org.processmining.framework.models.epcpack.EPCObject;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.epc.test.MergeTest;
import au.edu.swin.ict.serendip.tool.gui.SerendipEPCView;

/**
 * Merge multiple behavior terms based on the event identifiers
 * 
 * @author Malinda Kapuruge
 * 
 */
public class MergeBehavior {
    static Logger logger = Logger.getLogger(MergeBehavior.class);
    ConfigurableEPC[] epcs = null;
    ConfigurableEPC finalMergedEPC = null;

    public MergeBehavior(BehaviorTerm[] bts) {
	ArrayList<ConfigurableEPC> epcList = new ArrayList<ConfigurableEPC>();
	for (int i = 0; i < bts.length; i++) {
	    epcList.add(bts[i].constructEPC());
	}

	this.epcs = new ConfigurableEPC[epcList.size()];
	epcList.toArray(this.epcs);

	// Merge
	this.mergeAll();

    }

    public MergeBehavior(ConfigurableEPC[] epcs) {
	this.epcs = epcs;
	this.mergeAll();
    }

    /**
     * Merge all the behavior terms by going thru one by one
     */
    private void mergeAll() {

	if (epcs.length == 0) {
	    finalMergedEPC = null;
	    return;
	} else if (epcs.length == 1) {
	    finalMergedEPC = this.epcs[0];
	    return;
	}

	// Initialize
	ConfigurableEPC curEPC = this.epcs[0];
	for (int i = 1; i < epcs.length; i++) {
	    logger.debug("merging step " + i);
	    curEPC = mergeEPC(curEPC, epcs[i]);
	}

	this.finalMergedEPC = curEPC;
	// Finally we have all the epcs merged.
    }

    /**
     * 
     * @return Final merged EPC
     */
    public ConfigurableEPC getMergedBehaviorAsEPC() {
	return this.finalMergedEPC;
    }

    // For a given epc merge event e1 and e2
    // NOTE: Always delete first. There are restrictions of incoming and
    // outgoing arcs
    // e.g., The number of incoming arcs at each function should be at most 1
    // See more in ConfigurableEPC:198 addEdge()
    public static ConfigurableEPC mergeEvents(EPCEvent e1, EPCEvent e2,
	    ConfigurableEPC epc) {
	ArrayList<EPCObject> e1PreArr = epc.getPreceedingElements(e1);
	ArrayList<EPCObject> e2PreArr = epc.getPreceedingElements(e2);
	ArrayList<EPCObject> e1SucArr = epc.getSucceedingElements(e1);
	ArrayList<EPCObject> e2SucArr = epc.getSucceedingElements(e2);
	boolean delE1 = false, delE2 = false;
	// Process predecessors
	/**
	 * For EPCEvents there should be only **ONE** predecessor, which could
	 * be either null, Function, or Connector(AND, OR, XOR)
	 */

	EPCObject e1PreObject = (e1PreArr.size() < 1) ? null : e1PreArr.get(0);
	EPCObject e2PreObject = (e2PreArr.size() < 1) ? null : e2PreArr.get(0);

	if ((null == e1PreObject) && (null == e2PreObject)) {
	    // Both do not have predecessors.
	    // We do nothing. But e2 will be delted later
	} else if ((null != e1PreObject) && (null == e2PreObject)) {
	    // E1 has a predecessor and E2 not
	    // We do nothing. But e2 will be deleted later
	} else if ((null == e1PreObject) && (null != e2PreObject)) {
	    // E2 has a predecessor and E1 not.
	    // Since E2 is going to be delted later, attach its predeccessor to
	    // E1
	    // We delete e2pre -> e2 edge. Add e2pre ->e1 edge

	    epc.delEdge(e2PreObject, e2);
	    epc.addEdge(e2PreObject, e1);

	} else if ((null != e1PreObject) && (null != e2PreObject)) {
	    // Both E1 and E2 has predecessors. So we need to add a OR new
	    // connector.
	    // Introduce a new connector and three more edges. e1pre->OR,
	    // e2pre->OR, OR->e1
	    // Delete previous two edges e1pre->e1, e2pre->e2
	    EPCConnector orConnect = epc.addConnector(new EPCConnector(
		    EPCConnector.OR, epc));

	    epc.delEdge(e1PreObject, e1);
	    epc.delEdge(e2PreObject, e2);

	    EPCEdge orToEventEdge = epc.addEdge(orConnect, e1);
	    EPCEdge e1PreToOREdge = epc.addEdge(e1PreObject, orConnect);
	    EPCEdge e2PreToOREdge = epc.addEdge(e2PreObject, orConnect);

	}

	// Process successors
	EPCObject e1SucObject = (e1SucArr.size() < 1) ? null : e1SucArr.get(0);
	EPCObject e2SucObject = (e2SucArr.size() < 1) ? null : e2SucArr.get(0);

	if ((null == e1SucObject) && (null == e2SucObject)) {
	    // Both do not have successors
	    // We do nothing
	} else if ((null != e1SucObject) && (null == e2SucObject)) {
	    // E1 has a successor and E2 not
	    // We do nothing. But e2 will be deleted later

	} else if ((null == e1SucObject) && (null != e2SucObject)) {
	    // E2 has a successor and E1 not
	    // Since E2 is going to be deleted later, attach is successors to E1
	    // We delete e2->e2suc edge. and introduce a new edge e1->e2suc

	    epc.delEdge(e2, e2SucObject);
	    epc.addEdge(e1, e2SucObject);

	} else if ((null != e1SucObject) && (null != e2SucObject)) {
	    // Introduce a new AND connector and three more edges. e1->AND,
	    // AND->e1Suc, AND->e2Suc
	    // Delete previous two edges. e1->e1Suc, e2->e2Suc
	    EPCConnector andConnect = epc.addConnector(new EPCConnector(
		    EPCConnector.AND, epc));

	    epc.delEdge(e1, e1SucObject);
	    epc.delEdge(e2, e2SucObject);

	    EPCEdge andToE1SucEdge = epc.addEdge(andConnect, e1SucObject);
	    EPCEdge andToE2SucEdge = epc.addEdge(andConnect, e2SucObject);
	    EPCEdge e1ToAndConnectorEdge = epc.addEdge(e1, andConnect);

	}

	epc.delEvent(e2);// Always delete event e2 as now it is isolated
	return epc;
    }

    /**
     * Testing
     * 
     * @param net1
     * @param net2
     * @return
     */
    private static ConfigurableEPC preMergeEPC(ConfigurableEPC net1,
	    ConfigurableEPC net2) {
 

	HashMap<Long, EPCFunction> org2new = new HashMap<Long, EPCFunction>();
	net1.copyAllFrom(net2, org2new);
	return net1;
    }

    public static ConfigurableEPC mergeEPC(ConfigurableEPC epc1,
	    ConfigurableEPC epc2) {
	if(null == epc1){
	    return epc2;
	}
	if(null == epc2){
	    return epc1;
	}
	EPCMergeMethod mergeMethod = new EPCMergeMethod(epc1, epc2);
	// We now Test the new implementation preMergeEPC-Kau
	// Any probs?, revert to mergeMethod.analyse();
	ConfigurableEPC mergingEpc = preMergeEPC(epc1, epc2);// mergeMethod.analyse();

	// Testing: In case if you need to understand intermediate steps
	// MergeTest.frameIt(new SerendipEPCView( "mergingEpc",mergingEpc));

	// Now we have a merged EPC. But not the way we need. We need to
	// identify overlapped events by their identifiers. So if possible merge
	// them together

	ArrayList<EPCEvent> eventList = mergingEpc.getEvents();
	for (int i = 0; i < eventList.size(); i++) {
	    EPCEvent eCur = eventList.get(i);
	    for (int j = i + 1; j < eventList.size(); j++) {
		EPCEvent eTemp = eventList.get(j);
		if (eCur.getIdentifier().equals(eTemp.getIdentifier())) {
		    // We've got a match
		    // Merge pre-conditions
		    mergingEpc = mergeEvents(eCur, eTemp, mergingEpc);
		}
	    }
	}

	// 2. Analyze and replace
	/**
	 * TODO: Write the semantics of merging
	 */

	return mergingEpc;
    }
}
