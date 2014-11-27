package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Sink extends Receiver {

	public Sink(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		handleYoMessage(m, sender);
		node.chooseState();
	}

	@Override
	protected void whenReceivedIdOnAllLinks() {
		respondToAll();
	}

	@Override
	public int intValue() {
		return State.SINK;
	}
}
