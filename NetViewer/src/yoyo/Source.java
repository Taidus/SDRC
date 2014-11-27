package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Source extends YoyoAbstractState {

	public Source(ArbitraryNodeYoYo node) {
		super(node);
		// TODO implementare
	}
	
	@Override
	protected void whenAllResponsesReceived() {
		 node.chooseState();
	}

	@Override
	public int intValue() {
		return State.SOURCE;
	}

	@Override
	public void handle(NoMessage m, Link sender) {
		handleNoMessage(m, sender);
	}

	@Override
	public void handle(YesMessage m, Link sender) {
		handleYesMessage(m, sender);
	}

}
