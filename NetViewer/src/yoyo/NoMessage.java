package yoyo;

import netViewer.Link;

public class NoMessage implements YoyoMessage{

	private boolean toPrune;
	@Override
	public String printString() {
		return "Yes: toPrune = "+toPrune;
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isToPrune() {
		return toPrune;
	}

}
