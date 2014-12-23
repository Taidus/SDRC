package color;

import general.State;
import netViewer.Link;
import netViewer.TreeNodeColor;

public class FindingColor extends AbstractColorState {

	public FindingColor(TreeNodeColor node) {
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
