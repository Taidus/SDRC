package netViewer;

import general.Message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.LimitExceededException;

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
	private boolean pruned_ingoing;
	private Set<Link> outgoingEdges;
	private Set<Link> incomingEdges;

	private Set<Link> yesNeighbours;
	private Set<Link> noNeighbours;
	private Set<Link> receivedIDs;

	private Map<Integer, Set<Link>> linksPerSentId;
	private int minReceivedValue;

	int numOfResponsesNeeded;

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);

		nodeState = new Asleep(this);
		pruned_ingoing = false;
		outgoingEdges = new HashSet<>();
		incomingEdges = new HashSet<>();

		yesNeighbours = new HashSet<>();
		noNeighbours = new HashSet<>();
		receivedIDs = new HashSet<>();

		numOfResponsesNeeded = 0;
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

	public void sendMessage(Message message, Link link) {
		send(message, link);
	}

	public void setupEdge(SetupMessage m, Link sender) {
		if (m.getId() > getNodeId())
			outgoingEdges.add(sender);
		else
			incomingEdges.add(sender);
		if (outgoingEdges.size() + incomingEdges.size() == getLinks().size())
			chooseState();
	}

	public void chooseState() {
		if (incomingEdges.size() + outgoingEdges.size() == 0) {
			if (!pruned_ingoing) {
				become(new Leader(this));
			} else {
				become(new Follower(this));
			}
		} else if (incomingEdges.isEmpty()) {
			become(new Source(this));
			sendMessageToOutgoingEdges(new YoMessage(getNodeId()));
		} else if (outgoingEdges.isEmpty()) {
			become(new Sink(this));
		} else {
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
		assert incomingEdges.containsAll(links);
		incomingEdges.removeAll(links);
		outgoingEdges.addAll(links);
	}

	public void flipOutgoingLinks(Set<Link> links) {
		assert outgoingEdges.containsAll(links);
		outgoingEdges.removeAll(links);
		incomingEdges.addAll(links);
	}

	public void pruneIncomingLinks(Set<Link> links) {
		assert incomingEdges.containsAll(links);
		incomingEdges.removeAll(links);
	}

	public void pruneOutgoingLinks(Set<Link> links) {
		assert outgoingEdges.containsAll(links);
		outgoingEdges.removeAll(links);
	}

	public void pruneIncomingLink(Link link) {
		assert incomingEdges.contains(link);
		incomingEdges.remove(link);
	}

	public void pruneOutgoingLink(Link link) {
		assert outgoingEdges.contains(link);
		outgoingEdges.remove(link);
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

	public int getMinReceivedValue() {
		return minReceivedValue;
	}

	public void setMinReceivedValue(int minValue) {
		minReceivedValue = minValue;
	}

	public Set<Link> getLinksById(int id) {
		return new HashSet<Link>(linksPerSentId.get(id));
	}

	public Set<Link> linksThatSentDifferentId(int id) {
		Set<Link> links = new HashSet<>();
		for (Integer currentId : linksPerSentId.keySet()) {
			if (currentId != id) {
				links.addAll(linksPerSentId.get(currentId));
			}
		}
		return links;
	}

	public Set<Integer> getKeysOfLinksPerSentId() {
		return linksPerSentId.keySet();
	}

	public void receivedIdOn(Link link, int id) {
		linksPerSentId.get(id).add(link);
	}

	public void clearIdMap() {
		linksPerSentId.clear();
	}

	// FIXME: cambiare nome
	public boolean idReceivedFromAllLinks() {
		int size = 0;
		for (Set<Link> toCompute : linksPerSentId.values()) {
			size += toCompute.size();
		}
		return incomingEdges.size() == size;
	}

	public void sendMessageToOutgoingEdges(Message toSend) {
		for (Link toSendTo : outgoingEdges) {
			send(toSend, toSendTo);
			numOfResponsesNeeded++;
		}
	}

	public void sendMessageToAllIdLinks(Message toSend, int id) {
		for (Link toSendTo : linksPerSentId.get(id)) {
			send(toSend, toSendTo);
		}
	}

	// FIXME: non so se � possibile farlo in modo pi� rapido
	public void addElementToMap(int id, Link link) {
		if (linksPerSentId.containsKey(id)) {
			linksPerSentId.get(id).add(link);
		} else {
			Set<Link> singleLink = new HashSet<>();
			singleLink.add(link);
			linksPerSentId.put(id, singleLink);
		}
	}

	// FIXME: ho fatto cos� visto che ad Alessio Sarullo non gli piace ritornare
	// l'intero Set
	public Set<Link> copyIncomingEdges() {
		Set<Link> Edge = incomingEdges;
		return Edge;
	}

	public Set<Link> getLinksToPrune() {
		Set<Link> redudantLinks = new HashSet<Link>();
		for (Integer id : linksPerSentId.keySet()) {

			Set<Link> links = linksPerSentId.get(id);
			if (!links.isEmpty()) {
				redudantLinks.addAll(links);
				Iterator<Link> it = links.iterator();
				redudantLinks.remove(it.next());
			}

		}

		if (redudantLinks.size() == incomingEdges.size()) {
			return new HashSet<Link>(incomingEdges);
		} else {
			return redudantLinks;
		}
	}
}
