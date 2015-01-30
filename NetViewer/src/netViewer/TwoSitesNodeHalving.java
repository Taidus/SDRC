package netViewer;

import general.Message;
import halving.Asleep;
import halving.DataItemsDrawingController;
import halving.HalvingMessage;
import halving.HalvingState;
import halving.MedianMessage;
import halving.SettingUp;
import halving.SetupMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoSitesNodeHalving extends Node {

	private List<Integer> data;
	final private int k;
	private HalvingState nodeState;
	private Link neighbor;
	private List<Integer> originalData;
	private List<Integer> leftDiscarded;
	private List<Integer> rightDiscarded;
	private int currentStep;
	private Map<Integer, MedianMessage> queue;
	private int otherNodeId;
	private int otherNodeN;

	// TODO FIFO queue

	public TwoSitesNodeHalving(Integer ID, int k, List<Integer> data) {
		super(ID);
		this.k = k;
		this.data = new ArrayList<Integer>();
		this.data.addAll(data);
		this.originalData = data;
		this.leftDiscarded = new ArrayList<Integer>();
		this.rightDiscarded = new ArrayList<Integer>();
		this.currentStep = 0;
		this.queue = new HashMap<Integer, MedianMessage>();

		nodeState = new Asleep(this);
		Collections.sort(this.data);
		DataItemsDrawingController.update(this);
	}

	private void nextStep() {
		currentStep++;
	}

	public int getN() {
		return data.size();
	}

	public int getMedian() {
		return data.get(getMedianIndex());
	}

	public int getMedianIndex() {
		return getMedianIndex(getN());
	}

	private int getMedianIndex(int n) {
		int i = (int) (Math.ceil((double) (n) / 2) - 1);
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
		NetViewer.out.println("Node " + this.getNodeId()
				+ " received message: '" + ((HalvingMessage) msg).printString()
				+ "'");
		((HalvingMessage) msg).accept(nodeState);
	}

	public void send(Message m) {
		send(m, neighbor);
	}

	public void initialize() {

		// TODO remove this block

		List<Integer> tmp = new ArrayList<Integer>(data);
		TwoSitesNodeHalving n = (TwoSitesNodeHalving) neighbor
				.getOtherNode(this);
		tmp.addAll(n.getData());
		Collections.sort(tmp);
		System.out.println(tmp);
		if (n.getNodeId() > this.getNodeId()) {
			NetViewer.out.println("Expected value is: " + tmp.get(k - 1));
		}
		System.out.println("True k is: " + tmp.get(k - 1));

		//

		become(new SettingUp(this));
		SetupMessage m = new SetupMessage(getNodeId(), getN());
		send(m);
	}

	public void setupForKQuery() {

		int median = (int) (Math.ceil((double) (getN() + otherNodeN) / 2) - 1);
		// System.out.println("median: "+median+" k: "+k);

		if (k - 1 > median) {

			int max_right = getN() + otherNodeN - k;

			System.out.println("k >");

			int totN = getN() + otherNodeN;
			int index = getN() - max_right - 1;
			discardLeft(index);
			otherNodeN = Math.min(max_right + 1, otherNodeN);

			int discarded = totN - getN() - otherNodeN;
			int new_median = getMedianIndex(getN() + otherNodeN);
			System.out.println("discarded: " + discarded + "new_median "
					+ new_median);
			int padding = computePadding(getN(),otherNodeN, k,discarded);
			if (getNodeId() < otherNodeId) {

				System.out.println("padding " + (padding));
				if (padding > 0) {
					pad(0, padding);
				} else {
					pad(Math.abs(padding), 0);

				}
			} else {
				otherNodeN += Math.abs(padding);
			}

		} else if (k - 1 < median) {
			System.out.println("k <");

			if (getN() < k) {
				pad(0, k - getN());
			}
			discardRight(k);
			otherNodeN = k;
		}

	}

	private int computePadding(int n1, int n2, int k, int discarded) {
		System.out.println("n1: "+n1+" n2: "+n2+"k: "+k+"disc "+discarded);

		int padding = 0;
		int indexAdd=0;
		while (getMedianIndex(n1 + n2 + Math.abs(padding)) != k - discarded +indexAdd - 1) {
//			System.out.println("padding: "+padding);

			if (getMedianIndex(n1 + n2 + Math.abs(padding)) < k - discarded +indexAdd - 1) {
				padding++;
			} else {
				padding--;
				indexAdd++;
			}

		}
		return padding;
	}

	public void halve(int m, boolean lastIter) {

		if (!lastIter) {
			if (m < getMedian()) {

				discardRight(getMedianIndex() + 1);

			} else if (m > getMedian()) {

				discardLeft(getMedianIndex() + 1);

			} else {

				if (getNodeId() < getOtherNodeId()) {

					discardRight(getMedianIndex() + 1);

				} else {
					discardLeft(getMedianIndex() + 1);
				}

			}
		} else {
			if (m < getMedian()
					|| (m == getMedian() && getNodeId() < getOtherNodeId())) {
				rightDiscarded.addAll(data);
				data.clear();
			}
		}
		nextStep();
		DataItemsDrawingController.update(this);
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

		index = Math.max(index, 0);
		leftDiscarded.addAll(data.subList(0, index));
		data = data.subList(index, getN());
		DataItemsDrawingController.update(this);
	}

	private void discardRight(int index) {

		index = Math.min(index, getN());
		rightDiscarded.addAll(data.subList(index, getN()));
		data = data.subList(0, index);
		DataItemsDrawingController.update(this);
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void enqueueMessage(MedianMessage m) {
		queue.put(m.getStep(), m);
		// System.out.println("QEUEUEUE");
	}

	public MedianMessage nextEnqueuedMessage() {
		MedianMessage m = queue.remove(currentStep);
		return m;
	}

	private int getOtherNodeId() {
		return otherNodeId;
	}

	public void setOtherNodeId(int otherNodeId) {
		this.otherNodeId = otherNodeId;
	}

	public void pad(int n_left, int n_right) {
		// TODO something more elegant?

		for (int i = 0; i < n_left; i++) {
			data.add(Integer.MIN_VALUE);
		}

		for (int i = 0; i < n_right; i++) {
			data.add(Integer.MAX_VALUE);
		}

		Collections.sort(data);
		DataItemsDrawingController.update(this);
	}

	public int getOtherNodeN() {
		return otherNodeN;
	}

	public void setOtherNodeN(int otherNodeN) {
		this.otherNodeN = otherNodeN;
	}

}
