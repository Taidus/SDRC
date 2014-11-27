package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class YesAndPruneMessage extends YesMessage {

	@Override
	public void prune(ArbitraryNodeYoyo node, Link linkArrivedOn) {
		node.pruneOutgoingLink(linkArrivedOn);
	}

	@Override
	public String printString() {
		return "Yes and Prune";
	}
}
