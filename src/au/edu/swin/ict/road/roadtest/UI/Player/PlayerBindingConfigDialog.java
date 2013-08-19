package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;

import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.exception.CompositeNotFoundException;
import au.edu.swin.ict.road.roadtest.exception.RoleNotFoundException;

/**
 * This class represents an instance Player Binding Config UI that lets user to
 * bind with a role of other composition on network.
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class PlayerBindingConfigDialog extends JDialog {

    private JLabel JLabelAddress;
    private JLabel JLabelComposition;
    private JLabel JLabelPlayer;
    private JTextField jTextFieldAddress;
    private JComboBox jComboBoxComposition;
    private DefaultComboBoxModel modelComposition;
    private DefaultComboBoxModel modelRoles;
    private JComboBox jComboBoxRoles;
    private JButton jButtonGetComposition;
    private JButton jButtonGetRoles;
    private JButton jButtonBind;
    private JButton jButtonCancel;
    private JPanel contentPane;
    private JList jListSetting;
    private JScrollPane jScrollPane1;
    private Player player;

    /**
     * Constructor
     */
    public PlayerBindingConfigDialog(Player player) {
	super();
	this.player = player;
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * This function is used to initialize all the components of the frame
     */
    private void initializeComponent() {

	JLabelAddress = new JLabel("End-Point Address:");
	JLabelComposition = new JLabel("Select Composition:");
	JLabelPlayer = new JLabel("Select Role:");
	jTextFieldAddress = new JTextField();
	modelComposition = new DefaultComboBoxModel();
	modelRoles = new DefaultComboBoxModel();
	jComboBoxComposition = new JComboBox(modelComposition);
	jComboBoxRoles = new JComboBox(modelRoles);
	jButtonBind = new JButton("Bind");
	jButtonCancel = new JButton("Cancel");
	jButtonGetComposition = new JButton("Get Composition");
	jButtonGetRoles = new JButton("Get Roles");
	jScrollPane1 = new JScrollPane();
	jScrollPane1.setViewportView(jListSetting);
	contentPane = (JPanel) this.getContentPane();

	// register action listeners
	jButtonBind.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonBind_actionPerformed(e);
	    }
	});

	jButtonCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonCancel_actionPerformed(e);
	    }

	});

	jButtonGetComposition.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ButtonGetComposition_actionPerformed(e);
	    }

	});

	jButtonGetRoles.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonGetRoles_actionPerformed(e);
	    }

	});

	contentPane.setLayout(null);

	addComponent(contentPane, JLabelAddress, 22, 25, 200, 18);
	addComponent(contentPane, JLabelComposition, 22, 65, 200, 18);
	addComponent(contentPane, JLabelPlayer, 22, 105, 200, 18);
	addComponent(contentPane, jTextFieldAddress, 200, 25, 207, 22);
	addComponent(contentPane, jComboBoxComposition, 200, 65, 209, 22);
	addComponent(contentPane, jButtonGetComposition, 420, 22, 150, 25);
	addComponent(contentPane, jComboBoxRoles, 200, 105, 209, 22);
	addComponent(contentPane, jButtonGetRoles, 420, 63, 150, 25);
	addComponent(contentPane, jScrollPane1, 200, 265, 209, 100);
	addComponent(contentPane, jButtonBind, 200, 150, 83, 28);
	addComponent(contentPane, jButtonCancel, 287, 150, 83, 28);

	this.setTitle("Bind Player - " + player.getRole().getName());
	this.setLocation(new Point(11, 10));
	this.setSize(new Dimension(600, 250));
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setResizable(false);
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

    // Event listeners

    /**
     * Functionality to get the remote composition by specifying endpoint
     * location
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void ButtonGetComposition_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	modelComposition.removeAllElements();
	String address = this.jTextFieldAddress.getText().trim();
	if (!address.equals("")) {
	    try {
		String[] compOnServer = player.setConnection(address);
		for (String s : compOnServer) {
		    modelComposition.addElement(s);
		}
	    } catch (RemoteException e1) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(this, e1.getMessage(),
			"Remote Exception", JOptionPane.ERROR_MESSAGE);
		// e1.printStackTrace();
	    }
	}
    }

    /**
     * Functionality to get roles from remote composition
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonGetRoles_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	modelRoles.removeAllElements();
	if (modelComposition.getSize() > 0) {
	    String comp = (String) this.jComboBoxComposition.getSelectedItem();
	    try {
		Map<String, String> rolesOnServer = player
			.useCompositeOnServer(comp);
		Iterator it = rolesOnServer.keySet().iterator();
		while (it.hasNext()) {
		    String s = (String) it.next();
		    modelRoles.addElement(s);// rolesOnServer.get(s)
		}
	    } catch (RemoteException e1) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(this, e1.getMessage(),
			"Remote Exception", JOptionPane.ERROR_MESSAGE);
		// e1.printStackTrace();
	    } catch (CompositeNotFoundException e2) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(this, e2.getMessage(),
			"Composite Not Found Exception",
			JOptionPane.ERROR_MESSAGE);
	    }
	}

    }

    /**
     * Functionality to bind a role of local composition with remote role
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonBind_actionPerformed(ActionEvent e) {
	if (modelRoles.getSize() > 0) {
	    String role = this.jComboBoxRoles.getSelectedItem().toString();
	    try {
		player.bindRoleToPlay(role);
		String msg = "Player " + player.getRole().getName()
			+ " successfully binded with " + role;
		JOptionPane.showMessageDialog(this, msg, "Binding Successful",
			JOptionPane.PLAIN_MESSAGE);

	    } catch (RoleNotFoundException e2) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(this, e2.getMessage(),
			"Role Not Found Exception", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    /**
     * Functionality for cancel buttton
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}