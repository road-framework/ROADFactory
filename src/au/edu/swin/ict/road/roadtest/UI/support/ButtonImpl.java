package au.edu.swin.ict.road.roadtest.UI.support;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import au.edu.swin.ict.road.roadtest.Player;

/**
 * This class is responsible for implementing functionality for buttons in
 * jtable for inbox and outbox. The ButtonRenderer class render the instance of
 * button on jtable and ButtonImpl class provides the functionality
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class ButtonImpl extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private int btnCmd;
    private UUID uid;
    private Player player;
    private String msgContent = "";

    /**
     * Constructor for ButtonImpl
     */
    public ButtonImpl(JCheckBox checkBox, UUID uid, Player player,
	    String msgContents) {
	super(checkBox);
	this.player = player;
	this.uid = uid;
	this.msgContent = msgContents;
	button = new JButton();
	button.setOpaque(true);

	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		fireEditingStopped();
	    }
	});
    }

    /**
     * Implemented method origin DefaultCellEditor
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {
	if (isSelected) {
	    button.setForeground(Color.LIGHT_GRAY);
	    button.setBackground(table.getSelectionBackground());
	} else {
	    button.setForeground(table.getForeground());
	    button.setBackground(table.getBackground());
	}
	label = (value == null) ? "" : value.toString();
	button.setText(label);

	if (label.equals("")) {
	    btnCmd = 0;
	    isPushed = false;
	    button.setEnabled(false);
	} else if (label.equals("Stop")) {
	    uid = (UUID) table.getModel().getValueAt(row,
		    table.getColumn("Message ID").getModelIndex());
	    btnCmd = 1;
	    button.setEnabled(true);
	    isPushed = true;

	} else if (label.equals("Abort")) {
	    uid = (UUID) table.getModel().getValueAt(row,
		    table.getColumn("Message ID").getModelIndex());
	    btnCmd = 2;
	    button.setEnabled(true);
	    isPushed = true;
	} else if (label.equals("View")) {
	    // added this line to get the latest content for the row
	    msgContent = (String) table.getModel().getValueAt(row,
		    table.getColumn("Content").getModelIndex());
	    btnCmd = 3;
	    button.setEnabled(true);
	    isPushed = true;
	}

	return button;
    }

    /**
     * Implemented method origin DefaultCellEditor
     */
    public Object getCellEditorValue() {

	if (isPushed) {
	    String msg = "";
	    if (btnCmd == 1)// if stop
	    {
		msg = "Are you sure you want to stop the message with Message ID "
			+ uid + "?";
		displayAlert(msg);
	    } else if (btnCmd == 2)// i.e. abort
	    {
		msg = "Are you sure you want to abort the auto response with Message ID "
			+ uid + "?";
		displayAlert(msg);
	    } else if (btnCmd == 3)// i.e. view
		JOptionPane.showMessageDialog(null, msgContent,
			"Message Content", JOptionPane.PLAIN_MESSAGE);// (null,
								      // msgContent);
	}
	isPushed = false;
	return new String(label);
    }

    /**
     * This function generate the message box to notify user
     * 
     * @param msg
     *            <code>String</code> message content
     */
    private void displayAlert(String msg) {

	int response = JOptionPane.showConfirmDialog(null, msg, "Confirm",
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (response == JOptionPane.YES_OPTION && btnCmd == 1) {
	    player.terminateIntervalMessageById(this.uid);
	    btnCmd = 0;
	    this.label = "";
	} else if (response == JOptionPane.YES_OPTION && btnCmd == 2) {
	    player.terminateAutoResponseById(this.uid);
	    btnCmd = 0;
	    this.label = "";
	}
    }

    /**
     * Implemented method origin DefaultCellEditor
     */
    public boolean stopCellEditing() {
	isPushed = false;
	return super.stopCellEditing();
    }

    /**
     * Implemented method origin DefaultCellEditor
     */
    protected void fireEditingStopped() {
	super.fireEditingStopped();
    }
}
