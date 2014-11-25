package megaMerger;

import general.Message;
import general.State;
import netViewer.Link;


public class LetUsMergeMessage implements Message {

	private int level, id;
	private String name;

	public LetUsMergeMessage(int level, int id, String name) {
		this.level = level;
		this.id = id;
		this.name = name;
	}

	@Override
	public void accept(State s, Link sender) {
		s.handle(this, sender);
	}
	
	@Override
	public String printString() {
		return "Let us merge";
	}

	public int getLevel() {
		return level;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
