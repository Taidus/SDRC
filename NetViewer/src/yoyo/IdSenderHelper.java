package yoyo;

import java.util.HashSet;
import java.util.Set;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class IdSenderHelper {

	private ArbitraryNodeYoYo node;
	private IdSender owner;
	private Set<Link> yesNeighbours;
	private Set<Link> noNeighbours;
	private int numOfResponsesNeeded;
	
	public IdSenderHelper(ArbitraryNodeYoYo node, IdSender owner) {
		this.node = node;
		this.owner = owner;
		
		yesNeighbours = new HashSet<>();
		noNeighbours = new HashSet<>();
		numOfResponsesNeeded = 0;
	}
	
	public void handleNoMessage(NoMessage m, Link sender){
		noNeighbours.add(sender);
		m.prune(node, sender);
		checkIfReceivedAllResponses();
		node.flipOutgoingLink(sender);
	}
	
	public void handleYesMessage(YesMessage m, Link sender){
		yesNeighbours.add(sender);
		m.prune(node, sender);
		checkIfReceivedAllResponses();
	}
	
	private void checkIfReceivedAllResponses() {
		if (allResponseReceived()) {
			owner.whenAllResponsesReceived();
		}
	}

	private boolean allResponseReceived() {
		return getYesNeighboursSize() + getNoNeighboursSize() == getNumOfResponsesNeeded();
	}
	
	public void sendMessageToOutgoingLinks(YoyoMessage message) {
		node.sendToAll(message, node.getOutgoingLinks());
		numOfResponsesNeeded += node.getOutgoingLinks().size();
	}
	
	public int getYesNeighboursSize() {
		return yesNeighbours.size();
	}

	public int getNoNeighboursSize() {
		return noNeighbours.size();
	}

	public int getNumOfResponsesNeeded() {
		return numOfResponsesNeeded;
	}

}
