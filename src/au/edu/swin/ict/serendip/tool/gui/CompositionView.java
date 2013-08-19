package au.edu.swin.ict.serendip.tool.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.VertexImageShaperDemo;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;

import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.composition.view.BehaviorView;
import au.edu.swin.ict.serendip.composition.view.ProcessView;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.xml.bindings.*;
import au.edu.swin.ict.road.xml.bindings.SMC.Contracts;
import au.edu.swin.ict.serendip.tool.gui.CompositionView.VType;
import au.edu.swin.ict.serendip.util.CompositionUtil;

public class CompositionView extends JPanel implements ActionListener {
    private SerendipEngine engine = null;
    private final static String PD_PREFIX = "P:";
    private final static String BT_PREFIX = "B:";
    private final static String T_PREFIX = "T:";
    private JMenuItem addBTToProcessMenu = null, addNewProcessMenu = null,
	    updateMenu = null;

    private JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JButton updateBtn = new JButton("Refresh");
    private JButton exportBtn = new JButton("Export");

    public CompositionView(SerendipEngine engine) {
	this.engine = engine;
	this.createView();
    }

    private void createView() {

	// Now we add the vv
	this.split.add(this.createProcessPanel(this.engine.getComposition()));
	this.split.add(this.createSRPanel(this.engine.getComposition()));
	this.split.setDividerLocation(300);
	this.setLayout(new BorderLayout());
	this.add(this.split, BorderLayout.CENTER);

	JPanel nthPanel = new JPanel(new FlowLayout());
	JLabel label = new JLabel("Composite : "
		+ this.engine.getCompositionName());// +
	nthPanel.add(label);
	updateBtn.addActionListener(this);
	nthPanel.add(updateBtn);
	exportBtn.addActionListener(this);
	nthPanel.add(exportBtn);
	this.add(nthPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the process panel/view
     * 
     * @param comp
     * @return
     */
    private JScrollPane createProcessPanel(Composition comp) {
	JScrollPane scrol = new JScrollPane();
	if (null == comp.getComposite().getSmcBinding().getBehaviorTerms()) {
	    // If no behaviour terms return an empty scrolpane
	    scrol.getViewport().add(new JPanel());
	    return scrol;
	}
	Graph<VType, String> processGraph = new SparseMultigraph<VType, String>();
	HashMap<String, BTVType> btMap = new HashMap<String, BTVType>();
	HashMap<String, PDVType> pdMap = new HashMap<String, PDVType>();
	// First we will add BTs
	if (null != comp.getComposite().getSmcBinding().getBehaviorTerms()) {
	    final List<BehaviorTermType> behaviorTermTypeList = comp
		    .getComposite().getSmcBinding().getBehaviorTerms()
		    .getBehaviorTerm();
	    for (int i = 0; i < behaviorTermTypeList.size(); i++) {
		BTVType btt = new BTVType(behaviorTermTypeList.get(i));
		btMap.put(btt.getId(), btt);
		processGraph.addVertex(btt);
	    }
	} else {
	    // No behaviour terms
	}

	if (null != comp.getComposite().getSmcBinding().getProcessDefinitions()) {
	    // Then we add PDs and the edges
	    final List<ProcessDefinitionType> processDefinitionTypeList = comp
		    .getComposite().getSmcBinding().getProcessDefinitions()
		    .getProcessDefinition();

	    for (int i = 0; i < processDefinitionTypeList.size(); i++) {
		ProcessDefinitionType pdType = processDefinitionTypeList.get(i);
		// Add pd vertex
		PDVType pdt = new PDVType(pdType);
		pdMap.put(pdt.getId(), pdt);
		processGraph.addVertex(pdt);

		if (null == pdType.getBehaviorTermRefs()) {
		    continue;// Some process definitions may not contain any
			     // behaviour terms. e.g., newly created ones
		}
		List<String> btRefList = pdType.getBehaviorTermRefs()
			.getBehavirTermId();

		for (int j = 0; j < btRefList.size(); j++) {
		    // Add edges
		    String btId = btRefList.get(j);
		    // add an edge btwn ith process and the bt if
		    BTVType bttTemp = btMap.get(btId);

		    processGraph.addEdge(pdt.getId() + "-" + btId, bttTemp,
			    pdt, EdgeType.UNDIRECTED);
		}
	    }
	}

	// Add edges to show the specialisation : todo
	final List<BehaviorTermType> behaviorTerms = comp.getComposite()
		.getSmcBinding().getBehaviorTerms().getBehaviorTerm();

	for (BehaviorTermType btt : behaviorTerms) {
	    // IF there BT extends another
	    String extendStr = btt.getExtends();
	    if (null != extendStr) {
		String btid = btt.getId();
		processGraph.addEdge(btid + "-" + extendStr, btMap.get(btid),
			btMap.get(extendStr), EdgeType.DIRECTED);
	    }

	}

	Layout<VType, String> layout = new CircleLayout<VType, String>(
		processGraph);
	layout.setSize(new Dimension(500, 300)); // sets the initial size of the
	// space
	final VisualizationViewer<VType, String> vv = new VisualizationViewer<VType, String>(
		layout);

	vv.getRenderContext().setArrowDrawPaintTransformer(
		new Transformer<String, Paint>() {

		    @Override
		    public Paint transform(String input) {
			// TODO Auto-generated method stub
			if (input.startsWith("p")) {
			    return Color.cyan;
			} else {
			    return Color.BLACK;
			}
		    }
		});

	vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

	vv.getRenderContext().setVertexLabelTransformer(
		new ToStringLabeller<VType>() {
		    public String transform(VType input) {
			return input.getId();// changed
		    }
		});
	Transformer<VType, Paint> vertexPaint = new Transformer<VType, Paint>() {
	    public Paint transform(VType input) {
		if (input instanceof BTVType) {
		    return Color.BLUE;
		} else if (input instanceof PDVType) {
		    return Color.GREEN;
		} else {
		    return Color.BLACK;// Unknown
		}
	    }
	};
	Transformer<VType, Shape> shapeTransf = new Transformer<VType, Shape>() {
	    @Override
	    public Shape transform(VType input) {
		// if (input instanceof BTVType) {
		// return new java.awt.geom.RoundRectangle2D.Double();
		// } else if (input instanceof PDVType) {
		// return new java.awt.geom.Ellipse2D.Double();
		// } else {
		return new Rectangle(-20, -10, 40, 20);

		// }
	    }

	};
	vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
	vv.getRenderContext().setVertexShapeTransformer(shapeTransf);
	/*
	 * vv.getRenderContext().setVertexIconTransformer( new
	 * DefaultVertexIconTransformer<VType>() { public Icon transform(VType
	 * input) {
	 * 
	 * if (input instanceof BTVType) { return new
	 * ImageIcon("images/bt.gif"); } else if (input instanceof PDVType) {
	 * return new ImageIcon("images/process.png"); } else { return new
	 * ImageIcon("images/unknown.gif"); } } });
	 */

	// Title
	vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {
	    int x;
	    int y;
	    Font font;
	    FontMetrics metrics;
	    int swidth;
	    int sheight;
	    String str = "Process Definitions";

	    public void paint(Graphics g) {
		Dimension d = new Dimension(200, 60);
		if (font == null) {
		    font = new Font(g.getFont().getName(), Font.BOLD, 20);
		    metrics = g.getFontMetrics(font);
		    swidth = metrics.stringWidth(str);
		    sheight = metrics.getMaxAscent() + metrics.getMaxDescent();
		    x = 0;
		    y = (int) (d.height - sheight * 1.5);
		}
		g.setFont(font);
		Color oldColor = g.getColor();
		g.setColor(Color.lightGray);
		g.drawString(str, x, y);
		g.setColor(oldColor);
	    }

	    public boolean useTransform() {
		return false;
	    }
	});// Eof Title

	// Background etc.
	vv.setBackground(Color.white);
	vv.setToolTipText("Process definitions and related behavior terms");

	// Mouse mode
	DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
	gm.setMode(ModalGraphMouse.Mode.PICKING);
	vv.setGraphMouse(gm);

	// add action listener
	vv.addMouseListener(new MouseListener() {

	    @Override
	    public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		PickedState<VType> st = vv.getPickedVertexState();
		Set<VType> pickedSet = st.getPicked();
		Iterator<VType> iter = pickedSet.iterator();
		SerendipEPCView epcView = null;
		String id = null;
		while (iter.hasNext()) {
		    VType s = iter.next();

		    if (null == s) {
			return;
		    }
		    if (s instanceof PDVType) {// A process
			id = s.getId();
			ProcessView pv = null;
			try {
			    pv = engine.getModelFactory().getProcessView(id,
				    null);
			} catch (SerendipException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			epcView = new SerendipEPCView(pv);

		    } else if (s instanceof BTVType) {
			id = s.getId();
			BehaviorView bv = null;
			try {
			    bv = engine.getModelFactory().getBehaviorView(id);
			    if (null == bv) {
				showError("Cannot get view for " + id);
				return;
			    }
			} catch (SerendipException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			epcView = new SerendipEPCView(bv);
		    } else {
			// Do nothing;

		    }

		    // Show
		    SerendipJFrame frame = new SerendipJFrame(id);
		    if (null != epcView) {
			frame.getContentPane().add(epcView);
		    } else {
			frame.setTitle("Cannot generate the EPC view for " + id);
		    }
		    frame.setVisible(true);
		}
	    }

	    @Override
	    public void mouseEntered(MouseEvent arg0) {
	    }

	    @Override
	    public void mouseExited(MouseEvent arg0) {
	    }

	    @Override
	    public void mousePressed(MouseEvent arg0) {
	    }

	    @Override
	    public void mouseReleased(MouseEvent arg0) {
	    }
	});

	// Menu. Currently hidden

	// this.createProcessViewPopUp(vv);
	// Scrol

	scrol.getViewport().add(vv);
	return scrol;
    }

    // SR
    private JScrollPane createSRPanel(Composition comp) {
	JScrollPane scrol = new JScrollPane();
	// First we will create the graph

	Graph<String, String> graph = new SparseMultigraph<String, String>();

	Contracts contracts = comp.getComposite().getSmcBinding()
		.getContracts();

	if (null == contracts) {
	    return scrol;
	}
	List<ContractType> contractTypeList = contracts.getContract();
	for (ContractType ct : contractTypeList) {
	    String roleA = ct.getRoleAID();
	    String roleB = ct.getRoleBID();
	    graph.addEdge(ct.getId(), roleA, roleB);

	}
	Layout<String, String> layout = new CircleLayout<String, String>(graph);
	layout.setSize(new Dimension(500, 500)); // sets the initial size of the
	// space

	VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(
		layout);
	vv.setPreferredSize(new Dimension(500, 500)); // Sets the viewing area
	vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {
	    int x;
	    int y;
	    Font font;
	    FontMetrics metrics;
	    int swidth;
	    int sheight;
	    String str = "Service Relationships";

	    public void paint(Graphics g) {
		Dimension d = new Dimension(200, 60);
		if (font == null) {
		    font = new Font(g.getFont().getName(), Font.BOLD, 20);
		    metrics = g.getFontMetrics(font);
		    swidth = metrics.stringWidth(str);
		    sheight = metrics.getMaxAscent() + metrics.getMaxDescent();
		    x = 0;
		    y = (int) (d.height - sheight * 1.5);
		}
		g.setFont(font);
		Color oldColor = g.getColor();
		g.setColor(Color.lightGray);
		g.drawString(str, x, y);
		g.setColor(oldColor);
	    }

	    public boolean useTransform() {
		return false;
	    }
	});

	vv.setBackground(Color.white);
	vv.setToolTipText("Service relationships");

	// Create a graph mouse and add it to the visualization component
	DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
	gm.setMode(ModalGraphMouse.Mode.PICKING);
	vv.setGraphMouse(gm);

	scrol.getViewport().add(vv); // size
	// Get all the ContractTypes

	// vv.getRenderContext().setEdgeLabelTransformer(new
	// ToStringLabeller());
	vv.getRenderContext().setVertexLabelTransformer(
		new ToStringLabeller<String>());

	vv.getRenderContext().setEdgeDrawPaintTransformer(
		new PickableEdgePaintTransformer<String>(vv
			.getPickedEdgeState(), Color.blue, Color.cyan));

	vv.getRenderContext().setEdgeLabelTransformer(
		new ToStringLabeller<String>());

	Transformer<String, Shape> vertexSize = new Transformer<String, Shape>() {
	    public Shape transform(String input) {
		Ellipse2D circle = new Ellipse2D.Double(-15, -15, 50, 30);
		return circle;
	    }
	};

	vv.getRenderContext().setVertexShapeTransformer(vertexSize);
	// vv.getRenderContext().setEdgeLabelTransformer(
	// new Transformer<String, String>() {
	// String path = "images/contract.gif";
	// URL url = getClass().getResource(path);
	//
	// public String transform(String input) {
	// if (null == url) {
	//
	// }
	// return "<html>" + input + "<img src=\"" + url
	// + "\" ></html>";// height=10 width=21
	// // return
	// //
	// "<html>"+input+"<img src=images\\bt.gif  height=10 width=21></html>";
	// }
	// });

	// vv.getRenderContext().setVertexIconTransformer(
	// new DefaultVertexIconTransformer<String>() {
	// public Icon transform(String input) {
	// String fileName = "images/" + input + ".gif";
	// if (!(new File(fileName)).exists()) {
	// fileName = "images/defRole.gif";
	// }
	// return new ImageIcon(fileName);
	//
	// }
	// });

	return scrol;
    }

    private void showError(String errorMsg) {
	JOptionPane.showMessageDialog(this, errorMsg, "Error",
		JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoMessage(String msg) {
	JOptionPane.showMessageDialog(this, msg, "Info",
		JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 
     * @param component
     * @return
     */
    private JPopupMenu createProcessViewPopUp(JComponent component) {
	JPopupMenu popup = new JPopupMenu();
	this.updateMenu = new JMenuItem("Add new process ");
	this.updateMenu.addActionListener(this);
	popup.add(this.updateMenu);

	/*
	 * this.addNewProcessMenu = new JMenuItem("Add new process ");
	 * this.addNewProcessMenu.addActionListener(this);
	 * popup.add(this.addNewProcessMenu);
	 * 
	 * this.addBTToProcessMenu = new JMenuItem(
	 * "Integrate behavior term to the process");
	 * this.addBTToProcessMenu.addActionListener(this);
	 * popup.add(this.addBTToProcessMenu);
	 */

	MouseListener popupListener = new PopupListener(popup);
	component.addMouseListener(popupListener);
	return popup;
    }

    class PopupListener extends MouseAdapter {
	JPopupMenu popup;

	PopupListener(JPopupMenu popupMenu) {
	    popup = popupMenu;
	}

	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
		popup.show(e.getComponent(), e.getX(), e.getY());
	    }
	}
    }

    @Override
    public void actionPerformed(ActionEvent event) {
	// TODO Auto-generated method stub
	if (event.getSource().equals(this.addNewProcessMenu)) {
	    // String s = (String) JOptionPane.showInputDialog(this,
	    // "Enter name of the process", "New Process",
	    // JOptionPane.PLAIN_MESSAGE, null, null, "Untitled1");
	    // // TODO POPUP
	    // ProcessDefinitionType pdType = new ProcessDefinitionType();
	    // pdType.setId(s);
	    // boolean res = this.engine.getComposition().getComposite()
	    // .getSmcBinding().getProcessDefinitions()
	    // .getProcessDefinition().add(pdType);
	    // if (res) {
	    // // TODO:Check if this is all we have to do
	    // PDVType pdvt = new PDVType(pdType);
	    // this.pdMap.put(pdvt.getId(), pdvt);
	    // this.processGraph.addVertex(pdvt);
	    // } else {
	    // showError("Cannot add a new process definition");
	    // }

	} else if (event.getSource().equals(this.addBTToProcessMenu)) {
	    // String[] btArr = CompositionUtil
	    // .getAllBTIDOfComposition(this.engine.getComposition());
	    // String s = (String) JOptionPane.showInputDialog(this,
	    // "Select the behavior term to add", "Updating process",
	    // JOptionPane.PLAIN_MESSAGE, null, btArr, " ");
	} else if (event.getSource().equals(this.updateBtn)) {

	    this.updateViews();
	} else if (event.getSource().equals(this.exportBtn)) {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File dir = chooser.getSelectedFile();
		OrganiserOperationResult result = this.engine.getComposition()
			.getComposite().getOrganiserRole()
			.takeSnapshotAtDir(dir.getAbsolutePath());
		if (result.getResult()) {
		    JOptionPane.showMessageDialog(this, result.getMessage());
		} else {
		    JOptionPane.showMessageDialog(this, result.getMessage(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		}

	    }

	}
    }

    private void updateViews() {
	this.split.removeAll();
	this.split.add(this.createProcessPanel(this.engine.getComposition()));
	this.split.add(this.createSRPanel(this.engine.getComposition()));
	this.split.setDividerLocation(300);

	showInfoMessage("View refreshed");
    }

    // Inner classes for the graphs
    /**
     * The parent class that represenets a vertex of the VV
     */
    public class VType {
	protected String id;

	public String getId() {
	    return this.id;
	}

	public String toString() {
	    return this.id;
	}

    }

    public class PDVType extends VType {
	private ProcessDefinitionType pdType = null;

	public PDVType(ProcessDefinitionType pdType) {
	    this.id = pdType.getId();
	    this.pdType = pdType;
	}

	public ProcessDefinitionType getPdType() {
	    return pdType;
	}

	public void setPdType(ProcessDefinitionType pdType) {
	    this.pdType = pdType;
	}

    }

    public class BTVType extends VType {
	private BehaviorTermType btType = null;

	public BTVType(BehaviorTermType btType) {
	    this.id = btType.getId();
	    this.btType = btType;
	}

	public BehaviorTermType getBtType() {
	    return btType;
	}

	public void setBtType(BehaviorTermType btType) {
	    this.btType = btType;
	}

    }
}
