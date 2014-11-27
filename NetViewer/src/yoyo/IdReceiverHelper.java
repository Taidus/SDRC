package yoyo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class IdReceiverHelper {

	private ArbitraryNodeYoyo node;
	private IdReceiver owner;

	private Map<Integer, Set<Link>> linksPerReceivedId;
	private int minReceivedId;

	public IdReceiverHelper(ArbitraryNodeYoyo node, IdReceiver owner) {
		this.node = node;
		this.owner = owner;

		linksPerReceivedId = new HashMap<>();
		minReceivedId = Integer.MAX_VALUE;
		
	}

	public int getMinReceivedId() {
		return minReceivedId;
	}

	public void handleYoMessage(YoMessage m, Link sender) {
		addIdReceivedOnLink(m.getId(), sender);
		if (m.getId() < minReceivedId) {
			this.minReceivedId = m.getId();
		}
		if (idReceivedFromAllLinks()) {
			System.out.println("Receved from all Links id: " + node.getNodeId());
			owner.whenReceivedIdOnAllLinks();
		}
	}

	public void respondToAll() {
		Set<Link> sendNoLinks = node.getIncomingLinks();

		Set<Link> linksToPrune = getLinksToPrune();

		if (owner.hasYesToBeSent()) {
			sendNoLinks = getLinksThatSentDifferentId(minReceivedId);

			Set<Link> sendYesLinks = linksPerReceivedId.get(minReceivedId);
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

	private boolean idReceivedFromAllLinks() {
		int size = 0;
		for (Set<Link> toCompute : linksPerReceivedId.values()) {
			size += toCompute.size();
		}
		assert node.getIncomingLinks().size() >= size;
		return node.getIncomingLinks().size() == size;
	}

	private Set<Link> getLinksToPrune() {

		Set<Link> notToPruneLinks = new HashSet<Link>();
		for (Integer id : linksPerReceivedId.keySet()) {
			assert !linksPerReceivedId.get(id).isEmpty();
			notToPruneLinks.add(linksPerReceivedId.get(id).iterator().next());
		}
		if (node.getOutgoingLinks().size() == 0 && notToPruneLinks.size() == 1) {
			notToPruneLinks.clear();
		}

		Set<Link> toPruneLinks = node.getIncomingLinks();
		toPruneLinks.removeAll(notToPruneLinks);
		return toPruneLinks;

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
