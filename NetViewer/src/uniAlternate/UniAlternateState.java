package uniAlternate;

import general.State;

public interface UniAlternateState extends State {

	
	void changeState(UniAlternateState nextState);

	public void handle(ElectionMessage m);

	void handle(NotifyMessage m);
}
