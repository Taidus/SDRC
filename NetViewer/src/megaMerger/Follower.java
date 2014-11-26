package megaMerger;

import general.State;
import netViewer.ArbitraryNodeMegaMerger;


public class Follower extends MegaMergerAbstractState {
	
	public Follower(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.FOLLOWER;
	}


}
