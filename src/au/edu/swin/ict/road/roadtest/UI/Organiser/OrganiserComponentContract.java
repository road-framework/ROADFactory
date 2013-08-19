package au.edu.swin.ict.road.roadtest.UI.Organiser;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * This class represents Organiser Component that lets user to set Contract
 * related information
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserComponentContract extends JPanel {

    private JLabel jLabelID;
    private JLabel jLabelType;
    private JLabel jLabelName;
    private JLabel jLabelRules;
    private JLabel jLabelRoleA;
    private JLabel jLabelRoleB;
    private JLabel jLabelState;
    private JLabel jLabelDescription;
    private JTextField jTextFieldID;
    private JTextField jTextFieldName;
    private JTextField jTextFieldRules;
    private JTextField jTextFieldState;
    private JTextField jTextFieldDescription;

    private DefaultComboBoxModel modelBoxType;
    private DefaultComboBoxModel modelRoleA;
    private DefaultComboBoxModel modelRoleB;
    private JComboBox jComboBoxType;
    private JComboBox jComboBoxRoleA;
    private JComboBox jComboBoxRoleB;
    private JCheckBox jCheckBoxAbstract;
    private JButton jButtonSave;
    private JButton jButtonBrowse;

    private DefaultMutableTreeNode parentNode;
    private ROADTest myComposite;
    private Contract myContract;

    /**
     * Constructor for OrganiserComponentContract
     * 
     * @param obj
     *            <code>ActionListener</code> an actionlistener
     * @param myComposite
     *            <code>ROADTest</code> instance of ROADtest
     * @param node
     *            <code>DefaultMutableTreeNode</code> selected node
     */
    public OrganiserComponentContract(ROADTest myComposite,
	    DefaultMutableTreeNode node) {
	super();
	this.myComposite = myComposite;
	this.parentNode = node;
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * Function that initializes UI components for this UI
     */
    private void initializeComponent() {
	jLabelID = new JLabel("ID:");
	jLabelType = new JLabel("Type:");
	jLabelName = new JLabel("Name:");
	jLabelRules = new JLabel("Rules File:");
	jLabelRoleA = new JLabel("RoleA:");
	jLabelRoleB = new JLabel("RoleB:");
	jLabelState = new JLabel("State:");
	jLabelDescription = new JLabel("Description:");

	jTextFieldID = new JTextField();
	jTextFieldName = new JTextField();
	jTextFieldRules = new JTextField();
	jTextFieldDescription = new JTextField();
	jTextFieldState = new JTextField();
	modelBoxType = new DefaultComboBoxModel();
	modelRoleA = new DefaultComboBoxModel();
	modelRoleB = new DefaultComboBoxModel();
	jComboBoxType = new JComboBox(modelBoxType);
	jComboBoxRoleA = new JComboBox(modelRoleA);
	jComboBoxRoleB = new JComboBox(modelRoleB);

	jCheckBoxAbstract = new JCheckBox("Is Abstract?");
	jButtonSave = new JButton("Add Contract");
	jButtonBrowse = new JButton("Browse");

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }

	});
	jButtonSave.setActionCommand("Add Contract");

	jButtonBrowse.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonBrowse_actionPerformed(e);
	    }

	});

	populateRoles();

	this.setLayout(null);
	this.setBorder(new TitledBorder("Contract"));
	addComponent(this, jLabelID, 25, 35, 300, 15);
	addComponent(this, jLabelName, 25, 65, 300, 15);
	addComponent(this, jLabelDescription, 25, 95, 300, 15);
	addComponent(this, jLabelState, 25, 125, 300, 15);
	addComponent(this, jLabelRules, 25, 165, 300, 15);

	addComponent(this, jLabelType, 353, 35, 300, 18);
	addComponent(this, jLabelRoleA, 353, 65, 300, 18);
	addComponent(this, jLabelRoleB, 353, 95, 300, 18);
	addComponent(this, jCheckBoxAbstract, 443, 125, 200, 18);

	addComponent(this, jTextFieldID, 118, 35, 215, 22);
	addComponent(this, jTextFieldName, 118, 65, 215, 22);
	addComponent(this, jTextFieldDescription, 118, 95, 215, 22);
	addComponent(this, jTextFieldState, 118, 125, 215, 22);
	addComponent(this, jTextFieldRules, 118, 165, 450, 22);

	addComponent(this, jComboBoxType, 447, 35, 214, 22);
	addComponent(this, jComboBoxRoleA, 447, 65, 214, 22);
	addComponent(this, jComboBoxRoleB, 447, 95, 214, 22);

	addComponent(this, jButtonBrowse, 578, 165, 83, 22);
	addComponent(this, jButtonSave, 118, 206, 100, 28);
    }

    /**
     * This function adds the Component without a Layout Manager (Absolute
     * Positioning)
     * 
     * @param container
     *            Parent <code>Container</code> to which component needs to be
     *            added
     * @param c
     *            <code>Component</code> that needs to be added
     * @param x
     *            <code>Integer</code> x position
     * @param y
     *            <code>Integer</code> y position
     * @param width
     *            <code>Integer</code> width
     * @param height
     *            <code>Integer</code> height
     */
    private void addComponent(Container container, Component c, int x, int y,
	    int width, int height) {
	c.setBounds(x, y, width, height);
	container.add(c);
    }

    /**
     * Function that populates roles to combobox
     */
    private void populateRoles() {
	this.modelBoxType.addElement("permissive");
	Iterator itr = this.myComposite.getPlayer().iterator();
	while (itr.hasNext()) {
	    Object o = itr.next();
	    this.modelRoleA.addElement(o);
	    this.modelRoleB.addElement(o);
	}
    }

    /**
     * Functionality for browse button
     * 
     * @param e
     *            <code>ActionEvent</code> an actionevent
     */
    protected void jButtonBrowse_actionPerformed(ActionEvent e) {

	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("drl",
		"drl");
	chooser.setFileFilter(filter);
	int returnVal = chooser.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    this.jTextFieldRules.setText(chooser.getSelectedFile()
		    .getAbsolutePath());
	}

    }

    /**
     * The functionality for save button
     * 
     * @param e
     *            <code>ActionEvent</code> an actionevent
     */
    private void jButtonSave_actionPerformed(ActionEvent e) {

	String id = this.jTextFieldID.getText().trim();
	String name = this.jTextFieldName.getText().trim();
	String description = this.jTextFieldDescription.getText().trim();
	String state = this.jTextFieldState.getText().trim();
	String type = this.modelBoxType.getSelectedItem().toString();
	String str = jTextFieldRules.getText().trim();
	String ruleFile = str.substring(str.lastIndexOf(File.separator) + 1,
		str.length());
	boolean isAbstract = this.jCheckBoxAbstract.isSelected();
	String roleAId = ((Player) this.modelRoleA.getSelectedItem()).getRole()
		.getId();
	String roleBId = ((Player) this.modelRoleB.getSelectedItem()).getRole()
		.getId();

	myComposite.addNewContract(id, name, description, state, type,
		ruleFile, isAbstract, roleAId, roleBId);

    }

    /**
     * Funtion to return the instance of contract for this panel
     * 
     * @return <code>Contract</code> contract
     */
    public Contract getContract() {
	return this.myContract;
    }
}
