package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.grammar.behav.ScriptConstructor;
import au.edu.swin.ict.serendip.grammar.behav.model.Behavior;
import au.edu.swin.ict.serendip.grammar.behav.model.ModelTransform;
import au.edu.swin.ict.serendip.grammar.behav.model.Script;
import au.edu.swin.ict.serendip.textual.TextualAntlrWriter;

public class BehaviorModifyPanel extends JPanel implements ActionListener {
    private BehaviorTermType btt = null;
    private JLabel idLbl = new JLabel();
    private JPanel topPanel = new JPanel(new FlowLayout());
    private JPanel botPanel = new JPanel(new FlowLayout());
    private JTextArea ta = new JTextArea();// Can we highlight this?
    private JButton saveBtn = new JButton("Save");
    private JButton cancelBtn = new JButton("Cancel");

    public BehaviorModifyPanel(BehaviorTermType btt) {
	this.btt = btt;
	this.setLayout(new BorderLayout());
	this.add(topPanel, BorderLayout.NORTH);
	this.add(botPanel, BorderLayout.SOUTH);
	this.add(ta, BorderLayout.CENTER);

	this.topPanel.add(new JLabel(btt.getId()));

	Behavior behavior = ModelTransform.behaviorFwdTransform(btt);
	this.ta.setText(behavior.toString());

	this.saveBtn.addActionListener(this);
	this.cancelBtn.addActionListener(this);

	this.botPanel.add(this.saveBtn);
	// this.botPanel.add(this.cancelBtn);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
	if (event.getSource().equals(this.saveBtn)) {
	    String contents = this.ta.getText();
	    try {
		Script script = ScriptConstructor.constructScript(contents);
		if ((null != script.getBHvrs())
			&& (script.getBHvrs().size() == 1)) {
		    this.btt = ModelTransform.behaviorRevTransform(script
			    .getBHvrs().get(0), this.btt);

		} else {
		    throw new SerendipException("Script error");
		}

	    } catch (Exception e) {
		// TODO show message
		JOptionPane.showMessageDialog(this,
			"Cannot parse the behavior term. " + e.getMessage(),
			"Parse error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	    }
	}

    }

}
