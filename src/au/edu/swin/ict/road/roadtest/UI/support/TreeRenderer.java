package au.edu.swin.ict.road.roadtest.UI.support;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import au.edu.swin.ict.road.roadtest.UI.Player.ParametersUI;
import au.edu.swin.ict.road.roadtest.parameter.Content;

/**
 * This class is responsible to render a tree elements
 * 
 * @author Abhijeet (abhijeet.m.pai@gmail.com)
 * 
 */
public class TreeRenderer extends DefaultTreeCellRenderer {
    Icon setIcon;
    Icon unsetIcon;

    public TreeRenderer() {
	ImageIcon icon = createImageIcon("../images/set.jpg");
	ImageIcon icon1 = createImageIcon("../images/unset.jpg");
	if (icon != null && icon1 != null) {
	    setIcon = icon;
	    unsetIcon = icon1;
	} else {
	    System.err.println("Icon missing; using default.");
	}
    }

    /**
     * This function is to generate image icons for tree items
     * 
     * @param path
     * @return
     */
    private static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = ParametersUI.class.getResource(path);
	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
	    boolean sel, boolean expanded, boolean leaf, int row,
	    boolean hasFocus) {

	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
		row, hasFocus);
	if (leaf && isSet(value)) {
	    setIcon(setIcon);
	    setToolTipText(getValue(value));
	} else if (leaf && !isSet(value)) {
	    setIcon(unsetIcon);
	    setToolTipText(null);
	} else
	    setToolTipText(null);

	return this;
    }

    /**
     * This function checks if the value is set for a variable from tree
     * 
     * @param value
     *            <code>Object</code> node for which the value needs to checked
     * @return <code>boolean</code> true if value is set
     */
    protected boolean isSet(Object value) {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	Content o = (Content) node.getUserObject();
	if (o.getValue() != null) {
	    return true;
	}
	return false;
    }

    /**
     * This function will get a value for a variable from tree
     * 
     * @param value
     *            <code>Object</code> node for which the value needs to checked
     * @return <code>String</code> value that is set
     */
    protected String getValue(Object value) {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	Content o = (Content) node.getUserObject();
	if (o.getValue() != null) {
	    return o.getValue();
	}
	return "Value not set";
    }
}