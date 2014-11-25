package megaMerger;

import netViewer.Link;


public class OutsideMessage implements MegaMergerMessage {

	@Override
	public void accept(MegaMergerState s, Link linkArrivedOn) {
		s.handle(this, linkArrivedOn);
	}

	@Override
	public String printString() {
		return "Outside";
	}

}
