package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoyo;

public class Follower extends YoyoAbstractState{

	public Follower(ArbitraryNodeYoyo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.FOLLOWER;
	}

}
