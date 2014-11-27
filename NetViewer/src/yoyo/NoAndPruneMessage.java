package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class NoAndPruneMessage extends NoMessage {

	@Override
	public void flipOrPrune(ArbitraryNodeYoyo node, Link linkArrivedOn) {
		node.pruneOutgoingLink(linkArrivedOn);
	}

	@Override
	public String printString() {

		return "No and Prune";
	}
}
