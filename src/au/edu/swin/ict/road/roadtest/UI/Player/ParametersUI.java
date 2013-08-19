package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.roadtest.UI.support.ParameterTreeGenerator;
import au.edu.swin.ict.road.roadtest.UI.support.TreeRenderer;
import au.edu.swin.ict.road.roadtest.filemanager.FileManager;
import au.edu.swin.ict.road.roadtest.parameter.Content;
import au.edu.swin.ict.road.roadtest.parameter.ObjectParameterHelper;

/**
 * This class represents an instance Parameters UI where user sets values for
 * parameters and can save it to external resource for populating message
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class ParametersUI extends JDialog implements ActionListener {
    private static Logger log = Logger.getLogger(ParametersUI.class.getName());
    private ParameterTreeGenerator jTreeParameters;
    private TextArea jTextAreaMsgContent;
    private JScrollPane jScrollPaneMsgContent;
    private JScrollPane jScrollPaneLeft;
    private JButton jButtonSave;
    private JPanel contentPane;
    private Map<String, List<Content>> param;
    private String rootSig = "";
    private FileManager fileMgr;

    /**
     * Constructor
     * 
     * @param map
     *            <code>Map<String, List<Content>></code> A map containing the
     *            parameters
     * @param rootSig
     *            <code>String</code> name of the root for tree
     * @param fileMgr
     *            <code>FileManager</code> a file manager
     */
    public ParametersUI(Map<String, List<Content>> map, String rootSig,
	    FileManager fileMgr) {
	super();
	this.param = map;
	this.rootSig = rootSig;
	this.fileMgr = fileMgr;
	initializeComponent();
	this.setVisible(true);
    }

    /**
     * This function is used to initialise all the components of the frame
     */
    private void initializeComponent() {
	contentPane = (JPanel) this.getContentPane();

	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootSig);
	// try to put this function in MyJTree as its related to tree
	createNode(rootNode, param, false);

	jTreeParameters = new ParameterTreeGenerator(this, rootNode, param);
	jTreeParameters.setCellRenderer(new TreeRenderer());

	jTreeParameters.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
	jScrollPaneLeft = new JScrollPane();
	jButtonSave = new JButton("Save To File");
	jScrollPaneLeft.setViewportView(jTreeParameters);
	jTextAreaMsgContent = new TextArea();
	jScrollPaneMsgContent = new JScrollPane();

	// jScrollPaneMsgContent.setViewportView(jTextAreaMsgContent);

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }
	});

	fillTextArea();

	addComponent(contentPane, jTextAreaMsgContent, 305, 2, 465, 385);
	addComponent(contentPane, jScrollPaneLeft, 1, 2, 300, 385);
	addComponent(contentPane, jButtonSave, 305, 394, 120, 28);

	contentPane.setLayout(null);
	this.setTitle("Set method parameters");
	this.setLocation(new Point(10, 10));
	this.setSize(new Dimension(782, 457));
	this.setResizable(false);
    }

    /**
     * This function creates node based on structure of parent and child if it
     * contains custom object it generates sub elements of the node
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node to which
     *            childitems needs to be added
     * @param childItems
     *            <code>Map<String, List<Content>></code> map containing child
     *            elements
     * @param isClone
     *            <code>boolean</code> set true if you want to clone the object
     */
    private void createNode(DefaultMutableTreeNode parent,
	    Map<String, List<Content>> childItems, boolean isClone) {
	if (childItems != null) {
	    Iterator it = childItems.keySet().iterator();
	    while (it.hasNext()) {
		String key = (String) it.next();
		// generate item
		List<Content> cList = childItems.get(key);
		// this is set to 0 as currently roadfactory doesnot support
		// complex objects later this may change
		Content c = cList.get(0);
		try {// clonning is performed so that object can have different
		     // id's
		    if (isClone)
			c = (Content) c.clone();
		} catch (Exception ex) {
		    log.debug("Exception");
		}

		if (c != null) {
		    c.setName(key);
		    DefaultMutableTreeNode child = new DefaultMutableTreeNode(c);
		    if (!c.isPrimitive() && !c.isArray()) {// if custom object
							   // then create sub
							   // elements
			createNode(child, c.getMapValues(), isClone);
		    }
		    parent.add(child);
		}
	    }
	}
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
     * The function generates the XML based on the parameters which can be saved
     * to external source
     * 
     * @return <code>String</code> xml string
     */
    public String fillTextArea() {
	String s = ObjectParameterHelper.getXmlString(param).toString();
	this.jTextAreaMsgContent.setText(s);
	return s;
    }

    /**
     * This function is executed when the save button is clicked
     * 
     * @param e
     *            <code>ActionEvent e</code>
     */
    private void jButtonSave_actionPerformed(ActionEvent e) {

	String filename = showBrowseDialog();
	if (!filename.equals("")) {
	    String status = fileMgr.createFileWriter(filename);
	    String s = fillTextArea();
	    // if new file is created
	    if (status.equals("FILECREATED")) {
		Object[] arr = { s };
		writeToFile(arr);
	    } else {// if file exists
		int response = JOptionPane
			.showConfirmDialog(null,
				"Do you want to overwrite the file?",
				"Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
		    Object[] arr = { s };
		    writeToFile(arr);
		}
	    }
	}
    }

    /**
     * The functionality to save the parameters to a file
     * 
     * @param arr
     *            <code>Object[]</code> lines to write
     */
    private void writeToFile(Object[] arr) {
	if (fileMgr.writeLinesToFile(arr)) {
	    JOptionPane.showMessageDialog(this, "File written successfully");
	    this.setVisible(false);
	} else
	    JOptionPane.showMessageDialog(this,
		    "Error occurred while writing to File");
    }

    /**
     * Functionality that shows the filechooser dialog to user
     * 
     * @return <code>String</code> file location
     */
    private String showBrowseDialog() {
	JFileChooser chooser = new JFileChooser();

	int returnVal = chooser.showSaveDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    String fileName = chooser.getSelectedFile().getAbsolutePath();
	    if (fileName.contains(".xml"))
		return fileName;
	    else
		return fileName += ".xml";
	}
	return "";
    }

    /* code was here */

    @Override
    public void actionPerformed(ActionEvent e) {
	fillTextArea();
    }
}
