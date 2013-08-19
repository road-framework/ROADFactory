package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermRef;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.serendip4saas.gui.Serendip4SaasToolFrame;
import au.edu.swin.ict.serendip.serendip4saas.gui.SerendipJList;

public class ProcessAssemblyPanelGroup extends JPanel implements ActionListener {
    static Logger logger = Logger.getLogger(ProcessAssemblyPanelGroup.class);
    private Composite composite = null;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Vector<BehaviorTerm> behaviorTerms = null;

    public ProcessAssemblyPanelGroup(Composite composition) {
	this.composite = composition;
	try {
	    this.createUI();
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub

    }

    /**
     * Add existing processes to the UI
     * 
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedLookAndFeelException
     * @throws SerendipException
     */
    private void createUI() throws ClassNotFoundException,
	    InstantiationException, IllegalAccessException,
	    UnsupportedLookAndFeelException, SerendipException {

	this.setLayout(new BorderLayout());
	this.add(this.tabbedPane, BorderLayout.CENTER);
	ProcessDefinitionsType pDefTypes = this.composite.getSmcBinding()
		.getProcessDefinitions();
	if (pDefTypes == null) {
	    return;
	}
	// Create a tab for each and every PD
	for (ProcessDefinitionType pdType : pDefTypes.getProcessDefinition()) {
	    JPanel assembler = new ProcessAssemblyPanel(pdType, this.composite);
	    tabbedPane.addTab(pdType.getId(), null, assembler, pdType.getId());
	}

    }

}
