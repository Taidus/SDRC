package onDemandTraversal;

import general.State;


public interface OnDemandTraversalState extends State {
	
	// Message visitor

	public void handle(RequestMessage m, int dir);
	public void handle(TokenMessage m, int dir);
	
	void changeState(OnDemandTraversalState nextState);
	public void spontaneously();

}
