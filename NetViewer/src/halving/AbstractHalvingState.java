package halving;

import netViewer.Link;
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
		node.initialize();
	}
	
	
	public void handle(MedianMessage m, Link sender){
		defaultHandle(sender);
	}


	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " "
				+ sender;
	}
	
	protected void processMedianMessage(MedianMessage m){
		node.halve(m.getMedian());
		if (node.getN() <= 1) {
			node.become(new Done(node));
		} else {
			node.become(new Active(node));
			MedianMessage msg = new MedianMessage(node.getMedian());
			node.send(msg);
		}
	}


	
}
