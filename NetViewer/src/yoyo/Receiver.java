package yoyo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public abstract class Receiver extends YoyoAbstractState {
	private Map<Integer, Set<Link>> linksPerReceivedId;
	private int minReceivedId;

	public Receiver(ArbitraryNodeYoYo node) {
		super(node);
		linksPerReceivedId = new HashMap<>();
		minReceivedId = Integer.MAX_VALUE;
	}

	public int getMinReceivedId() {
		return minReceivedId;
	}

	public void setMinReceivedId(int minReceivedId) {
		this.minReceivedId = minReceivedId;
	}

	protected abstract void whenReceivedIdOnAllLinks();

	@Override
	protected void whenAllResponsesReceived() {
		respondToAll();
	}
	
	protected void handleYoMessage(YoMessage m, Link sender) {
		addIdReceivedOnLink(m.getId(), sender);
		if (m.getId() < getMinReceivedId()) {
			setMinReceivedId(m.getId());
		}
		if (idReceivedFromAllLinks()) {
			whenReceivedIdOnAllLinks();
		}
	}

	protected void respondToAll() {
		Set<Link> sendNoLinks = node.getIncomingLinks();

		Set<Link> linksToPrune = getLinksToPrune();

		if (receivedOnlyYes()) {
			sendNoLinks = getLinksThatSentDifferentId(getMinReceivedId());

			Set<Link> sendYesLinks = getLinksById(getMinReceivedId());
			selectiveSend(new YesMessage(), sendYesLinks, linksToPrune);
			sendYesLinks.retainAll(linksToPrune);
			node.sendToAll(new YesAndPruneMessage(), sendYesLinks);
		}

		node.flipIncomingLinks(sendNoLinks);

		selectiveSend(new NoMessage(), sendNoLinks, linksToPrune);
		sendNoLinks.retainAll(linksToPrune);
		node.sendToAll(new NoAndPruneMessage(), sendNoLinks);

		node.pruneIncomingLinks(linksToPrune);

		node.chooseState();
	}

	protected boolean idReceivedFromAllLinks() {
		int size = 0;
		for (Set<Link> toCompute : linksPerReceivedId.values()) {
			size += toCompute.size();
		}
		assert node.getIncomingLinks().size() >= size;
		return node.getIncomingLinks().size() == size;
	}

	protected boolean receivedOnlyYes() {
		return node.getYesNeighboursSize() == node.getNumOfResponsesNeeded();
	}

	private Set<Link> getLinksToPrune() {
		Set<Link> redudantLinks = new HashSet<>();

		for (Integer id : linksPerReceivedId.keySet()) {
			if (!linksPerReceivedId.get(id).isEmpty()) { // XXX sarebbe meglio un'asserzione: non ha senso che la lista relativa ad un id sia vuota
				redudantLinks.addAll(linksPerReceivedId.get(id));
				redudantLinks.remove(linksPerReceivedId.get(id).iterator().next());
			}
		}
		
		//TODO gestire caso size = 1

		return redudantLinks;
	}

	private void addIdReceivedOnLink(int id, Link link) {
		if (linksPerReceivedId.containsKey(id)) {
			linksPerReceivedId.get(id).add(link);
		}
		else {
			Set<Link> singleLink = new HashSet<>();
			singleLink.add(link);
			linksPerReceivedId.put(id, singleLink);
		}
	}

	private void selectiveSend(YoyoMessage message, Set<Link> allLinks, Set<Link> linksToIgnore) {
		for (Link link : allLinks) {
			if (!linksToIgnore.contains(link)) {
				node.send(message, link);
			}
		}
	}

	private Set<Link> getLinksById(int id) {
		return new HashSet<Link>(linksPerReceivedId.get(id));
	}

	private Set<Link> getLinksThatSentDifferentId(int id) {
		Set<Link> links = new HashSet<>();
		for (Integer currentId : linksPerReceivedId.keySet()) {
			if (currentId != id) {
				links.addAll(linksPerReceivedId.get(currentId));
			}
		}
		return links;
	}
}
