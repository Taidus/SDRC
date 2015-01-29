package halving;

import netViewer.TwoSitesNodeHalving;
import general.State;

public class Done extends AbstractHalvingState {

	public Done(TwoSitesNodeHalving node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.DONE;
	}

}
