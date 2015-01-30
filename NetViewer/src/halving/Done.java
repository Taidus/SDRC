package halving;

import java.util.List;

import netViewer.NetViewer;
import netViewer.TwoSitesNodeHalving;
import general.State;

public class Done extends AbstractHalvingState {

	public Done(TwoSitesNodeHalving node) {
		super(node);
		printResult();
	}

	@Override
	public int intValue() {
		return State.DONE;
	}

	private void printResult() {
		List<Integer> finalData = node.getData();
		if(!finalData.isEmpty()) {
			NetViewer.out.println("Computed value: " + node.getData().get(0));
		}
	}
	
}
