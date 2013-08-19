package au.edu.swin.ict.serendip.tool.gui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SerendipJFrame extends JFrame {

    public SerendipJFrame(String title) throws HeadlessException {
	// TODO Auto-generated constructor stub
	super(title);
	super.setIconImage(Toolkit.getDefaultToolkit().getImage(
		"images/serendip.ico"));
	super.setBounds(10, 10, 400, 400);
    }

}
