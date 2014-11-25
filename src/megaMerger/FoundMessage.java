package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class FoundMessage implements Message {

	private int childMin;
	
	public FoundMessage(int childMin) {
		this.childMin = childMin;
	}

	@Override
	public void accept(State s, Link sender) {
		s.handle(this, sender);
	}

	@Override
	public String printString() {
		return "Found: " + ((Integer.MAX_VALUE == childMin) ? "inf" : childMin);
	}

	public int getChildMin() {
		return childMin;
	}

	
}
