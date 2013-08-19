package au.edu.swin.ict.road.roadtest.UI.Organiser;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * This class represents Organiser Component that lets user to set Term related
 * information
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserComponentTerm extends JPanel {

    private JLabel jLabelID;
    private JLabel jLabelType;
    private JLabel jLabelName;
    private JLabel jLabelDeonticType;
    private JLabel jLabelDirection;
    private JLabel jLabelDescription;
    private JTextField jTextFieldID;
    private JTextField jTextFieldName;
    private JTextField jTextFieldDescription;
    private JComboBox jComboBoxType;
    private JComboBox jComboBoxDeonticType;
    private JComboBox jComboBoxDirection;
    private JButton jButtonSave;
    private DefaultComboBoxModel modelMsgType;
    private DefaultComboBoxModel modelDeonticType;
    private DefaultComboBoxModel modelDirection;

    private DefaultMutableTreeNode parentNode;
    private ROADTest myComposite;
    private Term myTerm;

    /**
     * The constructor for OrganiserComponentTerm
     * 
     * @param myComposite
     *            <code>ROADTest</code> roadtest instance
     * @param node
     *            <code>DefaultMutableTreeNode</code> node selected
     */
    public OrganiserComponentTerm(ROADTest myComposite,
	    DefaultMutableTreeNode node) {
	super();
	this.myComposite = myComposite;
	this.parentNode = node;
	initializeComponent();

	this.setVisible(true);
    }

    /**
     * Functionality to initialize the components for this UI
     */
    private void initializeComponent() {
	jLabelID = new JLabel("ID:");
	jLabelType = new JLabel("Type:");
	jLabelName = new JLabel("Name:");
	jLabelDeonticType = new JLabel("Deontic Type:");
	jLabelDirection = new JLabel("Direction:");
	jLabelDescription = new JLabel("Description:");

	jTextFieldID = new JTextField();
	jTextFieldName = new JTextField();
	jTextFieldDescription = new JTextField();

	modelMsgType = new DefaultComboBoxModel();
	modelDeonticType = new DefaultComboBoxModel();
	modelDirection = new DefaultComboBoxModel();

	jComboBoxType = new JComboBox(modelMsgType);
	jComboBoxDeonticType = new JComboBox(modelDeonticType);
	jComboBoxDirection = new JComboBox(modelDirection);
	populateModels();

	jButtonSave = new JButton("Add Term");

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }

	});
	jButtonSave.setActionCommand("Add Term");

	this.setLayout(null);
	this.setBorder(new TitledBorder("Term"));
	addComponent(this, jLabelID, 25, 35, 300, 15);
	addComponent(this, jLabelName, 25, 65, 300, 15);
	addComponent(this, jLabelDescription, 25, 95, 300, 15);

	addComponent(this, jLabelType, 353, 35, 300, 18);
	addComponent(this, jLabelDeonticType, 353, 65, 300, 18);
	addComponent(this, jLabelDirection, 353, 95, 300, 18);

	addComponent(this, jTextFieldID, 118, 35, 215, 22);
	addComponent(this, jTextFieldName, 118, 65, 215, 22);
	addComponent(this, jTextFieldDescription, 118, 95, 215, 22);

	addComponent(this, jComboBoxType, 447, 35, 214, 22);
	addComponent(this, jComboBoxDeonticType, 447, 65, 214, 22);
	addComponent(this, jComboBoxDirection, 447, 95, 214, 22);

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
     * Functionlity to populate the combobox
     */
    private void populateModels() {
	this.modelMsgType.addElement("push");
	this.modelMsgType.addElement("pull");
	this.modelDeonticType.addElement("permission");
	this.modelDeonticType.addElement("obligation");
	this.modelDirection.addElement("AtoB");
	this.modelDirection.addElement("BtoA");

    }

    /**
     * Functionality for save button
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    private void jButtonSave_actionPerformed(ActionEvent e) {
	Contract c = (Contract) this.parentNode.getUserObject();
	if (c != null) {
	    String id = this.jTextFieldID.getText().trim();
	    String name = this.jTextFieldName.getText().trim();
	    String messageType = this.jComboBoxType.getSelectedItem()
		    .toString();
	    String deonticType = this.jComboBoxDeonticType.getSelectedItem()
		    .toString();
	    String description = this.jTextFieldDescription.getText().trim();
	    String direction = this.jComboBoxDirection.getSelectedItem()
		    .toString();
	    String contractId = c.getId();
	    myComposite.addNewTerm(id, name, messageType, deonticType,
		    description, direction, contractId);

	}
    }

    /**
     * Functionality for getting instance of Term for this component
     * 
     * @return <code>Term</code> term
     */
    public Term getTerm() {
	return this.myTerm;
    }
}
