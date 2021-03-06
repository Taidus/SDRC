package megaMerger;

import general.State;

import java.util.Collection;
import java.util.Set;

import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;

public class FindingMergeEdge extends MegaMergerAbstractState {

	private static class NullLink extends Link {

		public static boolean isNull(Link link) {
			return link instanceof NullLink;
		}

		public NullLink() {
			super();
			super.setCost(Integer.MAX_VALUE);
		}

		@Override
		public void setCost(int cost) {}
	}

	private int minCostFoundByChildren;
	private Link bestChildren, myMinEdge;
	private int countChildrenResponse;
	private boolean edgeScanningDone;

	public FindingMergeEdge(ArbitraryNodeMegaMerger node) {
		super(node);

		this.minCostFoundByChildren = Integer.MAX_VALUE;
		this.countChildrenResponse = 0;
		this.edgeScanningDone = false;
		this.bestChildren = new NullLink();
		this.myMinEdge = new NullLink();

		node.updateInternalLinks();
	}

	@Override
	public int intValue() {
		return State.FINDING_MERGE_EDGE;
	}

	@Override
	public void handle(FoundMessage m, Link sender) {
		if (m.getChildMin() < minCostFoundByChildren) {
			minCostFoundByChildren = m.getChildMin();
			bestChildren = sender;
		}
		countChildrenResponse++;
		checkFound();
	}

	@Override
	public void handle(LetUsMergeMessage m, Link sender) {
		handleMergeRequest(m, sender);
	}

	@Override
	protected void absorb(Link linkToWeakerCity) {
		node.send(new UpdateAndFindMessage(node.getNodeName(), node.getLevel()), linkToWeakerCity);
		node.addChild(linkToWeakerCity);
	}

	@Override
	public void handle(InsideMessage m, Link sender) {
		node.addInternalLink(sender);
		findMinExternalEdge();
	}

	@Override
	public void handle(OutsideMessage m, Link sender) {
		myMinEdge = sender;
		edgeScanningDone = true;
		checkFound();
	}

	@Override
	public void handle(WhereMessage m, Link sender) {
		handleWhere(m, sender);
	}

	@Override
	protected void sameCity(Link sender) {
		if (askingEachOther(sender)) {
			node.addInternalLink(sender);
			findMinExternalEdge();
		}
		else {
			super.sameCity(sender);
		}
	}

	private boolean askingEachOther(Link sender) {
		return sender.equals(node.getWhereQuestionLink());
	}

	public void findMinExternalEdge() {
		Set<Link> nonInternalLinks = node.getNonInternalLinks();
		if (!nonInternalLinks.isEmpty()) {
			node.send(new WhereMessage(node.getNodeName(), node.getLevel()), FindingMergeEdge.getMinCostLink(nonInternalLinks));
		}
		else {
			edgeScanningDone = true;
			checkFound();
		}
	}

	private void checkFound() {
		if (allChildrenResponded() && edgeScanningDone) {

			if (internalSubtree() && node.isDowntown()) {
				node.sendToChildren(new NotifyDoneMessage());
				changeState(new Leader(node));
			}
			else {
				node.setMergePathNextLink(getBestPath());

				Awake newState = (isCandidate()) ? new Candidate(node) : new Awake(node);
				changeState(newState);

				if (!node.isDowntown()) {
					node.sendToParent(new FoundMessage(Math.min(minCostFoundByChildren, myMinEdge.getCost())));
				}
				else {
					newState.handleMergeDelegation();
				}
			}
		}
	}

	private boolean isCandidate() {
		return myMinEdge.getCost() <= minCostFoundByChildren;
	}

	private Link getBestPath() {
		return (isCandidate()) ? myMinEdge : bestChildren;
	}

	private boolean allChildrenResponded() {
		return countChildrenResponse == node.getChildrenNumber();
	}

	private boolean internalSubtree() {
		return NullLink.isNull(bestChildren) && NullLink.isNull(myMinEdge);
	}

	public static Link getMinCostLink(Collection<Link> linkSet) {
		assert !linkSet.isEmpty();
		Link result = new NullLink();
	
		for (Link link : linkSet) {
			if (link.getCost() <= result.getCost()) {
				result = link;
			}
		}
	
		assert !NullLink.isNull(result);
		return result;
	}
}
