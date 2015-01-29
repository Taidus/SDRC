package halving;

import general.State;
import netViewer.TwoSitesNodeHalving;

public class Asleep extends AbstractHalvingState {

	public Asleep(TwoSitesNodeHalving node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.ASLEEP;
	}

	public void handle(MedianMessage m) {
		node.initialize();
		processMedianMessage(m);

	}

}
