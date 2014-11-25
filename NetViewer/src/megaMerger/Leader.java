package megaMerger;

import general.State;
import netViewer.ArbitraryNodeMegaMerger;


public class Leader extends MegaMergerAbstractState {
	
	public Leader(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.LEADER;
	}
}
