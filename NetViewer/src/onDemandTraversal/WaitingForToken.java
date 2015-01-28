package onDemandTraversal;

import netViewer.RingNodeOnDemandTraversal;
import netViewer.RingNodeOnDemandTraversal.Direction;

public abstract class WaitingForToken extends AbstractOnDemandTraversalState {

	public WaitingForToken(RingNodeOnDemandTraversal node) {
		super(node);
	}

	@Override
	public void handle(RequestMessage m, int dir) {
		
		assert dir==Direction.RIGHT.getDir(): "Request arrived from RIGHT";
		
		if(!node.isRequestSent()){
			node.sendRequest();
			node.requestArrived();
		}
		
	}

	@Override
	public void handle(TokenMessage m, int dir) {
		changeState(new TokenHolder(node));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(node.isRequestArrived() || node.isRequestSent()){
			node.sendToken();
			changeState(new WaitingForTokenNonNeeding(node));
		}
		
		
		
		
		
		
	}
	
	protected void nextCriticalOpearation(){
				
		Thread a = new CriticalThread();				
		a.start();

	}
	
	private class CriticalThread extends Thread{
		
		
		
		@Override
		public void run() {
			//System.out.println("Critical operation awaker thread strated");
			long timeLeft = (long) (Math.random()*50000);
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized (node) {
				changeState(new WaitingForTokenNeeding(node));
				node.sendRequest();
			}
			
			
			
		}
	};
	
		
	}

