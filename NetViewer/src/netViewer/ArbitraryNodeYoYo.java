package netViewer;

import general.Message;
import yoyo.YoyoMessage;
import yoyo.YoyoState;

public class ArbitraryNodeYoYo extends Node {
	
	private YoyoState nodeState;

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	public void become(YoyoState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}
	
	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è difficile far di meglio
		((YoyoMessage) msg).accept(nodeState, link);
	}
	
	public void sendMessage(Message message, Link link) {
		send(message, link);
	}

}
