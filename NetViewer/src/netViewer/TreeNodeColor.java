package netViewer;

import general.Message;

import java.util.List;

import color.Asleep;
import color.Color;
import color.ColorMessage;
import color.ColorState;
import color.NewColorMessage;

public class TreeNodeColor extends TreeNode {

	private Color color;
	private ColorState nodeState;
	

	TreeNodeColor(Integer ID) {
		super(ID);
		nodeState = new Asleep(this);
	}
	
	public void become(ColorState nextState) {
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
		((ColorMessage) msg).accept(nodeState, link);
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
	
	
	public void setupInitColor(){
		//deve essere chiamato dopo l'avvio del protocollo perchè ho fatto un trick malzano per far venire la radice sempre
		//0 e come conseguenza nn può stare nel costruttore
		color = new Color(getNodeId()); 

	}

}
