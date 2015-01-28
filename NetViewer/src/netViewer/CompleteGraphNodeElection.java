package netViewer;

import general.Message;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class CompleteGraphNodeElection extends Node {

	private InternalState state;
	private int stage;
	private int value;
	private Link owner;
	private Link attack;
	private int ownerStage;
	private Enumeration others;
	private boolean close;
	private ArrayList<QueuedMessage> queue;

	CompleteGraphNodeElection(Integer ID) {
		super(ID);
		stage = 0;
		close = false;
		queue = new ArrayList<QueuedMessage>();
		state = new AsleepState();
	}

	private abstract class InternalState {

		public abstract void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value);

		public abstract boolean isFinalState();
	}

	private class AsleepState extends InternalState {

		public AsleepState() {
			become(general.State.ASLEEP);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {
			
			switch (msg) {

			case "Capture":
			// Receiving Capture Message
			CompleteGraphNodeElection.this.stage = 1;
			owner = linkMsgArrivedOn;
			ownerStage = stage + 1;
			state = new CapturedState();
			NetViewer.out.println("Node " + getNodeId()
					+ " received Capture from "
					+ getNeighbourId(linkMsgArrivedOn) + " and sent Accept");
			NetViewer.out.println("Node " + getNodeId() + " become CAPTURED");
			send("Accept", linkMsgArrivedOn, stage, value);
			
			case "Terminate":
				become(general.State.FOLLOWER);
			}


		}

		@Override
		public boolean isFinalState() {
			return false;
		}

	}

	private class CandidateState extends InternalState {

		CandidateState() {
			stage = 1;
			become(general.State.CANDIDATE);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {

			int currentStage = CompleteGraphNodeElection.this.stage;
			int currentValue = CompleteGraphNodeElection.this.value;

			switch (msg) {

			case "Capture":
				// System.out.println("I am candidate and i am being captured: my stage is:"
				// +currentStage+ "attack stage: "+stage);
				if ((stage < currentStage)
						|| (stage == currentStage && value > currentValue)) {
					send("Reject", linkMsgArrivedOn, currentStage, 0);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Reject");
				} else {
					
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Accept");
					owner = linkMsgArrivedOn;
					ownerStage = stage + 1;
					state = new CapturedState();
					NetViewer.out.println("Node " + getNodeId()
							+ " become CAPTURED");
					send("Accept", linkMsgArrivedOn, stage, value);
				}
				break;

			case "Accept":
				NetViewer.out.println();
				CompleteGraphNodeElection.this.stage++;
				// System.out.println("Stage: "+CompleteGraphNodeElection.this.stage);
				// System.out.println("ids: "+ids.size());
				if (CompleteGraphNodeElection.this.stage >= (1 + ids.size() / 2)) {
					Enumeration allLinks = links.elements();
					state = new LeaderState();
					while (allLinks.hasMoreElements())
						send("Terminate", (Link) allLinks.nextElement(), 0, 0);

					NetViewer.out
							.println("Node "
									+ getNodeId()
									+ " received Accept from "
									+ getNeighbourId(linkMsgArrivedOn)
									+ ", terminated and sent terminate in all directions.");
					
					NetViewer.out.println("Node " + getNodeId()
							+ " become LEADER");
				} else {
					if (others.hasMoreElements()) {
						Link next = (Link) others.nextElement();
						send("Capture", next,
								CompleteGraphNodeElection.this.stage,
								currentValue);
						NetViewer.out.println("Node " + getNodeId()
								+ " received Accept from "
								+ getNeighbourId(linkMsgArrivedOn)
								+ " and sent capture to "
								+ getNeighbourId(next));
					}
				}
				break;

			case "Reject":
				state = new PassiveState();
				NetViewer.out.println("Node " + getNodeId()
						+ " received Reject from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and become PASSIVE");
				break;

			case "Terminate":
				state = new FollowerState();
				NetViewer.out.println("Node " + getNodeId()
						+ " received Terminate from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and become FOLLOWER");
				break;

			case "Warning":
				if ((stage < currentStage)
						|| (stage == currentStage && value > currentValue)) {
					send("No", linkMsgArrivedOn, currentStage, 0);
					NetViewer.out
							.println("Node " + getNodeId()
									+ " received Warning from "
									+ getNeighbourId(linkMsgArrivedOn)
									+ " and sent No");
				} else {
					
					NetViewer.out.println("Node " + getNodeId()
							+ " received Warning from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Yes");
					state = new PassiveState();
					NetViewer.out.println("Node " + getNodeId()
							+ " become PASSIVE");
					send("Yes", linkMsgArrivedOn, stage, 0);
				}
				break;

			}
		}

		@Override
		public boolean isFinalState() {
			return false;
		}

	}

	private class CapturedState extends InternalState {

		public CapturedState() {
			become(general.State.CAPTURED);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {
			if ((close == false)
					|| (close == true && linkMsgArrivedOn == owner)) {
				_process(msg, linkMsgArrivedOn, stage, value);

				/*
				 * Se close=true vuol dire che il nodo deve mettere in coda
				 * tutti i messaggi che non arrivano da owner. Quado questo
				 * arriva rimetter� nuovamente close=true
				 */

				Iterator<QueuedMessage> it = queue.iterator();
				while (it.hasNext() && close == false)// se ci sono elementi
														// nella
														// coda e li possiamo
														// gestire
				{
					QueuedMessage q = it.next();
					NetViewer.out.println("Node " + getNodeId()
							+ " processed queue message from "
							+ getNeighbourId(linkMsgArrivedOn));
					_process(q.getMsg(), q.getLink(), q.getStage(),
							q.getValue());
					it.remove();
				}
			} else {
				// salvo i messaggi nella coda
				queue.add(new QueuedMessage(msg, linkMsgArrivedOn, stage, value));
				NetViewer.out.println("Node " + getNodeId()
						+ " received message from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and put it on queue");

			}
		}

		private void _process(String msg, Link linkMsgArrivedOn, int stage,
				int value) {
			switch (msg) {

			case "Capture":

				// System.out.println("I am CAPTURED : my stage is:"+CompleteGraphNodeElection.this.stage+" and i am being attacked by stage: "+stage);
				if (stage < ownerStage) {
					send("Reject", linkMsgArrivedOn, ownerStage, 0);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Reject");
				} else {
					attack = linkMsgArrivedOn;
					send("Warning", owner, stage, value);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Warning to " + getNeighbourId(owner)
							+ "(owner)");
					close = true;// close N(x)-{owner}
				}
				break;

			case "No":
				close = false;// open N(x)
				send("Reject", attack, stage, 0);
				NetViewer.out.println("Node " + getNodeId()
						+ " received No from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and sent Reject to " + attack + "(attack)");
				break;

			case "Yes":
				ownerStage = stage + 1;
				owner = attack;
				close = false;// open N(x)
				send("Accept", attack, stage, value);
				NetViewer.out.println("Node " + getNodeId()
						+ " received Yes from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and sent Accept to " + attack + "(attack)");

			case "Warning":
				if (stage < ownerStage) {
					send("No", linkMsgArrivedOn, ownerStage, 0);
					NetViewer.out
							.println("Node " + getNodeId()
									+ " received Warning from "
									+ getNeighbourId(linkMsgArrivedOn)
									+ " and sent No");
				} else {
					send("Yes", linkMsgArrivedOn, stage, 0);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Warning from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Yes");
				}
				break;

			case "Terminate":
				state = new FollowerState();
				NetViewer.out.println("Node " + getNodeId()
						+ " received Terminate from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and become FOLLOWER");
				break;
			}

		}

		@Override
		public boolean isFinalState() {
			return false;
		}

	}

	private class PassiveState extends InternalState {

		public PassiveState() {
			become(general.State.PASSIVE);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {

			int currentStage = CompleteGraphNodeElection.this.stage;
			int currentValue = CompleteGraphNodeElection.this.value;
			switch (msg) {

			case "Capture":

				// System.out.println("I am Passive with stage: "+currentStage+"and i am aatacked by stage: "+stage);
				if ((stage < currentStage)
						|| (stage == currentStage && value > currentValue)) {
					send("Reject", linkMsgArrivedOn, currentStage, 0);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Reject");
				} else {
					
					NetViewer.out.println("Node " + getNodeId()
							+ " received Capture from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Accept");
					owner = linkMsgArrivedOn;
					ownerStage = stage + 1;
					state = new CapturedState();
					NetViewer.out.println("Node " + getNodeId()
							+ " become CAPTURED");
					send("Accept", linkMsgArrivedOn, stage, value);
				}
				break;

			case "Warning":
				if ((stage < currentStage)
						|| (stage == currentStage && value > currentValue)) {
					send("No", linkMsgArrivedOn, currentStage, 0);
					NetViewer.out
							.println("Node " + getNodeId()
									+ " received Warning from "
									+ getNeighbourId(linkMsgArrivedOn)
									+ " and sent No");
				} else {
					send("Yes", linkMsgArrivedOn, stage, 0);
					NetViewer.out.println("Node " + getNodeId()
							+ " received Warning from "
							+ getNeighbourId(linkMsgArrivedOn)
							+ " and sent Yes");
				}
				break;

			case "Terminate":
				state = new FollowerState();
				NetViewer.out.println("Node " + getNodeId()
						+ " received Terminate from "
						+ getNeighbourId(linkMsgArrivedOn)
						+ " and become FOLLOWER");
				break;

			}

		}

		@Override
		public boolean isFinalState() {
			return false;
		}
	}

	private class LeaderState extends InternalState {

		public LeaderState() {
			become(general.State.LEADER);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {
			// DO NOTHING!
		}

		@Override
		public boolean isFinalState() {
			return true;
		}
	}

	private class FollowerState extends InternalState {

		public FollowerState() {
			become(general.State.FOLLOWER);
		}

		@Override
		public void processMessage(String msg, Link linkMsgArrivedOn,
				int stage, int value) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isFinalState() {
			// DO NOTHING!
			return true;
		}
	}

	protected void send(String msg, Link link, int stage, int value) {

		Message m = new CompleteElectMessage(msg,stage,value);
		send(m,link);
	}
	
	
	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		
		CompleteElectMessage m = (CompleteElectMessage) msg; 
		
		state.processMessage(m.getMsg(), link, m.getStage(), m.getValue());
	}

	protected void initialize() {
		value = getNodeId();
		others = links.elements();
		state = new CandidateState();
		if (others.hasMoreElements()) {
			Link next = (Link) others.nextElement();
			send("Capture", next, stage, value);
			NetViewer.out.println("Node " + getNodeId()
					+ " initialized. Sent Capture to " + getNeighbourId(next));
		}

		NetViewer.out.println("Node " + getNodeId() + " become CANDIDATE");
	}

	private int getNeighbourId(Link link) {
		int potentialId = link.getNode(0).getNodeId();
		if (potentialId != getNodeId()) {
			return potentialId;
		}
		return link.getNode(1).getNodeId();
	}

	private static class CompleteElectMessage implements Message {
		String msg;
		int stage;
		int value;

		public CompleteElectMessage(String msg, int stage, int value) {
			super();
			this.msg = msg;
			this.stage = stage;
			this.value = value;
		}

		public String getMsg() {
			return msg;
		}

		public int getStage() {
			return stage;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String printString() {
			return msg;
			// TODO
		}

	}

	private static class QueuedMessage {

		private String msg;
		private Link link;
		private int stage;
		private int value;

		/*
		 * Constructor
		 */
		public QueuedMessage(String msg, Link linkMsgArrivedOn, int stage, int value) {
			this.msg = msg;
			link = linkMsgArrivedOn;
			this.stage = stage;
			this.value = value;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Link getLink() {
			return link;
		}

		public void setLink(Link link) {
			this.link = link;
		}

		public int getStage() {
			return stage;
		}

		public void setStage(int stage) {
			this.stage = stage;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

	}

}
