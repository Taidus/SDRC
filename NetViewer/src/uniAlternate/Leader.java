package uniAlternate;

import netViewer.RingNodeUniAlternate;
import general.State;

public class Leader extends AbstractUniAlternateState{
	
	
	public Leader(RingNodeUniAlternate node) {
		super(node);
		node.sendAllEnqueuedMessages();

		// TODO Auto-generated constructor stub
	}

	@Override
	public int intValue() {
		return State.LEADER;
	}

}
