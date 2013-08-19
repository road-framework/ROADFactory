package au.edu.swin.ict.serendip.core.mgmt.action.custom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.io.FileUtils;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.ROADDeploymentEnv;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.player.PlayerBinding;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.Constants.commands;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;
import au.edu.swin.ict.serendip.core.mgmt.action.CustomDefAdaptationAction;

/**
 * This class is a typical example for how to write a CustomAdaptation action.
 * expandTheRole er=<RID> s=<rule_file> eps=<newEndPointsCommaSeperated>
 * expandTheRole er=GR s=lbs.drl eps="http://a.com/gr, http://b.com/gr";
 * 
 * @author Malinda
 * 
 */
public class CustomDefAdaptScaleOut extends CustomDefAdaptationAction {
    public static final String RR_PREFIX = "rr";
    public static final String COMPO_PREFIX = "exp";
    public static final String DEFRULEFILE = "z.drl";
    private String roleId = null;
    private String lblStratergy = null;
    private String smcId = null;
    private List<String> newEndPoints = new ArrayList<String>();

    /**
     * 
     * @param commandName
     * @param properties
     */
    public CustomDefAdaptScaleOut(String commandName, Properties properties) {
	super(commandName, properties);

	this.roleId = super.getProperties().getProperty("er");
	// this.numOfRoles =
	// Integer.parseInt(super.getProperties().getProperty("numOfRoles"));
	this.lblStratergy = super.getProperties().getProperty("s");
	String newEpStr = super.getProperties().getProperty("eps");
	if (null != newEpStr) {
	    String[] newErrArr = newEpStr.split(",");
	    this.newEndPoints = Arrays.asList(newErrArr);
	}
	this.smcId = this.roleId + COMPO_PREFIX;
    }

