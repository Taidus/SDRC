package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;
import general.State;

public class Awake extends YoyoAbstractState {

	public Awake(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.AWAKE;
	}

	@Override
	public void handle(SetupMessage m, Link sender) {
		node.setupLink(m, sender);
	}
	
	@Override
	public void handle(YoMessage m, Link sender){
		//node.enqueueMessage(m, sender);
	}
}
