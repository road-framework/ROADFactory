package au.edu.swin.ict.serendip.tool.gui;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class AdminLogAppender extends AppenderSkeleton {
    private JTextArea textArea = null;

    public AdminLogAppender(JTextArea textArea) {

	this.textArea = textArea;
    }

    // @Override //WriterAppender
    // public synchronized void doAppend(LoggingEvent event) {
    // // TODO Auto-generated method stub
    // final String message = this.layout.format(event);
    //
    //
    // SwingUtilities.invokeLater(new Runnable() {
    // public void run() {
    // textArea.append(message);
    // }
    // });
    // }

    @Override
    protected void append(LoggingEvent loggingEvent) {
	textArea.setText(textArea.getText() + loggingEvent.getMessage());
    }

    @Override
    public void close() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean requiresLayout() {
	// TODO Auto-generated method stub
	return false;
    }

    //
    // public void append(LoggingEvent loggingEvent) {
    // final String message = this.layout.format(loggingEvent);
    //
    // // Append formatted message to textarea using the Swing Thread.
    // SwingUtilities.invokeLater(new Runnable() {
    // public void run() {
    // logTextArea.append(message);
    // }
    // });
    // }

}
