package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class YesMessage extends ResponseMessage implements YoyoMessage {
	
	@Override
	public String printString() {
		return "Yes";
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
		
	}

	@Override
	public void prune(ArbitraryNodeYoyo node, Link responseLink){
		
	}
}
