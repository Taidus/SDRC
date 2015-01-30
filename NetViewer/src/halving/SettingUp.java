package halving;

import netViewer.TwoSitesNodeHalving;
import general.State;

public class SettingUp extends AbstractHalvingState {

	public SettingUp(TwoSitesNodeHalving node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.SETTING_UP;
	}
	
	
	
	public void handle(SetupMessage m) {
		processSetupMessage(m);

	}
	
	public void handle(MedianMessage m) {
		node.enqueueMessage(m);
	}
}
