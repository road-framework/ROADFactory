package au.edu.swin.ict.road.roadtest.UI.Organiser;

/**
 * The Organiser listener interface
 * 
 * @author Abhijeet M Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public interface OrganiserListener {
    /**
     * This event is generated when the role is added by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void addRoleEvent(OrganiserEvent e);

    /**
     * This event is generated when the role is removed by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void removeRoleEvent(OrganiserEvent e);

    /**
     * This event is generated when the contract is added by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void addContractEvent(OrganiserEvent e);

    /**
     * This event is generated when the contract is removed by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void removeContractEvent(OrganiserEvent e);

    /**
     * This event is generated when the term is added by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void addTermEvent(OrganiserEvent e);

    /**
     * This event is generated when the term is removed by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void removeTermEvent(OrganiserEvent e);

    /**
     * This event is generated when the operation is added by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void addOperationEvent(OrganiserEvent e);

    /**
     * This event is generated when the operation is removed by the organiser
     * 
     * @param e
     *            <code>OrganiserEvent</code>
     */
    public void removeOperationEvent(OrganiserEvent e);

}
