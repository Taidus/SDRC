package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Asleep extends YoyoAbstractState {

	public Asleep(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.ASLEEP;
	}
	
	@Override
	public void handle(SetupMessage m, Link sender){
		node.yoyoInitialize();
		node.setupLink(m, sender);
	}
	
	@Override
	public void spontaneously() {
		node.yoyoInitialize();
	}

}
