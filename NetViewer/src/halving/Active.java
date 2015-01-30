package halving;

import general.State;
import netViewer.TwoSitesNodeHalving;

public class Active extends AbstractHalvingState {

	public Active(TwoSitesNodeHalving node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.ACTIVE;
	}
	
	@Override
	public void handle(MedianMessage m) {
		processMedianMessage(m);

	}

}
