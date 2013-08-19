package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import au.edu.swin.ict.road.composite.MessageDeliverer;
import au.edu.swin.ict.serendip.composition.view.ProcessView;
import au.edu.swin.ict.serendip.composition.view.SerendipView;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationScriptEngine;
import au.edu.swin.ict.serendip.textual.TextualWriter;
import au.edu.swin.ict.serendip.util.CommonFileReader;

public class ProcessInstanceViewFrame extends SerendipJFrame implements
	ActionListener {
    /**
	 * 
	 */
    private static Logger log = Logger.getLogger(ProcessInstanceViewFrame.class
	    .getName());
    private static final long serialVersionUID = 1L;
    private ProcessInstance pi = null;
    private SerendipEngine engine = null;
    private JScrollPane cmdScrol = new JScrollPane();
    private JScrollPane consoleScrol = new JScrollPane();
    private JSplitPane mainsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private JTextArea cmdArea = new JTextArea();
    private JTextArea consoleArea = new JTextArea();
    private JButton loadBtn = new JButton("Load Script", new ImageIcon(
	    "./images/ui/load.png"));
    private JButton exeBtn = new JButton("Execute", new ImageIcon(
	    "./images/ui/run.png"));
    private JButton helpBtn = new JButton("Help", new ImageIcon(
	    "./images/ui/help.png"));
    private JButton refreshBtn = new JButton("Refresh", new ImageIcon(
	    "./images/ui/refresh.png"));
    private JButton validateSyntaxBtn = new JButton("Validate", new ImageIcon(
	    "./images/ui/verify.png"));
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private SerendipEPCView epcView = null;
    private String defaultText = null;

    public ProcessInstanceViewFrame(ProcessInstance pi, boolean withModif)
	    throws HeadlessException {
	super("Process Instance id=" + pi.getPId() + ", Process Def id="
		+ pi.getDefinitionId());
	// TODO Auto-generated constructor stub
	this.pi = pi;
	this.engine = pi.getEngine();
	// this.defaultText = "SetTaskProperty " + pi.getId() +
	// " SendGRLocation    eppre  RepairReqd";
	this.defaultText = "PI:"
		+ pi.getId()
		+ "{"
		// +"pause ;"
		+ "\n\t updatePropertyOfTask tid=\"PayTC\" property=\"postep\" value=\"TCPaid * TCPaymentSent\";"
		+ "\n\t updatePropertyOfTask tid=\"Notify\" property=\"preep\" value=\"MMNotif * TCPaymentSent\";"
		// +"resume ; "
		+ "\n}";

	// this.defaultText = "set_pp "+pi.getId()+" SendTowReq 12";
	try {
	    this.createView(withModif);
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void createView(boolean withModif) throws SerendipException {
	ProcessView pv = this.pi.getCurrentProcessView();

	SerendipView.markFiredEvents(pv.getViewAsEPC(), this.engine
		.getEventCloud().getEventRecordsForPid(pi.getPId()));
	SerendipView.markExecutedTasks(pv.getViewAsEPC(), this.pi
		.getCompletedTasks().keySet(), pi.getCurrentTasks().keySet());

	SerendipEPCView epcView = new SerendipEPCView(pv);
	this.mainPanel.add(mainsplit, BorderLayout.CENTER);
	this.mainsplit.add(epcView, JSplitPane.LEFT);

	if (withModif) {

	    // this.cmdArea.setBackground(Color.BLACK);
	    // this.cmdArea.setForeground(Color.GREEN);
	    // this.cmdArea.setCaretColor(Color.WHITE);
	    this.cmdArea.setLineWrap(true);
	    this.cmdArea.setText(this.defaultText);
	    this.loadBtn.addActionListener(this);
	    this.helpBtn.addActionListener(this);
	    this.exeBtn.addActionListener(this);
	    this.refreshBtn.addActionListener(this);
	    this.validateSyntaxBtn.addActionListener(this);
	    // JPanel cmdPanel = new JPanel(new GridLayout(2, 1));
	    JPanel cmdPanel = new JPanel(new BorderLayout());
	    // cmdPanel.add(new
	    // JLabel("Enter command to customzie the instance"));
	    this.cmdScrol.setViewportView(this.cmdArea);
	    cmdPanel.add(this.cmdScrol, BorderLayout.CENTER);
	    cmdPanel.setBorder(BorderFactory
		    .createTitledBorder("Enter script to customzie the instance"));

	    JPanel btnPanel = new JPanel(new FlowLayout());
	    btnPanel.add(this.loadBtn);
	    btnPanel.add(this.exeBtn);
	    btnPanel.add(this.validateSyntaxBtn);
	    btnPanel.add(this.refreshBtn);
	    btnPanel.add(this.helpBtn);
	    cmdPanel.add(btnPanel, BorderLayout.SOUTH);

	    //
	    this.consoleScrol.setViewportView(this.consoleArea);
	    JPanel consolePanel = new JPanel(new BorderLayout());
	    consolePanel.add(this.consoleScrol);
	    consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));
	    JPanel rightPanel = new JPanel(new GridLayout(2, 1));
	    rightPanel.add(cmdPanel);

	    rightPanel.add(consolePanel);
	    this.mainsplit.add(rightPanel, JSplitPane.RIGHT);
	    this.mainsplit.setDividerLocation(0.5);
	}

	this.getContentPane().add(mainPanel);
	this.setSize(800, 600);

    }

    private void updateView() throws SerendipException {
	// Update view
	if ((null != this.epcView) && (null != this.mainsplit)) {
	    this.mainsplit.remove(this.epcView);
	    // this.mainPanel.remove(this.epcView);
	}

	// Need to construct a new process view
	String curPid = this.pi.getId();
	// Get the up-to-date process instance (Note: This might not be true)
	this.pi = this.engine.getProcessInstance(curPid);

	ProcessView pv = this.pi.getCurrentProcessView();// Get a fresh view

	SerendipView.markFiredEvents(pv.getViewAsEPC(), this.engine
		.getEventCloud().getEventRecordsForPid(pi.getPId()));
	SerendipView.markExecutedTasks(pv.getViewAsEPC(), this.pi
		.getCompletedTasks().keySet(), pi.getCurrentTasks().keySet());

	this.epcView = new SerendipEPCView(pv);// this.pi.getPDef().getId()
	this.mainsplit.add(this.epcView, JSplitPane.LEFT);
	this.mainsplit.setDividerLocation(0.5);
	this.mainPanel.updateUI();
	this.repaint();
    }

    /**
     * Display a message by appending to the console area
     * 
     * @param message
     * @param isError
     */
    private void displayMessage(String message, boolean isError) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	String prefix = dateFormat.format(date);
	if (isError) {
	    prefix += "[ERROR]";
	}
	this.consoleArea.append("\n" + prefix + "\t" + message + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(this.exeBtn)
		| e.getSource().equals(this.cmdArea)) {
	    boolean res = false;
	    String script = cmdArea.getText();
	    try {
		// This where we do the change
		new AdaptationScriptEngine(this.engine).execute(script);

		// No exceptions. Everything is good!
		// JOptionPane.showMessageDialog(this,
		// "Script executed  successfully\n" + script,
		// "Done", JOptionPane.INFORMATION_MESSAGE);
		this.displayMessage("Script executed successfully", false);

	    } catch (SerendipException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
		// JOptionPane.showMessageDialog(this,
		// "Something went wrong while executing \n"
		// + script + "\n\n" + e2.getMessage(),
		// "Error", JOptionPane.ERROR_MESSAGE);
		this.displayMessage(
			"Script executtion error." + e2.getMessage(), true);
	    }

	    // Update
	    try {
		this.updateView();
	    } catch (SerendipException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	} else if (e.getSource().equals(this.refreshBtn)) {
	    try {

		this.updateView();

		// TEST
		String s = TextualWriter.processToText(this.pi);
		s = TextualWriter.btToText(this.pi.getBehaviorTerm("CO-TC_B1"));
		log.debug(s);
	    } catch (SerendipException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	} else if (e.getSource().equals(this.validateSyntaxBtn)) {
	    try {
		boolean result = new AdaptationScriptEngine(this.engine)
			.validateScriptSyntax(cmdArea.getText());
		if (result) {
		    this.displayMessage("Verifcation Successful", false);
		} else {
		    this.displayMessage("Verifcation unsuccessful", false);
		}
	    } catch (Exception e1) {
		// TODO Auto-generated catch block
		this.displayMessage(
			"Verifcation unsuccessful \n" + e1.getMessage(), false);
	    }
	} else if (e.getSource().equals(this.loadBtn)) {

	    final JFileChooser fc = new JFileChooser(
		    ModelProviderFactory.getProperty("DEFAULT_LOC"));
	    String fileName = null;
	    int returnVal = fc.showOpenDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		fileName = fc.getSelectedFile().getAbsolutePath();
	    } else {
		return;// We Ignore
	    }
	    StringBuffer contents = CommonFileReader.readFileContents(fileName);
	    this.cmdArea.setText(contents.toString());
	    this.displayMessage("Script loaded from " + fileName, false);

	} else if (e.getSource().equals(this.helpBtn)) {
	    // String text = "[ALLOWED] add_event " + pi.getId()
	    // + " PayRepair RepairOK pre AND\n" + "[ALLOWED] set_pp "
	    // + pi.getId() + " SendTowReq 12";
	    JOptionPane.showMessageDialog(null, new JTextArea("Coming soon", 5,
		    10));

	}

    }

}
