package sixColors;

import general.State;
import netViewer.Link;
import netViewer.TreeNodeSixColors;

public class Asleep extends AbstractSixColorsState {

	public Asleep(TreeNodeSixColors node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.ASLEEP;
	}

	@Override
	public void spontaneously() {
		
		//node.setupInitColor();
		node.sendColorToChildren();
		if(node.isRoot()){
			System.out.println("ROOT OF EVIL!!! id: "+node.getNodeId());
			changeState (new Colored(node));
		}
	}

	@Override
	public void handle(NewColorMessage m, Link sender) {
		//node.setupInitColor();
		node.sendColorToChildren();
		node.setupNewColor(m.getColor());
		if (!hasColorBeenFound()) {
			changeState(new FindingColor(node));
		} else {
			changeState(new Colored(node));
		}
		node.sendColorToChildren();
	}

}
