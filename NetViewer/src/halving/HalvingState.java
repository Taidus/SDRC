package halving;

import general.State;

public interface HalvingState extends State{
	
	public void handle(MedianMessage m);

	void changeState(HalvingState nextState);


}
