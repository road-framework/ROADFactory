package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;

import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.ROADTest;
import au.edu.swin.ict.road.roadtest.filemanager.FileManager;
import au.edu.swin.ict.road.roadtest.UI.Organiser.OrganiserEvent;
import au.edu.swin.ict.road.roadtest.UI.Organiser.OrganiserListener;
import au.edu.swin.ict.road.roadtest.UI.Organiser.OrganiserPanelComponent;

/**
 * This class represents the UI for each player from composition It contains
 * tabs for each player from which message could be sent and received
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class PlayersUI extends JDialog implements OrganiserListener {

    // this Map holds the player tabs for the current composition
    private Map<String, PlayerPanelComponent> playerTabs;
    private static final String MESSAGE_FOLDER = System.getProperty("user.dir")
	    + File.separator + "messages";

    private static final String startServerText = "Start Server",
	    stopServerText = "Stop Server";
    private static final int organiserPanelPositionIndex = 0;
    private JTabbedPane jTabbedPane1;
    private JPanel contentPane;
    private ROADTest myComposite;
    private OrganiserPanelComponent jPanelOrganiser;
    private JLabel status;
    private String activePlayer = "";
    private FileManager fileMgr;
    private JMenuItem server;
    private JMenuItem organiser;

    /**
     * Constructor for playersUI which accepts the parameter of type
     * <code>ROADTest</code> and <code>Frame</code>
     */
    public PlayersUI(ROADTest myComposite, Frame w) {
	super(w);
	this.myComposite = myComposite;
	this.fileMgr = new FileManager();
	playerTabs = new HashMap<String, PlayerPanelComponent>();

	// register to organiser event listener
	myComposite.addOrganiserListener(this);

	// get the files from the messages folder
	fileMgr.readFolder(MESSAGE_FOLDER);

	createMenu();
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * Initialise the component
     */
    private void initializeComponent() {

	contentPane = (JPanel) this.getContentPane();
	contentPane.setLayout(null);
	status = new JLabel("");

	jTabbedPane1 = new JTabbedPane();
	addComponent(contentPane, jTabbedPane1, 0, 0, 804, 690);
	addComponent(contentPane, status, 27, 22, 162, 15);

	// generate all players and organiser tab
	generatePlayerTabs();
	populateOrganiser();

	// Register event listeners
	jTabbedPane1.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		jTabbedPane1_stateChanged(e);
	    }
	});

	this.setTitle("Players-Composition");
	this.setLocation(new Point(5, 1));
	this.setSize(new Dimension(812, 735));
	this.setResizable(false);
	this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
     * This function populates the organiser panel for specified composition
     */
    private void populateOrganiser() {
	jPanelOrganiser = new OrganiserPanelComponent(myComposite, fileMgr);
	jPanelOrganiser.setLayout(null);
	jPanelOrganiser.setOpaque(false);
	jTabbedPane1.insertTab("Organiser", null, jPanelOrganiser, "",
		organiserPanelPositionIndex);
    }

    /**
     * This function generates tabs for each player.
     */
    private void generatePlayerTabs() {
	Iterator itr = myComposite.getPlayer().iterator();
	while (itr.hasNext()) {
	    Player p = (Player) itr.next();
	    // if the tab already exists
	    if (!playerTabs.containsKey(p.getRole().getId())) {
		PlayerPanelComponent jPanelPlayer = new PlayerPanelComponent(p,
			fileMgr);
		jPanelPlayer.setLayout(null);
		jPanelPlayer.setOpaque(false);

		// add the tab and insert the component into playersTabs
		// collection
		jTabbedPane1.addTab(p.getRole().getId(), jPanelPlayer);
		playerTabs.put(p.getRole().getId(), jPanelPlayer);
	    }
	}
    }

    /**
     * This function removes the tab for specific player
     * 
     * @param id
     *            <code>String</code> players id
     */
    public void removePlayerTab(String id) {
	jTabbedPane1.remove(playerTabs.get(id));
	playerTabs.remove(id);
    }

    // Event listeners

    /**
     * This function checks the active player and refreshes the
     * messageSignatures and contracts for that player
     */
    private void jTabbedPane1_stateChanged(ChangeEvent e) {

	activePlayer = jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());

	// this should not be done for organiser
	if (activePlayer != "Organiser") {
	    PlayerPanelComponent playerTab = (PlayerPanelComponent) jTabbedPane1
		    .getSelectedComponent();
	    playerTab.populateMessageSignatures();
	    playerTab.populateContracts();
	}
    }

    /**
     * Creates GUI for menu and its items
     */
    public void createMenu() {

	JMenuBar menubar = new JMenuBar();

	// creates top level menus
	JMenu mnu = new JMenu("Menu");

	// creates submenus of Configuration
	server = new JMenuItem(startServerText);

	// menu item login
	server.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		server_actionPerformed(event);
	    }
	});

	mnu.add(server);
	menubar.add(mnu);
	this.setJMenuBar(menubar);
    }

    /**
     * Functionality to start the local server that will listen to incoming
     * connections
     * 
     * @param e
     *            <code>ActionEvent</code>
     */
    protected void server_actionPerformed(ActionEvent event) {
	try {
	    if (server.getText().equals(startServerText)) {
		myComposite.startAsServer();
		server.setText(stopServerText);
		JOptionPane.showMessageDialog(this, "The server is started.",
			"Server Status", JOptionPane.PLAIN_MESSAGE);
	    } else {
		myComposite.stopAsServer();
		server.setText(startServerText);
		JOptionPane.showMessageDialog(this, "The server is stopped.",
			"Server Status", JOptionPane.PLAIN_MESSAGE);
	    }

	} catch (RemoteException e) {
	    JOptionPane.showMessageDialog(this, "Exception: Remote Exception "
		    + e.toString(), "Exception", JOptionPane.ERROR_MESSAGE);
	} catch (MalformedURLException e) {
	    JOptionPane.showMessageDialog(this,
		    "Exception: Malformed URL Exception " + e.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    public void addRoleEvent(OrganiserEvent e) {
	generatePlayerTabs();
    }

    @Override
    public void removeRoleEvent(OrganiserEvent e) {
	String str = e.getSource().toString();
	removePlayerTab(str);
    }

    @Override
    public void addContractEvent(OrganiserEvent e) {
    }

    @Override
    public void addOperationEvent(OrganiserEvent e) {

    }

    @Override
    public void addTermEvent(OrganiserEvent e) {

    }

    @Override
    public void removeContractEvent(OrganiserEvent e) {

    }

    @Override
    public void removeOperationEvent(OrganiserEvent e) {

    }

    @Override
    public void removeTermEvent(OrganiserEvent e) {

    }

}
