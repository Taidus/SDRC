package netViewer;

/*
 * NetViewer
 *
 * Network: Ring
 * Algorithm: As Far As It Can (Chang & Roberts)
 *
 * Description:
 *
 */

import general.Message;
import general.StringMessage;

import java.util.Collections;

class RingNodeFarAsCan extends Node {

	RingNodeFarAsCan(Integer ID) {
		super(ID);
	}

	private void send(String str, int dir) {
		Message m = new StringMessage(str);
		send(m, dir);
	}

	public synchronized void receive(Message m, int dir) {
		StringMessage sm = (StringMessage) m;
		receive(sm.getMsg(), dir);
	}

	/*
	 * Receive a message. Dispatch to correct method depending on state.
	 */
	private void receive(String msg, int dir) {
		NetViewer.out.println("Node " + nodeId + " received message " + msg
				+ " on the " + ((dir == 0) ? ("RIGHT") : ("LEFT")) + ".");
		switch (state) {
		case general.State.ASLEEP:
			asleep(msg, dir);
			break;
		case general.State.CANDIDATE:
			candidate(msg, dir);
			break;
		case general.State.PASSIVE:
			passive(msg, dir);
			break;
		}
	}

	/*
	 * Process message received while state = ASLEEP. dir is the direction from
	 * which the message arrived.
	 */
	private void asleep(String msg, int dir) {
		initialize();
		int msgInt = Integer.parseInt(msg);
		if (nodeId < msgInt) {
			// do nothing; remain candidate
			// We don't need to resend id; a node only ever sends its ID once in
			// this algorithm.
			NetViewer.out.println("Node " + nodeId
					+ " has been woken up, remained candidate, defeated " + msg
					+ ".");
		} else /* msg < id */{
			become(general.State.PASSIVE);
			send(msg, Math.abs(dir - 1)); // send in opposite direction
			NetViewer.out.println("Node " + nodeId
					+ " has been woken up, become passive, and sent msg " + msg
					+ " to the "
					+ ((Math.abs(dir - 1) == 0) ? ("RIGHT") : ("LEFT")) + ".");
		}
	}

	/*
	 * Process message received while state = CANDIDATE. dir is the direction
	 * from which the message arrived.
	 */
	private void candidate(String msg, int dir) {
		int msgInt = Integer.parseInt(msg);
		if (nodeId < msgInt) {
			// do nothing; remain candidate
			NetViewer.out.println("Node " + nodeId
					+ " remains candidate, defeats " + msg);
		} else if (msgInt < nodeId) {
			become(general.State.PASSIVE);
			send(msg, Math.abs(dir - 1)); // send in opposite direction
			NetViewer.out.println("Node " + nodeId
					+ " defeated. Became passive and forwarded " + msg
					+ " to the "
					+ ((Math.abs(dir - 1) == 0) ? ("RIGHT") : ("LEFT")) + ".");
		} else { /* received id */
			become(general.State.LEADER);
			send("notification", Math.abs(dir - 1));
			NetViewer.out.println("**** LEADER FOUND **** Node " + nodeId
					+ ". Sent notification.");
		}
	}

	/*
	 * Process message received while state = PASSIVE. dir is the direction from
	 * which the message arrived.
	 */
	private void passive(String msg, int dir) {
		if (msg == "notification")
			become(general.State.FOLLOWER);
		send(msg, Math.abs(dir - 1)); // send in opposite direction
		NetViewer.out.println("Passive node " + nodeId + " forwarded message "
				+ msg + " to the "
				+ ((Math.abs(dir - 1) == 0) ? ("RIGHT") : ("LEFT")) + ".");
	}

	/*
	 * Initialization sequence.
	 */
	protected void initialize() {
		become(general.State.CANDIDATE);
		send(idString(), RIGHT);
		NetViewer.out.println("Node " + nodeId
				+ " initialized. ID sent to the right.");
	}

	/*------------- CASES (best/worst/average) -------------*/

	/*
	 * Arrange ids into average case configuration (randomize).
	 */
	public static boolean average() {
		Collections.shuffle(ids); // randomize
		return true;
	}

	/*
	 * Arrange ids into best case configuration (decreasing around ring).
	 */
	public static boolean best() {
		Collections.sort(ids);
		Collections.reverse(ids); // decreasing around ring
		NetViewer.setSynchronous(true); // makes it easier to follow animation
		NetViewer.setInstantWakeUp(true); // makes it easier to follow animation
		return true;
	}

	/*
	 * Arrange ids into worst case configuration (increasing around ring).
	 */
	public static boolean worst() {
		Collections.sort(ids); // increasing around ring
		NetViewer.setSynchronous(true); // makes it easier to follow animation
		NetViewer.setInstantWakeUp(true); // makes it easier to follow animation
		return true;
	}
}
