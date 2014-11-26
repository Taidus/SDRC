package megaMerger;

import netViewer.Link;


public class UpdateAndFindMessage implements MegaMergerMessage {
	
	private String name;
	private int level;
	
	public UpdateAndFindMessage(String name, int level) {
		this.name = name;
		this.level = level;
	}
	
	@Override
	public void accept(MegaMergerState s, Link sender) {
		s.handle(this, sender);
	}
	
	@Override
	public String printString() {
		return "Update+find"; 
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}
	
	

}
