package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public class NoAndPruneMessage extends NoMessage {

	@Override
	public void prune(ArbitraryNodeYoyo node, Link responseLink) {
		node.pruneOutgoingLink(responseLink);
	}

	@Override
	public String printString() {

		return "No and Prune";
	}
}
