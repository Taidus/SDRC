package netViewer;

import java.awt.event.MouseEvent;

public class TwoSitesPanel extends ArbitraryPanel {	
	private static final long serialVersionUID = 1L;

	TwoSitesPanel(NetworkPanel parent) {
		super(parent);
		this.addComponentListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

}
