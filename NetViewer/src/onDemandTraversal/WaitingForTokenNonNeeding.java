package onDemandTraversal;

import netViewer.RingNodeOnDemandTraversal;
import general.State;

public class WaitingForTokenNonNeeding extends WaitingForToken{

	

	public WaitingForTokenNonNeeding(RingNodeOnDemandTraversal node) {
		super(node);
		//nextCriticalOpearation();
	}

	@Override
	public int intValue() {
		return State.WAITING_FOR_TOKEN_NON_NEEDING;
	}
	
	@Override
	public void spontaneously() {
	nextCriticalOpearation();
	}

}
