package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class NotifyDoneMessage implements Message {

	@Override
	public void accept(State s, Link sender) {
		s.handle(this, sender);
	}
	
	@Override
	public String printString() {
		return "Done";
	}

}
