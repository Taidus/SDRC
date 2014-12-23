package sixColors;

import general.State;
import netViewer.Link;
import netViewer.TreeNodeSixColors;

public class FindingColor extends AbstractSixColorsState {

	public FindingColor(TreeNodeSixColors node) {
		super(node);
	}

	@Override
	public void handle(NewColorMessage m, Link sender) {
		node.setupNewColor(m.getColor());
		node.sendColorToChildren();
		if(hasColorBeenFound()){
			changeState(new Colored(node));
		}
		
	}
		

	@Override
	public int intValue() {
		return State.FINDING_COLOR;
	}


}
