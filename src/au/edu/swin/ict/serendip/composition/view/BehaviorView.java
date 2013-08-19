package au.edu.swin.ict.serendip.composition.view;

import java.util.Vector;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;

public class BehaviorView extends ProcessView {

    public BehaviorView(String id, BehaviorTerm bt) {
	this.id = id;
	this.btVec.add(bt);
	this.constructView();
    }

    // public BehaviorView(String id, Vector<BehaviorTerm> btVec) {
    // super(id, btVec);
    // }

    @Override
    public String toString() {

	return super.toString();
    }

}
