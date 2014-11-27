package megaMerger;

import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;

public abstract class MegaMergerAbstractState implements MegaMergerState {

	protected ArbitraryNodeMegaMerger node;

	public MegaMergerAbstractState(ArbitraryNodeMegaMerger node) {
		this.node = node;
	}

	@Override
	public void changeState(MegaMergerState nextState) {
		node.become(nextState);
	}
	
	@Override
	public void spontaneously() {}

	protected void handleWhere(WhereMessage m, Link sender) {
		if (node.getNodeName().equals(m.getName())) {
			sameCity(sender);
		}
		else if (node.getLevel() >= m.getLevel()) {
			differentCity(sender);
		}
		else {
			node.suspendQuestion(m, sender);
		}
	}

	protected void handleMergeRequest(LetUsMergeMessage m, Link sender) {
		if (!node.isRequestLate(sender)) {
			if (m.getLevel() < node.getLevel()) {
				absorb(sender);
			}
			else if (m.getLevel() == node.getLevel()) {
				mergeRequestBySameLevelCity(m, sender);
			}
			else {
				node.suspendRequest(m, sender);
			}
		}
	}

	protected void mergeRequestBySameLevelCity(LetUsMergeMessage request, Link sender) {
		node.suspendRequest(request, sender);
	}

	protected void sameCity(Link sender) {
		node.addInternalLink(sender);
		node.send(new InsideMessage(), sender);
	}

	protected void differentCity(Link sender) {
		node.send(new OutsideMessage(), sender);
	}

	protected void absorb(Link linkToWeakerCity) {
		node.send(new UpdateMessage(node.getNodeName(), node.getLevel()), linkToWeakerCity);
		node.addChild(linkToWeakerCity);
	}

	@Override
	public String toString() {
		return "State: " + getClass().getName().toLowerCase();
	}

	@Override
	public void handle(FoundMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(InsideMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(LetUsMergeMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(MakeMergeRequestMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(NotifyDoneMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(OutsideMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(UpdateAndFindMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(UpdateMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(WhereMessage m, Link sender) {
		defaultHandle(sender);
	}

	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " " + sender;
	}
}