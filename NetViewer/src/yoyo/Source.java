package yoyo;

import java.util.HashSet;
import java.util.Set;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Source extends YoyoAbstractState{

	public Source(ArbitraryNodeYoYo node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void handle(NoMessage m, Link sender){
		
		m.prune(node,sender);
		node.addNoNeighbours(sender);
		processYesNo(sender);
	}
	
	@Override
	public void handle(YesMessage m, Link sender){
		
		m.prune(node,sender); 
		node.addYesNeighbours(sender);
		processYesNo(sender);
	}
	
		    	

}
