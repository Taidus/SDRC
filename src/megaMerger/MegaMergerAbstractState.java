package megaMerger;

import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;

public abstract class MegaMergerAbstractState implements MegaMergerState{

	protected ArbitraryNodeMegaMerger node;

	public MegaMergerAbstractState(ArbitraryNodeMegaMerger node) {
		this.node = node;
	}

	@Override
	public void changeState(MegaMergerState nextState) {
		node.become(nextState);
	}

	protected void handleWhere(WhereMessage m, Link sender){
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
	
	protected void handleMergeRequest(LetUsMergeMessage m, Link sender){
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
	
	protected void mergeRequestBySameLevelCity(LetUsMergeMessage request, Link sender){
		node.suspendRequest(request, sender);
	}
	
	protected void sameCity(Link sender){
		node.addInternalEdge(sender);
		node.sendMessage(new InsideMessage(), sender);
	}
	
	protected void differentCity(Link sender){
		node.sendMessage(new OutsideMessage(), sender);
	}
	
	protected void absorb(Link sender){
		node.sendMessage(new UpdateMessage(node.getNodeName(), node.getLevel()), sender);
		node.addChild(sender);
	}
	
	@Override
	public String toString() {
		return "State: " + getClass().getName().toLowerCase();
	}
}
