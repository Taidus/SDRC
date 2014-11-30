package yoyo;

import java.util.HashMap;
import java.util.Map;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public abstract class YoyoAbstractState implements YoyoState {

	protected ArbitraryNodeYoyo node;
	protected Map<Link, YoMessage> futureYoMessaggeQueue;

	public YoyoAbstractState(ArbitraryNodeYoyo node) {
		this.node = node;
		this.futureYoMessaggeQueue = new HashMap<>();
	}

	protected void chooseState() {
		if (node.getIncomingLinks().isEmpty() && node.getOutgoingLinks().isEmpty()) {
			allLinksPruned();
		}
		else if (node.getIncomingLinks().isEmpty()) {
			Source nextState = new Source(node);
			assert futureYoMessaggeQueue.isEmpty();
			changeState(nextState);
			nextState.sendMessageToOutgoingLinks(new YoMessage(node.getNodeId()));
		}
		else if (node.getOutgoingLinks().isEmpty()) {
			changeState(new Sink(node));
		}
		else {
			changeState(new Internal(node));
		}
	}

	protected void allLinksPruned() {
		changeState(new Follower(node));
	};

	protected void enqueueMessage(YoMessage message, Link sender) {
		futureYoMessaggeQueue.put(sender, message);
	}

	protected void processEnqueuedMessages(YoyoState nextState) {
		for (Link link : futureYoMessaggeQueue.keySet()) {
			futureYoMessaggeQueue.get(link).accept(nextState, link);
		}

		futureYoMessaggeQueue.clear();
	}
	
	@Override
	public void changeState(YoyoState nextState) {
		node.become(nextState);
		processEnqueuedMessages(nextState);
	}

	@Override
	public void spontaneously() {}

	@Override
	public void handle(SetupMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		defaultHandle(sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		defaultHandle(sender);
	}

	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " " + sender;
	}
	
	protected void handleSetupMessage(SetupMessage m, Link sender){
		node.setupLink(m, sender);
		if(node.isSetupCompleted()){
			chooseState();
		}
	}

}
