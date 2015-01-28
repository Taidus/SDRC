package uniAlternate;

import general.State;
import netViewer.RingNodeUniAlternate;


public class Passive extends AbstractUniAlternateState {


	public Passive(RingNodeUniAlternate node) {
		super(node);
		node.sendAllEnqueuedMessages();
	}

	@Override
	public void handle(ElectionMessage m) {
		node.send(m);

	}

	@Override
		public void handle(NotifyMessage m) {
			node.send(m);
			if (m.getValue() == node.getNodeId()) {
				node.become(new Leader(node));
			} else {
				node.become(new Follower(node));
			}

		}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return State.PASSIVE;
	}

}
