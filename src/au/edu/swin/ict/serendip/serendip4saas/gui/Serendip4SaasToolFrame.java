package au.edu.swin.ict.serendip.serendip4saas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Task;

import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.epc.EPMLWriter;
import au.edu.swin.ict.serendip.epc.MergeBehavior;
import au.edu.swin.ict.serendip.epc.PatternToEPC;
import au.edu.swin.ict.serendip.tool.gui.SerendipEPCView;
import au.edu.swin.ict.serendip.tool.gui.SerendipJFrame;

/**
 * TODO: Fix the visualization issue.
 * 
 * @author Malinda
 * 
 */
public class Serendip4SaasToolFrame extends JFrame implements ActionListener {
    static Logger log = Logger.getLogger(Serendip4SaasToolFrame.class);
    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, toolsMenu, wizardMenu, helpMenu;
    private JMenuItem openItem, exitItem;
    private JMenuItem processViewItem, srViewItem;
    private JMenuItem addNewTaskWiz, addNewBTWiz, addNewPDWiz, addNewSRWiz;
    private JMenuItem prefManuItem, cmdManuItem;
    private JMenuItem helpManuItem, aboutManuItem;
    private Vector<BehaviorTerm> behaviorTerms = null;
    private JButton selectBtn, removeBtn, modifyBtn, epcBtn = null;
    private SerendipJList fromList, toList = null;
    private JScrollPane epcViewScrol = null;

    public Serendip4SaasToolFrame(Vector<BehaviorTerm> behaviorTerms)
	    throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedLookAndFeelException {
	this.behaviorTerms = behaviorTerms;
	// UI
	this.createUI();
    }

    private void createUI() throws ClassNotFoundException,
	    InstantiationException, IllegalAccessException,
	    UnsupportedLookAndFeelException {
	UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setIconImage(new ImageIcon("images/Refresh-icon.png").getImage());
	this.createMenus();
	this.setTitle("Designer view");
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel tenant1Panel = new JPanel(new BorderLayout());

	this.createTenantView(tenant1Panel);
	tabbedPane.addTab("Tenant1: InsuranceCo", null, tenant1Panel,
		"InsuranceCo");

	tabbedPane.addTab("Tenant2: TravelCo", null, new JPanel(), "TravelCo");
	tabbedPane.addTab("Tenant3: CarSalesCo", null, new JPanel(),
		"CarSalesCo");
	tabbedPane.addTab("Organization : RoSaaS", null, new JPanel(),
		"Organization");

	this.getContentPane().add(tabbedPane);
	this.setSize(600, 400);

	this.setVisible(true);
    }

