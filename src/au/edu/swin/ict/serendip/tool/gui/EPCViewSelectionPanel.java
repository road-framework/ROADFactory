package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.xml.bindings.ConstraintType;
import au.edu.swin.ict.serendip.composition.view.SerendipView;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.util.CompositionUtil;

public class EPCViewSelectionPanel extends JPanel implements ActionListener {
    private static Logger log = Logger.getLogger(EPCViewSelectionPanel.class
	    .getName());
    private JComboBox selections = new JComboBox();
    private JButton viewBtn = new JButton("View");
    private JButton updateBtn = new JButton("Update");
    private SerendipEngine engine = null;
    private boolean isProcess = true;
    private JPanel epcViewPanel = null;
    private JPanel constraintViewPanel = null;
    private JTextArea constraintArea = new JTextArea();
    private JCheckBox isDataChckBox = new JCheckBox("Show Data Dependencies",
	    false);

    // private CompositionType compType = null;

    public EPCViewSelectionPanel(SerendipEngine engine, boolean isProcess) {
	super();
	// TODO Auto-generated constructor stub
	this.engine = engine;
	this.isProcess = isProcess;
	this.createView();
    }

    public void createView() {

	this.viewBtn.addActionListener(this);
	this.updateBtn.addActionListener(this);
	this.setLayout(new BorderLayout());

	String[] strList = null;
	String text = "-";
	if (this.isProcess) {
	    strList = CompositionUtil.getAllProcessDefinitionIds(this.engine
		    .getComposition());
	    text = "Select a process definition Id";
	} else {
	    strList = CompositionUtil.getAllSRIds(this.engine.getComposition());
	    text = "Select a service relationship Id";
	}
	JLabel label = new JLabel(text);
	for (int i = 0; i < strList.length; i++) {
	    this.selections.addItem(strList[i]);
	}

	JPanel topPanel = new JPanel(new FlowLayout());
	topPanel.add(label);
	topPanel.add(this.selections);
	topPanel.add(this.isDataChckBox);
	topPanel.add(this.viewBtn);
	topPanel.add(this.updateBtn);
	this.add(topPanel, BorderLayout.NORTH);

	this.constraintViewPanel = new JPanel();
	constraintViewPanel.setBorder(BorderFactory
		.createTitledBorder("Constraints"));
	this.constraintViewPanel.add(this.constraintArea);

	this.add(constraintViewPanel, BorderLayout.SOUTH);
    }

    private void setTextArea(String id) {
	String text = "";
	ConstraintType[] constraints = null;
	if (this.isProcess) {
	    constraints = CompositionUtil.getAllConstriantsForPD(id, true,
		    this.engine.getComposition());

	} else {
	    constraints = CompositionUtil.getAllConstriantsForSR(id,
		    this.engine.getComposition());
	}

	for (int i = 0; i < constraints.length; i++) {
	    ConstraintType constraint = constraints[i];
	    text += constraint.getId() + "\t" + constraint.getExpression()
		    + "\n";
	}

	this.constraintArea.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if (e.getSource().equals(this.viewBtn)) {
	    String selection = (String) this.selections.getSelectedItem();
	    SerendipView sv = null;
	    // Remove current panel
	    if (null != this.epcViewPanel) {
		this.remove(this.epcViewPanel);
	    }
	    // Set the new panel
	    try {
		if (this.isProcess) {

		    sv = engine.getModelFactory().getProcessView(selection,
			    null);
		    // Add data dependencies
		    if (isDataChckBox.isSelected()) {
			Map<String, String> map = CompositionUtil
				.getTaskRuleMapping(this.engine
					.getComposition().getComposite());
			for (String s : map.keySet()) {
			    log.debug(s);
			}
			SerendipView.markDataDependencies(sv.getViewAsEPC(),
				map);
		    }

		} else {
		    // sv = engine.getModelFactory().getSRView(selection);
		}
	    } catch (SerendipException e1) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(this,
			"Error while generating the view for " + selection,
			"View generation error", JOptionPane.ERROR_MESSAGE);

		e1.printStackTrace();
	    }
	    this.epcViewPanel = new SerendipEPCView(sv);

	    this.add(epcViewPanel, BorderLayout.CENTER);
	    this.setTextArea(selection);
	    this.repaint();

	} else if (e.getSource().equals(this.viewBtn)) {
	    String[] strList = null;
	    if (this.isProcess) {
		strList = CompositionUtil
			.getAllProcessDefinitionIds(this.engine
				.getComposition());
	    } else {
		strList = CompositionUtil.getAllSRIds(this.engine
			.getComposition());
	    }
	    // Remove existing items and update
	    this.selections.removeAllItems();
	    for (int i = 0; i < strList.length; i++) {
		this.selections.addItem(strList[i]);
	    }
	    System.out.println("Selections Updated ");
	}
    }

}
