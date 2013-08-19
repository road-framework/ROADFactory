package au.edu.swin.ict.road.roadtest.UI.support;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * This class is responsible for implementing rendering for buttons in jtable
 * for inbox and outbox. The ButtonRenderer class render the instance of button
 * on jtable and ButtonImpl class provides the functionality
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    /**
     * Constructor for ButtonRenderer
     */
    public ButtonRenderer() {
	setOpaque(true);
    }

    /**
     * Implemented method origin DefaultCellEditor
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	if (isSelected) {
	    setForeground(table.getSelectionForeground());
	    setBackground(table.getSelectionBackground());
	} else {
	    setForeground(table.getForeground());
	    setBackground(UIManager.getColor("Button.background"));
	}
	setText((value == null) ? "" : value.toString());
	return this;
    }
}
