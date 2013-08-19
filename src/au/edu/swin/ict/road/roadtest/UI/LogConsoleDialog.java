package au.edu.swin.ict.road.roadtest.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class represents a console to user which shows the logger information
 * and notifies the user about various events happened in ROADTest and
 * ROADFactory
 * 
 * @author Abhijeet Pai (abhijeet.m.pai@gmail.com)
 * 
 */
public class LogConsoleDialog extends JDialog {

    // Variables declaration
    private JTextArea jTextAreaLog;
    private JScrollPane jScrollPane1;
    private JButton jButtonClear;
    private JPanel contentPane;

    // End of variables declaration

    /**
     * Constructor
     */
    public LogConsoleDialog(Frame w) {
	super(w);
	initializeComponent();
	// this.setVisible(true);
    }

    /**
     * This function is used to initialise all the components of the dialog
     */
    private void initializeComponent() {
	jTextAreaLog = new JTextArea();
	jTextAreaLog.setEditable(false);
	jScrollPane1 = new JScrollPane();
	jButtonClear = new JButton("Clear");
	contentPane = (JPanel) this.getContentPane();
	jTextAreaLog.setLineWrap(true);
	jScrollPane1.setViewportView(jTextAreaLog);

	// register action listener
	jButtonClear.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		jButtonClear_actionPerformed(e);
	    }
	});

	contentPane.setLayout(null);

	addComponent(contentPane, jScrollPane1, 1, 2, 778, 150);
	addComponent(contentPane, jButtonClear, 1, 155, 70, 20);

	this.setTitle("Log");
	this.setLocation(new Point(25, 443));
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	this.setSize(new Dimension(786, 205));
    }

    /**
     * This function adds the Component without a Layout Manager (Absolute
     * Positioning)
     * 
     * @param container
     *            Parent <code>Container</code> to which component needs to be
     *            added
     * @param c
     *            <code>Component</code> that needs to be added
     * @param x
     *            <code>Integer</code> x position
     * @param y
     *            <code>Integer</code> y position
     * @param width
     *            <code>Integer</code> width
     * @param height
     *            <code>Integer</code> height
     */
    private void addComponent(Container container, Component c, int x, int y,
	    int width, int height) {
	c.setBounds(x, y, width, height);
	container.add(c);
    }

    /**
     * This function appends the text to textarea
     * 
     * @param content
     *            <code>String</code> content
     */
    public void addText(String content) {
	jTextAreaLog.append(content + "\n");
	jScrollPane1.setAutoscrolls(true);
	jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength());
    }

    /**
     * Functionality to clear the textarea
     * 
     * @param e
     *            <code>ActionEvent</code> an action event
     */
    private void jButtonClear_actionPerformed(ActionEvent e) {
	jTextAreaLog.setText("");
    }

}
