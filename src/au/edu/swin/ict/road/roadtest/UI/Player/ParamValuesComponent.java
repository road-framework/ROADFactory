package au.edu.swin.ict.road.roadtest.UI.Player;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.roadtest.parameter.Content;

public class ParamValuesComponent extends JPanel {
    private static Logger log = Logger.getLogger(ParamValuesComponent.class
	    .getName());
    private JTable jTableValues;
    private JScrollPane jScrollPaneRight;
    private JButton jButtonSave;
    private JButton jButtonAdd;
    private DefaultTableModel modelJTableValues;
    private Content parameterContent;
    private final int TABLE_VALUE_COLUMN = 2;

    public ParamValuesComponent(Content parameterContent) {
	super();
	this.parameterContent = parameterContent;
	initializeComponent();
	this.setVisible(true);
    }

    private void initializeComponent() {
	// TODO Auto-generated method stub
	jTableValues = new JTable();
	jTableValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	jTableValues.setEditingColumn(TABLE_VALUE_COLUMN);
	jTableValues.setRowSelectionAllowed(false);
	jScrollPaneRight = new JScrollPane();
	jButtonSave = new JButton("Save");
	jButtonAdd = new JButton("Add");
	generateTable();
	generateRows();
	// jTableValues.setModel(new DefaultTableModel(13, 3));
	jScrollPaneRight.setViewportView(jTableValues);

	jButtonSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonSave_actionPerformed(e);
	    }

	});

	jButtonAdd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonAdd_actionPerformed(e);
	    }

	});

	addComponent(this, jScrollPaneRight, 0, 0, 450, 200);
	addComponent(this, jButtonSave, 0, 220, 83, 28);
	addComponent(this, jButtonAdd, 100, 220, 83, 28);
	this.setLayout(null);
	this.setSize(new Dimension(782, 457));
    }

    private void generateTable() {
	String[] columnNamesOutbox = { "Parameter Name", "Type", "Value" };
	jTableValues.setModel(new DefaultTableModel(columnNamesOutbox, 0));
	modelJTableValues = (DefaultTableModel) jTableValues.getModel();
	jTableValues.getTableHeader().setReorderingAllowed(false);
    }

    private void generateRows() {
	Object[] row;
	// if array then display add button else hide add button
	if (this.parameterContent.isPrimitive()) {
	    if (!this.parameterContent.isArray()) {
		row = new Object[] { parameterContent.getName(),
			parameterContent.getType(), "" };
		this.jButtonAdd.setVisible(false);
	    } else {
		row = new Object[] {
			parameterContent.getName() + "["
				+ modelJTableValues.getRowCount() + "]",
			parameterContent.getType(), "" };
		this.jButtonAdd.setVisible(true);
	    }
	    modelJTableValues.addRow(row);
	}
    }

    private void addComponent(Container container, Component c, int x, int y,
	    int width, int height) {
	c.setBounds(x, y, width, height);
	container.add(c);
    }

    protected void jButtonAdd_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	generateRows();
    }

    protected void jButtonSave_actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if (JOptionPane.showConfirmDialog(null,
		"Press OK to confirm all values are set properly.",
		"Comfirmation", JOptionPane.OK_CANCEL_OPTION) == 0) {
	    for (int i = 0; i < jTableValues.getRowCount(); i++) {
		// this.parameterContent.setValue();
		log.debug(modelJTableValues.getValueAt(i, TABLE_VALUE_COLUMN));
	    }
	}
	// if(!this.parameterContent.isArray())

    }
}
