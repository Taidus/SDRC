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
			
	}
	
	@Override
	public void handle(NoMessage m, Link sender) {
		
	}
	
	@Override
	public void handle(YesMessage m, Link sender) {
		
	}

	
}
