package au.edu.swin.ict.road.roadtest.UI.support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import au.edu.swin.ict.road.roadtest.parameter.Content;

/**
 * This class represents the tree instance generated inside the container which
 * contains implementation for menu
 * 
 * @author Abhijeet Pai(abhijeet.m.pai@gmail.com)
 * 
 */
public class ParameterTreeGenerator extends JTree implements ActionListener {
    JPopupMenu popup;
    JMenuItem item;
    ActionListener obj;

    /**
     * Constructor for MyJTree class
     * 
     * @param parent
     *            <code>DefaultMutableTreeNode</code> parent node
     * @param param
     *            <code>Map<String, List<Content>></code> child node content
     */
    public ParameterTreeGenerator(ActionListener obj,
	    DefaultMutableTreeNode parent, Map<String, List<Content>> param) {
	super(parent);
	this.obj = obj;
	addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
		performActions(e);
	    }

	    public void mouseReleased(MouseEvent e) {
		performActions(e);
	    }
	});
    }

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
	/*
	 * item = new JMenuItem("Insert a children");
	 * item.addActionListener(this); item.setActionCommand("insert");
	 * popup.add(item);
	 */
	item = new JMenuItem("Set Value");
	item.addActionListener(this);
	item.setActionCommand("set");
	popup.add(item);
	item = new JMenuItem("Get Value");
	item.addActionListener(this);
	item.setActionCommand("get");
	popup.add(item);
	item = new JMenuItem("Clear Value");
	item.addActionListener(this);
	item.setActionCommand("clear");
	popup.add(item);
	/*
	 * item = new JMenuItem("Remove this node");
	 * item.addActionListener(this); item.setActionCommand("remove");
	 * popup.add(item);
	 */
	popup.setOpaque(true);
	popup.setLightWeightPopupEnabled(true);
	return popup;
    }

    /**
     * This function is responsible to perform actions based on ActionEvent
     * 
     */
    public void actionPerformed(ActionEvent ae) {

	DefaultMutableTreeNode currentNode, node;
	TreePath path = this.getSelectionPath();
	if (path != null) {
	    currentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	    if (!currentNode.isRoot()) {
		// get content of current node
		Content c = (Content) currentNode.getUserObject();
		if (ae.getActionCommand().equals("insert")) {
		    /*
		     * if (c.isArray()) { if (c.isPrimitive()) { Content
		     * newElement = generateArrayElement(c); node = new
		     * DefaultMutableTreeNode(newElement);
		     * //c.getMapValues().put(newElement.getName(), newElement);
		     * currentNode.add(node); ((DefaultTreeModel)
		     * this.getModel()).nodeStructureChanged((TreeNode)
		     * currentNode); } else if (!c.isPrimitive()) { Content
		     * newElement = generateArrayElement(c); node = new
		     * DefaultMutableTreeNode(newElement);
		     * 
		     * //try to put this create node function in
		     * generateArrayElement //code and also replace c with
		     * newElement createNode(node, c.getMapValues(),true);
		     * currentNode.add(node); ((DefaultTreeModel)
		     * this.getModel()).nodeStructureChanged((TreeNode)
		     * currentNode); } }
		     */
		}
		if (ae.getActionCommand().equals("set")) {
		    if (c.isArray() || !c.isPrimitive()) {

		    } else {
			String input = JOptionPane.showInputDialog(null,
				"Set Value:", "Set",
				JOptionPane.WARNING_MESSAGE);
			if (input != null) {
			    c.setValue(input);
			}
			obj.actionPerformed(ae);
			// fillTextArea();
		    }
		}
		if (ae.getActionCommand().equals("get")) {
		    if (c.isArray() || !c.isPrimitive()) {

		    } else {
			if (c.getValue() != null)
			    JOptionPane.showMessageDialog(null, c.getValue());
			else
			    JOptionPane
				    .showMessageDialog(null, "Value not set");
		    }

		}
		if (ae.getActionCommand().equals("clear")) {
		    if (c.isArray() || !c.isPrimitive()) {

		    } else {
			c.setValue(null);
		    }
		    obj.actionPerformed(ae);
		    // fillTextArea();
		}
		if (ae.getActionCommand().equals("remove")) {
		    node = (DefaultMutableTreeNode) currentNode.getParent();
		    // this function is commented as currently we dont have
		    // complex object or array
		    /*
		     * if (!node.isRoot()) { Content nodeC = (Content)
		     * currentNode.getUserObject(); Content par = (Content)
		     * node.getUserObject(); if (par.isArray()) { //
		     * nodeC.setMapValues(null); // declare an integer to hold
		     * the selected nodes index // remove any children of
		     * selected node // remove the selected node,retain its
		     * siblings int nodeIndex = node.getIndex(currentNode);
		     * currentNode.removeAllChildren(); node.remove(nodeIndex);
		     * ((DefaultTreeModel)
		     * this.getModel()).nodeStructureChanged((TreeNode)
		     * currentNode); } }
		     */

		}
	    }
	}
    }

    /**
     * This function will generate arrayelement when we try to insert an element
     * to array or complex object
     * 
     * @param c
     *            <code>Content</code> Parent element object
     * @return <code>Content</code> new element that will be based on parent
     *         element
     */
    public Content generateArrayElement(Content c) {
	Content newC = new Content();
	newC.setArray(false);
	/*
	 * if(c.isArray() && c.isPrimitive()) { if(c.getMapValues()==null) {
	 * c.setMapValues(new HashMap<String, Content>());
	 * newC.setName(c.getName()+"["+c.getMapValues().size()+"]"); } else
	 * newC.setName(c.getName()+"["+c.getMapValues().size()+"]");
	 * c.getMapValues().put(newC.getName(), newC); } else if(c.isArray() &&
	 * !c.isPrimitive()) {
	 * 
	 * //here yuou should add create node function from above
	 * newC.setMapValues(c.getMapValues()); newC.setName(c.getName()); }
	 */

	newC.setPrimitive(c.isPrimitive());
	newC.setType(c.getType());
	newC.setValue(c.getValue());

	return newC;
    }
}