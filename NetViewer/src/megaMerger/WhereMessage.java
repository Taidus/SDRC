package megaMerger;

import netViewer.Link;


public class WhereMessage implements MegaMergerMessage {

	private String name;
	private int level;

	public WhereMessage(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public void accept(MegaMergerState s, Link linkArrivedOn) {
		s.handle(this, linkArrivedOn);
	}

	@Override
	public String printString() {
		return "Where?";
	}

}
