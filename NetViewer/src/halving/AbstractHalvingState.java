package halving;

import netViewer.TwoSitesNodeHalving;

public abstract class AbstractHalvingState implements HalvingState {

	protected TwoSitesNodeHalving node;

	public AbstractHalvingState(TwoSitesNodeHalving node) {
		super();
		this.node = node;
	}

	@Override
	public void changeState(HalvingState nextState) {
		node.become(nextState);
	}

	@Override
	public void spontaneously() {
	}

	public void handle(MedianMessage m) {
		defaultHandle();
	}

	public void handle(SetupMessage m) {
		defaultHandle();
	}

	private final void defaultHandle() {
		assert false : this.getClass() + " " + this.node.getNodeId() + " ";
	}

	protected void processSetupMessage(SetupMessage m) {

		int q = Math.max(node.getN(), m.getN());
		int p = (int) Math.ceil(Math.log(q) / Math.log(2));
		int r = (int) Math.pow(2,p);

		double n_symbols = 2 * r - m.getN() - node.getN();
		int tot_minus = (int) Math.floor(n_symbols / 2);
		int tot_plus = (int) Math.ceil(n_symbols / 2);

		int min_id_N =( node.getNodeId() < m.getId())? node.getN(): m.getN();
		
		double n1_places = r - min_id_N;
		int n1_plus = (int) Math.floor(n1_places / 2);
		int n1_minus = (int) Math.ceil(n1_places / 2);

		if (node.getNodeId() < m.getId()) {
			

			node.pad(n1_minus, n1_plus);

		} else {

			node.pad(tot_minus - n1_minus, tot_plus - n1_plus);

		}

		node.become(new Active(node));
		MedianMessage msg = new MedianMessage(node.getMedian(),
				node.getCurrentStep());
		node.send(msg);
	}

	protected void processMedianMessage(MedianMessage m) {

		if (m.getStep() != node.getCurrentStep()) {
			// System.out.println("Curretn styep: "+node.getCurrentStep()+" , mstep: "+m.getStep());
			node.enqueueMessage(m);

		} else {

			boolean lastIter = false;
			if (node.getN() == 1) {
				lastIter = true;
			}
			node.halve(m.getMedian(), lastIter);

			if (node.getN() <= 1 && lastIter) {
				node.become(new Done(node));
			} else {
				// node.become(new Active(node));
				MedianMessage msg = new MedianMessage(node.getMedian(),
						node.getCurrentStep());
				node.send(msg);
			}

			m = node.nextEnqueuedMessage();
			if (m != null) {
				processMedianMessage(m);
			}

		}
	}

}
