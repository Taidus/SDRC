package netViewer;

import general.Message;
import halving.HalvingMessage;
import halving.HalvingState;
import halving.MedianMessage;

import java.util.List;

public class TwoSitesNodeHalving extends Node {

	private List<Integer> data;
	private int k;
	private HalvingState nodeState;

	// TODO FIFO queue

	public TwoSitesNodeHalving(Integer ID) {
		super(ID);
	}

	public void setK(int k) {
		this.k = k;
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
		return (int) (Math.floor(getN() / 2) - 1);
	}

	// TODO: defensive copies
	public List<Integer> getData() {
		return data;
	}

	public void become(HalvingState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((HalvingMessage) msg).accept(nodeState, link);
	}
	
	public void send(Message m){
		//TODO
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