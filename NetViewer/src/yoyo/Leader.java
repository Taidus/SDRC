package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoyo;

public class Leader extends YoyoAbstractState {

	public Leader(ArbitraryNodeYoyo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.LEADER;
	}

}
