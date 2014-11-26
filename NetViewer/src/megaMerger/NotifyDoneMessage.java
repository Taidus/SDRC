package megaMerger;

import netViewer.Link;


public class NotifyDoneMessage implements MegaMergerMessage {

	@Override
	public void accept(MegaMergerState s, Link sender) {
		s.handle(this, sender);
	}
	
	@Override
	public String printString() {
		return "Done";
	}

}
