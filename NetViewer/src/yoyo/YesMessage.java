package yoyo;

import netViewer.ArbitraryNodeYoYo;
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
	public void prune(ArbitraryNodeYoYo node, Link responseLink){
		
	}
}
