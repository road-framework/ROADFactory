package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.UUID;
import javax.swing.*;

import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.filemanager.FileManager;

/**
 * This class represents an instance AutoResponse UI that lets user to set auto
 * response for a perticular message signature.
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class AutoResponseDialog extends JDialog {

    // Variables declaration
    private JLabel jLabelRequest;
    private JLabel jLabelResponse;
    private JLabel jLabelContent;
    private JLabel jLabelError;
    private JCheckBox jCheckBoxDelay;
    private JTextField jTextFieldDelay;
    private JComboBox jComboBoxRequest;
    private DefaultComboBoxModel modelRequest;
    private JComboBox jComboBoxResponse;
    private DefaultComboBoxModel modelResponse;
    private JButton jButtonSave;
    private JButton jButtonCancel;
    private JTextArea jTextAreaContent;
    private JScrollPane jScrollPaneContent;
    private JLabel jLabelMessage;
    private DefaultComboBoxModel modelMessages;
    private JComboBox jComboBoxMessages;
    private JPanel contentPane;
    private Player player;
    private FileManager fileMgr;
    private boolean isSelected = false;
    public static final String DELAYFIELD_VALUE = "0";

    // End of variables declaration

    /**
     * Constructor for UI
     * 
     * @param player
     *            <code>Player</code> player
     */
    public AutoResponseDialog(Player player, FileManager fileMgr) {
	super();
	this.player = player;
	this.fileMgr = fileMgr;
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * Function to initialize all the components
     */
    private void initializeComponent() {

	jLabelRequest = new JLabel("Incoming Message Signature:");
	jLabelResponse = new JLabel("Outgoing Message Signature:");
	jLabelError = new JLabel();
	jLabelContent = new JLabel("Message Content:");
	jCheckBoxDelay = new JCheckBox("Response with Delay (ms)");
	jCheckBoxDelay.setSelected(false);
	jTextFieldDelay = new JTextField(DELAYFIELD_VALUE);
	jTextFieldDelay.setEnabled(false);
	modelRequest = new DefaultComboBoxModel();
	jComboBoxRequest = new JComboBox(modelRequest);
	jComboBoxRequest.setEditable(true);
	modelResponse = new DefaultComboBoxModel();
	jComboBoxResponse = new JComboBox(modelResponse);
	jComboBoxResponse.setEditable(true);
	jLabelMessage = new JLabel("Select Message");
	modelMessages = new DefaultComboBoxModel();
	jComboBoxMessages = new JComboBox(modelMessages);
	jButtonSave = new JButton("Save");
	jButtonCancel = new JButton("Cancel");
	jTextAreaContent = new JTextArea();
	jScrollPaneContent = new JScrollPane();
	contentPane = (JPanel) this.getContentPane();
	jScrollPaneContent.setViewportView(jTextAreaContent);
	contentPane.setLayout(null);

	populateMessageSignatures();
	generateMessage(modelRequest.getSelectedItem().toString(),
		modelResponse.getSelectedItem().toString(),
		"Message Response Content");
	getMessages();
	// register action listeners
	jCheckBoxDelay.addItemListener(new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		jCheckBoxDelay_itemStateChanged(e);
	    }

	});

	jComboBoxRequest.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jComboBoxRequest_actionPerformed(e);
	    }

	});

	jComboBoxResponse.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jComboBoxResponse_actionPerformed(e);
	    }

	});

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }
	});

	jButtonCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonCancel_actionPerformed(e);
	    }

	});

	jComboBoxMessages.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jComboBoxMessages_actionPerformed(e);
	    }

	});

	addComponent(contentPane, jLabelRequest, 23, 26, 189, 18);
	addComponent(contentPane, jLabelResponse, 23, 70, 189, 18);
	addComponent(contentPane, jLabelError, 15, 302, 534, 18);
	addComponent(contentPane, jLabelMessage, 23, 114, 189, 18);
	addComponent(contentPane, jLabelContent, 23, 158, 189, 18);
	addComponent(contentPane, jCheckBoxDelay, 219, 268, 200, 24);
	addComponent(contentPane, jTextFieldDelay, 470, 268, 68, 22);
	addComponent(contentPane, jComboBoxRequest, 220, 26, 320, 22);
	addComponent(contentPane, jComboBoxResponse, 220, 70, 320, 22);
	addComponent(contentPane, jComboBoxMessages, 220, 114, 320, 22);
	addComponent(contentPane, jButtonSave, 217, 307, 71, 28);
	addComponent(contentPane, jButtonCancel, 297, 307, 71, 28);
	addComponent(contentPane, jScrollPaneContent, 220, 158, 321, 100);

	this.setTitle("Auto Response");
	this.setLocation(new Point(10, 10));
	this.setSize(new Dimension(570, 400));
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.setModal(true);
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

    /**
     * This function is executed when the user clicks on save button
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonSave_actionPerformed(ActionEvent e) {
	try {
	    String msgContent = jTextAreaContent.getText().trim();
	    if (!msgContent.equals("")) {
		long delay = Integer.parseInt(this.jTextFieldDelay.getText()
			.trim());
		UUID uidAutoResp = player.setUpAutoResponder(modelRequest
			.getSelectedItem().toString(), modelResponse
			.getSelectedItem().toString(), msgContent, delay);
		player.startAutoResponseById(uidAutoResp);
		JOptionPane.showMessageDialog(this, "Autoresponse Saved for "
			+ jComboBoxRequest.getSelectedItem().toString());
		this.dispose();
	    } else
		this.jLabelError.setText("Message Content is empty");
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this,
		    "Exception: Interval should be numeric " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception: " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Functionality when user changes the preference by clicking the checkbox
     * for delay
     * 
     * @param e
     *            <code>ItemEvent</code>
     */
    private void jCheckBoxDelay_itemStateChanged(ItemEvent e) {
	if (this.jCheckBoxDelay.isSelected()) {
	    this.jTextFieldDelay.setEnabled(true);
	} else {
	    this.jTextFieldDelay.setEnabled(false);
	    this.jTextFieldDelay.setText(DELAYFIELD_VALUE);
	}
    }

    /**
     * Functionality to load messages
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jComboBoxMessages_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	isSelected = true;
	generateMessage(modelRequest.getSelectedItem().toString(),
		modelResponse.getSelectedItem().toString(),
		this.fileMgr.getFileContent(this.jComboBoxMessages
			.getSelectedItem().toString()));

    }

    /**
     * This function gets the messages loaded from filemanager to dropdown list
     * which can be then displayed in message content textfield
     */
    private void getMessages() {
	String[] loc = this.fileMgr.getFileLocations();
	for (int i = 0; i < loc.length; i++) {
	    this.modelMessages.addElement(loc[i]);
	}
    }

    /**
     * Function that populates all the message signature in the dropdown list
     */
    private void populateMessageSignatures() {
	Iterator itr = player.getRequiredSignatures().iterator();
	while (itr.hasNext()) {
	    modelRequest.addElement(itr.next());
	}

	Iterator itr1 = player.getProvidedSignatures().iterator();
	while (itr1.hasNext()) {
	    modelResponse.addElement(itr1.next());
	}
    }

    /**
     * User clicks on combo box for request signatures
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jComboBoxRequest_actionPerformed(ActionEvent e) {
	generateMessage(modelRequest.getSelectedItem().toString(),
		modelResponse.getSelectedItem().toString(),
		"Message Response Content");
    }

    /**
     * User clicks on combo box for response signatures
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jComboBoxResponse_actionPerformed(ActionEvent e) {
	generateMessage(modelRequest.getSelectedItem().toString(),
		modelResponse.getSelectedItem().toString(),
		"Message Response Content");
    }

    /**
     * Function that generates the message content based on the user selection
     * of message signatures
     * 
     * @param msgInput
     *            <code>String</code> msgInput
     * @param msgOutput
     *            <code>String</code> msgOutput
     * @param Content
     *            <code>String</code> Content
     */
    private void generateMessage(String msgInput, String msgOutput,
	    String Content) {
	if (isSelected) {
	    this.jTextAreaContent.setText(this.fileMgr
		    .getFileContent(this.jComboBoxMessages.getSelectedItem()
			    .toString()));
	} else {
	    this.jTextAreaContent.setText(msgOutput + " >> " + msgInput + " : "
		    + Content);
	}
	jTextAreaContent.setCaretPosition(0);
    }

    /**
     * When user clicks the cancel button
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}
