package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class SetupMessage implements YoyoMessage{
	
	private int id;

	//FIXME: forse è meglio passare direttamente l'id?
	public SetupMessage(ArbitraryNodeYoYo sender) {
		this.id = sender.getNodeId();
	}
	
	public String printString() {
		return "Setup:"+id;
	}

	public int getId() {
		return id;
	}

	@Override
	public void accept(YoyoState state, Link sender) {
		state.handle(this, sender);
	}
	
	

}
