package au.edu.swin.ict.serendip.serendip4saas.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SerendipJList extends JPanel {
    private JList list = null;
    private DefaultListModel listModel = null;

    public SerendipJList(String listName, String[] data) {
	this.setLayout(new BorderLayout());
	JLabel header = new JLabel(listName);
	header.setBorder(BorderFactory.createEtchedBorder());
	this.add(header, BorderLayout.NORTH);
	listModel = new DefaultListModel();
	for (String s : data) {
	    listModel.addElement(s);
	}
	this.list = new JList(listModel);
	list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	JScrollPane scroll = new JScrollPane();
	scroll.setViewportView(list);
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	this.add(scroll, BorderLayout.CENTER);
	this.setBorder(BorderFactory.createEtchedBorder());
    }

    public String[] getAllData() {
	ArrayList<String> btIdList = new ArrayList<String>();
	for (int i = 0; i < this.list.getModel().getSize(); i++) {
	    btIdList.add((String) list.getModel().getElementAt(i));
	}
	String[] btIds = new String[btIdList.size()];
	return btIdList.toArray(btIds);
    }

    public boolean isInList(String item) {
	for (String s : this.getAllData()) {
	    if (s.equals(item)) {
		return true;
	    }
	}
	return false;
    }

    public String getSelectedItem() {
	return (String) this.list.getSelectedValue();
    }

    public int getSelectedIndex() {
	return this.list.getSelectedIndex();
    }

    public void addItem(String item) {
	this.listModel.addElement(item);
    }

    public void removeItem(int index) {
	this.listModel.remove(index);
    }

    public void removeItem(String item) {
	for (int i = 0; i < listModel.getSize(); i++) {
	    if (listModel.get(i).equals(item)) {
		listModel.remove(i);

	    }
	}
    }
}
