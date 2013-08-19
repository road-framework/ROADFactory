package au.edu.swin.ict.road.composite.message.analyzer;

import java.util.ArrayList;
import java.util.List;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.message.MessageWrapper.SyncType;

/**
 * This class holds all the information that is required to perform either the
 * disjunct or conjunct transformations. The instance of this class is prepared
 * by the InTransform and OutTransform classes and passed to the disjunct and
 * conjunct methods of the MessageAnalyzer.
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */

public class TransformLogic {
    private List<String> xsltFilePaths;
    private Role role;
    private String deliveryType;
    private String taskId;
    private String operationName;
    private boolean isResponse;
    private SyncType syncType;

    public TransformLogic() {
	xsltFilePaths = new ArrayList<String>();
	role = null;
	deliveryType = null;
	taskId = null;
    }

    public List<String> getXsltFilePath() {
	return xsltFilePaths;
    }

    public void addXsltFilePath(String xsltFilePath) {
	this.xsltFilePaths.add(xsltFilePath);
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public String getDeliveryType() {
	return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
	this.deliveryType = deliveryType;
    }

    public String getTaskId() {
	return taskId;
    }

    public void setTaskId(String taskId) {
	this.taskId = taskId;
    }

    public void setOperationName(String operationName) {
	this.operationName = operationName;
    }

    public String getOperationName() {
	return operationName;
    }

    public boolean isResponse() {
	return isResponse;
    }

    public void setResponse(boolean isResponse) {
	this.isResponse = isResponse;
    }

    public void setSyncType(SyncType syncType) {
	this.syncType = syncType;
    }

    public SyncType getSyncType() {
	return syncType;
    }
}
