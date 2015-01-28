package onDemandTraversal;

import netViewer.RingNodeOnDemandTraversal;
import netViewer.RingNodeOnDemandTraversal.Direction;
import general.State;

public class TokenHolder extends AbstractOnDemandTraversalState {

	public TokenHolder(RingNodeOnDemandTraversal node) {
		super(node);
	}

	@Override
	public void handle(RequestMessage m, int dir) {
		assert dir==Direction.RIGHT.getDir() : "Request arrived from RIGHT";
		
		node.sendToken();
		changeState(new WaitingForTokenNonNeeding(node));

	}

	@Override
	public void handle(TokenMessage m, int dir) {
		assert false: "Two tokens is the ring!";

	}

	@Override
	public int intValue() {
		return State.TOKEN_HOLDER;
	}

}
