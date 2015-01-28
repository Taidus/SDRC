package uniAlternate;

import netViewer.RingNodeUniAlternate;

public abstract class AbstractUniAlternateState implements UniAlternateState{
	
	
	protected RingNodeUniAlternate node;

	public AbstractUniAlternateState(RingNodeUniAlternate node) {
		super();
		this.node = node;
	}

	@Override
	public void changeState(UniAlternateState nextState) {
		node.become(nextState);
	}

	@Override
	public void handle(ElectionMessage m) {
		defaultHandle();

	}
	
	@Override
	public void handle( NotifyMessage m) {
		defaultHandle();

	}
	

	@Override
	public void spontaneously() {
		node.initialize();
	}

	private final void defaultHandle() {
		assert false : this.getClass() + " " + this.node.getNodeId();
				
	}


}
