package sixColors;

import netViewer.Link;
import netViewer.TreeNodeSixColors;

public class Colored extends AbstractSixColorsState {
	
	public static int count=0;

	public Colored(TreeNodeSixColors node) {
		super(node);
		count++;
		System.out.println("COUNT: "+count+" color= "+getColorStateNumber()+" node id: "+node.getNodeId());
	}

	@Override
	public int intValue() {
		return getColorStateNumber();
	}
	
	@Override
	public void handle(NewColorMessage m, Link sender) {
		//Nothing to do, the message is simply dropped.

	}
	
	
	

}
