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
	}

	@Override
	protected void whenReceivedIdOnAllLinks() {
		System.out.println("Sink id:"+node.getNodeId()+" respond to All");
		respondToAll();
		node.chooseState();

	}

	@Override
	public int intValue() {
		return State.SINK;
	}
}
