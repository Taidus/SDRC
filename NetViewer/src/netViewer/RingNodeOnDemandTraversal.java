package netViewer;

import java.awt.Color;

import general.Message;
import onDemandTraversal.OnDemandTraversalMessage;
import onDemandTraversal.OnDemandTraversalState;
import onDemandTraversal.RequestMessage;
import onDemandTraversal.TokenHolder;
import onDemandTraversal.TokenMessage;
import onDemandTraversal.WaitingForTokenNonNeeding;

public class RingNodeOnDemandTraversal extends Node {
	
	private OnDemandTraversalState nodeState;
	
	private boolean requestArrived;
	private boolean requestSent;
	

	public RingNodeOnDemandTraversal(Integer ID) {
		super(ID);
		requestArrived=false;
		requestSent=false;
		if(getNodeId()==0){
			become(new TokenHolder(this));
		}else{
			become(new WaitingForTokenNonNeeding(this));
		}
	}
	
	public static enum Direction {
		LEFT(1), RIGHT(0);

		Direction(int i) {
			dir = i;
		}

		private final int dir;

		public int getDir() {
			return dir;
		}

		public String toString() {
			return dir == 1 ? "L" : "R";

		}

	}
	
	
	
	@Override
	protected void initialize() {
		nodeState.spontaneously();
	}
	
	public void become(OnDemandTraversalState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
	}
	
	
	@Override
	protected synchronized void receive(Message msg, int dir) {
		synchronized (this) {
			((OnDemandTraversalMessage) msg).accept(nodeState, dir);
		}
		
	}
	
	private void send(Message m,Direction dir){
		send(m,dir.getDir());
	}
	
	public  void  sendRequest(){
		
		if(!isRequestSent()){
		requestSent=true;
		Link l = getLink(Direction.LEFT.getDir());
		l.setColor(Color.red);
		send(new RequestMessage(),Direction.LEFT);
		}
	}
	
	public void sendToken(){
		Link l = getLink(Direction.RIGHT.getDir());
		l.setColor(Color.black);
		requestArrived= false;
		requestSent=false;
		send(new TokenMessage(),Direction.RIGHT);
		
	}

	

	public boolean isRequestArrived() {
		return requestArrived;
	}

	public boolean isRequestSent() {
		return requestSent;
	}


	public void requestArrived() {
		this.requestArrived = true;
	}
	
	
	
	

}
