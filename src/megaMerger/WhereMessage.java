package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class WhereMessage implements Message {

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
	public void accept(State s, Link linkArrivedOn) {
		s.handle(this, linkArrivedOn);
	}

	@Override
	public String printString() {
		return "Where?";
	}

}
