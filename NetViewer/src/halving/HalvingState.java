package halving;

import netViewer.Link;
import general.State;

public interface HalvingState extends State{
	
	public void handle(MedianMessage m, Link sender);

	void changeState(HalvingState nextState);


}
