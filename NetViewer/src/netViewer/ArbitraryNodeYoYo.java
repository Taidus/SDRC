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
	private Set<Link> ingoingEdges;

	private Set<Link> yes_neighbours;
	private Set<Link> no_neighbours;
	private Set<Link> receivedIDs;

	private Map<Integer, Set<Link>> linksThatSentThatId;
	private int minReceivedValue;
	
	int num_of_responses_needed;

	public ArbitraryNodeYoYo(Integer ID) {
		super(ID);

		nodeState = new Asleep(this);
		pruned_ingoing = false;
		outgoingEdges = new HashSet<>();
		ingoingEdges = new HashSet<>();

		yes_neighbours = new HashSet<>();
		no_neighbours = new HashSet<>();
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
			ingoingEdges.add(sender);
		if (outgoingEdges.size() + ingoingEdges.size() == getLinks().size())
			chooseState();
	}

	public void chooseState() {
		if(ingoingEdges.size() + outgoingEdges.size() == 0) {
			if(!pruned_ingoing) {
				become(new Leader(this));
			} else {
				become(new Follower(this));
			}
		} else if(ingoingEdges.isEmpty()) {
			become(new Source(this));
			sendMessageToOutgoingEdges(new YoMessage(getNodeId()));
		} else if(outgoingEdges.isEmpty()) {
			become(new Sink(this));
		} else {
			become(new InternalNode(this));
		}
	}
	
	private void sendMessageToOutgoingEdges(Message toSend) {
		for(Link toSendTo:outgoingEdges) {
			send(toSend, toSendTo);
		}
	}
}
