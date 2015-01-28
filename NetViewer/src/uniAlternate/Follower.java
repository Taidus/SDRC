package uniAlternate;

import general.State;
import netViewer.RingNodeUniAlternate;

public class Follower extends AbstractUniAlternateState{
	
	
	public Follower(RingNodeUniAlternate node) {
		super(node);
		node.sendAllEnqueuedMessages();
	}

	@Override
	public int intValue() {
		return State.FOLLOWER;
	}
	
	
	@Override
	public void handle(ElectionMessage m) {
		//TODO DO NOTHING

	}
	
	@Override
	public void handle( NotifyMessage m) {
		//TODO DO NOTHING

	}

}
