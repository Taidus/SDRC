package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;

public class Leader extends YoyoAbstractState {

	public Leader(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.LEADER;
	}

}
