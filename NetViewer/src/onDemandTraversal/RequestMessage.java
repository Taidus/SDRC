package onDemandTraversal;


public class RequestMessage implements OnDemandTraversalMessage {

	@Override
	public void accept(OnDemandTraversalState state, int dir) {
		state.handle(this, dir);

	}

	@Override
	public String printString() {
		return "Request";
	}

}
