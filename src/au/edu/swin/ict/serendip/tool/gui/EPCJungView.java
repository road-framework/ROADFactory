package au.edu.swin.ict.serendip.tool.gui;

import java.util.ArrayList;

import org.processmining.framework.log.LogEvent;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCFunction;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

import au.edu.swin.ict.serendip.composition.view.SerendipView;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;

public class EPCJungView {
    private SerendipView serendipView = null;
    private ModelProviderFactory mpv = null;

    public EPCJungView(SerendipEngine engine, String id,
	    SerendipView serendipView) {

	mpv = engine.getModelFactory();
	this.serendipView = serendipView;
	this.createView();
    }

    private void createView() {
	if (null == serendipView) {
	    return;
	}
	ConfigurableEPC epc = serendipView.getViewAsEPC();

	Graph<String, String> g = new SparseMultigraph<String, String>();

	ArrayList<EPCFunction> funcList = epc.getAllFunctions((LogEvent) null);

	funcList.get(0).getEPC();

    }

}
