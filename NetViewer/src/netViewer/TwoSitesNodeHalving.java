package netViewer;

import general.Message;
import halving.Active;
import halving.Asleep;
import halving.HalvingMessage;
import halving.HalvingState;
import halving.MedianMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoSitesNodeHalving extends Node {

	private List<Integer> data;
	final private int k;
	private HalvingState nodeState;
	private Link neighbor;
	private List<Integer> originalData;
	private List<Integer> leftDiscarded;
	private List<Integer> rightDiscarded;

	// TODO FIFO queue

	public TwoSitesNodeHalving(Integer ID, int k, List<Integer> data) {
		super(ID);
		this.k = k;
		this.data = new ArrayList<Integer>();
		this.data.addAll(data);
		this.originalData = data;
		this.leftDiscarded = new ArrayList<Integer>();
		this.rightDiscarded = new ArrayList<Integer>();

		nodeState = new Asleep(this);
		Collections.sort(this.data);
	}

	public int getN() {
		return data.size();
	}

	public int getMedian() {
		return data.get(getMedianIndex());
	}

	private int getMedianIndex() {
		int i = (int) (Math.ceil((double) (getN()) / 2) - 1);
		return i;
	}

	public List<Integer> getData() {
		return new ArrayList<Integer>(data);
	}

	public List<Integer> getOriginalData() {
		return originalData;
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

	public void send(Message m) {
		send(m, neighbor);
	}

	public void initialize() {

		int n = getN();

		// TODO CHECK n/2
		double t = Math.ceil(((double) n * 2) / 2);
		if (k > t) {
			
			discardLeft(n - k + 1);

		} else if (k < t) {

			discardRight(k);

		}

		become(new Active(this));
		MedianMessage m = new MedianMessage(getMedian());
		send(m);
	}

	public void halve(int m, boolean lastIter) {

		if (!lastIter) {
			if (m < getMedian()) {

				discardRight(getMedianIndex() + 1);

			} else if (m > getMedian()) {

				discardLeft(getMedianIndex() + 1);

			}else{
				
				//TODO same median
			}
		} else {
			if (m < getMedian()) {
				rightDiscarded.addAll(data);
				data.clear();
			}
		}
	}

	public int getK() {
		return k;
	}

	public Link getLink() {
		return neighbor;
	}

	public List<Integer> getLeftDiscarded() {
		return new ArrayList<Integer>(leftDiscarded);
	}

	public List<Integer> getRightDiscarded() {
		return new ArrayList<Integer>(rightDiscarded);
	}

	private void discardLeft(int index) {

		List<Integer> tmp = new ArrayList<Integer>(data);
		data = data.subList(index, getN());
		tmp.removeAll(data);
		leftDiscarded.addAll(tmp);

	}

	private void discardRight(int index) {

		List<Integer> tmp = new ArrayList<Integer>(data);
		data = data.subList(0, index);
		tmp.removeAll(data);
		rightDiscarded.addAll(tmp);
	}

}
