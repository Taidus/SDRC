package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class UpdateMessage implements Message {

	private String name;
	private int level;
	
	public UpdateMessage(String name, int level) {
		this.name = name;
		this.level = level;
	}

	@Override
	public void accept(State s, Link sender) {
		s.handle(this, sender);
	}

	@Override
	public String printString() {
		return "Update";
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}
	
	
}
