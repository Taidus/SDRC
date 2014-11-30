package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class Source extends YoyoAbstractState implements IdSender {

	private IdSenderHelper idSenderHelper;

	public Source(ArbitraryNodeYoyo node) {
		super(node);
		idSenderHelper = new IdSenderHelper(node, this);
	}

	@Override
	public void whenAllResponsesReceived() {
		System.out.println("Source: all responses arrived");
		idSenderHelper.flipNoNeighbours();
		chooseState();
	}

	@Override
	public void sendMessageToOutgoingLinks(YoyoMessage message) {
		idSenderHelper.sendMessageToOutgoingLinks(message);
	}
	
	@Override
	protected void allLinksPruned() {
		changeState(new Leader(node));
	}

	@Override
	public int intValue() {
		return State.SOURCE;
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		idSenderHelper.handleNoMessage(m, sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		idSenderHelper.handleYesMessage(m, sender);
	}
	
	@Override
	public void handle(YoMessage m, Link sender) {
		enqueueMessage(m, sender);
	}

}
