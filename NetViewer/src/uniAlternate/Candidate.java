package uniAlternate;

import general.State;
import netViewer.RingNodeUniAlternate;
import netViewer.RingNodeUniAlternate.Direction;


public class Candidate extends AbstractUniAlternateState {



	public Candidate(RingNodeUniAlternate node) {
		super(node);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void handle(ElectionMessage m) {
		if (node.checkStageAndEnqueue(m)) {

			if (m.getValue() != node.getValue()) {

				if (m.getDir().equals(Direction.RIGHT)) {
					if (node.getValue() < m.getValue()) {
						node.nextStep();

						ElectionMessage e = new ElectionMessage(
								node.getStep(), Direction.LEFT, node.getValue());
						node.send(e);

					} else {
						node.become(new Passive(node));
					}
				} else {

					if (node.getValue() > m.getValue()) {
						node.nextStep();
						node.setValue(m.getValue());
						ElectionMessage e = new ElectionMessage(
								node.getStep(), Direction.RIGHT, node.getValue());
						node.send(e);

					} else {
						node.become(new Passive(node));
					}

				}

			} else {
				if (m.getValue() == node.getNodeId()) {
					node.become(new Leader(node));
				} else {
					node.become(new Follower(node));

				}

				node.send(new NotifyMessage(node.getValue()));
			}

			node.checkAndProcessNext();

		}

	}
	


			@Override
			public int intValue() {
				return State.CANDIDATE;
			}


}
