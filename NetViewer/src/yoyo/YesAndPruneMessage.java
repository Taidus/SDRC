package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class YesAndPruneMessage extends YesMessage {

	@Override
	public void prune(ArbitraryNodeYoyo node, Link responseLink) {
		node.pruneOutgoingLink(responseLink);
	}

	@Override
	public String printString() {
		return "Yes and Prune";
	}
}
