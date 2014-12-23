package netViewer;

import general.Message;

import java.util.List;

import sixColors.Asleep;
import sixColors.Color;
import sixColors.NewColorMessage;
import sixColors.SixColorsMessage;
import sixColors.SixColorsState;

public class TreeNodeSixColors extends TreeNode {

	private Color color;
	private SixColorsState nodeState;
	

	TreeNodeSixColors(Integer ID) {
		super(ID);
		setupInitColor();
		nodeState = new Asleep(this);
	}
	
	public void become(SixColorsState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}

	public Color getColor() {
		return color;
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è
		// difficile far di meglio
		((SixColorsMessage) msg).accept(nodeState, link);
	}

	@Override
	protected void initialize() {
		nodeState.spontaneously();
	}

	public void sendColorToChildren() {
		List<Link> children = getChildrenLinks();
		NewColorMessage m = new NewColorMessage(color);
		for (Link l : children) {
			send(m, l);
		}

	}
	
	public void setupNewColor(Color c){
		color = color.shrink(c);
		System.out.println("New color: "+color+"for Node: "+getNodeId());
	}
	
	
	private void setupInitColor(){
		color = new Color(getNodeId()); 

	}

}
