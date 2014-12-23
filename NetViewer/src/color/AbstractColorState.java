package color;

import general.State;
import netViewer.Link;
import netViewer.TreeNodeColor;

public abstract class AbstractColorState implements ColorState {

	protected TreeNodeColor node;

	public AbstractColorState(TreeNodeColor node) {
		super();
		this.node = node;
	}

	@Override
	public void changeState(ColorState nextState) {
		node.become(nextState);
	}

	@Override
	public void handle(NewColorMessage m, Link sender) {
		defaultHandle(sender);

	}

	@Override
	public void spontaneously() {
	}

	private final void defaultHandle(Link sender) {
		assert false : this.getClass() + " " + this.node.getNodeId() + " "
				+ sender;
	}

	public boolean hasColorBeenFound() {
		if (node.getColor().isLeqFive()) {
			return true;
		} else {
			return false;
		}
	}

	// XXX
	public int getColorStateNumber() {
			return node.getColor().getId() + State.COLORED_OFFSET;
		}
	}


