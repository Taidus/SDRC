package netViewer;
import java.util.HashMap;
import java.util.Map;

public class RingNodeUniAlternate extends Node {

	private int step;
	private int value;

	private final int trueId; // perché gli id vengono modificati per stampare

	private Map<Integer, String> msgsPerStep;

	private static final int ringDirection = Node.LEFT;
	private static final String msgSeparator = ";";
	private static final String notification = "Leader: ";

	RingNodeUniAlternate(Integer ID) {
		super(ID);
		this.msgsPerStep = new HashMap<>();

		this.trueId = ID.intValue();
	}

	protected void initialize() {
		this.step = 1;
		this.value = this.nodeId;

		NetViewer.out.println("Node " + trueId + " has been initialized");

		become(general.State.CANDIDATE);
		sendCandidateMessage();
	}

	@Override
	protected synchronized void receive(String msg, int dir) {
		if (dir == ringDirection) { // è arrivato un messaggio dalla direzione dell'anello (cioè 'controcorrente')
			throw new IllegalStateException("Received a message from wrong direction");
		}

		if (state != general.State.FOLLOWER && state != general.State.LEADER) {
			if (msg.matches(notification + "\\d+")) {
				int leaderId = Integer.parseInt(msg.split(notification)[1]);
				checkLeaderAndForward(leaderId);
				NetViewer.out.println("\tNode " + trueId + " forwards notification message");
			}
			else {
				if (general.State.ASLEEP == state) {
					asleep(msg);
				}
				else if (general.State.CANDIDATE == state) {
					candidate(msg);
				}
				else if (general.State.PASSIVE == state) {
					sendMessage(msg);
					NetViewer.out.println("\tNode " + trueId + " with value " + this.value + " forwards message \"" + msg + "\"");
				}
			}
		}
	}

	private void checkLeaderAndForward(int leaderId) {
		this.nodeId = trueId;

		if (this.nodeId != leaderId) {
			become(general.State.FOLLOWER);
		}
		else {
			become(general.State.LEADER);
		}
		sendMessage(notification + leaderId);
	}

	private void asleep(String msg) {
		NetViewer.out.println("\tNode " + trueId + " receives message " + msg + " while asleep");
		initialize();
		// if (NetViewer.isFIFO()) {
		// try {
		// Thread.sleep(100); // in modo che aspetti un po' ad inviare il prossimo messaggio. Essendo la coda FIFO, il risultato è
		// // equivalente
		// } catch (InterruptedException e) {
		// }
		// }
		candidate(msg);
	}

	private void candidate(String msg) {
		int[] msgParts = parseMsg(msg);
		int msgValue = msgParts[0];
		int msgStep = msgParts[1];

		if (msgStep > this.step) {
			msgsPerStep.put(msgStep, msg);
			NetViewer.out.println("Node " + trueId + " is at step " + this.step + " and enqueues message " + msg);
		}
		else {
			if (msgStep < this.step) {
				throw new IllegalStateException("Received a message from past");
			}
			processMessage(msgValue, msgStep);
		}
	}

	private void processMessage(int msgValue, int msgStep) {
		if (msgValue == this.value) {
			checkLeaderAndForward(msgValue);
			NetViewer.out.println("\tNode " + trueId + " sends notification message");
		}
		else {
			int msgDir = msgStep % 2;

			if (LEFT == msgDir) {
				if (msgValue > this.value) {
					NetViewer.out.println("Node " + trueId + " with value " + this.value + " receives " + msgValue + msgSeparator + msgStep
							+ " and is defeated.");
					becomePassive();
				}
				else {
					NetViewer.out.println("Node " + trueId + " with value " + this.value + " receives " + msgValue + msgSeparator + msgStep
							+ " and survives.");
					this.value = msgValue;
					nextStep();
				}
			}
			else if (RIGHT == msgDir) {
				if (msgValue < this.value) {
					NetViewer.out.println("Node " + trueId + " with value " + this.value + " receives " + msgValue + msgSeparator + msgStep
							+ " and is defeated.");
					becomePassive();
				}
				else {
					NetViewer.out.println("Node " + trueId + " with value " + this.value + " receives " + msgValue + msgSeparator + msgStep
							+ " and survives.");
					nextStep();
				}
			}
		}
	}

	private void nextStep() {
		this.nodeId = this.value; // per mostrare a video il valore corrente
		this.step++;

		sendCandidateMessage();

		String msg = msgsPerStep.remove(this.step);
		if (null != msg) {
			candidate(msg);
		}
	}

	private void becomePassive() {
		become(general.State.PASSIVE);
		this.nodeId = this.trueId;

		if (!msgsPerStep.isEmpty()) {
			NetViewer.out.print("\tNode " + trueId + " with value " + this.value + " sends all enqueued messages: ");
			for (Integer s : msgsPerStep.keySet()) {
				String curr_msg = msgsPerStep.get(s); // Nota: i messaggi non vengono rimossi. Questo non è un problema, perché il nodo è diventato
														// passivo e non estrarrà nuovamente messaggi dalla coda
				sendMessage(curr_msg);
				NetViewer.out.print(curr_msg + " ");
			}
			NetViewer.out.println();
		}
	}

	private void sendCandidateMessage() {
		String msg = value + msgSeparator + step;
		sendMessage(msg);
		NetViewer.out.println("\tNode " + trueId + " with value " + this.value + " sends message \"" + msg + "\"");
	}

	private void sendMessage(String msg) {
		send(msg, ringDirection);
	}

	private int[] parseMsg(String msg) {
		String[] msgParts = msg.split(msgSeparator);
		int[] parsedMsg = new int[msgParts.length];

		for (int i = 0; i < msgParts.length; i++) {
			parsedMsg[i] = Integer.parseInt(msgParts[i]);
		}

		return parsedMsg;
	}
}
