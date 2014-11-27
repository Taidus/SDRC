package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class YesAndPruneMessage extends YesMessage {

	@Override
	public void prune(ArbitraryNodeYoYo node, Link responseLink) {
		node.pruneOutgoingLink(responseLink);
	}

	@Override
	public String printString() {
		return "Yes and Prune";
	}
}
