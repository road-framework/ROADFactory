package au.edu.swin.ict.road.roadtest.UI.Organiser;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.roadtest.ROADTest;
import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;

/**
 * This class represents Organiser Component that lets user to set Operation,
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserComponentOperation extends JPanel {

    // Variables declaration
    private JLabel jLabelName;
    private JLabel jLabelType;
    private JLabel jLabelParameters;
    private JTextField jTextFieldName;
    private JTextField jTextFieldType;
    private JList jListParameters;
    private JScrollPane jScrollPane1;
    private JPanel jPanelOperation;
    private JLabel jLabelParamName;
    private JLabel jLabelParamType;
    private JTextField jTextFieldParamName;
    private JTextField jTextFieldParamType;
    private JButton jButtonAdd;
    private JButton jButtonRemove;
    private JButton jButtonAddOperation;
    private JPanel jPanelManageParam;
    private DefaultListModel modelListParams;

    private ROADTest myComposite;

    private DefaultMutableTreeNode parentNode;

    private Operation myOperation;
    private ArrayList<Parameter> myParams;

    /**
     * Constructor for OrganiserComponentOperation
     * 
     * @param myComposite
     *            <code>ROADTest</code> roadtest instance
     * @param node
     *            <code>DefaultMutableTreeNode</code> node selected
     */
    public OrganiserComponentOperation(ROADTest myComposite,
	    DefaultMutableTreeNode node) {
	super();
	myOperation = new Operation("", null, "");
	myParams = new ArrayList<Parameter>();
	this.myComposite = myComposite;
	this.parentNode = node;
	initializeComponent();

	this.setVisible(true);
    }

    /**
     * This function initializes all the components of this UI
     */
    private void initializeComponent() {

	jLabelName = new JLabel("Name:");
	jLabelType = new JLabel("Type:");
	jLabelParameters = new JLabel("Parameters:");
	jLabelParamName = new JLabel("Name:");
	jLabelParamType = new JLabel("Type:");
	jTextFieldName = new JTextField();
	jTextFieldType = new JTextField();
	jTextFieldParamName = new JTextField();
	jTextFieldParamType = new JTextField();
	modelListParams = new DefaultListModel();

	jListParameters = new JList(modelListParams);
	jScrollPane1 = new JScrollPane();

	jPanelManageParam = new JPanel();
	jPanelManageParam.setLayout(null);
	jButtonAdd = new JButton("Add");
	jButtonRemove = new JButton("Remove");
	jButtonAddOperation = new JButton("Add Operation");

	jScrollPane1.setViewportView(jListParameters);

	this.setLayout(null);
	this.setBorder(new TitledBorder("Operation"));

	addComponent(this, jLabelName, 25, 35, 34, 15);
	addComponent(this, jLabelType, 376, 35, 34, 15);
	addComponent(this, jLabelParameters, 23, 72, 60, 18);
	addComponent(this, jTextFieldName, 118, 33, 185, 22);
	addComponent(this, jTextFieldType, 460, 33, 185, 22);
	addComponent(this, jScrollPane1, 118, 72, 185, 87);
	addComponent(this, jPanelManageParam, 376, 65, 274, 90);
	addComponent(this, jButtonAddOperation, 118, 206, 150, 28);
	addComponent(this, jButtonRemove, 476, 165, 83, 28);
	addComponent(this, jButtonAdd, 376, 165, 83, 28);

	jButtonAdd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonAdd_actionPerformed(e);
	    }

	});
	jButtonAdd.setActionCommand("Add");

	jButtonRemove.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonRemove_actionPerformed(e);
	    }

	});
	jButtonRemove.setActionCommand("Remove");

	jButtonAddOperation.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonAddOperation_actionPerformed(e);
	    }

	});
	jButtonAddOperation.setActionCommand("Add Operation");

	jPanelManageParam.setBorder(new TitledBorder("Parameters"));
	addComponent(jPanelManageParam, jLabelParamName, 15, 28, 60, 18);
	addComponent(jPanelManageParam, jLabelParamType, 15, 58, 60, 18);
	addComponent(jPanelManageParam, jTextFieldParamName, 85, 28, 150, 21);
	addComponent(jPanelManageParam, jTextFieldParamType, 85, 58, 150, 21);

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
     * Functionality to add the parameter in JList
     * 
     * @param e
     *            <code>ActionEvent</code> action event e
     */
    protected void jButtonAdd_actionPerformed(ActionEvent e) {

	Parameter op = new Parameter(this.jTextFieldParamType.getText().trim(),
		this.jTextFieldParamName.getText().trim());
	myParams.add(op);
	this.modelListParams.addElement(op);
    }

    /**
     * Functionality for removing an item from JList
     * 
     * @param e
     *            <code>ActionEvent</code> action event e
     */
    protected void jButtonRemove_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	Object o = this.jListParameters.getSelectedValue();
	if (o != null) {
	    myParams.remove(o);
	    this.modelListParams.removeElement(o);
	}
    }

    /**
     * Functionality for adding operation in tree
     * 
     * @param e
     *            <code>ActionEvent</code> action event e
     */
    protected void jButtonAddOperation_actionPerformed(ActionEvent e) {

	Term t = (Term) this.parentNode.getUserObject();
	Contract c = (Contract) ((DefaultMutableTreeNode) this.parentNode
		.getParent()).getUserObject();

	if (t != null && c != null) {
	    String operationName = this.jTextFieldName.getText().trim();
	    String operationReturnType = this.jTextFieldType.getText().trim();

	    Parameter[] param = new Parameter[myParams.size()];
	    param = myParams.toArray(param);
	    String termId = t.getId();
	    myComposite.addOperation(operationName, operationReturnType, param,
		    termId, c.getId());

	    myOperation.setName(operationName);
	    myOperation.setReturnType(operationReturnType);
	    myOperation.setParameters(myParams);
	    // t.setOperation(myOperation);

	    try {
		this.myComposite.getPlayerById(c.getRoleA().getId())
			.addProvidedOperation(myOperation);
		this.myComposite.getPlayerById(c.getRoleB().getId())
			.addRequiredOperation(myOperation);
	    } catch (PlayerNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    // myComposite.getOrganier().changeOperation(myOperation,t);
	}
    }

    /**
     * Functionality to return instance of operation for current panel
     * 
     * @return <code>Operation</code> operation
     */
    private Operation getOperation() {
	return this.myOperation;
    }
}
