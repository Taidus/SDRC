package netViewer;

/*
 * NetViewer
 * Class: RingPanel
 */

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

class RingPanel extends DrawingPanel implements ComponentListener {

	private static final long serialVersionUID = 1L;

	RingPanel(NetworkPanel parent) {
		super(parent);
		this.addComponentListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	} // end paintComponent

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
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

}
