package megaMerger;

import netViewer.Link;


public class MakeMergeRequestMessage implements MegaMergerMessage {

	@Override
	public void accept(MegaMergerState s, Link sender) {
		s.handle(this, sender);
	}

	@Override
	public String printString() {
		return "Merge";
	}

}
