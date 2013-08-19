package au.edu.swin.ict.serendip.tool.gui;

import javax.swing.table.AbstractTableModel;

import au.edu.swin.ict.serendip.core.ProcessInstance;

public class ProcessViewTableModel extends AbstractTableModel {

    private String[] columnNames = { "Pid", "Status", "CurrentTask",
	    "ObligatedRole" };
    private Object[][] data = { { "001", "Active", "Pay", "CO" },
	    { "002", "Active", "Repair", "GR" } };

    public ProcessViewTableModel() {
	// TODO Auto-generated constructor stub

    }

    @Override
    public int getColumnCount() {
	// TODO Auto-generated method stub
	return this.columnNames.length;
    }

    @Override
    public int getRowCount() {
	// TODO Auto-generated method stub
	return this.data.length;
    }

    @Override
    public Object getValueAt(int column, int row) {
	// TODO Auto-generated method stub
	return this.data[column][row];
    }

    public String getColumnName(int col) {
	return columnNames[col];
    }

    //
    public void addRecord(ProcessInstance pi) {

    }

}
