package yoyo;

import netViewer.ArbitraryNodeYoyo;
import netViewer.Link;
import general.State;

public class Awake extends YoyoAbstractState {

	public Awake(ArbitraryNodeYoyo node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.AWAKE;
	}

	@Override
	public void handle(SetupMessage m, Link sender) {
		handleSetupMessage(m, sender);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		enqueueMessage(m, sender);
	}
}
