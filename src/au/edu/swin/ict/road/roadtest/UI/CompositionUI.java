package au.edu.swin.ict.road.roadtest.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.MessageDeliverer;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;
import au.edu.swin.ict.road.roadtest.ROADTest;
import au.edu.swin.ict.road.roadtest.UI.Player.PlayersUI;

/**
 * This class represents an instance of main application it provides user with
 * an option to specify the location of composition.xml file and add the
 * composite to the tester a user can also flexibly select the role from
 * composition that he wants to test
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class CompositionUI extends JFrame {

    private static Logger logFactory = Logger.getLogger(Composite.class
	    .getName());
    private static Logger logROADWorker = Logger
	    .getLogger(MessageDeliverer.class.getName());
    private static Logger logROADTest = Logger.getLogger(ROADTest.class
	    .getName());

    // Variables declaration
    private JTabbedPane jTabbedPane2;
    private JPanel contentPane;
    private DefaultListModel model;
    private JLabel jLabelBrowse;
    private JLabel jLabelCurrentComposites;
    private JTextField jTextFieldFileLoc;
    private JList jListCompositions;
    private JScrollPane jScrollPane6;
    private JButton jButtonBrowse;
    private JButton jButtonGenerate;
    private JButton jButtonLog;
    private JButton jButtonDelete;
    private JButton jButtonLoad;
    private JLabel jLabelStatus;
    private JPanel jPanel5;
    private ArrayList<ROADTest> myCompositions;
    private ArrayList<PlayersUI> myPlayersUI;
    private LogConsoleDialog myLog;

    // End of variables declaration

    /**
     * Constructor for CompositionUI
     */
    public CompositionUI() {
	super();
	initiateLogger();
	myPlayersUI = new ArrayList<PlayersUI>();
	myCompositions = new ArrayList<ROADTest>();
	initializeComponent();
	this.setVisible(true);

    }

    /**
     * This function is used to initialise all the components of the frame
     */
    private void initializeComponent() {
	myLog = new LogConsoleDialog(this);
	jTabbedPane2 = new JTabbedPane();
	contentPane = (JPanel) this.getContentPane();

	jLabelBrowse = new JLabel("Load Composite:");
	jLabelCurrentComposites = new JLabel("Current Composites:");
	jTextFieldFileLoc = new JTextField();
	model = new DefaultListModel();
	jListCompositions = new JList(model);
	jListCompositions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	jScrollPane6 = new JScrollPane();
	jButtonBrowse = new JButton("Browse");
	jLabelStatus = new JLabel();
	jButtonGenerate = new JButton("Generate Instance");
	jButtonDelete = new JButton("Delete Composite");
	jButtonLog = new JButton("View Log");
	jButtonLoad = new JButton("View Composition");
	jPanel5 = new JPanel();

	contentPane.setLayout(null);

	jScrollPane6.setViewportView(jListCompositions);
	jPanel5.setLayout(null);
	jPanel5.setOpaque(false);
	jTabbedPane2.addTab("Composition Information", jPanel5);

	addComponent(contentPane, jTabbedPane2, 1, 2, 778, 410);
	addComponent(jPanel5, jLabelStatus, 29, 350, 400, 18);
	addComponent(jPanel5, jLabelBrowse, 29, 36, 150, 16);
	addComponent(jPanel5, jLabelCurrentComposites, 26, 154, 150, 18);
	addComponent(jPanel5, jTextFieldFileLoc, 200, 35, 322, 22);
	addComponent(jPanel5, jScrollPane6, 200, 157, 323, 100);
	addComponent(jPanel5, jButtonBrowse, 525, 34, 100, 25);
	addComponent(jPanel5, jButtonGenerate, 200, 72, 150, 28);
	addComponent(jPanel5, jButtonLog, 370, 72, 150, 28);
	addComponent(jPanel5, jButtonDelete, 200, 271, 150, 28);
	addComponent(jPanel5, jButtonLoad, 370, 271, 150, 28);

	this.setTitle("ROADfactory v1.1beta");
	this.setLocation(new Point(25, 4));
	this.setSize(new Dimension(786, 440));
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setResizable(false);

	// Register event listeners

	jButtonBrowse.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonBrowse_actionPerformed(e);
	    }

	});

	jButtonLoad.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonLoad_actionPerformed(e);
	    }

	});

	jButtonLog.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonLog_actionPerformed(e);
	    }

	});

	jButtonGenerate.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonGenerate_actionPerformed(e);
	    }

	});

	jButtonDelete.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonDelete_actionPerformed(e);
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
     * This function implements the functionality for the logger by registering
     * handler with each logger instance
     */
    private void initiateLogger() {
	Handler h = new Handler() {
	    @Override
	    public void close() throws SecurityException {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void flush() {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void publish(LogRecord record) {
		myLog.addText(record.getMessage());
	    }
	};
	logFactory.addHandler(h);
	logROADWorker.addHandler(h);
	logROADTest.addHandler(h);
    }

    // Event listeners

    /**
     * Functionality for browse button
     */
    private void jButtonBrowse_actionPerformed(ActionEvent e) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("xml",
		"xml");
	chooser.setFileFilter(filter);

	int returnVal = chooser.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    jTextFieldFileLoc.setText(chooser.getSelectedFile()
		    .getAbsolutePath());
	}
    }

    /**
     * Functionality for generate button which creates the composition based on
     * the file location
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    private void jButtonGenerate_actionPerformed(ActionEvent e) {
	try {
	    String location = jTextFieldFileLoc.getText().trim();
	    // location = "D:/IP/ROADtest/data/LaserPrint-smc.xml";
	    // location = "D:/IP/ROADtest/data/printScenario-smc.xml";
	    if (location.equals("") || location.equals(null)) {
		jLabelStatus
			.setText("Please provide proper path for loading the composition");
	    } else {
		ROADTest rt = new ROADTest(location.trim());
		myCompositions.add(rt);
		rt.startComposite();
		model.add(jListCompositions.getModel().getSize(), location);

		PlayersUI p = new PlayersUI(rt, this);
		myPlayersUI.add(p);
		// p.setModal(true);
		// v1 = new CompositeViewer(rt);
	    }

	} catch (CompositeDemarshallingException ex) {
	    JOptionPane
		    .showMessageDialog(
			    this,
			    "Exception: Composite Demarshalling Error "
				    + ex.toString(), "Exception",
			    JOptionPane.ERROR_MESSAGE);

	} catch (ConsistencyViolationException ex) {
	    JOptionPane.showMessageDialog(this,
		    "Exception: Consistancy Violation Error " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);

	} catch (CompositeInstantiationException ex) {
	    JOptionPane
		    .showMessageDialog(
			    this,
			    "Exception: Composite Instantiation Error "
				    + ex.toString(), "Exception",
			    JOptionPane.ERROR_MESSAGE);

	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(this, "Exception: " + ex.toString(),
		    "Exception", JOptionPane.ERROR_MESSAGE);

	}
    }

    /**
     * Functionality for delete button which removes the composition
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    private void jButtonDelete_actionPerformed(ActionEvent e) {
	int index = jListCompositions.getSelectedIndex();
	if (index != -1) {
	    String str = model.get(index).toString();
	    model.removeElementAt(index);
	    myCompositions.remove(index);
	    myPlayersUI.remove(index);
	    jLabelStatus.setText("Deleted following composition: " + str);
	} else
	    jLabelStatus.setText("Please select the composition to delete");
    }

    /**
     * Functionality for reloading the panel of composition from memory
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    protected void jButtonLoad_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	int index = this.jListCompositions.getSelectedIndex();
	if (index != -1) {
	    this.myPlayersUI.get(index).setVisible(true);
	} else
	    jLabelStatus.setText("Please select the composition to view");
    }

    /**
     * Functionality for view log button which shows the console to user
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    protected void jButtonLog_actionPerformed(ActionEvent e) {
	myLog.setVisible(true);
    }

}
