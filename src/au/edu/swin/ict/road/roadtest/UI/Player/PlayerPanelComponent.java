package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.Component;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Calendar;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.roadtest.AutoResponseMessage;
import au.edu.swin.ict.road.roadtest.IntervalMessage;
import au.edu.swin.ict.road.roadtest.Message;
import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.UI.CompositionUI;
import au.edu.swin.ict.road.roadtest.UI.support.ButtonImpl;
import au.edu.swin.ict.road.roadtest.UI.support.ButtonRenderer;
import au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent;
import au.edu.swin.ict.road.roadtest.events.MessageSentEvent;
import au.edu.swin.ict.road.roadtest.filemanager.FileManager;
import au.edu.swin.ict.road.roadtest.listeners.MessageArrivedEventListener;
import au.edu.swin.ict.road.roadtest.listeners.MessageSentEventListener;

/**
 * This class represents the component for each player on PlayersUI it has
 * components related to message inbox, outbox and other controllers
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class PlayerPanelComponent extends JPanel implements
	MessageArrivedEventListener, MessageSentEventListener {

    private JLabel jLabelRoleID;
    private JLabel jLabelName;
    private JLabel jLabelError;
    private JLabel jLabelDescription;
    private JLabel jLabelMsgSignature;
    private JLabel jLabelContracts;
    private JLabel jLabelMsgContent;
    private JLabel jLabelOutbox;
    private JLabel jLabelInbox;
    private JLabel jLabelMessages;
    private JCheckBox jCheckBoxInterval;
    private JTextField jTextFieldInterval;
    private JComboBox jComboBoxMsgSignatures;
    private JComboBox jComboBoxContracts;
    private TextArea jTextAreaMsgContent;
    private JTable jTableOutbox;
    private DefaultTableModel modelOutbox;
    private JTable jTableInbox;
    private DefaultTableModel modelInbox;
    private JScrollPane jScrollPaneOutbox;
    private JScrollPane jScrollPaneInbox;
    private JButton jButtonSend;
    private JButton jButtonStopAll;
    private JButton jButtonAutoResponse;
    private JButton jButtonClearInbox;
    private JButton jButtonSaveInbox;
    private JButton jButtonClearOutbox;
    private JButton jButtonSaveOutbox;
    private JButton jButtonPullMessage;
    private JButton jButtonViewResponseSig;
    private JButton jButtonRefresh;
    private JButton jButtonSetParameters;
    private JButton jButtonConfig;
    private DefaultComboBoxModel model;
    private DefaultComboBoxModel model1;
    private DefaultComboBoxModel modelMessages;
    private JComboBox jComboBoxMessages;
    private Player player;
    private FileManager fileMgr;

    private static final String INTERVALFIELD_VALUE = "3000";
    private static final String MESSAGE_FOLDER = System.getProperty("user.dir")
	    + File.separator + "messages";

    /**
     * Constructor for <code>PlayerPanel</code> which accepts a parameter of
     * type <code>Player</code>
     */
    public PlayerPanelComponent(Player player, FileManager fileMgr) {
	super();
	this.player = player;
	this.fileMgr = fileMgr;
	player.getInbox().addMessageArrivedEventListener(this);
	player.getOutbox().addMessageSentEventListener(this);
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * This function is used to initialise all the components of the frame
     */
    private void initializeComponent() {

	jLabelRoleID = new JLabel("ID: " + player.getRole().getId());
	jLabelName = new JLabel("Name: " + player.getRole().getName());
	jLabelDescription = new JLabel("Description: "
		+ player.getRole().getDescription());
	jLabelMsgSignature = new JLabel("Message Signature: ");
	jLabelMsgContent = new JLabel("Message Content: ");
	jLabelContracts = new JLabel("Contracts:");
	jLabelMessages = new JLabel("Select Message: ");
	jLabelOutbox = new JLabel("Outbox: ");
	jLabelInbox = new JLabel("Inbox: ");
	jCheckBoxInterval = new JCheckBox("Send with interval (ms)");
	jTextFieldInterval = new JTextField(INTERVALFIELD_VALUE);
	jTextFieldInterval.setEnabled(false);
	jLabelError = new JLabel();

	model = new DefaultComboBoxModel();
	jComboBoxMsgSignatures = new JComboBox(model);
	jComboBoxMsgSignatures.setEditable(true);

	model1 = new DefaultComboBoxModel();
	jComboBoxContracts = new JComboBox(model1);
	jComboBoxContracts.setEditable(false);

	modelMessages = new DefaultComboBoxModel();
	jComboBoxMessages = new JComboBox(modelMessages);

	jTextAreaMsgContent = new TextArea("Message content");
	jTableOutbox = new JTable();
	jTableOutbox.setCellSelectionEnabled(false);
	jTableInbox = new JTable();
	jTableInbox.setCellSelectionEnabled(false);
	jScrollPaneOutbox = new JScrollPane();
	jScrollPaneInbox = new JScrollPane();
	jButtonSend = new JButton("Send");
	jButtonRefresh = new JButton("Refresh");
	jButtonStopAll = new JButton("Stop All");
	jButtonAutoResponse = new JButton();
	jButtonAutoResponse.setToolTipText("Set Auto Respond");// saveicon.jpg
	String rootPath = System.getProperty("user.dir");
	String imagePath = rootPath + File.separator + "images/";
	jButtonAutoResponse.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "autoreplyicon.gif")));
	jButtonClearInbox = new JButton();
	jButtonClearInbox.setToolTipText("Clear Inbox");
	jButtonClearInbox.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "clearicon.gif")));
	jButtonClearOutbox = new JButton();
	jButtonClearOutbox.setToolTipText("Clear Outbox");
	jButtonClearOutbox.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "clearicon.gif")));
	jButtonPullMessage = new JButton();
	jButtonPullMessage.setToolTipText("Pull Message");
	jButtonPullMessage.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "pullmessageicon.gif")));
	jButtonSaveInbox = new JButton();
	jButtonSaveInbox.setToolTipText("Save Inbox Messages");
	jButtonSaveInbox.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "saveicon.jpg")));
	jButtonSaveOutbox = new JButton();
	jButtonSaveOutbox.setToolTipText("Save Outbox Messages");
	jButtonSaveOutbox.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "saveicon.jpg")));

	jButtonConfig = new JButton("Config");
	jButtonViewResponseSig = new JButton("Show Required");
	jButtonSetParameters = new JButton("Set Parameters");
	jScrollPaneOutbox.setViewportView(jTableOutbox);
	jScrollPaneInbox.setViewportView(jTableInbox);

	populateMessageSignatures();
	generateOutbox();
	generateInbox();
	populateContracts();
	getMessages();

	// add components to frame
	addComponent(this, jLabelRoleID, 27, 22, 162, 15);
	addComponent(this, jLabelName, 190, 22, 162, 15);
	addComponent(this, jLabelDescription, 27, 52, 500, 15);
	addComponent(this, jLabelMsgSignature, 27, 93, 162, 15);
	addComponent(this, jLabelContracts, 27, 120, 162, 15);
	addComponent(this, jLabelMessages, 27, 147, 162, 15);
	addComponent(this, jLabelMsgContent, 27, 180, 162, 15);
	addComponent(this, jLabelOutbox, 10, 380, 162, 15);
	addComponent(this, jLabelInbox, 10, 520, 162, 15);
	addComponent(this, jCheckBoxInterval, 190, 330, 159, 24);
	addComponent(this, jTextFieldInterval, 351, 330, 54, 22);
	addComponent(this, jComboBoxMsgSignatures, 190, 90, 345, 22);
	addComponent(this, jComboBoxContracts, 190, 120, 345, 22);
	addComponent(this, jComboBoxMessages, 190, 147, 345, 22);
	addComponent(this, jButtonViewResponseSig, 540, 90, 110, 25);
	addComponent(this, jButtonRefresh, 540, 147, 110, 25);
	addComponent(this, jButtonSetParameters, 660, 90, 110, 25);
	addComponent(this, jTextAreaMsgContent, 190, 180, 459, 145);
	addComponent(this, jScrollPaneOutbox, 1, 395, 797, 100);
	addComponent(this, jScrollPaneInbox, 1, 540, 797, 100);
	addComponent(this, jButtonSend, 480, 330, 83, 25);
	addComponent(this, jButtonStopAll, 567, 330, 83, 25);
	addComponent(this, jButtonClearOutbox, 765, 365, 30, 25);
	addComponent(this, jButtonConfig, 570, 20, 80, 25);
	addComponent(this, jButtonClearInbox, 765, 510, 30, 25);
	addComponent(this, jButtonAutoResponse, 735, 510, 30, 25);
	addComponent(this, jButtonPullMessage, 705, 510, 30, 25);
	addComponent(this, jButtonSaveInbox, 675, 510, 30, 25);
	addComponent(this, jButtonSaveOutbox, 735, 365, 30, 25);
	addComponent(this, jLabelError, 10, 645, 797, 15);

	// Register event listeners
	jCheckBoxInterval.addItemListener(new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		jCheckBoxInterval_itemStateChanged(e);
	    }
	});

	jComboBoxMessages.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jComboBoxMessages_actionPerformed(e);
	    }

	});

	jButtonRefresh.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonRefresh_actionPerformed(e);
	    }
	});
	jButtonViewResponseSig.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonViewResponseSig_actionPerformed(e);
	    }
	});

	jButtonSetParameters.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSetParameters_actionPerformed(e);
	    }
	});

	jButtonSaveInbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSaveInbox_actionPerformed(e);
	    }
	});

	jButtonSaveOutbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSaveOutbox_actionPerformed(e);
	    }
	});

	jButtonAutoResponse.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonAutoResponse_actionPerformed(e);
	    }
	});

	jButtonSend.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSend_actionPerformed(e);
	    }
	});

	jButtonStopAll.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonStopAll_actionPerformed(e);
	    }
	});

	jButtonClearInbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonClearInbox_actionPerformed(e);
	    }
	});

	jButtonClearOutbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonClearOutbox_actionPerformed(e);
	    }
	});

	jButtonPullMessage.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonPullMessage_actionPerformed(e);
	    }
	});

	jButtonConfig.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonConfig_actionPerformed(e);
	    }
	});

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
     * Functionality to generate UI for Inbox
     */
    private void generateInbox() {
	String[] columnNamesInbox = { "Message Signature", "Content",
		"Origin Role", "Message Type", "Time", "View" };
	jTableInbox.setModel(new DefaultTableModel(columnNamesInbox, 0));
	modelInbox = (DefaultTableModel) jTableInbox.getModel();
	jTableInbox.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Functionality to generate UI for outbox
     */
    private void generateOutbox() {
	String[] columnNamesOutbox = { "Message ID", "Message Signature",
		"Content", "Interval(Sec)", "Count", "Time", "View", "Stop" };
	jTableOutbox.setModel(new DefaultTableModel(columnNamesOutbox, 0));
	modelOutbox = (DefaultTableModel) jTableOutbox.getModel();
	jTableOutbox.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * This function gets the messages loaded from filemanager to dropdown list
     * which can be then displayed in message content textfield
     */
    private void getMessages() {
	modelMessages.removeAllElements();
	String[] loc = this.fileMgr.getFileLocations();
	for (int i = 0; i < loc.length; i++) {
	    this.modelMessages.addElement(loc[i]);
	}
    }

    /**
     * This function is to fill the list box with the contract details
     */
    public void populateContracts() {
	model1.removeAllElements();
	Contract[] con = player.getRole().getAllContracts();
	if (con != null) {
	    for (Contract c : con) {
		model1.addElement(c.getId() + ": " + c.getDescription());
	    }
	}
    }

    /**
     * This function populates the message signatures for players
     */
    public void populateMessageSignatures() {
	model.removeAllElements();
	Iterator itr = player.getProvidedSignatures().iterator();
	while (itr.hasNext()) {
	    model.addElement(itr.next());
	}
    }

    // Event registration

    /**
     * Reacts to event when the user clicks on refresh button interval
     */
    protected void jButtonRefresh_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	fileMgr.readFolder(MESSAGE_FOLDER);
	getMessages();
    }

    /**
     * Reacts to event when the user clicks on checkbox to send message in
     * interval
     */
    private void jCheckBoxInterval_itemStateChanged(ItemEvent e) {
	if (this.jCheckBoxInterval.isSelected()) {
	    this.jTextFieldInterval.setEnabled(true);
	    this.jButtonSend.setText("Start");
	} else {
	    this.jTextFieldInterval.setEnabled(false);
	    this.jTextFieldInterval.setText(INTERVALFIELD_VALUE);
	    this.jButtonSend.setText("Send");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @seeau.edu.swin.ict.road.roadtest.listeners.MessageArrivedEventListener#
     * messageArrived(au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent)
     */
    public void messageArrived(MessageArrivedEvent msgArrivedEvent) {
	// TODO Auto-generated method stub
	addRowToInbox(msgArrivedEvent.getMessage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * au.edu.swin.ict.road.roadtest.listeners.MessageSentEventListener#messageSent
     * (au.edu.swin.ict.road.roadtest.events.MessageSentEvent)
     */
    public void messageSent(MessageSentEvent msgSentEvent) {
	// TODO Auto-generated method stub
	addRowToOutbox(msgSentEvent.getMessage());
    }

    /**
     * The functionality to add a new row or update existing row of the outbox
     * 
     * @param msg
     *            <code>Message</code> message
     */
    private void addRowToOutbox(Message msg) {

	try {
	    Calendar cal = Calendar.getInstance();
	    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
	    int msgCount = 1;
	    long msgInterval = 0;
	    UUID uid = UUID.randomUUID();
	    Object[] row;

	    // check if the message is of type interval message
	    // interval message is inserted for the first time and rest of the
	    // time
	    // its just updated

	    if (msg instanceof IntervalMessage) {
		uid = ((IntervalMessage) msg).getUid();
		msgCount = ((IntervalMessage) msg).getSentMessageCount();
		msgInterval = ((IntervalMessage) msg).getIntervalUnit();

		row = new Object[] { uid, msg.getOperationName(),
			msg.getMessageContent().toString(), msgInterval,
			msgCount, dateFormat.format(msg.getTimeStamp()),
			"View", "Stop" };

		// check if the entry exist in table
		// if it returns -1 then it does not exist so we have to add it
		// or else we have to update the row
		int i = checkIfExists(uid);
		if (i != -1)
		    modelOutbox.removeRow(i);
	    } else if (msg instanceof AutoResponseMessage) {
		uid = ((AutoResponseMessage) msg).getUid();
		msgCount = ((AutoResponseMessage) msg).getSentMessageCount();
		msgInterval = ((AutoResponseMessage) msg).getDelay();
		row = new Object[] { uid, msg.getOperationName(),
			msg.getMessageContent().toString(), msgInterval,
			msgCount, dateFormat.format(msg.getTimeStamp()),
			"View", "Abort" };

		int i = checkIfExists(uid);
		if (i != -1)
		    modelOutbox.removeRow(i);
	    } else {
		row = new Object[] { uid, msg.getOperationName(),
			msg.getMessageContent().toString(), msgInterval,
			msgCount, dateFormat.format(msg.getTimeStamp()),
			"View", "" };
	    }
	    // jTableOutbox.setToolTipText((String)getValueAt(rowIndex,
	    // vColIndex));
	    modelOutbox.insertRow(0, row);
	    jTableOutbox.getColumn("View")
		    .setCellRenderer(new ButtonRenderer());
	    jTableOutbox.getColumn("Stop")
		    .setCellRenderer(new ButtonRenderer());
	    ButtonImpl myButton = new ButtonImpl(new JCheckBox(), uid, player,
		    msg.getMessageContent().toString());
	    jTableOutbox.getColumn("View").setCellEditor(myButton);
	    jTableOutbox.getColumn("Stop").setCellEditor(myButton);

	    this.jLabelError.setText("Message is sent");
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception: " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Functionality to send the message to players
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonSend_actionPerformed(ActionEvent e) {

	try {
	    String msgContent = jTextAreaMsgContent.getText();
	    if (msgContent.equals("") || msgContent.equals(null)) {
		JOptionPane.showMessageDialog(this,
			"Error: The message content can not be empty ",
			"Exception", JOptionPane.ERROR_MESSAGE);
	    } else {
		String msgSignature = jComboBoxMsgSignatures.getSelectedItem()
			.toString();
		long interval = Integer.parseInt(this.jTextFieldInterval
			.getText());

		// check if user has selected the checkbox to send the
		// messages on regular interval or not and what is the interval
		// else check if the user has asked to send only a single
		// message
		if (this.jCheckBoxInterval.isSelected() && interval > 0) {
		    UUID uid = player.setUpIntervalMessage(msgSignature,
			    msgContent, interval);
		    player.startIntervalMessageById(uid);
		} else if (!this.jCheckBoxInterval.isSelected()) {
		    player.sendMessage(msgSignature, msgContent, false);
		} else {
		    this.jLabelError.setText("Error in input");
		}
	    }
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this,
		    "Exception: Interval should be numeric " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception:" + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * This function adds a row to inbox when a message is arrived
     * 
     * @param msg
     *            <code>Message</code> message
     */
    private void addRowToInbox(Message msg) {
	/*
	 * if(modelInbox.getRowCount()>1) modelInbox.setRowCount(0);
	 */
	try {
	    Calendar cal = Calendar.getInstance();
	    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
	    Object[] row = new Object[] { msg.getOperationName(),
		    msg.getMessageContent().toString(), msg.getOriginRoleId(),
		    msg.getMessageType(),
		    dateFormat.format(msg.getTimeStamp()), "View" };
	    ButtonImpl myButton = new ButtonImpl(new JCheckBox(), null, player,
		    msg.getMessageContent().toString());
	    jTableInbox.getColumn("View").setCellRenderer(new ButtonRenderer());
	    jTableInbox.getColumn("View").setCellEditor(myButton);
	    modelInbox.insertRow(0, row);
	    this.jLabelError.setText("New message is received");

	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception:" + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Functionality to load messages
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jComboBoxMessages_actionPerformed(ActionEvent e) {
	if (modelMessages.getSize() > 0) {
	    // TODO Auto-generated method stub
	    this.jTextAreaMsgContent.setText(this.fileMgr
		    .getFileContent(this.jComboBoxMessages.getSelectedItem()
			    .toString()));
	    jTextAreaMsgContent.setCaretPosition(0);
	}
    }

    /**
     * Functionality to stop button which terminates all the messages
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    private void jButtonStopAll_actionPerformed(ActionEvent e) {
	try {
	    this.player.terminateAllIntervalMessages();
	    JOptionPane.showMessageDialog(this, "All messages from "
		    + player.getRole().getName() + " are stopped.", "Status",
		    JOptionPane.INFORMATION_MESSAGE);
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception: " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);

	}
    }

    /**
     * Function that checks if the UUID exist in the table
     * 
     * @param uid
     *            <code>Integer</code> returns the index of row
     * @return
     */
    private int checkIfExists(UUID uid) {
	for (int i = 0; i < jTableOutbox.getRowCount(); i++) {
	    if (uid.equals(jTableOutbox.getValueAt(i, 0)))
		return i;
	}
	return -1;
    }

    /**
     * Functionality to display required signatures on UI
     * 
     * @param e
     *            <code>ActionEvent</code> event
     */
    protected void jButtonViewResponseSig_actionPerformed(ActionEvent e) {
	String msg = "Required Signatures:";
	Iterator itr = player.getRequiredSignatures().iterator();
	while (itr.hasNext()) {
	    msg += "\n - " + itr.next();
	}
	JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * This function generates the new window to let user fill the parameters
     * and generate the xml message for testing
     * 
     * @param e
     *            <code>ActionEvent</code> event
     */
    protected void jButtonSetParameters_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	String sig = this.jComboBoxMsgSignatures.getSelectedItem().toString();
	if (sig != null) {
	    ParametersUI u = new ParametersUI(
		    player.getProvidedParameters(sig), sig, fileMgr);
	}
    }

    /**
     * Functionality to generate autoresponse UI
     * 
     * @param e
     *            <code>ActionEvent</code> event
     */
    protected void jButtonAutoResponse_actionPerformed(ActionEvent e) {
	new AutoResponseDialog(this.player, this.fileMgr);
    }

    /**
     * Functionality to clear the inbox UI
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonClearInbox_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	this.modelInbox.setRowCount(0);
    }

    /**
     * Functionality to clear the outbox UI
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonClearOutbox_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	this.modelOutbox.setRowCount(0);
    }

    /**
     * Functionality to save the outbox to file
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonSaveOutbox_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	writeContent(1);
    }

    /**
     * Functionality to save the inbox to file
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonSaveInbox_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	writeContent(0);
    }

    /**
     * Functionality to save the message history or log to external file it
     * creates the file or asks user to overwrite or not if file already exists
     * 
     * @param opt
     *            <code>Integer<code> 0 for inbox 1 for outbox
     */
    private void writeContent(int opt) {
	String filename = showBrowseDialog();
	if (!filename.equals("")) {
	    String status = fileMgr.createFileWriter(filename);

	    // if new file is created
	    if (status.equals("FILECREATED")) {
		writeTofile(opt);
	    } else {// if file exists
		int response = JOptionPane
			.showConfirmDialog(null,
				"Do you want to overwrite the file?",
				"Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
		    writeTofile(opt);
		}
	    }
	}
    }

    /**
     * Functionality to write the message history or log to external file
     * 
     * @param opt
     *            <code>Integer<code> 0 for inbox 1 for outbox
     */
    private void writeTofile(int opt) {
	Object[] arr;
	if (opt == 0)
	    arr = this.player.getInbox().getMessageHistory().toArray();
	else
	    arr = this.player.getOutbox().getMessageHistory().toArray();
	if (fileMgr.writeLinesToFile(arr))
	    JOptionPane.showMessageDialog(this, "Log written successfully");
	else
	    JOptionPane.showMessageDialog(this,
		    "Error occurred while writing to log");
    }

    /**
     * Functionality that shows the filechooser dialog to user
     * 
     * @return
     */
    private String showBrowseDialog() {
	JFileChooser chooser = new JFileChooser();
	/*
	 * FileNameExtensionFilter filter = new
	 * FileNameExtensionFilter("txt","txt"); chooser.setFileFilter(filter);
	 */

	int returnVal = chooser.showSaveDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    String fileName = chooser.getSelectedFile().getAbsolutePath();
	    if (fileName.contains(".txt"))
		return fileName;
	    else
		return fileName += ".txt";
	}
	return "";
    }

    /**
     * Functionality to pull the message from the outqueue if the message type
     * is pull
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonPullMessage_actionPerformed(ActionEvent e) {
	int count = 0;
	for (Message m : this.player.getInbox().getPullMessages()) {
	    addRowToInbox(m);
	    count += 1;
	}
	this.jLabelError.setText(count + " Messages pulled");
    }

    /**
     * Displays the configuration window to user if user wants to bind the role
     * to remote role
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void jButtonConfig_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	PlayerBindingConfigDialog config = new PlayerBindingConfigDialog(
		this.player);

    }

}
