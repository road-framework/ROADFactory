//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.01 at 06:11:07 PM EST 
//

package au.edu.swin.ict.road.xml.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProcessDefinitions" type="{http://www.ict.swin.edu.au/serendip/types}ProcessDefinitionsType" minOccurs="0"/>
 *         &lt;element name="BehaviorTerms" type="{http://www.ict.swin.edu.au/serendip/types}BehaviorTermsType" minOccurs="0"/>
 *         &lt;element name="Messages" type="{http://www.ict.swin.edu.au/serendip/types}MessagesType" minOccurs="0"/>
 *         &lt;element name="Events" type="{http://www.ict.swin.edu.au/serendip/types}EventsType" minOccurs="0"/>
 *         &lt;element name="Facts" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Fact" type="{http://www.swin.edu.au/ict/road/fact}FactType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Roles" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Role" type="{http://www.swin.edu.au/ict/road/role}RoleType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Contracts" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Contract" type="{http://www.swin.edu.au/ict/road/contract}ContractType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PlayerBindings" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PlayerBinding" type="{http://www.swin.edu.au/ict/road/player}PlayerBindingType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OrganiserBinding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageAnalyzers" type="{http://www.ict.swin.edu.au/serendip/types}MessageAnalyzersType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataDir" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="routingRuleFile" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="compositeRuleFile" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "processDefinitions", "behaviorTerms",
	"messages", "events", "facts", "roles", "contracts", "playerBindings",
	"organiserBinding", "description", "messageAnalyzers" })
@XmlRootElement(name = "SMC", namespace = "http://www.swin.edu.au/ict/road/smc")
public class SMC {

