package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.sun.codemodel.JLabel;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;
import au.edu.swin.ict.serendip.composition.Role;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.composition.view.SerendipView;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.util.CompositionUtil;
import au.edu.swin.ict.serendip.core.log.LogWriter;

public class AdminView extends JFrame implements ActionListener {
    static Logger log = Logger.getLogger(AdminView.class);
    private SerendipEngine engine = null;
    private JTabbedPane tabbedPane = null;
    private JTextArea logTextArea = null;
    private JPanel logViewPanel, processViewPanel = null;
    private AdminLogAppender logAppender = null;
    private DefaultTableModel processDtModel = null;
    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, toolsMenu, wizardMenu, helpMenu;
    private JMenuItem openItem, exitItem;
    private JMenuItem processViewItem, srViewItem;
    private JMenuItem addNewTaskWiz, addNewBTWiz, addNewPDWiz, addNewSRWiz;
    private JMenuItem prefManuItem, cmdManuItem;
    private JMenuItem helpManuItem, aboutManuItem;
    private JButton fireEventBtn = new JButton("FireEvent", new ImageIcon(
	    "./images/ui/fire.png"));
    private JButton saveLogBtn = new JButton("Save Log", new ImageIcon(
	    "./images/ui/save.png"));
    private JButton pauseAllBtn = new JButton("Pause All", new ImageIcon(
	    "./images/ui/pause.png"));
    private JButton abortProcessBtn = new JButton("Abort", new ImageIcon(
	    "./images/ui/abort.png"));
    private JTextField eventRecordTF, processIdTF;

