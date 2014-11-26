package netViewer;

import general.Message;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import yoyo.Asleep;
import yoyo.SetupMessage;
import yoyo.YoMessage;
import yoyo.YoyoMessage;
import yoyo.YoyoState;

public class ArbitraryNodeYoYo extends Node {

	private YoyoState nodeState;
	private boolean pruned_ingoing;
	private Set<Link> outgoingEdges;
	private Set<Link> incomingEdges;

	private Set<Link> yesNeighbours;
	private Set<Link> noNeighbours;
	private Set<Link> receivedIDs;

	private Map<Integer, Set<Link>> linksThatSentThatId;
	private int minReceivedValue;
	
	int num_of_responses_needed;

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);

		nodeState = new Asleep(this);
		pruned_ingoing = false;
		outgoingEdges = new HashSet<>();
		incomingEdges = new HashSet<>();

		yesNeighbours = new HashSet<>();
		noNeighbours = new HashSet<>();
		receivedIDs = new HashSet<>();

		num_of_responses_needed = 0;
	}

	public void become(YoyoState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((YoyoMessage) msg).accept(nodeState, link);
	}

	public void sendMessage(Message message, Link link) {
		send(message, link);
	}

	public void setupEdge(SetupMessage m, Link sender) {
		if (m.getId() > getNodeId())
			outgoingEdges.add(sender);
		else
			incomingEdges.add(sender);
		if (outgoingEdges.size() + incomingEdges.size() == getLinks().size())
			chooseState();
	}

	public void chooseState() {
		if(incomingEdges.size() + outgoingEdges.size() == 0) {
			if(!pruned_ingoing) {
				become(new Leader(this));
			} else {
				become(new Follower(this));
			}
		} else if(incomingEdges.isEmpty()) {
			become(new Source(this));
			sendMessageToOutgoingEdges(new YoMessage(getNodeId()));
		} else if(outgoingEdges.isEmpty()) {
			become(new Sink(this));
		} else {
			become(new InternalNode(this));
		}
	}
	
	public void addOutgoingEdge(Link toAdd) {
		outgoingEdges.add(toAdd);
	}
	
	public void addIncomingEdge(Link toAdd) {
		incomingEdges.add(toAdd);
	}	
	
	public void addYesNeighbours(Link toAdd) {
		yesNeighbours.add(toAdd);
	}
	
	public void addNoNeighbours(Link toAdd) {
		noNeighbours.add(toAdd);
	}
	
	public void removeOutgoingEdge(Link toRemove) {
		outgoingEdges.remove(toRemove);
	}
	
	public void removeIncomingEdge(Link toRemove) {
		incomingEdges.remove(toRemove);
	}

	public void removeSetOfIncomingEdges(Set<Link> toRemove) {
		incomingEdges.removeAll(toRemove);
	}
	
	public void removeSetOfOutgoingEdges(Set<Link> toRemove) {
		outgoingEdges.removeAll(toRemove);
	}
	
	public void receivedIdOn(Link link, int id) {
		linksThatSentThatId.get(id).add(link);
	}
	
	public void clearIdMap() {
		linksThatSentThatId.clear();
	}
	
	//FIXME: cambiare nome
	public boolean idReceivedFromAllLinks() {
		int size = 0;
		for(Set<Link> toCompute : linksThatSentThatId.values()) {
			size += toCompute.size();
		}
		return incomingEdges.size() == size;
	}
	
	private void sendMessageToOutgoingEdges(Message toSend) {
		for(Link toSendTo:outgoingEdges) {
			send(toSend, toSendTo);
		}
	}
	
	public void sendMessageToAllIdLinks(Message toSend, int id) {
		for(Link toSendTo : linksThatSentThatId.get(id)) {
			send(toSend, toSendTo);
		}
	}
}
