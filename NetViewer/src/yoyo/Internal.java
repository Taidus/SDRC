package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class Internal extends YoyoAbstractState implements IdSender, IdReceiver {

	private IdSenderHelper idSenderHelper;
	private IdReceiverHelper idReceiverHelper;

	public Internal(ArbitraryNodeYoyo node) {
		super(node);
		this.idSenderHelper = new IdSenderHelper(node, this);
		this.idReceiverHelper = new IdReceiverHelper(node, this);
	}

	@Override
	public int intValue() {
		return State.INTERNAL;
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		if (node.getIncomingLinks().contains(sender)) {
			idReceiverHelper.handleYoMessage(m, sender);
		}
		else {
			assert node.getOutgoingLinks().contains(sender);
			enqueueMessage(m, sender);
		}
	}

	@Override
	public void whenAllResponsesReceived() {
		idReceiverHelper.respondToAll();
		idSenderHelper.flipNoNeighbours();
		chooseState();
	}

	@Override
	public void sendMessageToOutgoingLinks(YoyoMessage message) {
		idSenderHelper.sendMessageToOutgoingLinks(message);
	}

	@Override
	public void whenReceivedIdOnAllLinks() {
		System.out.println("Internal send to all outgoing");
		sendMessageToOutgoingLinks(new YoMessage(idReceiverHelper.getMinReceivedId()));
	}

	@Override
	public boolean hasYesToBeSent() {
		return idSenderHelper.getYesNeighboursSize() == idSenderHelper.getNumOfResponsesNeeded();
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		idSenderHelper.handleNoMessage(m, sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		idSenderHelper.handleYesMessage(m, sender);
	}

}
