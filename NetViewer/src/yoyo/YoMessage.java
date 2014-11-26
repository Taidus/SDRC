package yoyo;

import netViewer.Link;

public class YoMessage implements YoyoMessage{

	private int id;
	
	public String printString() {
		return "Yo:"+id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
		
	}
}
