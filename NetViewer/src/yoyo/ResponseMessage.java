package yoyo;

import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public abstract class ResponseMessage implements YoyoMessage {

	public abstract void prune(ArbitraryNodeYoYo node, Link responseLink);
}
