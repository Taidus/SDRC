package netViewer;

/*
 * NetViewer
 * Class: CompleteGraphPanel
 */

import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import java.awt.event.ComponentListener;

class CompleteGraphPanel extends DrawingPanel implements ComponentListener {

	private static final long serialVersionUID = 1L;

	CompleteGraphPanel(NetworkPanel parent) {
		super(parent);
		this.addComponentListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/*
	 * Resize component. Recalculate coordinates of nodes and links.
	 */
	public void componentResized(ComponentEvent e) {
		resizeBackgroundImage();
		if (drawingAreaIsBlank) {
			repaint(); // background
			return; // nothing to resize
		}
		positionNodesInACirlce();
		setLinkCoords();
		repaint();
	} // resize

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

}
