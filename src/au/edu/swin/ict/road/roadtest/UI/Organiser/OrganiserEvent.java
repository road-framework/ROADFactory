package au.edu.swin.ict.road.roadtest.UI.Organiser;

/**
 * This class represents the OrganiserEvent
 * 
 * @author Abhijeet M. Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserEvent {
    private Object source;
    private String sourceid;

    /**
     * Constructor
     * 
     * @param sourceid
     *            <code>String</code> id of the component
     * @param source
     *            <code>Object</code> source of the event
     */
    public OrganiserEvent(String sourceid, Object source) {
	this.sourceid = sourceid;
	this.source = source;
    }

    /**
     * To get the source
     * 
     * @return <code>Object </code> source
     */
    public Object getSource() {
	return source;
    }

    /**
     * To get the source id
     * 
     * @return <code>String</code> sourceid
     */
    public String getSourceid() {
	return sourceid;
    }

}
