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

	private final void defaultHandle() {
		assert false : this.getClass() + " " + this.node.getNodeId() + " ";
	}

	protected void processMedianMessage(MedianMessage m) {
		boolean lastIter=false;
		if(node.getN()==1){
			lastIter=true;
		}
		node.halve(m.getMedian(), lastIter);
				
		if (node.getN() <= 1 && lastIter) {
			node.become(new Done(node));
		} else {
//			node.become(new Active(node));
			MedianMessage msg = new MedianMessage(node.getMedian());
			node.send(msg);
		}
	}

}
