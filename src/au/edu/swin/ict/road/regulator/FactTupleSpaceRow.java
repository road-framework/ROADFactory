/**
 * 
 */
package au.edu.swin.ict.road.regulator;

import java.util.*;

/**
 * @author Aditya
 * 
 */
public class FactTupleSpaceRow {

    private String factType;
    private List<FactObject> factList;
    private Date time;
    private List<Object> sources;
    private List<Object> subscribers;
    private FactObject masterFact;

    public FactTupleSpaceRow(FactObject masterFact) {
	this.factType = masterFact.getFactType();
	this.factList = new ArrayList<FactObject>();
	this.time = new Date();
	this.sources = new ArrayList<Object>();
	this.subscribers = new ArrayList<Object>();
	this.masterFact = masterFact;
    }

    public FactObject getFactObjectByIdValue(String idVal) {
	for (FactObject fo : this.factList) {
	    if (fo.getIdentifierValue().equals(idVal)) {
		return fo;
	    }
	}
	return null;
    }

    public String getFactType() {
	return factType;
    }

    public void setFactType(String factType) {
	this.factType = factType;
    }

    public List<FactObject> getFactList() {
	return factList;
    }

    public void setFactList(List<FactObject> factList) {
	this.factList = factList;
    }

    public void addFact(FactObject fact) {
	this.factList.add(fact);
    }

    public boolean deleteFact(FactObject fact) {
	return this.factList.remove(fact);
    }

    public Date getTime() {
	return time;
    }

    public void setTime(Date time) {
	this.time = time;
    }

    public List<Object> getSources() {
	return sources;
    }

    public void setSources(List<Object> sources) {
	this.sources = sources;
    }

    public List<Object> getSubscribers() {
	return subscribers;
    }

    public void setSubscribers(List<Object> subscribers) {
	this.subscribers = subscribers;
    }

    public FactObject getMasterFact() {
	return masterFact;
    }

    public void setMasterFact(FactObject masterFact) {
	this.masterFact = masterFact;
    }

}
