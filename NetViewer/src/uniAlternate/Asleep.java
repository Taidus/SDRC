package uniAlternate;

import general.State;
import netViewer.RingNodeUniAlternate;


public class Asleep extends AbstractUniAlternateState {

	public Asleep(RingNodeUniAlternate node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(ElectionMessage m) {
		node.initialize(m);
	}

	@Override
	public void handle(NotifyMessage m) {
		System.err.println("Notify message arrived in asleep state");

	}

	@Override
	public int intValue() {
		return State.ASLEEP;
	}

}
