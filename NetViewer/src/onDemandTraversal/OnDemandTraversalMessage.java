package onDemandTraversal;

import general.Message;

public interface OnDemandTraversalMessage extends Message {
	
	public void accept(OnDemandTraversalState state, int dir); 
}