    private void createTenantView(JPanel panel) {

	JScrollPane controlScrol = new JScrollPane();
	this.epcViewScrol = new JScrollPane();

	// Add controls view
	JPanel controlsPanel = new JPanel(new BorderLayout());

	controlScrol.setViewportView(controlsPanel);
	// Add north label
	controlsPanel
		.add(new JLabel(
			"Select/Remove behavior terms to update the process view for PD_InsuranceCo.\n"),
			BorderLayout.NORTH);

	// Add south advanced controls
	JPanel southPanel = new JPanel(new FlowLayout());
	southPanel.add(new JCheckBox("Hide abstracts", true));
	southPanel.add(new JButton("Refresh"));// new
					       // ImageIcon("images/Refresh-icon.png")
	southPanel.add(new JButton("Validate"));
	southPanel.add(new JButton("Export EPML"));
	southPanel.add(new JButton("Export PetriNet"));
	controlsPanel.add(southPanel, BorderLayout.SOUTH);

	// Add East
	epcBtn = new JButton("}");
	this.epcBtn.addActionListener(this);
	epcBtn.setFont(new Font(Font.SERIF, Font.PLAIN, 88));
	controlsPanel.add(epcBtn, BorderLayout.EAST);
	// Add center lists
	JPanel listsPanel = new JPanel(new GridLayout(1, 3));
	controlsPanel.add(listsPanel, BorderLayout.CENTER);

	JPanel arrowBtnPanel = new JPanel(new GridLayout(8, 1));
	arrowBtnPanel.setSize(50, 100);
	arrowBtnPanel.add(new JLabel(""));
	arrowBtnPanel.add(new JLabel(""));
	arrowBtnPanel.add(new JLabel(""));
	this.selectBtn = new JButton("Select >>");
	this.removeBtn = new JButton("<< Remove");
	this.modifyBtn = new JButton("View / Modify");

	this.selectBtn.addActionListener(this);
	this.removeBtn.addActionListener(this);
	this.modifyBtn.addActionListener(this);

	arrowBtnPanel.add(selectBtn);
	arrowBtnPanel.add(removeBtn);
	arrowBtnPanel.add(new JLabel(""));
	arrowBtnPanel.add(modifyBtn);

	// Populate fromList
	ArrayList<String> btIdList = new ArrayList<String>();
	for (BehaviorTerm bt : this.behaviorTerms) {
	    btIdList.add(bt.getId());
	}
	/*
	 * String[] btIds = new String[]{ "ZZZ", "ZZZ", "ZZZ", "ZZZ", "ZZZ",
	 * "ZZZ", "ZZZ", "ZZZ",
	 * "ProvideTaxi","ProvideTaxi2","ProvideTaxi3","ProvideHotel"
	 * ,"ProvideHotel2"
	 * ,"ProvideHotel3","Repairing","Repairing2","Repairing3"
	 * ,"Repairing4","Towing","Towing2", "Towing3", "ZZZ", "ZZZ", "ZZZ",
	 * "ZZZ", "ZZZ"};
	 */
	String[] btIds = new String[btIdList.size()];
	this.fromList = new SerendipJList("Available Behaviors",
		btIdList.toArray(btIds));
	fromList.setSize(50, 100);
	this.toList = new SerendipJList("Selected Behaviors", new String[] {});// Pass
									       // an
									       // empty
									       // array
	toList.setSize(50, 100);
	listsPanel.add(fromList);
	listsPanel.add(arrowBtnPanel);
	listsPanel.add(toList);

	// Add an EPC view later
	this.createProcessView2();

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		controlScrol, epcViewScrol);
	splitPane.setDividerLocation(200);
	panel.add(splitPane, BorderLayout.CENTER);
    }

    private BehaviorTerm getBTFromBTVector(String id) {
	for (BehaviorTerm b : this.behaviorTerms) {
	    if (b.getId().equals(id)) {
		log.debug("Found bt " + id);
		return b;
	    }
	}
	return null;
    }

    private void createProcessView() {

	String[] btIds = this.toList.getAllData();
	// if there are no selected behavior terms we will send an empty JPanel
	if (btIds.length < 1) {
	    log.info("Cannot generate the view");
	    this.epcViewScrol.setViewportView(new JPanel());
	    return;
	}

	ArrayList<BehaviorTerm> selectedBehaviorTerms = new ArrayList<BehaviorTerm>();
	for (String s : btIds) {
	    BehaviorTerm b = this.getBTFromBTVector(s);
	    if (null != b) {
		selectedBehaviorTerms.add(b);
	    }
	}

	ArrayList<ConfigurableEPC> epcArrayList = new ArrayList<ConfigurableEPC>();
	for (BehaviorTerm b : selectedBehaviorTerms) {
	    ConfigurableEPC epc = b.constructEPC();
	    epcArrayList.add(epc);
	}

	ConfigurableEPC[] epcs = new ConfigurableEPC[epcArrayList.size()];
	epcs = epcArrayList.toArray(epcs);

	ConfigurableEPC result = epcs[0];
	for (int i = 1; i < epcs.length; i++) {
	    result = MergeBehavior.mergeEPC(result, epcs[i]);
	}

	SerendipEPCView epcView = new SerendipEPCView("PD_InsuranceCo", result);

	this.epcViewScrol.setViewportView(epcView);

	EPMLWriter epmlWriter = new EPMLWriter(result, true);
	try {
	    epmlWriter.writeToFile("E:\\test.epml");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void createProcessView2() {
	try {
	    ConfigurableEPC[] epcs = {
		    // BT_Towing
		    PatternToEPC.convertToEPC("[ComplaintRcvd]",
			    "CO.SendTowReq",
			    "[TowReqSent] AND [PickupLocKnown]"),
		    PatternToEPC.convertToEPC("[TowReqSent]",
			    "GR.SendGRLocation", "[DestinationKnown]"),
		    PatternToEPC.convertToEPC(
			    "[PickupLocKnown] AND [DestinationKnown]",
			    "TC.Tow", "[CarTowSuccess] OR [CarTowFailed]"),
		    PatternToEPC.convertToEPC("[CarTowSuccess]",
			    "GR.TowingAck", "[TowingAckedByGR]"),
		    PatternToEPC.convertToEPC(
			    "[CarTowSuccess] AND [TowingAckedByGR]",
			    "CO.PayTow", "[TCPaid]"),

		    // BT_Repair
		    PatternToEPC.convertToEPC("[ComplaintRcvd]",
			    "CO.SendGRReq", "[GRReqSent]"),
		    PatternToEPC.convertToEPC(
			    "[GRReqSent] AND [CarTowSuccess]", "GR.ReqAdvPay",
			    "[AdvPayReqSent]"),
		    PatternToEPC.convertToEPC(
			    "[CarTowSuccess] AND [AdvPayReqSent]",
			    "CO.PayAdvToGR", "[AdvToGRPaid]"),
		    PatternToEPC.convertToEPC(
			    "[CarTowSuccess] AND [AdvToGRPaid]", "GR.Repair",
			    "[CarRepairSuccess] AND [CarRepairFailed]"),
		    PatternToEPC
			    .convertToEPC("[CarRepairSuccess]", "MM.Inspect",
				    "[CarRepairOKConfirmed] OR [CarRepairFailureNoified]"),
		    PatternToEPC.convertToEPC(
			    "[CarRepairSuccess] AND [CarRepairOKConfirmed] ",
			    "CO.PayGR", "[GRPaid]"),

	    };
	    // ConfigurableEPC t3 = PatternToEPC.convertoEPC("e5 | e2", "t1",
	    // "e6");

	    ConfigurableEPC result = epcs[0];
	    for (int i = 1; i < epcs.length; i++) {
		result = MergeBehavior.mergeEPC(result, epcs[i]);
	    }
	    log.debug("Done");
	    SerendipEPCView epcView = new SerendipEPCView("PD_InsuranceCo",
		    result);

	    this.epcViewScrol.setViewportView(epcView);
	} catch (SerendipException e) {
	    log.debug(e);

	}
    }

    private void createMenus() {

	menuBar = new JMenuBar();
	fileMenu = new JMenu("File");
	menuBar.add(fileMenu);
	viewMenu = new JMenu("View");
	menuBar.add(viewMenu);
	toolsMenu = new JMenu("Tools");
	menuBar.add(toolsMenu);
	wizardMenu = new JMenu("Wizards");
	menuBar.add(wizardMenu);
	menuBar.add(Box.createHorizontalGlue());
	helpMenu = new JMenu("Help");
	menuBar.add(helpMenu);

	// File Menu

	openItem = new JMenuItem("Open");
	fileMenu.add(openItem);
	exitItem = new JMenuItem("Exit");
	fileMenu.add(exitItem);

	// View Menu
	srViewItem = new JMenuItem("SR View");
	viewMenu.add(srViewItem);
	processViewItem = new JMenuItem("Process View");
	viewMenu.add(processViewItem);

	// Tools Menu
	prefManuItem = new JMenuItem("Preferences");
	toolsMenu.add(prefManuItem);
	cmdManuItem = new JMenuItem("Command Shell");
	toolsMenu.add(cmdManuItem);

	// Wizards Menu
	addNewTaskWiz = new JMenuItem("Add new Task");
	wizardMenu.add(addNewTaskWiz);
	addNewBTWiz = new JMenuItem("Add new BT");
	wizardMenu.add(addNewBTWiz);
	addNewPDWiz = new JMenuItem("Add new PD");
	wizardMenu.add(addNewPDWiz);
	addNewSRWiz = new JMenuItem("Add new SR");
	wizardMenu.add(addNewSRWiz);

	// Help Menu
	helpManuItem = new JMenuItem("Help");
	helpMenu.add(helpManuItem);
	aboutManuItem = new JMenuItem("About");
	helpMenu.add(aboutManuItem);

	this.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

	if (event.getSource().equals(this.selectBtn)) {
	    String selected = this.fromList.getSelectedItem();
	    if (null != selected) {
		this.toList.addItem(selected);
		this.fromList.removeItem(this.fromList.getSelectedIndex());
	    }
	} else if (event.getSource().equals(this.removeBtn)) {
	    String selected = this.toList.getSelectedItem();
	    if (null != selected) {
		this.fromList.addItem(selected);
		this.toList.removeItem(this.toList.getSelectedIndex());
	    }
	} else if (event.getSource().equals(this.epcBtn)) {

	    this.createProcessView2();

	} else if (event.getSource().equals(this.modifyBtn)) {

	    String id = this.fromList.getSelectedItem();
	    if (id != null) {
		BehaviorTerm bt = this.getBTFromBTVector(id);

		ConfigurableEPC epc = bt.constructEPC();
		if (null == epc) {
		    log.debug("EPC is null");
		}
		SerendipEPCView epcView = new SerendipEPCView(id, epc);
		SerendipJFrame frame = new SerendipJFrame(id);
		if (null != epcView) {
		    frame.getContentPane().add(epcView);
		} else {
		    frame.setTitle("Cannot generate the EPC view for " + id);
		}
		frame.setVisible(true);

	    }

	}

    }

}
