package yoyo;

import netViewer.Link;
import general.Message;

public interface  YoyoMessage extends Message {
	
	public void accept(YoyoState state, Link sender); 
}
