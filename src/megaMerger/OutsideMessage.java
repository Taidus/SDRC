package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class OutsideMessage implements Message {

	@Override
	public void accept(State s, Link linkArrivedOn) {
		s.handle(this, linkArrivedOn);
	}

	@Override
	public String printString() {
		return "Outside";
	}

}
