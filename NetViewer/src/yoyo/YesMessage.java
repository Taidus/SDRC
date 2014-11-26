package yoyo;

import netViewer.Link;

public class YesMessage implements YoyoMessage {

	private boolean toPrune;
	
	@Override
	public String printString() {
		return "Yes: toPrune = "+toPrune;
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
		
	}

	public boolean isToPrune() {
		return toPrune;
	}

	
}
