package yoyo;

import java.util.HashSet;
import java.util.Set;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public abstract class YoyoAbstractState implements YoyoState{
	

	protected ArbitraryNodeYoYo node;

	public YoyoAbstractState(ArbitraryNodeYoYo node) {
		this.node = node;
	}
	
	@Override
	public void handle(SetupMessage m, Link sender){		
		defaultHandle(sender);
}
	
	@Override
	public void handle(YoMessage m, Link sender) {
		defaultHandle(sender);	
	}
	
	@Override
	public void handle(NoMessage m, Link sender) {
		defaultHandle(sender);	
	}
	
	@Override
	public void handle(NoAndPruneMessage m, Link sender) {
		defaultHandle(sender);	
	}
	
	@Override
	public void handle(YesMessage m,Link sender){
		defaultHandle(sender);
	}
	
	@Override
	public void handle(YesAndPruneMessage m, Link sender) {
		defaultHandle(sender);	
	}


	@Override
	public void changeState(YoyoState nextState) {
		node.become(nextState);
	}
	
	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " " + sender;
	}
	
	
public void processYesNo(Link sender, boolean prune){
		if (prune){
			node.pruneOutgoingLink(sender);
		}
		
		if (node.getYesNeighboursSize() + node.getNoNeighboursSize() == node.getNumOfResponsesNeeded()){			
			Set<Link> sendYesLinks;
			Set<Link> sendNoLinks;
			
			if (node.getYesNeighboursSize() == node.getNumOfResponsesNeeded()){
				int minId = node.getMinReceivedValue();
			    sendYesLinks = node.getLinksById(minId);
			    sendNoLinks =  node.linksThatSentDifferentId(minId);		    
			}
			
			else{		
				sendYesLinks=new HashSet<Link>();
				sendNoLinks = node.copyIncomingEdges();
			}
			
			node.flipIncomingLinks(sendNoLinks);
		
			Set<Link> linksToPrune = node.getLinksToPrune();
						
			for(Link l: sendYesLinks){
				if (linksToPrune.contains(l)){
					node.pruneIncomingLink(l);
					node.send(new YesAndPruneMessage(), l); 
				}
				else {
					node.send(new YesMessage(), l);
					
				}				
			}
			
			for(Link l: sendNoLinks){
				if (linksToPrune.contains(l)){
					node.pruneIncomingLink(l);
					node.send(new NoAndPruneMessage(), l);
				}
				else {
					node.send(new NoMessage(), l);
					
				}				
			}
	   
	      node.chooseState();
		}
	}
	
	
//	public Set<Link> processSelectedPruneLinks(){
//		Set<Link> linksToPrune = new HashSet<>();
//		Set<Integer> id = node.getKeysOfLinksPerSentId();
//	
//		for(Integer currentId : id){
//			Set<Link> currentLinks = node.getLinksById(currentId);
//			if (currentLinks.size()>1){
//				//TODO : rimuovere un elemento random
//			}
//			linksToPrune.add(currentLinks);
//		}
//		
//		//TODO : continuare (devo andare a cenare che ho fameeeeeeeeeeeeeeeeeeeeeeeeeeee)
//		return null;
//	}
}
