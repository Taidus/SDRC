package netViewer;
/*
 * NetViewer
 *
 * Network: Arbitrary
 * Algorithm: Wake Up
 *
 * Descripion: A node wakes up and sends the wake up message on all its
 * links. A node woken up by the wake up message sends the wake up message
 * on all its links except the one the message came from. There is
 * no official termination; a node does not know when all other nodes
 * are awake. For simulation purposes, termination is imposed by an
 * external entity that checks if all nodes are awake. At this
 * point the animation ceases.
 *
 */

import java.util.Enumeration;

class ArbitraryNodeWakeUp extends Node {

	ArbitraryNodeWakeUp(Integer ID) {
		super(ID);
	}

	/* Overrides Node */
  public boolean isFinished() {
    return (state == general.State.AWAKE);
  }

	/* Receive a message
	 * Dispatch to correct method depending on state.
	 */
	public synchronized void receive(String msg, Link link) {
		switch (state) {
			case general.State.ASLEEP: asleep(msg, link);
					 				 break;
			case general.State.AWAKE:  awake(msg, link);
					 			   break;
		}
	}

	/* Process message received while state = ASLEEP.
	 */
	private void asleep(String msg, Link linkMsgArrivedOn) {
    become(general.State.AWAKE);
		Link link;
		Enumeration allLinks = links.elements();
		while (allLinks.hasMoreElements()) {
			link = (Link)allLinks.nextElement();
			if (link != linkMsgArrivedOn)
				send(msg, link);
		}
		NetViewer.out.println("Node "+nodeId+" has been woken up. Sent wake up to other neighbours.");
	}

	/* Process message received while state = AWAKE.
	 */
	private void awake(String msg, Link linkMsgArrivedOn) {
		NetViewer.out.println("Node "+nodeId+" is already awake and received "+msg);
	}

  /* Initialization sequence.
   * Send Wake Up call in ALL directions (broadcast).
   */
  protected void initialize() {
    become(general.State.AWAKE);
    Enumeration allLinks = links.elements();
    while (allLinks.hasMoreElements())
			send ("wake up", (Link)allLinks.nextElement());
    NetViewer.out.println("Node "+nodeId+" initialized. Sent wake up in all directions.");
  }

}
