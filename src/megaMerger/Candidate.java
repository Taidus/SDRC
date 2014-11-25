package megaMerger;

import netViewer.ArbitraryNodeMegaMerger;


public class Candidate extends Awake {

	public Candidate(ArbitraryNodeMegaMerger node) {
		super(node);
	}
	
	@Override
	protected void handleMergeDelegation() {
		node.sendMergeRequest();
	}

}