    @XmlElement(name = "ProcessDefinitions")
    protected ProcessDefinitionsType processDefinitions;
    @XmlElement(name = "BehaviorTerms")
    protected BehaviorTermsType behaviorTerms;
    @XmlElement(name = "Messages")
    protected MessagesType messages;
    @XmlElement(name = "Events")
    protected EventsType events;
    @XmlElement(name = "Facts")
    protected SMC.Facts facts;
    @XmlElement(name = "Roles")
    protected SMC.Roles roles;
    @XmlElement(name = "Contracts")
    protected SMC.Contracts contracts;
    @XmlElement(name = "PlayerBindings")
    protected SMC.PlayerBindings playerBindings;
    @XmlElement(name = "OrganiserBinding")
    protected String organiserBinding;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "MessageAnalyzers")
    protected MessageAnalyzersType messageAnalyzers;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String dataDir;
    @XmlAttribute(required = true)
    protected String routingRuleFile;
    @XmlAttribute(required = true)
    protected String compositeRuleFile;

    /**
     * Gets the value of the processDefinitions property.
     * 
     * @return possible object is {@link ProcessDefinitionsType }
     * 
     */
    public ProcessDefinitionsType getProcessDefinitions() {
	return processDefinitions;
    }

    /**
     * Sets the value of the processDefinitions property.
     * 
     * @param value
     *            allowed object is {@link ProcessDefinitionsType }
     * 
     */
    public void setProcessDefinitions(ProcessDefinitionsType value) {
	this.processDefinitions = value;
    }

    /**
     * Gets the value of the behaviorTerms property.
     * 
     * @return possible object is {@link BehaviorTermsType }
     * 
     */
    public BehaviorTermsType getBehaviorTerms() {
	return behaviorTerms;
    }

    /**
     * Sets the value of the behaviorTerms property.
     * 
     * @param value
     *            allowed object is {@link BehaviorTermsType }
     * 
     */
    public void setBehaviorTerms(BehaviorTermsType value) {
	this.behaviorTerms = value;
    }

    /**
     * Gets the value of the messages property.
     * 
     * @return possible object is {@link MessagesType }
     * 
     */
    public MessagesType getMessages() {
	return messages;
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param value
     *            allowed object is {@link MessagesType }
     * 
     */
    public void setMessages(MessagesType value) {
	this.messages = value;
    }

    /**
     * Gets the value of the events property.
     * 
     * @return possible object is {@link EventsType }
     * 
     */
    public EventsType getEvents() {
	return events;
    }

    /**
     * Sets the value of the events property.
     * 
     * @param value
     *            allowed object is {@link EventsType }
     * 
     */
    public void setEvents(EventsType value) {
	this.events = value;
    }

    /**
     * Gets the value of the facts property.
     * 
     * @return possible object is {@link SMC.Facts }
     * 
     */
    public SMC.Facts getFacts() {
	return facts;
    }

    /**
     * Sets the value of the facts property.
     * 
     * @param value
     *            allowed object is {@link SMC.Facts }
     * 
     */
    public void setFacts(SMC.Facts value) {
	this.facts = value;
    }

    /**
     * Gets the value of the roles property.
     * 
     * @return possible object is {@link SMC.Roles }
     * 
     */
    public SMC.Roles getRoles() {
	return roles;
    }

    /**
     * Sets the value of the roles property.
     * 
     * @param value
     *            allowed object is {@link SMC.Roles }
     * 
     */
    public void setRoles(SMC.Roles value) {
	this.roles = value;
    }

    /**
     * Gets the value of the contracts property.
     * 
     * @return possible object is {@link SMC.Contracts }
     * 
     */
    public SMC.Contracts getContracts() {
	return contracts;
    }

    /**
     * Sets the value of the contracts property.
     * 
     * @param value
     *            allowed object is {@link SMC.Contracts }
     * 
     */
    public void setContracts(SMC.Contracts value) {
	this.contracts = value;
    }

    /**
     * Gets the value of the playerBindings property.
     * 
     * @return possible object is {@link SMC.PlayerBindings }
     * 
     */
    public SMC.PlayerBindings getPlayerBindings() {
	return playerBindings;
    }

    /**
     * Sets the value of the playerBindings property.
     * 
     * @param value
     *            allowed object is {@link SMC.PlayerBindings }
     * 
     */
    public void setPlayerBindings(SMC.PlayerBindings value) {
	this.playerBindings = value;
    }

    /**
     * Gets the value of the organiserBinding property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrganiserBinding() {
	return organiserBinding;
    }

    /**
     * Sets the value of the organiserBinding property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrganiserBinding(String value) {
	this.organiserBinding = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDescription(String value) {
	this.description = value;
    }

    /**
     * Gets the value of the messageAnalyzers property.
     * 
     * @return possible object is {@link MessageAnalyzersType }
     * 
     */
    public MessageAnalyzersType getMessageAnalyzers() {
	return messageAnalyzers;
    }

    /**
     * Sets the value of the messageAnalyzers property.
     * 
     * @param value
     *            allowed object is {@link MessageAnalyzersType }
     * 
     */
    public void setMessageAnalyzers(MessageAnalyzersType value) {
	this.messageAnalyzers = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(String value) {
	this.name = value;
    }

    /**
     * Gets the value of the dataDir property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDataDir() {
	return dataDir;
    }

    /**
     * Sets the value of the dataDir property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDataDir(String value) {
	this.dataDir = value;
    }

    /**
     * Gets the value of the routingRuleFile property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoutingRuleFile() {
	return routingRuleFile;
    }

    /**
     * Sets the value of the routingRuleFile property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoutingRuleFile(String value) {
	this.routingRuleFile = value;
    }

    /**
     * Gets the value of the compositeRuleFile property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCompositeRuleFile() {
	return compositeRuleFile;
    }

    /**
     * Sets the value of the compositeRuleFile property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCompositeRuleFile(String value) {
	this.compositeRuleFile = value;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Contract" type="{http://www.swin.edu.au/ict/road/contract}ContractType" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "contract" })
    public static class Contracts {

	@XmlElement(name = "Contract")
	protected List<ContractType> contract;

	/**
	 * Gets the value of the contract property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the contract property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getContract().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ContractType }
	 * 
	 * 
	 */
	public List<ContractType> getContract() {
	    if (contract == null) {
		contract = new ArrayList<ContractType>();
	    }
	    return this.contract;
	}

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Fact" type="{http://www.swin.edu.au/ict/road/fact}FactType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "fact" })
    public static class Facts {

	@XmlElement(name = "Fact", required = true)
	protected List<FactType> fact;

	/**
	 * Gets the value of the fact property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the fact property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFact().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link FactType }
	 * 
	 * 
	 */
	public List<FactType> getFact() {
	    if (fact == null) {
		fact = new ArrayList<FactType>();
	    }
	    return this.fact;
	}

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="PlayerBinding" type="{http://www.swin.edu.au/ict/road/player}PlayerBindingType" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "playerBinding" })
    public static class PlayerBindings {

	@XmlElement(name = "PlayerBinding")
	protected List<PlayerBindingType> playerBinding;

	/**
	 * Gets the value of the playerBinding property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the playerBinding property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPlayerBinding().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PlayerBindingType }
	 * 
	 * 
	 */
	public List<PlayerBindingType> getPlayerBinding() {
	    if (playerBinding == null) {
		playerBinding = new ArrayList<PlayerBindingType>();
	    }
	    return this.playerBinding;
	}

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Role" type="{http://www.swin.edu.au/ict/road/role}RoleType" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "role" })
    public static class Roles {

	@XmlElement(name = "Role")
	protected List<RoleType> role;

	/**
	 * Gets the value of the role property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the role property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRole().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link RoleType }
	 * 
	 * 
	 */
	public List<RoleType> getRole() {
	    if (role == null) {
		role = new ArrayList<RoleType>();
	    }
	    return this.role;
	}

    }

}