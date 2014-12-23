package color;

import netViewer.Link;
import netViewer.TreeNodeColor;

public class Colored extends AbstractColorState {
	
	public static int count=0;

	public Colored(TreeNodeColor node) {
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
