package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class Sink extends YoyoAbstractState implements IdReceiver{
	
	private IdReceiverHelper idReceiverHelper;

	public Sink(ArbitraryNodeYoyo node) {
		super(node);
		this.idReceiverHelper = new IdReceiverHelper(node, this);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		idReceiverHelper.handleYoMessage(m, sender);
	}

	@Override
	public void whenReceivedIdOnAllLinks() {
		System.out.println("Sink id:"+node.getNodeId()+" respond to All");
		idReceiverHelper.respondToAll();
		chooseState();
	}
	
	@Override
	public boolean hasYesToBeSent() {
		return true;
	}

	@Override
	public int intValue() {
		return State.SINK;
	}
}
