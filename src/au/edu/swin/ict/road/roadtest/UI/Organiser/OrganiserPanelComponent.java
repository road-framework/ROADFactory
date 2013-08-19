package au.edu.swin.ict.road.roadtest.UI.Organiser;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.ROADTest;
import au.edu.swin.ict.road.roadtest.UI.CompositionUI;
import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;
import au.edu.swin.ict.road.roadtest.filemanager.FileManager;

/**
 * This class represents Organiser Component that lets user to set Contract,
 * Term and Role related information
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class OrganiserPanelComponent extends JPanel implements
	OrganiserListener {

    private OrganiserTreeGenerator jTreeComposition;
    private JScrollPane jScrollPaneComposition;
    private OrganiserComponentRole jPanelRole;
    private OrganiserComponentContract jPanelContract;
    private OrganiserComponentTerm jPanelTerm;
    private OrganiserComponentOperation jPanelOperation;
    private ROADTest myComposite;
    private DefaultMutableTreeNode rootNode;
    private JTable jTableInbox;
    private DefaultTableModel modelInbox;
    private JScrollPane jScrollPaneInbox;

    private JButton jButtonClearInbox;

    /**
     * Constructor for OrganiserPanelComponent
     * 
     * @param myComposite
     *            <code>ROADTest</code> instance of ROADtest
     * @param fileMgr
     *            <code>FileManager</code> file manager
     */
    public OrganiserPanelComponent(ROADTest myComposite, FileManager fileMgr) {
	super();
	this.myComposite = myComposite;
	initializeComponent();
	this.setVisible(false);
    }

    /**
     * The function to initialize UI components
     */
    private void initializeComponent() {

	rootNode = new DefaultMutableTreeNode(myComposite);

	// generate the tree
	createNode(rootNode);
	jTreeComposition = new OrganiserTreeGenerator(rootNode);

	jScrollPaneComposition = new JScrollPane();
	jScrollPaneComposition.setViewportView(jTreeComposition);

	jTableInbox = new JTable();
	jTableInbox.setCellSelectionEnabled(false);
	jScrollPaneInbox = new JScrollPane();
	jScrollPaneInbox.setViewportView(jTableInbox);
	jButtonClearInbox = new JButton();
	jButtonClearInbox.setToolTipText("Clear Inbox");
	jButtonClearInbox.setIcon(new ImageIcon(CompositionUI.class
		.getResource("images/" + "clearicon.gif")));
	generateInbox();

	addComponent(this, jButtonClearInbox, 765, 510, 30, 25);
	addComponent(this, jScrollPaneComposition, 10, 21, 775, 206);
	addComponent(this, jScrollPaneInbox, 1, 540, 797, 100);

	myComposite.addOrganiserListener(this);
	// myComposite.getOrganier().registerNewOrganiserListener(this);

	jButtonClearInbox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonClearInbox_actionPerformed(e);
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
     * Function to populate the tree with nodes
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node
     */
    private void createNode(DefaultMutableTreeNode parent) {
	addRoles(parent, myComposite.getPlayer());
    }

    /**
     * Function to add roles node to tree
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param playersList
     *            <code>List</code> List of players
     */
    private void addRoles(DefaultMutableTreeNode parent,
	    List<Player> playersList) {
	if (playersList != null) {
	    Iterator it = playersList.iterator();
	    while (it.hasNext()) {
		Player p = (Player) it.next();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(p);
		addContracts(child, p.getRole().getAllContracts());
		parent.add(child);
	    }
	}
    }

    /**
     * Function to add contract nodes to players
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param contractList
     *            <code>Contract[]</code> array of contracts
     */
    private void addContracts(DefaultMutableTreeNode parent,
	    Contract[] contractList) {
	if (contractList != null) {
	    for (Contract c : contractList) {
		// only add contract node for Role A
		if (checkContractRoleA(parent, c)) {
		    DefaultMutableTreeNode child = new DefaultMutableTreeNode(c);
		    addTerms(child, c.getTermList());
		    parent.add(child);
		}
	    }
	}
    }

    /**
     * Function that adds terms node to tree
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param termList
     *            <code>List</code> List of terms
     */
    private void addTerms(DefaultMutableTreeNode parent, List<Term> termList) {
	if (termList != null) {
	    Iterator it = termList.iterator();
	    while (it.hasNext()) {
		Term t = (Term) it.next();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(t);

		addOperation(child, t.getOperation());
		parent.add(child);
	    }
	}
    }

    /**
     * Function that adds operation node to tree
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param op
     *            <code>Operation</code> Operation that needs to be added
     */
    private void addOperation(DefaultMutableTreeNode parent, Operation op) {
	if (op != null) {
	    DefaultMutableTreeNode child = new DefaultMutableTreeNode(op);
	    addParameters(child, op.getParameters());
	    parent.add(child);
	}
    }

    /**
     * This function is responsible for removing the operation from the ROADtest
     * instance
     * 
     * @param node
     */
    private void removeOperation(DefaultMutableTreeNode node) {
	Contract c = (Contract) ((DefaultMutableTreeNode) node.getParent()
		.getParent()).getUserObject();
	Operation o = (Operation) node.getUserObject();
	try {
	    myComposite.getPlayerById(c.getRoleA().getId()).removeOperation(o);
	    myComposite.getPlayerById(c.getRoleB().getId()).removeOperation(o);
	} catch (PlayerNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Function that generates parameter nodes for tree
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param param
     *            <code>List</code> List of parameters
     */
    private void addParameters(DefaultMutableTreeNode parent,
	    List<Parameter> param) {
	if (param != null) {
	    Iterator it = param.iterator();
	    while (it.hasNext()) {
		Parameter p = (Parameter) it.next();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(p);

		// addOperation(child, t.getOperation());
		parent.add(child);
	    }
	}
    }

    /**
     * Function that generates the role, contract, term and operation panel
     * based on level of node selected
     * 
     * @param i
     *            <code>int</code> depth of node
     * @param node
     *            <code>DefaultMutableTreeNode</code> tree node
     */
    private void generatePanel(int i, DefaultMutableTreeNode node) {
	// first clear existing panels
	clearExistingPanels();
	if (i == 0) {
	    jPanelRole = new OrganiserComponentRole(myComposite, node);
	    addComponent(this, jPanelRole, 8, 246, 778, 246);
	} else if (i == 1) {
	    jPanelContract = new OrganiserComponentContract(myComposite, node);
	    addComponent(this, jPanelContract, 8, 246, 778, 246);
	} else if (i == 2) {
	    jPanelTerm = new OrganiserComponentTerm(myComposite, node);
	    addComponent(this, jPanelTerm, 8, 246, 778, 246);
	} else if (i == 3) {
	    jPanelOperation = new OrganiserComponentOperation(myComposite, node);
	    addComponent(this, jPanelOperation, 8, 246, 778, 246);
	}
	this.repaint();
    }

    /**
     * This function is responsible for clearing all the existing panels on
     * organiser interface
     */
    private void clearExistingPanels() {
	if (jPanelRole != null)
	    this.remove(jPanelRole);
	if (jPanelContract != null)
	    this.remove(jPanelContract);
	if (jPanelTerm != null)
	    this.remove(jPanelTerm);
	if (jPanelOperation != null)
	    this.remove(jPanelOperation);
	this.repaint();
    }

    /**
     * Function that checks if the node is playing RoleA for specific contract
     * its required to check so that contract nodes can be added to just one
     * role and not both
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which child
     *            needs to be appended
     * @param c
     *            <code>Contract</code> a Contract
     * @return <code>boolean</code> true if role is RoleA in contract
     */
    private boolean checkContractRoleA(DefaultMutableTreeNode parent, Contract c) {
	if (c.getRoleA().getId()
		.equals(((Player) parent.getUserObject()).getPlayerId()))
	    return true;
	return false;
    }

    /**
     * Functionality to generate UI for Inbox
     */
    private void generateInbox() {
	String[] columnNamesInbox = { "Message Event", "Time" };
	jTableInbox.setModel(new DefaultTableModel(columnNamesInbox, 0));
	modelInbox = (DefaultTableModel) jTableInbox.getModel();
	jTableInbox.getTableHeader().setReorderingAllowed(false);
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

    public void organiserMessageRecieved(IOrganiserRole organiser) {

	MessageWrapper ms = organiser.getNextManagementMessage();
	addRowToInbox(ms);
    }

    /**
     * This function adds a row to inbox when a message is arrived
     * 
     * @param msg
     *            <code>Message</code> message
     */
    private void addRowToInbox(MessageWrapper msg) {
	/*
	 * if(modelInbox.getRowCount()>1) modelInbox.setRowCount(0);
	 */
	try {
	    Calendar cal = Calendar.getInstance();
	    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
	    Object[] row = new Object[] {
		    msg.getMessage().getClass().getSimpleName(),
		    dateFormat.format(cal.getTime()) };
	    modelInbox.insertRow(0, row);

	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception:" + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * This class represents the tree instance generated inside the container
     * which contains implementation for menu
     * 
     * @author Abhijeet M Pai(abhijeet.m.pai@gmail.com)
     * 
     */
    class OrganiserTreeGenerator extends JTree implements ActionListener {
	JPopupMenu popup;
	JMenuItem item;

	/**
	 * Constructor for MyJTree class
	 * 
	 * @param parent
	 *            <code>DefaultMutableTreeNode</code> parent node
	 */
	public OrganiserTreeGenerator(DefaultMutableTreeNode parent) {
	    super(parent);

	    // the mouselistener needs to be associated with both the events
	    // because of the platform dependence
	    addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		    performActions(e);
		}

		public void mouseReleased(MouseEvent e) {
		    performActions(e);
		}
	    });

	}

	/**
	 * The funcion generates the popup menu once the user right clicks on
	 * the tree
	 * 
	 * @param e
	 */
	private void performActions(MouseEvent e) {
	    if (e.isPopupTrigger()) {
		popup = generateMenu();
		popup.show((JComponent) e.getSource(), e.getX(), e.getY());
	    }
	}

	/**
	 * This function generates popup menu
	 * 
	 * @return <code>JPopupMenu</code> popup menu to operate on tree
	 */
	public JPopupMenu generateMenu() {
	    popup = new JPopupMenu();
	    item = new JMenuItem("Add component");
	    item.addActionListener(this);
	    item.setActionCommand("add");
	    popup.add(item);
	    item = new JMenuItem("Remove component");
	    item.addActionListener(this);
	    item.setActionCommand("remove");
	    popup.add(item);
	    popup.setOpaque(true);
	    popup.setLightWeightPopupEnabled(true);
	    return popup;
	}

	/**
	 * The function removes a node from populated tree
	 * 
	 * @param i
	 *            <code>int</code> depth of node
	 * @param node
	 *            <code>DefaultMutableTreeNode</code> tree node
	 */
	private void removeNode(int i, DefaultMutableTreeNode node) {
	    if (i == 1) {
		Role r = ((Player) node.getUserObject()).getRole();
		myComposite.removePlayer(r);
		myComposite.removeRole(r.getId());
	    } else if (i == 2) {
		Contract c = (Contract) node.getUserObject();
		myComposite.removeContract(c.getId());
	    } else if (i == 3) {
		Contract c = (Contract) node.getUserObject();
		myComposite.removeTerm(c.getId(),
			((Term) node.getUserObject()).getId());
	    } else if (i == 4) {
		removeOperation(node);
		myComposite.removeOperation(new Operation("newoperation", null,
			"").getName(), ((Term) ((DefaultMutableTreeNode) node
			.getParent()).getUserObject()).getId());
	    }
	    this.repaint();
	}

	/**
	 * This function is responsible to perform actions based on ActionEvent
	 * 
	 */
	public void actionPerformed(ActionEvent ae) {

	    DefaultMutableTreeNode currentNode, node;
	    TreePath path = this.getSelectionPath();
	    if (path != null) {
		currentNode = (DefaultMutableTreeNode) path
			.getLastPathComponent();
		if (ae.getActionCommand().equals("add")) {
		    generatePanel(currentNode.getLevel(), currentNode);
		}
		if (ae.getActionCommand().equals("remove")) {
		    removeNode(currentNode.getLevel(), currentNode);
		}
	    }
	}

    }

    @Override
    public void addContractEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	clearExistingPanels();
	initializeComponent();
    }

    @Override
    public void addOperationEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	clearExistingPanels();
	initializeComponent();

    }

    @Override
    public void addRoleEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	clearExistingPanels();
	initializeComponent();
    }

    @Override
    public void addTermEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	clearExistingPanels();
	initializeComponent();
    }

    @Override
    public void removeContractEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	initializeComponent();
    }

    @Override
    public void removeOperationEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	initializeComponent();
    }

    @Override
    public void removeRoleEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	initializeComponent();
    }

    @Override
    public void removeTermEvent(OrganiserEvent e) {
	jTreeComposition.getParent().getParent().getParent().removeAll();
	initializeComponent();
    }

}
