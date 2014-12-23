package sixColors;

import general.State;
import netViewer.Link;
import netViewer.TreeNodeSixColors;

public abstract class AbstractSixColorsState implements SixColorsState {

	protected TreeNodeSixColors node;

	public AbstractSixColorsState(TreeNodeSixColors node) {
		super();
		this.node = node;
	}

	@Override
	public void changeState(SixColorsState nextState) {
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


