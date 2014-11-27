package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class NoMessage extends ResponseMessage implements YoyoMessage{

	@Override
	public String printString() {
		return "No";
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
		
	}
	
	@Override
	public void prune(ArbitraryNodeYoyo node, Link responseLink) {
		
	}

}
