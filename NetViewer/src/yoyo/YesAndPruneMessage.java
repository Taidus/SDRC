package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class YesAndPruneMessage extends YesMessage {

	@Override
	public void prune(ArbitraryNodeYoYo node, Link sender) {
		node.pruneOutgoingLink(sender);

	}
	@Override
	public String printString() {
		
		return "Yes and Prune";
	}
}
