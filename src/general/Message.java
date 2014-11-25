package general;

import netViewer.Link;

public interface Message {
	public String printString();
	
	// Visited by State
	public void accept(State currentState, Link sender);

}
