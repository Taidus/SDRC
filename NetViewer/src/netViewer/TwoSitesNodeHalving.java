package netViewer;

import general.Message;
import halving.Asleep;
import halving.HalvingMessage;
import halving.HalvingState;
import halving.MedianMessage;

import java.util.Collections;
import java.util.List;


public class TwoSitesNodeHalving extends Node {

	private List<Integer> data;
	final private int k;
	private HalvingState nodeState;
	private Link neighbor;

	// TODO FIFO queue

	public TwoSitesNodeHalving(Integer ID, int k) {
		super(ID);
		this.k = k;
		nodeState= new Asleep(this);
		Collections.sort(data);
	}

	public int getN() {
		return data.size();
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}

	public int getMedian() {
		return data.get(getMedianIndex());
	}

	private int getMedianIndex() {
		int i = (int) (Math.floor((double)(getN()) / 2) - 1);
		System.out.println(getN()+","+i);
		return i;
	}

	// TODO: defensive copies
	public List<Integer> getData() {
		return data;
	}

	public void become(HalvingState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}

	public void setLink(Link neighbor) {
		this.neighbor = neighbor;
	}
	
	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((HalvingMessage) msg).accept(nodeState);
	}
	
	public void send(Message m){
		send(m, neighbor);
	}
	

	public void initialize() {

		int n = getN();

		// TODO CHECK n/2
		double t = Math.ceil(n / 2);
		if (k > t) {

			data = data.subList(n - k + 1, n - 1);

		} else if (k < t) {

			data = data.subList(0, k - 1);

		}
		
		MedianMessage m = new MedianMessage(getMedian());
		send(m);
	}

	public void halve(int m) {
		if (m > getMedian()) {

			data = data.subList(0, getMedianIndex());

		} else {

			data = data.subList(getMedianIndex() + 1, getN());

		}
	}

}
