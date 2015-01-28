package onDemandTraversal;


public class TokenMessage implements OnDemandTraversalMessage {

	@Override
	public void accept(OnDemandTraversalState state, int dir) {
		state.handle(this, dir);

	}

	@Override
	public String printString() {
		return "Token";
	}

}
