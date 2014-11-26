package netViewer;

import general.Message;

import java.util.HashSet;
import java.util.Set;

import yoyo.Asleep;
import yoyo.YoyoMessage;
import yoyo.YoyoState;

public class ArbitraryNodeYoYo extends Node {
	
	private YoyoState nodeState;
	private boolean pruned_ingoing;
	Set<Link> outgoingEdges;
	Set<Link> ingoingEdges; 
	
	Set<Link> yes_neighbours; 
	Set<Link> 	no_neighbours; 
	Set<Link> receivedIDs; 
	
	int num_of_responses_needed;
	

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);
		
		nodeState=new Asleep(this);
		pruned_ingoing=false;
		outgoingEdges = new HashSet<>();
		ingoingEdges = new HashSet<>();
		
		yes_neighbours = new HashSet<>();
		no_neighbours = new HashSet<>();
		receivedIDs = new HashSet<>();
		
		num_of_responses_needed=0;
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
