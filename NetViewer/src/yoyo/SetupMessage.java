package yoyo;

import netViewer.Link;

public class SetupMessage implements YoyoMessage{
	
	private int id;

	public String printString() {
		return "Setup:"+id;
	}

	public int getId() {
		return id;
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		// TODO Auto-generated method stub
		
	}
	
	

}
