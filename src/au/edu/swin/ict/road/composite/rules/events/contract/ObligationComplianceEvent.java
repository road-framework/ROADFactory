package au.edu.swin.ict.road.composite.rules.events.contract;

public class ObligationComplianceEvent {

    private String term;
    private String obligation;
    private int level;

    public String getTerm() {
	return term;
    }

    public void setTerm(String term) {
	this.term = term;
    }

    public String getObligation() {
	return obligation;
    }

    public void setObligation(String obligation) {
	this.obligation = obligation;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

}
