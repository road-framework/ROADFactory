package au.edu.swin.ict.road.roadtest.UI.Organiser;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * This class represents Organiser Component that lets user to set Role related
 * information
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserComponentRole extends JPanel {

    private JLabel jLabelID;
    private JLabel jLabelName;
    private JLabel jLabelDescription;
    private JTextField jTextFieldID;
    private JTextField jTextFieldName;
    private JTextField jTextFieldDescription;
    private JButton jButtonSave;
    private ROADTest myComposite;
    private DefaultMutableTreeNode parentNode;
    private Role myRole;

    /**
     * Constructor for OrganiserComponentRole
     * 
     * @param myComposite
     *            <code>ROADTest</code> instance of ROADTest
     * @param node
     *            <code>DefaultMutableTreeNode</code> node that is selected
     */
    public OrganiserComponentRole(ROADTest myComposite,
	    DefaultMutableTreeNode node) {
	super();
	this.myComposite = myComposite;
	this.parentNode = node;
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * The function initializes all the UI components for given panel
     */
    private void initializeComponent() {
	jLabelID = new JLabel("ID:");
	jLabelName = new JLabel("Name:");
	jLabelDescription = new JLabel("Description:");
	jTextFieldID = new JTextField();
	jTextFieldName = new JTextField();
	jTextFieldDescription = new JTextField();
	jButtonSave = new JButton("Add Role");

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }
	});
	jButtonSave.setActionCommand("Add Role");

	this.setLayout(null);
	this.setBorder(new TitledBorder("Role"));
	addComponent(this, jLabelID, 25, 35, 300, 15);
	addComponent(this, jTextFieldID, 135, 30, 215, 22);
	addComponent(this, jLabelName, 25, 65, 300, 15);
	addComponent(this, jTextFieldName, 135, 60, 215, 22);
	addComponent(this, jLabelDescription, 25, 95, 300, 15);
	addComponent(this, jTextFieldDescription, 135, 90, 215, 22);
	addComponent(this, jButtonSave, 135, 204, 83, 28);
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
     * Functionality when user clicks on save button
     * 
     * @param e
     *            <code>ActionEvent</code> e an action event
     */
    private void jButtonSave_actionPerformed(ActionEvent e) {

	String id = this.jTextFieldID.getText().trim();
	String name = this.jTextFieldName.getText().trim();
	String description = this.jTextFieldDescription.getText().trim();
	myRole = new Role();
	myRole.setId(id);
	myRole.setName(name);
	myRole.setDescription(description);
	myComposite.addPlayer(myRole);
	myComposite.addNewRole(id, name, description);

    }

    /**
     * Function that returns the role created by this component
     * 
     * @return <code>Role</code>
     */
    public Role getRole() {
	return this.myRole;
    }

}
