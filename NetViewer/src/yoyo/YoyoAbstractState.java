package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public abstract class YoyoAbstractState implements YoyoState {

	protected ArbitraryNodeYoyo node;

	public YoyoAbstractState(ArbitraryNodeYoyo node) {
		this.node = node;
	}
	
	@Override
	public void spontaneously() {}

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
}
