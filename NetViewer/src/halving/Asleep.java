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
	
	@Override
	public void spontaneously() {
		node.initialize();
	}

	public void handle(SetupMessage m) {
		node.initialize();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 processSetupMessage(m);
		
		

	}

}
