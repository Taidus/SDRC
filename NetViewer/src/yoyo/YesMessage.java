package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class YesMessage implements YoyoMessage {
	
	@Override
	public String printString() {
		return "Yes";
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
		
	}

	public void prune(ArbitraryNodeYoYo node, Link sender){
		
	}
}