    public boolean adapt(Composite comp) throws AdaptationException {
	comp.getBenchUtil().addBenchRecord("SCALE_OUT", this.roleId);
	IRole role = comp.getRoleByID(this.roleId);
	if (null == role) {
	    throw new AdaptationException("Cannot find role " + this.roleId);
	}
	// Deploy the composite
	this.writeSubComFile(comp);
	// Wait until the composite is properly deployed in the deployment
	// environment. Query the environment.
	while (true) {
	    if (comp.getRoadDepEnv().isCompositeDeployed(this.smcId)) {
		// Composite is deployed. Now we can bind the new player.
		// Bind the reference of the Router Role to the role
		this.bindPlayer(comp, role);
		return true;
	    } else {
		// Not yet. We wait.
		try {
		    System.out.println("............Wait for " + this.smcId);
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }

    private void bindPlayer(Composite comp, IRole role) {
	String playerId = this.roleId + "_binding";

	String srverAdress = "http://localhost:8080/axis2/services/";// need to
								     // get
								     // dynamically.
								     // As same
								     // as the
								     // address
								     // of the
								     // parent
								     // organisaiton:
								     // TODO
								     // comp.getCompositeProperties().get(Constants.COMP_PROP_HOST)
								     // ;
	String endPointOfRouterRole = srverAdress + this.roleId + COMPO_PREFIX
		+ "_" + this.roleId + RR_PREFIX;
	endPointOfRouterRole = endPointOfRouterRole.toLowerCase();// Need to
								  // convert to
								  // a lower
								  // case

	PlayerBinding pb = new PlayerBinding();
	pb.setId(playerId);
	pb.setName(playerId);
	pb.setEndpoint(endPointOfRouterRole);
	pb.getRoleIdList().add(this.roleId);
	comp.getPlayerBindingMap().put(playerId, pb);

	role.unBind();// unbind the existing player. How do we know whether
		      // there any incomplete tranasactions?
	// TODO: How to ensure at that point the new composition is deployed and
	// ready to be bound?
	// try {
	// Thread.sleep(10000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	role.bind(pb.getEndpoint());
    }

    /**
     * <?xml version="1.0" encoding="UTF-8"?> <tns:SMC
     * dataDir="$SMC_NAME$/data/" compositeRuleFile="composite.drl"
     * routingRuleFile="routing.drl" name="$SMC_NAME$"
     * xmlns:tns="http://www.swin.edu.au/ict/road/smc"
     * xmlns:tns1="http://www.ict.swin.edu.au/serendip/types"
     * xmlns:tns2="http://www.swin.edu.au/ict/road/term"
     * xmlns:tns3="http://www.swin.edu.au/ict/road/fact"
     * xmlns:tns4="http://www.swin.edu.au/ict/road/role"
     * xmlns:tns5="http://www.swin.edu.au/ict/road/contract"
     * xmlns:tns6="http://www.swin.edu.au/ict/road/monitor"
     * xmlns:tns7="http://www.swin.edu.au/ict/road/player"
     * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     * xsi:schemaLocation="http://www.swin.edu.au/ict/road/smc smc.xsd ">
     * 
     * <!--Process Definitions are listed below--> <ProcessDefinitions>
     * </ProcessDefinitions>
     * 
     * <!--Behavior Terms are listed below--> <BehaviorTerms> </BehaviorTerms>
     * 
     * <!--Contract Definitions will be listed below--> <Contracts> </Contracts>
     * 
     * <!--Role Definitions will be listed below--> <Roles> </Roles>
     * 
     * <!--Player bindings will be listed below--> <PlayerBindings>
     * </PlayerBindings> <OrganiserBinding>http://localhost:8080/axis2/services/
     * $SMC_NAME$_organizer</OrganiserBinding> <Description>This is the
     * descriptor for the organisation $SMC_NAME$, Generated by Serendip
     * Adaptation Engine</Description>
     * 
     * </tns:SMC>
     */
    public void writeSubComFile(Composite comp) throws AdaptationException {
	OMFactory factory = OMAbstractFactory.getOMFactory();
	OMNamespace roadNS = factory.createOMNamespace(
		"http://www.swin.edu.au/ict/road/smc", "tns");
	// Create SMC root element
	OMElement smcElem = factory.createOMElement("SMC", roadNS);// root

	// Add attributes of SMC
	OMAttribute dataDirAttr = factory.createOMAttribute("dataDir", null,
		"sample/" + this.smcId + "/data/");
	OMAttribute compositeRuleFileAttr = factory.createOMAttribute(
		"compositeRuleFile", null, "composite.drl");
	OMAttribute routingRuleFile = factory.createOMAttribute(
		"routingRuleFile", null, "routing.drl");
	OMAttribute nameAttr = factory.createOMAttribute("name", null,
		this.smcId);
	smcElem.addAttribute(dataDirAttr);
	smcElem.addAttribute(compositeRuleFileAttr);
	smcElem.addAttribute(routingRuleFile);
	smcElem.addAttribute(nameAttr);
	// We copy the composite.drl and routing.drl rule files. Composite.drl
	// file is the same.
	String dataDirOfNewComposite = comp.getRulesDir() + File.separator
		+ ".." + File.separator + ".." + File.separator + ".."
		+ File.separator + this.smcId + File.separator + "data"
		+ File.separator;
	String rulesDirOfNewComposite = dataDirOfNewComposite + File.separator
		+ "rules" + File.separator;
	String transDirOfNewComposite = dataDirOfNewComposite + File.separator
		+ "trans" + File.separator;
	// Create data directory with rules and trans subdirectories
	new File(dataDirOfNewComposite).mkdir();
	new File(rulesDirOfNewComposite).mkdir();
	new File(transDirOfNewComposite).mkdir();

	this.writeRuleFile(comp.getRulesDir() + File.separator
		+ "composite.drl", rulesDirOfNewComposite + "composite.drl");
	if (new File(comp.getRulesDir() + File.separator + this.lblStratergy)
		.isFile()) {
	    this.writeRuleFile(comp.getRulesDir() + File.separator
		    + this.lblStratergy, rulesDirOfNewComposite
		    + this.lblStratergy);
	} else {// no such stratergy file. Use default
	    this.writeRuleFile(comp.getRulesDir() + File.separator
		    + "routing.drl", "routing.drl");
	}

	// Create roles
	OMElement rolesElem = factory.createOMElement("Roles", null);// root
	smcElem.addChild(rolesElem);
	// Add router role
	String routerRoleId = this.roleId + RR_PREFIX;// <ER>RR
	{
	    OMElement roleElem = factory.createOMElement("Role", null);// root
	    OMAttribute ridAttr = factory.createOMAttribute("id", null,
		    routerRoleId);
	    OMAttribute rnameAttr = factory.createOMAttribute("name", null,
		    routerRoleId);
	    roleElem.addAttribute(ridAttr);
	    roleElem.addAttribute(rnameAttr);
	    // Finally add the role to the roles
	    rolesElem.addChild(roleElem);
	}
	// Add functional roles for each end point
	for (int i = 0; i < this.newEndPoints.size(); i++) {
	    OMElement roleElem = factory.createOMElement("Role", null);
	    OMAttribute ridAttr = factory.createOMAttribute("id", null, roleId
		    + i);// <ER>i
	    OMAttribute rnameAttr = factory.createOMAttribute("name", null,
		    roleId + i);
	    roleElem.addAttribute(ridAttr);
	    roleElem.addAttribute(rnameAttr);
	    // Add Tasks
	    // OMElement tasksElem = factory.createOMElement("Tasks",null);

	    // Finally add the role to the role collection
	    rolesElem.addChild(roleElem);
	}

	// Create contracts
	OMElement contractsElem = factory.createOMElement("Contracts", null);
	smcElem.addChild(contractsElem);
	for (int i = 0; i < this.newEndPoints.size(); i++) {
	    String contractId = routerRoleId + "_" + roleId + i;// contractId =
								// <ER>RR_<ER>i
	    OMElement contractElem = factory.createOMElement("Contract", null);
	    OMAttribute cidAttr = factory.createOMAttribute("id", null,
		    contractId);
	    OMAttribute ctypeAttr = factory.createOMAttribute("type", null,
		    "permissive");
	    OMAttribute ruleFileAttr = factory.createOMAttribute("ruleFile",
		    null, contractId + ".drl");// TODO: Create drl file
	    contractElem.addAttribute(cidAttr);
	    contractElem.addAttribute(ctypeAttr);
	    contractElem.addAttribute(ruleFileAttr);
	    // Create the default contract rule file for the new contract. It
	    // contains empty rules
	    this.writeRuleFile(comp.getRulesDir() + File.separator
		    + DEFRULEFILE, rulesDirOfNewComposite + contractId + ".drl");
	    // Copy the default rule file so that subcomposites too can expand.
	    this.writeRuleFile(comp.getRulesDir() + File.separator
		    + DEFRULEFILE, rulesDirOfNewComposite + File.separator
		    + DEFRULEFILE);

	    {// TODO: Get terms from the contracts of role=RID and add them to
	     // this contract.
		OMElement termsElem = factory.createOMElement("Terms", null);
		contractElem.addChild(termsElem);
		IRole role = comp.getRoleByID(this.roleId);
		Map<String, Contract> contracts = comp.getContractMap();
		for (Map.Entry<String, Contract> entry : contracts.entrySet()) {
		    Contract contract = entry.getValue();
		    boolean routerIsRoleA = true;// To switch the operation
						 // direction

		    if (contract.getRoleA().getId().equals(role.getId())) {
			routerIsRoleA = false;
		    } else if (contract.getRoleB().getId().equals(role.getId())) {
			routerIsRoleA = true;
		    } else {
			// Skip. Role is not part of this contract
			continue;
		    }
		    List<Term> terms = contract.getTermList();
		    for (Term term : terms) {
			// Add <Term id="complain" name="complain">
			OMElement termElem = factory.createOMElement("Term",
				null);
			termsElem.addChild(termElem);
			OMAttribute termIdAttr = factory.createOMAttribute(
				"id", null, term.getId());
			OMAttribute termNameAttr = factory.createOMAttribute(
				"name", null, term.getName());
			termElem.addAttribute(termIdAttr);
			termElem.addAttribute(termNameAttr);
			OMElement termDirElem = factory.createOMElement(
				"Direction", null);
			termElem.addChild(termDirElem);
			if (routerIsRoleA) {
			    termDirElem.setText(term.getDirection());
			} else {// Router is Role B
				// Then switch the direction
			    if (term.getDirection().equals("AtoB")) {
				termDirElem.setText("BtoA");
			    } else {// BtoA
				termDirElem.setText("AtoB");
			    }
			}
			// Add Operation
			if (null != term.getOperation()) {
			    // Add <Operation name="complain">
			    OMElement operationElem = factory.createOMElement(
				    "Operation", null);
			    termElem.addChild(operationElem);
			    OMAttribute operationNameAttr = factory
				    .createOMAttribute("name", null, term
					    .getOperation().getName());
			    operationElem.addAttribute(operationNameAttr);
			    // Add <Parameters>
			    OMElement paramsElem = factory.createOMElement(
				    "Parameters", null);
			    operationElem.addChild(paramsElem);
			    for (Parameter param : term.getOperation()
				    .getParameters()) {
				// Add <Parameter>
				OMElement paramElem = factory.createOMElement(
					"Parameter", null);
				paramsElem.addChild(paramElem);
				OMElement paramTypeElem = factory
					.createOMElement("Type", null);
				paramTypeElem.setText(param.getType());
				paramElem.addChild(paramTypeElem);
				OMElement paramNameElem = factory
					.createOMElement("Name", null);
				paramNameElem.setText(param.getName());
				paramElem.addChild(paramNameElem);
			    }
			    // Add <Return>String</Return>
			    OMElement returnElem = factory.createOMElement(
				    "Return", null);
			    operationElem.addChild(returnElem);
			    returnElem.setText(term.getOperation()
				    .getReturnType());
			}
		    }
		}
	    }
	    // Add Role A and Role B
	    OMElement roleAElem = factory.createOMElement("RoleAID", null);
	    roleAElem.setText(routerRoleId);// IMPORTANT:Router role is always
					    // RoleA
	    OMElement roleBElem = factory.createOMElement("RoleBID", null);
	    roleBElem.setText(roleId + i);
	    contractElem.addChild(roleAElem);
	    contractElem.addChild(roleBElem);
	    // Finally add the contract to the roles
	    contractsElem.addChild(contractElem);
	}

	// Create <PlayerBindings>
	OMElement pbsElem = factory.createOMElement("PlayerBindings", null);
	smcElem.addChild(pbsElem);

	for (int i = 0; i < this.newEndPoints.size(); i++) {
	    OMElement pbElem = factory.createOMElement("PlayerBinding", null);
	    OMAttribute pbidAttr = factory.createOMAttribute("id", null, roleId
		    + i + "pb");// <role_id>pb
	    pbElem.addAttribute(pbidAttr);
	    OMElement endpointElem = factory.createOMElement("Endpoint", null);
	    if (i == 0) {
		// The first role is always the currently playing player
		IRole role = comp.getRoleByID(this.roleId);
		if (null == role) {
		    endpointElem.setText("Unknown");
		} else {
		    String pb = role.getPlayerBinding();
		    endpointElem.setText(pb);
		}

	    } else {
		endpointElem.setText(this.newEndPoints.get(i - 1));
	    }
	    pbElem.addChild(endpointElem);
	    OMElement pbRolesElem = factory.createOMElement("Roles", null);
	    pbElem.addChild(pbRolesElem);
	    OMElement pbRoleIDElem = factory.createOMElement("RoleID", null);
	    pbRoleIDElem.setText(roleId + i);
	    pbRolesElem.addChild(pbRoleIDElem);
	    pbsElem.addChild(pbElem);
	}

	// Add Organizer role
	OMElement orgpbElem = factory.createOMElement("OrganiserBinding", null);
	orgpbElem.setText("organizer_endpoint");
	smcElem.addChild(orgpbElem);
	// Add Description
	OMElement descrElem = factory.createOMElement("Description", null);
	descrElem.setText("This is the descriptor for the organisation "
		+ this.smcId + ", Generated by Serendip Adaptation Tool");
	smcElem.addChild(orgpbElem);

	smcElem.build();
	// Flush
	XMLOutputFactory xof = XMLOutputFactory.newInstance();
	XMLStreamWriter writer;
	try {
	    String decrFilPath = System.getenv("AXIS2_HOME")
		    + File.separatorChar + Constants.SERENDIP_COMPOSITE_DIR
		    + File.separatorChar + this.smcId
		    + Constants.SERENDIP_COMPOSITION_FILE_EXT;
	    FileOutputStream fos = new FileOutputStream(new File(decrFilPath));
	    writer = xof.createXMLStreamWriter(fos);
	    smcElem.serialize(writer);
	    writer.flush();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new AdaptationException("Cannot write the sub composite" + e);
	}

    }

    private void writeRuleFile(String srcFileName, String tgtFileName)
	    throws AdaptationException {
	// File srcFile = new
	// File(comp.getRulesDir()+File.separator+DEFRULEFILE);
	try {
	    FileUtils.copyFile(new File(srcFileName), new File(tgtFileName));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    throw new AdaptationException("Cannot create the rule file "
		    + tgtFileName + " : " + e.getMessage());
	}
    }

    // TEST
    public static void main(String[] args) {
	System.out.println("Test1");
	Properties props = new Properties();
	props.put("er", "GR");
	props.put("s", "routing.drl ");
	props.put("eps", "http://a.com/gr, http://b.com/gr");
	CustomDefAdaptScaleOut action = new CustomDefAdaptScaleOut(
		commands.expandTheRole.name(), props);
	try {
	    action.writeSubComFile(null);
	} catch (AdaptationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
