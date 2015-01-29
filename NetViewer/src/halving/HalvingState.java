package halving;

import general.State;

public interface HalvingState extends State{
	
	public void handle(MedianMessage m);
	public void handle(SetupMessage m);


	void changeState(HalvingState nextState);


}
