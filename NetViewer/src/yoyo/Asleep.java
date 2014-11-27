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
		//TODO implementare
	}
	
	@Override
	public void spontaneously() {
		//TODO implementare
	}

}
