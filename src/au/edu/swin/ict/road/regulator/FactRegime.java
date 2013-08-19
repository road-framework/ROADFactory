/**
 * 
 */
package au.edu.swin.ict.road.regulator;

/**
 * @author Aditya
 * 
 */
public class FactRegime {

    private boolean isContextProvider;
    private boolean isMonitor;
    private int acquisitionSyncInterval;
    private int provisionSyncInterval;
    private boolean onChange;

    /**
     * @param isContextProvider
     * @param isMonitor
     * @param acquisitionSyncInterval
     * @param provisionSyncInterval
     * @param onChange
     */
    public FactRegime(boolean isContextProvider, boolean isMonitor,
	    int acquisitionSyncInterval, int provisionSyncInterval,
	    boolean onChange) {
	super();
	this.isContextProvider = isContextProvider;
	this.isMonitor = isMonitor;
	this.acquisitionSyncInterval = acquisitionSyncInterval;
	this.provisionSyncInterval = provisionSyncInterval;
	this.onChange = onChange;
    }

    public boolean isContextProvider() {
	return isContextProvider;
    }

    public void setContextProvider(boolean isContextProvider) {
	this.isContextProvider = isContextProvider;
    }

    public boolean isMonitor() {
	return isMonitor;
    }

    public void setMonitor(boolean isMonitor) {
	this.isMonitor = isMonitor;
    }

    public int getAcquisitionSyncInterval() {
	return acquisitionSyncInterval;
    }

    public void setAcquisitionSyncInterval(int acquisitionSyncInterval) {
	this.acquisitionSyncInterval = acquisitionSyncInterval;
    }

    public int getProvisionSyncInterval() {
	return provisionSyncInterval;
    }

    public void setProvisionSyncInterval(int provisionSyncInterval) {
	this.provisionSyncInterval = provisionSyncInterval;
    }

    public boolean isOnChange() {
	return onChange;
    }

    public void setOnChange(boolean onChange) {
	this.onChange = onChange;
    }

}
