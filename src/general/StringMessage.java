package general;

import netViewer.Link;

public class StringMessage implements Message {

	private String msg;

	public StringMessage(String msg) {
		this.msg = msg;
	}

	@Override
	public String printString() {
		return msg;
	}

	@Override
	public void accept(State currentState, Link sender) {
		 currentState.handle(this, sender);
	}

}
