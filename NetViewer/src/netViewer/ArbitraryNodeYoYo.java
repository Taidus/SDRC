package netViewer;

import general.Message;

import java.util.HashSet;
import java.util.Set;

import yoyo.Awake;
import yoyo.Asleep;
import yoyo.Follower;
import yoyo.Internal;
import yoyo.Leader;
import yoyo.SetupMessage;
import yoyo.Sink;
import yoyo.Source;
import yoyo.YoMessage;
import yoyo.YoyoMessage;
import yoyo.YoyoState;

public class ArbitraryNodeYoYo extends Node {

	private YoyoState nodeState;
	private boolean prunedIncoming;
	private Set<Link> outgoingLinks;
	private Set<Link> incomingLinks;

	private Set<Link> yesNeighbours;
	private Set<Link> noNeighbours;
	private int numOfResponsesNeeded;

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);

		nodeState = new Asleep(this);
		prunedIncoming = false;
		outgoingLinks = new HashSet<>();
		incomingLinks = new HashSet<>();

		yesNeighbours = new HashSet<>();
		noNeighbours = new HashSet<>();

		numOfResponsesNeeded = 0;
	}
	
	@Override
	protected void initialize() {
		nodeState.spontaneously();
		// TODO eventualmente aggiungere altro
	}
	
	public void yoyoInitialize() {
		sendToNeighbours(new SetupMessage(this));
		become(new Awake(this));
	}

	public void become(YoyoState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((YoyoMessage) msg).accept(nodeState, link);
	}

	public void setupLink(SetupMessage m, Link sender) {
		//FIXME: è giusta questa guardia?
		if (m.getId() < getNodeId())
			outgoingLinks.add(sender);
		else
			incomingLinks.add(sender);
		if (outgoingLinks.size() + incomingLinks.size() == getLinks().size())
			chooseState();
	}

	public void chooseState() {
		// FIXME mi sa che da qualche parte numOfResponsesNeeded dovrà essere resettata
		if (incomingLinks.size() + outgoingLinks.size() == 0) {
			if (!prunedIncoming) {
				become(new Leader(this));
			}
			else {
				become(new Follower(this));
			}
		}
		else if (incomingLinks.isEmpty()) {
			become(new Source(this));
			sendMessageToOutgoingLinks(new YoMessage(getNodeId()));
		}
		else if (outgoingLinks.isEmpty()) {
			become(new Sink(this));
		}
		else {
			become(new Internal(this));
		}
	}

	public void addYesNeighbours(Link toAdd) {
		yesNeighbours.add(toAdd);
	}

	public void addNoNeighbours(Link toAdd) {
		noNeighbours.add(toAdd);
	}

	public void flipIncomingLinks(Set<Link> links) {
		assert incomingLinks.containsAll(links);
		incomingLinks.removeAll(links);
		outgoingLinks.addAll(links);
	}

	public void flipOutgoingLinks(Set<Link> links) {
		assert outgoingLinks.containsAll(links);
		outgoingLinks.removeAll(links);
		incomingLinks.addAll(links);
	}

	public void pruneIncomingLinks(Set<Link> links) {
		assert incomingLinks.containsAll(links);
		incomingLinks.removeAll(links);
	}

	public void pruneOutgoingLinks(Set<Link> links) {
		assert outgoingLinks.containsAll(links);
		outgoingLinks.removeAll(links);
	}

	public void pruneIncomingLink(Link link) {
		assert incomingLinks.contains(link);
		incomingLinks.remove(link);
		prunedIncoming = true;
	}

	public void pruneOutgoingLink(Link link) {
		assert outgoingLinks.contains(link);
		outgoingLinks.remove(link);
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

	public void sendMessageToOutgoingLinks(YoyoMessage toSend) {
		sendToAll(toSend, outgoingLinks);
		numOfResponsesNeeded += outgoingLinks.size();
	}

	public Set<Link> getIncomingLinks() {
		return new HashSet<Link>(incomingLinks);
	}
	
	public Set<Link> getOutgoingLinks() {
		return new HashSet<Link>(outgoingLinks);
	}

	public void sendToAll(YoyoMessage message, Set<Link> links) {
		for (Link link : links) {
			send(message, link);
		}
	}

	//FIXME: forse ha senso metterla in Node
	private void sendToNeighbours(YoyoMessage message) {
		for(Link toSendTo:getLinks()) {
			send(message, toSendTo);
		}
	}
	
}
