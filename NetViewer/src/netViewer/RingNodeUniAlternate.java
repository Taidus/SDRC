package netViewer;

import general.Message;

import java.util.Collection;
import java.util.HashMap;

import uniAlternate.Asleep;
import uniAlternate.Candidate;
import uniAlternate.ElectionMessage;
import uniAlternate.UniAlternateMessage;
import uniAlternate.UniAlternateState;

public class RingNodeUniAlternate extends Node {

	private static final Direction defaultDir = Direction.RIGHT;
	private int step;
	private int value;
	private UniAlternateState state;
	private HashMap<Integer, ElectionMessage> buffer; // Coda di messaggi
														// utilizzata quando la
														// rete non è FIFO

	RingNodeUniAlternate(Integer ID) {
		super(ID);
		become(new Asleep(this));
		step = 0;
		value = ID;
		buffer = new HashMap<>();
	}
	
	
	
	public void become(UniAlternateState nextState) {
		this.state = nextState;
		become(nextState.intValue());
	}

	protected synchronized void receive(Message msg, int dir) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((UniAlternateMessage) msg).accept(state);
	}

	public void send(Message p) {
		send(p, defaultDir.getDir());
	}

	public void initialize() {
		step = 1;
		ElectionMessage m = new ElectionMessage(step, Direction.RIGHT, value);
		become(new Candidate(this));
		send(m);

	}
	
	
	public void initialize(ElectionMessage m){
		initialize();
		try {
			Thread.sleep(200); // Per non far sovrapporre i messaggi
								// nell'interfaccia grafica
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		m.accept(state);
	}
	

	public static enum Direction {
		LEFT(1), RIGHT(0);

		Direction(int i) {
			dir = i;
		}

		private final int dir;

		public int getDir() {
			return dir;
		}

		public String toString() {
			return dir == 1 ? "L" : "R";

		}

	}

	public void sendAllEnqueuedMessages() {
		Collection<ElectionMessage> messages = buffer.values();
		for (ElectionMessage m : messages) {
			send(m);
			try {
				Thread.sleep(200); // Per non far sovrapporre i messaggi
									// nell'interfaccia grafica
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean checkStageAndEnqueue(ElectionMessage m) {
		
		if(m.getStep() < step){
			System.err.println("Something went wrong! Election message arrived from the past");
		}

		if (m.getStep() != step) {
			buffer.put(m.getStep(), m);
			return false;
		} else {
			return true;
		}
	}

	public void checkAndProcessNext() {
		if (buffer.containsKey(step)) {
			ElectionMessage nextMessage = buffer.remove(step);
			state.handle(nextMessage);
		}
	}



	public int getStep() {
		return step;
	}
	
	public void nextStep(){
		step++;
	}



	public int getValue() {
		return value;
	}



	public void setValue(int value) {
		this.value = value;
	}
	
	



}
