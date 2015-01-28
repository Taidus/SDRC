package netViewer;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class TwoSitesPanel extends RingPanel {

	TwoSitesPanel(NetworkPanel parent) {
		super(parent);
		this.addComponentListener(this);
	}

}
