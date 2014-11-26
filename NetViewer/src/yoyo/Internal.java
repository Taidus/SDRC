package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;



public class Internal extends YoyoAbstractState{
	
	public Internal(ArbitraryNodeYoYo node) {
		super(node);
	}
	
	@Override
	public int intValue() {
		return State.INTERNAL;
	}
	
	@Override
	public void handle(YoMessage m, Link sender) {
		node.addElementToMap(m.getId(),sender);
		if (m.getId() < node.getMinReceivedValue()){
			node.setMinReceivedValue(m.getId());
		}
		if (node.idReceivedFromAllLinks()) {
			node.sendMessageToOutgoingEdges( new YoMessage(node.getMinReceivedValue()));
		}
	}
	
	@Override
	public void handle(NoMessage m, Link sender) {
		node.addNoNeighbours(sender);
	}
	
	@Override
	public void handle(NoAndPruneMessage m, Link sender) {
		node.addNoNeighbours(sender);
		node.pruneIncomingLink(sender);
	}
	
	@Override
	public void handle(YesMessage m, Link sender) {
		node.addYesNeighbours(sender);
	}
	
	@Override
	public void handle(YesAndPruneMessage m, Link sender) {
		node.addYesNeighbours(sender);
		node.pruneIncomingLink(sender);
	}
	
	
	

	
}
