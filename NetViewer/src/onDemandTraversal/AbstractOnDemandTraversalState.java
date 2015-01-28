package onDemandTraversal;

import netViewer.RingNodeOnDemandTraversal;

public abstract class AbstractOnDemandTraversalState implements OnDemandTraversalState{
	
	protected RingNodeOnDemandTraversal node;
	
	
	
	public AbstractOnDemandTraversalState(RingNodeOnDemandTraversal node) {
		super();
		this.node = node;
	}

	@Override
	public void changeState(OnDemandTraversalState nextState) {
		node.become(nextState);
	}

	@Override
	public void spontaneously() {
	}

}
