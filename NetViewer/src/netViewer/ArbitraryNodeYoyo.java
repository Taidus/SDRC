package netViewer;

import general.Message;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import yoyo.Asleep;
import yoyo.SetupMessage;
import yoyo.YoyoMessage;
import yoyo.YoyoState;

public class ArbitraryNodeYoyo extends Node {

	private YoyoState nodeState;
	private Set<Link> outgoingLinks;
	private Set<Link> incomingLinks;

	public ArbitraryNodeYoyo(Integer ID) {
		super(ID);

		nodeState = new Asleep(this);
		outgoingLinks = new HashSet<>();
		incomingLinks = new HashSet<>();
	}

	@Override
	protected void initialize() {
		nodeState.spontaneously();
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
		}
		else {
			incomingLinks.add(sender);
		}
	}

	public void flipIncomingLinks(Set<Link> linksToFlip) {
		Set<Link> incomingLinksToFlip = linksToFlip;
		incomingLinksToFlip.retainAll(incomingLinks);
		assert incomingLinks.containsAll(incomingLinksToFlip);
		incomingLinks.removeAll(incomingLinksToFlip);
		outgoingLinks.addAll(incomingLinksToFlip);
	}

	public void flipOutgoingLinks(Set<Link> linksToFlip) {
		Set<Link> incomingLinksToFlip = linksToFlip;
		incomingLinksToFlip.retainAll(outgoingLinks);
		assert outgoingLinks.containsAll(linksToFlip);
		outgoingLinks.removeAll(linksToFlip);
		incomingLinks.addAll(linksToFlip);
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

	public void sendSetupMessage() {
		sendToAll(new SetupMessage(nodeId), links);
	}
	
	public boolean isSetupCompleted(){
		return incomingLinks.size() + outgoingLinks.size() == links.size();
	}

	private Set<Link> createSingleton(Link link) {
		Set<Link> singleton = new HashSet<Link>();
		singleton.add(link);
		return singleton;
	}

}
