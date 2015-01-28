package onDemandTraversal;

import general.State;
import netViewer.RingNodeOnDemandTraversal;

public class WaitingForTokenNeeding extends WaitingForToken {

	public WaitingForTokenNeeding(RingNodeOnDemandTraversal node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.WAITING_FOR_TOKEN_NEEDING;
	}
	
	@Override
	public void handle(TokenMessage m, int dir) {
		
		super.handle(m, dir);
		nextCriticalOpearation();
		
	}

}
