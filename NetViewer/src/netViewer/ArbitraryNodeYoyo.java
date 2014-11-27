package netViewer;

import general.Message;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
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

public class ArbitraryNodeYoyo extends Node {

	private YoyoState nodeState;
	private boolean prunedIncoming;
	private Set<Link> outgoingLinks;
	private Set<Link> incomingLinks;

	private Queue<QueueMessage> queue;
	

	public ArbitraryNodeYoyo(Integer ID) {
		super(ID);

		queue = new ArrayDeque<QueueMessage>();
		nodeState = new Asleep(this);
		prunedIncoming = false;
		outgoingLinks = new HashSet<>();
		incomingLinks = new HashSet<>();
	}
	
	private class QueueMessage {
		private YoyoMessage message;
		private Link sender;

		public YoyoMessage getMessage() {
			return message;
		}

		public Link getSender() {
			return sender;
		}

		public QueueMessage(YoyoMessage message, Link sender) {
			super();
			this.message = message;
			this.sender = sender;
		}

	}

	public void enqueueMessage(YoyoMessage m, Link sender) {
		queue.add(new QueueMessage(m, sender));
	}

	public void processEnqueuedMessages() {
		QueueMessage m = queue.poll();
		while (m != null) {

			m.getMessage().accept(nodeState, m.getSender());
			m = queue.poll();
		}
	}

	@Override
	protected void initialize() {
		nodeState.spontaneously();
	}

	public void yoyoInitialize() {
		// XXX ha davvero senso o si può spostare in Asleep?
		sendToNeighbours(new SetupMessage(nodeId));
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
		if (m.getId() > getNodeId()) {
			outgoingLinks.add(sender);
		} else {
			incomingLinks.add(sender);
		}
		if (outgoingLinks.size() + incomingLinks.size() == getLinks().size()) {
			chooseState();
		}
	}

	public void chooseState() {
		if (incomingLinks.size() + outgoingLinks.size() == 0) {
			if (!prunedIncoming) {
				become(new Leader(this));
			} else {
				become(new Follower(this));
			}
		} else if (incomingLinks.isEmpty()) {
			Source nextState = new Source(this);
			become(nextState);
			nextState.sendMessageToOutgoingLinks(new YoMessage(getNodeId()));
		} else if (outgoingLinks.isEmpty()) {
			become(new Sink(this));
			processEnqueuedMessages();
		} else {
			become(new Internal(this));
			processEnqueuedMessages();
		}
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

	public void flipIncomingLink(Link link) {
		flipIncomingLinks(createSingleton(link));
	}

	public void flipOutgoingLink(Link link) {
		flipOutgoingLinks(createSingleton(link));
	}

	public void pruneIncomingLinks(Set<Link> links) {
		assert incomingLinks.containsAll(links);
		incomingLinks.removeAll(links);
		prunedIncoming = true;
	}

	public void pruneOutgoingLinks(Set<Link> links) {
		assert outgoingLinks.containsAll(links);
		outgoingLinks.removeAll(links);
	}

	public void pruneIncomingLink(Link link) {
		pruneIncomingLinks(createSingleton(link));
	}

	public void pruneOutgoingLink(Link link) {
		pruneOutgoingLinks(createSingleton(link));
	}

	public Set<Link> getIncomingLinks() {
		return new HashSet<Link>(incomingLinks);
	}

	public Set<Link> getOutgoingLinks() {
		return new HashSet<Link>(outgoingLinks);
	}

	public void sendToAll(YoyoMessage message, Collection<Link> links) {
		for (Link link : links) {
			send(message, link);
		}
	}

	// XXX: forse ha senso metterla in Node
	private void sendToNeighbours(YoyoMessage message) {
		sendToAll(message, links);
	}
	
	private Set<Link> createSingleton(Link link){
		Set<Link> singleton = new HashSet<Link>();
		singleton.add(link);
		return singleton;
	}

}