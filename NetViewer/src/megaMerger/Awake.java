package megaMerger;

import general.State;
import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;

public class Awake extends MegaMergerAbstractState {

	public Awake(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.AWAKE;
	}

	@Override
	public void handle(MakeMergeRequestMessage m, Link sender) {
		handleMergeDelegation();
	}

	@Override
	public void handle(WhereMessage m, Link sender) {
		handleWhere(m, sender);
	}

	@Override
	public void handle(LetUsMergeMessage m, Link sender) {
		handleMergeRequest(m, sender);
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
	public void handle(NotifyDoneMessage m, Link sender) {
		node.sendToChildren(m);
		changeState(new Follower(node));
		sender.markAsSurvived();
	}
	

	protected void handleMergeDelegation() {
		node.delegateMergeRequest();
	}

}