    public AdminView() {
	this.setLookNFeel();
	this.startView();
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);// Dont add for other
						     // constructor
    }

    public AdminView(SerendipEngine engine) {
	super();
	this.startView();
	this.createView(engine);

    }

    // Start the initial view
    private void startView() {

	this.createMenus();// TODO
	this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	this.add(this.tabbedPane);
	this.setSize(800, 600);
	this.setVisible(true);
    }

    // Populate the view for a given engine instance
    private void createView(SerendipEngine engine) {
	this.engine = engine;

	// this.setLookNFeel();
	this.setTitle(this.engine.getCompositionName()
		+ " : SeRenDiP Monitoring Tool");
	// this.setVisible(true);
	//
	this.setSize(800, 720);

	// Add tabs
	this.addLogView();
	this.addCompositionView();
	// this.addRuntimeView();
	this.addProcessTreeView(this.engine);
	this.addPDViews();

	// this.createMenus();// TODO
    }

    // For other tools extending the admin view e.g. SaaS
    protected void addTab(String title, JPanel panel) {
	this.tabbedPane.add(title, panel);
    }

    private void setLookNFeel() {
	try {
	    // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    // for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
	    // {
	    // if ("Nimbus".equals(info.getName())) {
	    // UIManager.setLookAndFeel(info.getClassName());
	    // break;
	    // }
	    // }
	    // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void addPDViews() {
	this.tabbedPane.add("Process Views", new EPCViewSelectionPanel(
		this.engine, true));
    }

    private void addCompositionView() {
	this.tabbedPane.add("Overview", new CompositionView(this.engine));
    }

    // private void addSnapshotView(){
    // JPanel snapshotPane = new JPanel(new BorderLayout());
    //
    // }

    private void createMenus() {

	menuBar = new JMenuBar();
	fileMenu = new JMenu("File");
	menuBar.add(fileMenu);
	viewMenu = new JMenu("View");
	// menuBar.add(viewMenu);
	toolsMenu = new JMenu("Tools");
	// menuBar.add(toolsMenu);
	wizardMenu = new JMenu("Wizards");
	// menuBar.add(wizardMenu);

	// shift to the right
	menuBar.add(Box.createGlue());
	helpMenu = new JMenu("Help");
	menuBar.add(helpMenu);

	// File Menu

	openItem = new JMenuItem("Open");
	openItem.addActionListener(this);
	fileMenu.add(openItem);
	exitItem = new JMenuItem("Exit");
	exitItem.addActionListener(this);
	fileMenu.add(exitItem);

	// View Menu
	srViewItem = new JMenuItem("SR View");
	viewMenu.add(srViewItem);
	processViewItem = new JMenuItem("Process View");
	viewMenu.add(processViewItem);

	// Tools Menu
	// prefManuItem = new JMenuItem("Preferences");
	// toolsMenu.add(prefManuItem);
	// cmdManuItem = new JMenuItem("Command Shell");
	// toolsMenu.add(cmdManuItem);
	//
	// // Wizards Menu
	// addNewTaskWiz = new JMenuItem("Add new Task");
	// wizardMenu.add(addNewTaskWiz);
	// addNewBTWiz = new JMenuItem("Add new BT");
	// wizardMenu.add(addNewBTWiz);
	// addNewPDWiz = new JMenuItem("Add new PD");
	// wizardMenu.add(addNewPDWiz);
	// addNewSRWiz = new JMenuItem("Add new SR");
	// wizardMenu.add(addNewSRWiz);

	// Help Menu
	helpManuItem = new JMenuItem("Help");
	helpMenu.add(helpManuItem);
	aboutManuItem = new JMenuItem("About");
	helpMenu.add(aboutManuItem);

	this.setJMenuBar(menuBar);
    }

    private void addProcessTreeView(SerendipEngine engine) {
	ProcessTreeView ptView = new ProcessTreeView(engine);
	this.tabbedPane.addTab("Process Instances", ptView);
    }

    private void addLogView() {
	this.logViewPanel = new JPanel(new BorderLayout());
	this.tabbedPane.addTab("Runtime Monitor", logViewPanel);

	// Add text are
	this.logTextArea = new JTextArea();

	this.logTextArea.setAutoscrolls(true);
	this.logTextArea.setEditable(true);
	// this.logTextArea.setBackground(Color.BLACK);
	// this.logTextArea.setForeground(Color.green);
	JScrollPane logScrollPane = new JScrollPane(logTextArea);
	this.logViewPanel.add(logScrollPane, BorderLayout.CENTER);

	// Event related panel
	eventRecordTF = new JTextField("ComplainRcvd,null");
	JPanel eventPanel = new JPanel(new FlowLayout());
	eventPanel.setBorder(BorderFactory.createTitledBorder("Events"));
	fireEventBtn.addActionListener(this);
	pauseAllBtn.addActionListener(this);
	saveLogBtn.addActionListener(this);
	eventPanel.add(eventRecordTF);
	eventPanel.add(fireEventBtn);
	eventPanel.add(pauseAllBtn);
	// Process related
	JPanel processPanel = new JPanel(new FlowLayout());
	processPanel.setBorder(BorderFactory.createTitledBorder("Process"));
	processIdTF = new JTextField("Process Id");
	abortProcessBtn.addActionListener(this);
	processPanel.add(processIdTF);
	processPanel.add(abortProcessBtn);

	// Add all to the lower panel
	JPanel lowerBtnPanel = new JPanel(new FlowLayout());

	lowerBtnPanel.add(eventPanel);
	lowerBtnPanel.add(processPanel);
	lowerBtnPanel.add(saveLogBtn);
	logViewPanel.add(lowerBtnPanel, BorderLayout.SOUTH);
	// run the update thread
	UpdateThread ut = new UpdateThread(this.logTextArea);
	this.engine.subscribeLogWriter(ut);
	ut.start();

	this.logTextArea
		.append("Message listener is running. Waiting for log messages...");

    }

    private DefaultTableModel createDefaultTableModel() {
	String[] columnNames = { "Pid", "DefId", "pi status", "Current Task",
		"Oblig Role" };
	Object[][] data = {};
	processDtModel = new DefaultTableModel();
	processDtModel.setDataVector(data, columnNames);

	return processDtModel;
    }

    // External methods
    public void addProcessInstanceToView(ProcessInstance pi) {

	// First remove all the tasks
	for (int i = 0; i < this.processDtModel.getRowCount(); i++) {
	    String tbl_pid = (String) this.processDtModel.getValueAt(i,
		    this.processDtModel.findColumn("Pid"));// Get the pid
	    if (tbl_pid.equals(pi.getPId()))
		;
	    {// We remove tasks of this process instance only
		this.processDtModel.removeRow(i);
	    }
	}

	Set<String> set = pi.getCurrentTasks().keySet();

	Iterator<String> itr = set.iterator();
	while (itr.hasNext()) {
	    String taskId = itr.next();
	    Task task = pi.getCurrentTasks().get(taskId);

	    String ObligRoleId = null;

	    if (null != task) {
		ObligRoleId = task.getObligatedRoleId();
	    } else {
		taskId = "N/A";
		ObligRoleId = "N/A";
	    }
	    if (ProcessInstance.status.completed != pi.getCurrentStatus()) {
		this.processDtModel.addRow(new Object[] { pi.getPId(),
			pi.getDefinitionId(), pi.getCurrentStatus(), taskId,
			ObligRoleId });
	    }
	}
    }

    public void updateProcessView() {
	Hashtable<String, ProcessInstance> processInstances = this.engine
		.getLiveProcessInstances();
	for (int i = 0; i < processInstances.size(); i++) {
	    ProcessInstance pi = processInstances.get(i);
	    this.addProcessInstanceToView(pi);
	}
    }

    public void exit() {
	System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	// Menu items
	if (e.getSource().equals(this.exitItem)) {
	    this.exit();
	} else if (e.getSource().equals(this.openItem)) {
	    Composite composite = null;
	    final JFileChooser fc = new JFileChooser(
		    ModelProviderFactory.getProperty("DEFAULT_LOC"));
	    String fileName = null;
	    int returnVal = fc.showOpenDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		fileName = fc.getSelectedFile().getAbsolutePath();
	    } else {
		return;// We Ignore
	    }
	    CompositeDemarshaller demarsheller = new CompositeDemarshaller();
	    try {
		File file = new File(fileName);
		composite = demarsheller.demarshalSMC(file.getAbsolutePath());
		if (null == composite) {
		    log.error("Cannot instantiate the composite from file "
			    + file.getAbsoluteFile());
		}

	    } catch (CompositeDemarshallingException ex) {
		ex.printStackTrace();
	    } catch (ConsistencyViolationException ex) {
		ex.printStackTrace();
	    } catch (CompositeInstantiationException ex) {
		ex.printStackTrace();
	    }

	    Thread compo = new Thread(composite);
	    compo.start();

	    // Test starting a new instance
	    SerendipEngine engine = composite.getSerendipEngine();
	    this.createView(engine);

	    // Buttons
	} else if (e.getSource().equals(this.fireEventBtn)) {

	    String eventRcord = this.eventRecordTF.getText().trim();
	    String[] split = eventRcord.split(",");
	    log.debug("Firing event " + eventRcord);
	    try {
		if ((split[1].equals("")) || (split[1].equals("null"))) {
		    this.engine.addEvent(new EventRecord(split[0], null));
		} else {
		    this.engine.addEvent(new EventRecord(split[0], split[1]));
		}
	    } catch (SerendipException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	} else if (e.getSource().equals(this.saveLogBtn)) {
	    String s = this.logTextArea.getText();
	    final JFileChooser fc = new JFileChooser(".");
	    String fileName = null;
	    int returnVal = fc.showSaveDialog(this.logTextArea);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		fileName = fc.getSelectedFile().getAbsolutePath();
	    } else {
		return;// We Ignore
	    }
	    try {
		// Create file
		FileWriter fstream = new FileWriter(fileName);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(s);
		// Close the output stream
		out.close();
	    } catch (Exception ex) {// Catch exception if any
		System.err.println("Error: " + ex.getMessage());
	    }
	} else if (e.getSource().equals(this.pauseAllBtn)) {
	    if (this.pauseAllBtn.getText().equals("Pause All")) {
		this.engine.pauseAllProcessInstances();
		this.pauseAllBtn.setText("Resume All");
		this.pauseAllBtn.setIcon(new ImageIcon("./images/ui/run.png"));
	    } else {
		this.engine.resumeAllProcessInstances();
		this.pauseAllBtn.setText("Pause All");
		this.pauseAllBtn
			.setIcon(new ImageIcon("./images/ui/pause.png"));
	    }
	} else if (e.getSource().equals(this.abortProcessBtn)) {
	    String pid = this.processIdTF.getText();
	    if (pid.equals("")) {
		JOptionPane.showMessageDialog(this,
			"Please enter a valid process id");
		return;
	    }
	    ProcessInstance pi = this.engine.getProcessInstance(pid.trim());
	    if (null == pi) {
		JOptionPane.showMessageDialog(this, "No such process instance "
			+ pid);
		return;
	    }
	    this.engine.removeProcessInstance(pi);
	}
    }

    /**
     * The logwriter class to act as a runtime monitor
     * 
     * @author Malinda
     * 
     */
    class UpdateThread extends Thread implements LogWriter {

	private JTextArea la = null;

	public UpdateThread(JTextArea aTextArea) {
	    this.la = aTextArea;
	}

	private Queue<String> logMsgQ = new ConcurrentLinkedQueue<String>();

	@Override
	public void run() {
	    while (true) {
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// // e.printStackTrace();
		// }
		while (!this.logMsgQ.isEmpty()) {
		    this.la.append(this.logMsgQ.poll());
		    this.la.setCaretPosition(la.getDocument().getLength());// auto
									   // scrol
		    this.la.repaint();
		}
	    }
	}

	@Override
	public void writeLog(String context, String message) {
	    // logTextArea.append("\n[" + context + "]\t:" + message);
	    // logTextArea.updateUI();
	    log.debug("\n[" + context + "]\t:" + message);
	    this.logMsgQ.add("\n[" + context + "]\t:" + message);

	}
    }

    public static void main(String[] args) {
	AdminView av = new AdminView();
    }
}
