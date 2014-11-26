package megaMerger;

import netViewer.Link;


public class FoundMessage implements MegaMergerMessage {

	private int childMin;
	
	public FoundMessage(int childMin) {
		this.childMin = childMin;
	}

	@Override
	public void accept(MegaMergerState s, Link sender) {
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
