package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Asleep extends YoyoAbstractState {

	public Asleep(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void handle(SetupMessage m, Link sender){
		
	}

}
