package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;

public abstract class ResponseMessage implements YoyoMessage {

	public abstract void prune(ArbitraryNodeYoyo node, Link responseLink);
}
