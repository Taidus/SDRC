package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Internal extends Receiver {

	public Internal(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.INTERNAL;
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		handleYoMessage(m, sender);
	}
	
	@Override
	protected void whenReceivedIdOnAllLinks() {
		node.sendMessageToOutgoingLinks(new YoMessage(getMinReceivedId()));
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		handleNoMessage(m, sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		handleYesMessage(m, sender);
	}

}
