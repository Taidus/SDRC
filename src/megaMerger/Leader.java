package megaMerger;

import netViewer.ArbitraryNodeMegaMerger;
import general.State;


public class Leader extends MegaMergerAbstractState {
	
	public Leader(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.LEADER;
	}
}
