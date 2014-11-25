package megaMerger;

import netViewer.ArbitraryNodeMegaMerger;
import general.State;


public class Follower extends MegaMergerAbstractState {
	
	public Follower(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.FOLLOWER;
	}


}
