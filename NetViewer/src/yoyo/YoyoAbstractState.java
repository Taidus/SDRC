package yoyo;

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
	public void changeState(YoyoState nextState) {
		node.become(nextState);
	}
	
	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " " + sender;
	}
}
