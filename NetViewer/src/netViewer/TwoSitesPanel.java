package netViewer;

//TODO: ripulire
public class TwoSitesPanel extends RingPanel {	
	private static final long serialVersionUID = 1L;

	TwoSitesPanel(NetworkPanel parent) {
		super(parent);
		this.addComponentListener(this);
	}

}
