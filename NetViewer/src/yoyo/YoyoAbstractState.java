package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public abstract class YoyoAbstractState implements YoyoState {

	protected ArbitraryNodeYoYo node;

	public YoyoAbstractState(ArbitraryNodeYoYo node) {
		this.node = node;
	}

	@Override
	public void handle(SetupMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void changeState(YoyoState nextState) {
		node.become(nextState);
	}

	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " " + sender;
	}
	
	protected void handleNoMessage(NoMessage m, Link sender){
		node.addNoNeighbours(sender);
		m.prune(node, sender);
		checkIfReceivedAllResponses();
		//FIXME non ci vorrebbe un flip?
	}
	
	protected void handleYesMessage(YesMessage m, Link sender){
		node.addYesNeighbours(sender);
		m.prune(node, sender);
		checkIfReceivedAllResponses();
	}

	protected final void checkIfReceivedAllResponses() {
		if (allResponseReceived()) {
			whenAllResponsesReceived();
		}
	}

	protected void whenAllResponsesReceived(){ //XXX abstract?
	}

	protected boolean allResponseReceived() {
		return node.getYesNeighboursSize() + node.getNoNeighboursSize() == node.getNumOfResponsesNeeded();
	}
}
