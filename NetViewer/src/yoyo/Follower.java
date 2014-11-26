package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;

public class Follower extends YoyoAbstractState{

	public Follower(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.FOLLOWER;
	}

}
