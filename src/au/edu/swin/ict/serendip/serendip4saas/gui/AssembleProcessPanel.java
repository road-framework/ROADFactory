package au.edu.swin.ict.serendip.serendip4saas.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class AssembleProcessPanel extends JPanel implements ActionListener {
    private JPanel centrePanel = new JPanel();
    private JList allBTList = null, selBTList = null;
    private JScrollPane allBTScrol = null, selBTListScrol = null;

    public AssembleProcessPanel() {

    }

    public void createView() {
	this.setLayout(new BorderLayout());
	this.centrePanel.setLayout(new GridLayout(2, 2));

    }

    private JList createList(String title) {
	JList list = new JList();
	list.setBorder(BorderFactory.createTitledBorder(title));
	list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	list.setLayoutOrientation(JList.HORIZONTAL_WRAP);

	return list;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub

    }
}
