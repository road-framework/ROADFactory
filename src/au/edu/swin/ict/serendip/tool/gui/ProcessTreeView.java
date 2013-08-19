package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;

//import com.jaxfront.core.log.Logger;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.composition.view.ProcessView;
import au.edu.swin.ict.serendip.composition.view.SerendipView;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventCloud;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.textual.TextualWriter;

public class ProcessTreeView extends JPanel implements TreeSelectionListener,
	ActionListener {
    private JTree tree;
    private SerendipEngine engineInstance = null;
    private JButton refreshBtn = new JButton("Refresh");
    private DefaultMutableTreeNode rootNode = null;
    private JScrollPane treeViewScrol, textViewScrol, epcViewScrol = null;
    private JMenuItem modifyMenuItem, removeMenuItem, showViewMenuItem = null;;
    private JLabel infoLable = new JLabel(
	    "Refresh to view current process instances ");
    private JTextArea textArea = new JTextArea();
    private JCheckBox showOldChkBox = new JCheckBox("Show Completed Processes",
	    false);
    private JCheckBox autoUpdateChkBox = new JCheckBox("Auto Update", false);

    // private JPanel epcViewPanel = new JPanel();
    public ProcessTreeView(SerendipEngine engineInstance) {
	super();
	this.engineInstance = engineInstance;
	this.createPINodes();

	// CreateGUI
	tree = new JTree(rootNode);

	tree.setToolTipText("Right Click -> Modify \n to modify a process instance");
	tree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);

	// Listen for when the selection changes.
	tree.addTreeSelectionListener(this);

	// Create the scroll pane and add the tree to it.
	treeViewScrol = new JScrollPane(tree);
	treeViewScrol.setBorder(new TitledBorder("Process Instances"));

	textViewScrol = new JScrollPane(textArea);
	textViewScrol.setBorder(new TitledBorder("Description"));

	epcViewScrol = new JScrollPane(new JLabel(getInitLableText())
	// "<--- Select a process-instance/behavior or a task from the  tree to visualize. "
	// +
	// "\n Then Right Click > Modify \n to modify a process instance. " +
	// "\n Refresh to retireve the latest process instances. " +
	// "\n Check above check-box to view the completed process instances")
	);

	JSplitPane epcTextSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		epcViewScrol, textViewScrol);
	epcTextSplit.setDividerLocation(300);
	refreshBtn.addActionListener(this);

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		treeViewScrol, epcTextSplit);
	splitPane.setDividerLocation(200);
	this.setLayout(new BorderLayout());
	this.createPopupMenu();// Popup menu to modify, remove etc.

	JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	btnPanel.add(this.infoLable);
	btnPanel.add(this.refreshBtn);
	btnPanel.add(this.autoUpdateChkBox);
	btnPanel.add(this.showOldChkBox);
	this.add(splitPane, BorderLayout.CENTER);
	this.add(btnPanel, BorderLayout.NORTH);

    }

    private String getInitLableText() {
	return "<html>How to use this view? "
		+ "<P>"
		+ "Please follow the instructions below"
		+ "<UL>"
		+

		"  <LI>Press <b>Refresh</b> button to retireve the latest process instances"
		+ "  <LI>Once retrieved, select a process-instance/behavior or a task from the <b>tree</b> to visualize."
		+ "  <LI>Fill above check-box and press refresh button to view the completed process instances. "
		+

		"</UL>";
    }

    private void createPINodes() {

	// Remove current branches
	if (null != this.rootNode) {
	    this.rootNode.removeAllChildren();
	    this.rootNode = null;
	}
	// Create the nodes.
	this.rootNode = new DefaultMutableTreeNode(
		this.engineInstance.getCompositionName());

	Hashtable<String, ProcessInstance> processInstances = null;

	if (this.showOldChkBox.isSelected()) {
	    processInstances = this.engineInstance
		    .getCompletedProcessInstances();
	} else {
	    processInstances = this.engineInstance.getLiveProcessInstances();
	}

	// Iterate thru process instances and populate the tree
	Enumeration<String> enumProcessIns = processInstances.keys();
	while (enumProcessIns.hasMoreElements()) {

	    ProcessInstance pi = processInstances.get(enumProcessIns
		    .nextElement());

	    DefaultMutableTreeNode piNode = new DefaultMutableTreeNode(
		    pi.getId());
	    rootNode.add(piNode);
	    Vector<BehaviorTerm> btVec = pi.getBtVec();
	    for (int j = 0; j < btVec.size(); j++) {
		BehaviorTerm bt = btVec.get(j);
		DefaultMutableTreeNode btNode = new DefaultMutableTreeNode(
			bt.getId());
		piNode.add(btNode);

		Vector<Task> taskVec = bt.getTasksVec();
		for (int k = 0; k < taskVec.size(); k++) {
		    Task task = taskVec.get(k);
		    // DefaultMutableTreeNode taskNode = new
		    // DefaultMutableTreeNode(
		    // task.getId() + task.getEventPattern() + "("
		    // + task.getObligatedRoleId() + "."
		    // + task.getTaskDescr() + ")");
		    DefaultMutableTreeNode taskNode = new DefaultMutableTreeNode(
			    task.getId());

		    btNode.add(taskNode);
		}
	    }
	}
	// Set icon
	ImageIcon leafIcon = null;// createImageIcon("images/process_arrow.ico");//
				  // E:/ROAD/svn/serendip/trunk
	if (leafIcon != null) {
	    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	    renderer.setLeafIcon(leafIcon);
	    tree.setCellRenderer(renderer);
	}
    }

    public void updateView() {

	this.createPINodes();
	this.tree.setModel(new DefaultTreeModel(this.rootNode));
	this.tree.repaint();

    }

    private ConfigurableEPC markVisitedEventsAndTasks(ConfigurableEPC epc,
	    ProcessInstance pi) {
	Vector<EventRecord> triggeredEvents = this.engineInstance
		.getEventCloud().getEventRecordsForPid(pi.getPId());
	epc = SerendipView.markFiredEvents(epc, triggeredEvents);
	epc = SerendipView.markExecutedTasks(epc, pi.getCompletedTasks()
		.keySet(), pi.getCurrentTasks().keySet());
	return epc;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

	String text = null;

	// TODO Add info about task to the text area
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
		.getLastSelectedPathComponent();
	if (null == node) {
	    return;
	}
	TreeNode[] nodes = node.getPath();
	if (null == nodes) {
	    return;
	}
	if (nodes.length <= 1) {// If the selected node is a level 1 node, just
				// show the name
	    this.textArea.setText(nodes[0].toString());
	    return;
	}
	if (nodes.length <= 2) {
	    ProcessInstance pi = this.engineInstance
		    .getProcessInstance(nodes[1].toString());
	    String s = TextualWriter.processToText(pi);

	    this.textArea.setText(s);
	    ConfigurableEPC showEPC = null;
	    try {
		if (null != pi) {
		    ProcessView pv = pi.getCurrentProcessView();
		    showEPC = pv.getViewAsEPC();
		    // Here we mark the triggered events in a different color
		    showEPC = this.markVisitedEventsAndTasks(showEPC, pi);

		    SerendipEPCView epcView = new SerendipEPCView(pi.getId(),
			    showEPC);
		    this.epcViewScrol.setViewportView(epcView);
		} else {
		    System.err
			    .println("ERROR: Process instance is null in Process Tree View");
		}
	    } catch (SerendipException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    return;
	}

	ProcessInstance pi = this.engineInstance.getProcessInstance(nodes[1]
		.toString());
	if (null != pi) {
	    String btId = nodes[2].toString();
	    BehaviorTerm bt = pi.getBehaviorTerm(btId);

	    if (null != bt) {

		if (nodes.length >= 4) { // Task is selected

		    Task task = bt.getTask(nodes[3].toString());

		    text = "Task\t\t: " + task.getId()
			    + "\nPre-EP\t\t: "
			    // + task.getEventPattern() + "\nInput Msgs\t\t:"
			    // + task.getInputMsgs() + "\nObligatedBy\t\t:"
			    + task.getObligatedRoleId() + "\nDescription\t\t:"
			    + task.getTaskDescr() + "\npost-EP\t\t: "
		    // + task.getPostEventPattern() + "\nOutput Msg\t\t:"
		    // + task.getOutMessageId()
		    ;

		    this.textArea.setText(text);
		    ConfigurableEPC showEPC = null;
		    if (null != task.constructEPC()) {
			showEPC = task.getEpc();
		    } else {
			showEPC = bt.getEpc();
		    }
		    showEPC = this.markVisitedEventsAndTasks(showEPC, pi);
		    SerendipEPCView epcView = new SerendipEPCView(task.getId(),
			    showEPC);
		    this.epcViewScrol.setViewportView(epcView);

		} else {
		    // Task is not selected but a behavior term is
		    // this.textArea.setText(bt.getId());

		    String btText = TextualWriter.btToText(pi
			    .getBehaviorTerm(bt.getId()));
		    this.textArea.setText(btText);

		    // Some extra work to give a graphical representation
		    ConfigurableEPC btEPC = bt.getEpc();
		    btEPC = this.markVisitedEventsAndTasks(btEPC, pi);
		    SerendipEPCView epcView = new SerendipEPCView(bt.getId(),
			    btEPC);
		    this.epcViewScrol.setViewportView(epcView);
		}
	    }
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	try {
	    if (e.getSource().equals(this.refreshBtn)) {
		this.updateView();
	    } else if (e.getSource().equals(this.removeMenuItem)) {
		// this.handleRemove(e);
	    } else if (e.getSource().equals(this.modifyMenuItem)) {
		this.handleModify();
	    } else if (e.getSource().equals(this.showViewMenuItem)) {

		this.handleShowView();

	    }
	} catch (SerendipException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

    private void handleModify() throws SerendipException {

	DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
		.getLastSelectedPathComponent();
	TreeNode[] nodes = node.getPath();
	if (null == nodes[1]) {
	    JOptionPane.showMessageDialog(this, "Invalid process instance");
	    return;
	}
	String pId = nodes[1].toString();

	ProcessInstance pi = this.engineInstance.getProcessInstance(pId);

	ProcessInstanceViewFrame piFrame = new ProcessInstanceViewFrame(pi,
		true);
	piFrame.setVisible(true);

    }

    private void handleShowView() throws SerendipException {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
		.getLastSelectedPathComponent();
	if (null == node) {
	    JOptionPane
		    .showMessageDialog(this,
			    "Please select a process instance from the tree. \nRight click to modify it");
	}
	TreeNode[] nodes = node.getPath();

	String pId = nodes[1].toString();

	ProcessInstance pi = this.engineInstance.getProcessInstance(pId);
	ProcessInstanceViewFrame piFrame = new ProcessInstanceViewFrame(pi,
		false);
	piFrame.setVisible(true);
    }

    private String getSelectedLeafNodeText() {
	String str = null;
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
		.getLastSelectedPathComponent();

	if (null == node) {
	    return null;
	}

	Object nodeInfo = node.getUserObject();
	if (node.isLeaf()) {
	    str = (String) nodeInfo;
	}

	return str;
    }

    protected ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = this.getClass().getResource(path);

	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {

	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    public void createPopupMenu() {

	// Create the popup menu.
	JPopupMenu popup = new JPopupMenu();
	this.modifyMenuItem = new JMenuItem("Modify");
	this.modifyMenuItem.addActionListener(this);
	popup.add(this.modifyMenuItem);
	// this.removeMenuItem = new JMenuItem("Remove");
	// this.removeMenuItem.addActionListener(this);
	// popup.add(this.removeMenuItem);

	this.showViewMenuItem = new JMenuItem("Show Process View");
	this.showViewMenuItem.addActionListener(this);
	popup.add(this.showViewMenuItem);

	// Add listener to the text area so the popup menu can come up.
	MouseListener popupListener = new PopupListener(popup);
	this.tree.addMouseListener(popupListener);
    }

    class PopupListener extends MouseAdapter {
	JPopupMenu popup;

	PopupListener(JPopupMenu popupMenu) {
	    popup = popupMenu;
	}

	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
		popup.show(e.getComponent(), e.getX(), e.getY());
	    }
	}
    }
}
