package netViewer;
import general.Message;
import general.StringMessage;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;

public class ArbitraryNodeShout extends Node {
	
	private boolean root;
	private Vector treeNeighbors;
	private Integer counter;
	private Link parent;
	
	ArbitraryNodeShout(Integer ID)
	{
		super(ID);
		treeNeighbors = new Vector();
		counter = 0;
		if(ID == (int)ids.firstElement()) {
			become(general.State.INITIATOR);
			root = true;
			setWakeUpDelay(1); // instant wakeup
			setWakeUpPosition(1); // instant wakeup		
		}
		else {
			become(general.State.IDLE);
			root = false;
		}
	}	
	
	public boolean isFinished() {
		return (state == general.State.DONE);
	}
	
	public synchronized void receive(Message m, Link link) {
		String msg = ((StringMessage) m).getMsg();
		switch (state) {
			case general.State.IDLE: 
				idle(msg, link);
				break;
			case general.State.ACTIVE:  
				active(msg, link);
				break;
		}
	}
	
	protected void initialize() {
		if (state == general.State.INITIATOR)
		{
			become(general.State.ACTIVE);
			
			Link link;
			Enumeration allLinks = links.elements();
			while (allLinks.hasMoreElements()) {
				link = (Link)allLinks.nextElement();
				send("Q", link);
			}
			NetViewer.out.println("Node "+getNodeId()+" is the initiator and sent a request Q to all his neighbours");
		}
		
	}
	
	private void idle(String msg, Link linkMsgArrivedOn)
	{
		parent = linkMsgArrivedOn;
		treeNeighbors.add(linkMsgArrivedOn);
		send("Yes", linkMsgArrivedOn);
		NetViewer.out.println("Node "+getNodeId()+" has become son of " + getNeighbourId(linkMsgArrivedOn) + " and replied Yes to him");
		counter++;
		if(counter == links.size()) {
			become(general.State.DONE);
		}
		else{
			Link link;
			Enumeration allLinks = links.elements();
			while (allLinks.hasMoreElements()) {
				link = (Link)allLinks.nextElement();
				if (link != linkMsgArrivedOn)
					send("Q", link);
			}
			become(general.State.ACTIVE);
			NetViewer.out.println("Node "+getNodeId()+" has sent a Q to all his neighbors except his parent " + getNeighbourId(linkMsgArrivedOn));
		}
	}
	
	private void active(String msg, Link linkMsgArrivedOn)
	{
		switch (msg) {
			case "Q":
				send("No", linkMsgArrivedOn);
				NetViewer.out.println("Node "+getNodeId()+" received a Q  from " + getNeighbourId(linkMsgArrivedOn) +" and answered No");
				break;
			case "Yes":
				treeNeighbors.add(linkMsgArrivedOn);
				NetViewer.out.println("Node "+getNodeId()+" received Yes from " + getNeighbourId(linkMsgArrivedOn) + " and became his parent");
				counter++;
				if(counter == links.size()) {
					become(general.State.DONE);
					NetViewer.out.println("Node "+getNodeId()+" is done");
				}
				break;
			case "No":
				NetViewer.out.println("Node "+getNodeId()+" received a No from " + getNeighbourId(linkMsgArrivedOn));
				counter++;
				if(counter == links.size()) {
					become(general.State.DONE);
					NetViewer.out.println("Node "+getNodeId()+" is done");
				}
				linkMsgArrivedOn.setColor(Color.cyan);
				break;
		}
	}	
	
	private int getNeighbourId(Link link) {
		int potentialId = link.getNode(0).getNodeId();
		if (potentialId != getNodeId()) {
			return potentialId;
		}
		return link.getNode(1).getNodeId();
	}
}