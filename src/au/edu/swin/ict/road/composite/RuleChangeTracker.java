package au.edu.swin.ict.road.composite;

import java.util.StringTokenizer;

/**
 * RuleChangeTracker class is used to track and store changes done to the drl
 * files by adding or deleting rules via the organiser role. This information is
 * then used to re-construct the updated drl files for the snapshot.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class RuleChangeTracker {

    private String action;
    private String drlFile;
    private String ruleInformation;
    private String ruleName;
    private String contractId;

    /**
     * Constructor for the Rule Change Tracker Class
     * 
     * @param action
     *            which signifies whether the rule was inserted or deleted
     * @param drlFile
     *            which signifies the drools file associated with the rule
     *            change
     * @param ruleInformation
     *            which contains the actual rule String
     * @param contractId
     *            which signifies the contract which the rule change is
     *            associated with
     */
    public RuleChangeTracker(String action, String drlFile,
	    String ruleInformation, String contractId) {
	super();
	this.action = action;
	this.drlFile = drlFile;
	this.ruleInformation = ruleInformation;

	// Store the name of the rule
	StringTokenizer nameTokenizer = new StringTokenizer(ruleInformation,
		"\"");

	// For inserting a rule the format of ruleInformation is - Rule
	// "<Rule Name>" <when condition> <then condition>
	// For deleting an existing rule the format of the rule information is
	// <Rule Name>
	if (action.equalsIgnoreCase("remove")) {
	    ruleName = nameTokenizer.nextToken();
	} else {
	    nameTokenizer.nextToken();
	    ruleName = nameTokenizer.nextToken();

	}

	this.contractId = contractId;

    }

    /**
     * Returns the action associated with this rule change
     * 
     * @return action associated with this rule change
     */
    public String getAction() {
	return action;
    }

    /**
     * Sets the action associated with this rule change
     * 
     * @param action
     *            String which signifies whether the rule is inserted or deleted
     *            associated with this rule change
     */
    public void setAction(String action) {
	this.action = action;
    }

    /**
     * Returns the drools file associated with this rule change
     * 
     * @return the drools file associated with this rule change
     */
    public String getDrlFile() {
	return drlFile;
    }

    /**
     * Sets the drools file associated location with this rule change
     * 
     * @param drlFile
     *            drools file location associated with this rule change
     */
    public void setDrlFile(String drlFile) {
	this.drlFile = drlFile;
    }

    /**
     * Returns the rule String associated with this rule change
     * 
     * @return ruleInformation associated with this rule change
     */
    public String getRuleInformation() {
	return ruleInformation;
    }

    /**
     * Sets the rule information associated with this rule change
     * 
     * @param ruleInformation
     *            String which contains the rule associated with this rule
     *            change
     */
    public void setRuleInformation(String ruleInformation) {
	this.ruleInformation = ruleInformation;
    }

    /**
     * Returns the rule name associated with this rule change
     * 
     * @return ruleName associated with this rule change
     */
    public String getRuleName() {
	return ruleName;
    }

    /**
     * Sets the rule name associated with this rule change
     * 
     * @param ruleInformation
     *            String which contains the rule String associated with this
     *            rule change
     */
    public void setRuleName(String ruleName) {
	this.ruleName = ruleName;
    }

    /**
     * Returns the contract ID associated with this rule change
     * 
     * @return contractId associated with this rule change
     */
    public String getContractId() {
	return contractId;
    }

    /**
     * Sets the contract Id associated with this rule change
     * 
     * @param contractId
     *            the contract id associated with this rule change
     */
    public void setContractId(String contractId) {
	this.contractId = contractId;
    }

}
