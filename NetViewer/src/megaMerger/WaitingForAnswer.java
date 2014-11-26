package megaMerger;

import general.State;
import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;

public class WaitingForAnswer extends MegaMergerAbstractState {

	public WaitingForAnswer(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	public void checkForPreviousRequests() {
		if (node.previousFriendlyMergeRequestOnMergeEdgeFound()) {
			acceptFriendlyMerge(node.processSuspendedRequest(node.getMergePathNextEdge()).getId());
		}
	}

	@Override
	public int intValue() {
		return State.WAITING_FOR_ANSWER;
	}

	private void acceptFriendlyMerge(int otherCityNode_id) {
		if (newDowntown(otherCityNode_id)) {
			node.addChild(node.getMergePathNextEdge());
			node.broadcastUpdateAndFind(node.getMergePathNextEdge().getName(), node.getLevel() + 1, null);
		}
	}

	private boolean newDowntown(int otherCityNode_id) {
		return node.getNodeId() < otherCityNode_id;
	}

	@Override
	public void handle(WhereMessage m, Link sender) {
		handleWhere(m, sender);
	}

	@Override
	public void handle(UpdateMessage m, Link sender) {
		node.broadcastUpdate(m.getName(), m.getLevel(), sender);
	}

	@Override
	public void handle(UpdateAndFindMessage m, Link sender) {
		node.broadcastUpdateAndFind(m.getName(), m.getLevel(), sender);
	}

	@Override
	public void handle(LetUsMergeMessage m, Link sender) {
		handleMergeRequest(m, sender);
	}

	@Override
	protected void mergeRequestBySameLevelCity(LetUsMergeMessage request, Link sender) {
		if (mergeRequestOnSameLink(sender)) {
			acceptFriendlyMerge(request.getId());
		}
		else {
			super.mergeRequestBySameLevelCity(request, sender);
		}
	}

	private boolean mergeRequestOnSameLink(Link link) {
		return link.equals(node.getMergePathNextEdge());
	}
}
