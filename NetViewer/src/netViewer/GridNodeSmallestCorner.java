package netViewer;
/*
 * NetViewer
 *
 * Grid Network
 * Find a leader without sense of direction
 * Strategy: Elect the smallest corner.
 * Uses a ring algorithm (Chang & Roberts "As Far As It Can") on the four corners to find the smallest
 */

import java.util.Vector;
import java.util.Enumeration;

class GridNodeSmallestCorner extends Node {

	private int myType;
	static final int INTERNAL = 0;
	static final int BOUNDARY = 1;
	static final int CORNER = 2;
	private Link neverSendHereAgain;
	private final String SEARCH_BOUNDARY = "looking for boundary";
	private final String SEARCH_CORNER = "looking for corner";
	private final String NEVER_SEND_HERE_AGAIN = "never send here again";
	private Vector alreadyNotified;

	GridNodeSmallestCorner(Integer ID) {
		super(ID);
		myType = -1;
		alreadyNotified = new Vector();
	}

	/* Receive a message.
	 * Dispatch to correct method depending on state.
	 */
	public synchronized void receive(String msg, Link link) {
		NetViewer.out.println("Node "+nodeId+" received "+msg);
		switch (state) {
			case general.State.ASLEEP: asleep(msg, link);
					 break;
			case general.State.CANDIDATE: candidate(msg, link);
					    break;
			case general.State.PASSIVE: passive(msg, link);
					    break;
		}
	}

  /* Process message received while state = ASLEEP
   */
  private void asleep(String msg, Link linkMsgArrivedOn) {
    try {
      int msgInt = Integer.parseInt(msg);
      // if we get this far, we know we received an ID because it parsed to an integer
      if (getType() == CORNER) { // start ring algorithm (Chang & Roberts "As Far As It Can")
        if (nodeId < msgInt) {
          become(general.State.CANDIDATE);
          broadcast(idString(), linkMsgArrivedOn); // will find opposite corner link
          NetViewer.out.println("Corner node "+nodeId+" woken up by "+msg+". Sent "+nodeId+" on opposite link.");
        }
        else /* msg < id */ {
          become(general.State.PASSIVE);
          broadcast(msg, linkMsgArrivedOn); // will find opposite corner link
          NetViewer.out.println("Corner node "+nodeId+" woken up by "+msg+". Forwarded "+msg+" and became PASSIVE.");
        }
      } // if corner
      else if (getType() == BOUNDARY) {
        become(general.State.PASSIVE);
        broadcast(msg, linkMsgArrivedOn); // forward the ID. Sends everywhere BUT link
        NetViewer.out.println("Boundary node "+nodeId+" woken up by "+msg+". Became PASSIVE and broadcast "+msg);
      }
      else /* I am an INTERNAL node. */ {
        become(general.State.PASSIVE);
        NetViewer.out.print("Internal node "+nodeId+" woken up by "+msg+". Became passive ");
        if (!alreadyNotified.contains(linkMsgArrivedOn)) {
          send(NEVER_SEND_HERE_AGAIN, linkMsgArrivedOn); // tell sender never to send here again - this node is not on the boundary and doesn't need to receive anything
          alreadyNotified.add(linkMsgArrivedOn);
          NetViewer.out.print("and sent '"+NEVER_SEND_HERE_AGAIN+"'");
        }
        NetViewer.out.println();
      }
   } // try
   catch (NumberFormatException e) { // message was a string
    if (msg == SEARCH_CORNER) {
	if (getType() == CORNER) { // start ring algorithm (Chang & Roberts "As Far As It Can")
        become(general.State.CANDIDATE);
        boolean notSent = true;
        int toSendOn = 0;
        while (notSent) {
          if (links.get(toSendOn) != null) {
            send(idString(), (Link)links.get(toSendOn)); // send id on first non-null link in array (so direction will be random)
            notSent = false;
          }
          else toSendOn++;
        }
        NetViewer.out.println("Corner node "+nodeId+" woken up by "+msg+". Started ring alg. Sent ID in one direction.");
      }
      else if (getType() == BOUNDARY) {
        become(general.State.PASSIVE);
        broadcast(SEARCH_CORNER, linkMsgArrivedOn); // keep searching for corner. Sends everywhere BUT linkMsgArrivedOn
        NetViewer.out.println("Boundary node "+nodeId+" woken up by "+msg+". Became PASSIVE and sent '"+SEARCH_CORNER+"'");
      }
      else /* my type is INTERNAL */ {
	  		become(general.State.PASSIVE);
        NetViewer.out.print("Internal node "+nodeId+" woken up by "+msg+". Became passive ");
        if (!alreadyNotified.contains(linkMsgArrivedOn)) {
          send(NEVER_SEND_HERE_AGAIN, linkMsgArrivedOn); // tell sender never to send here again - this node is not on the boundary and doesn't need to receive anything
          alreadyNotified.add(linkMsgArrivedOn);
          NetViewer.out.print("and sent '"+NEVER_SEND_HERE_AGAIN+"'");
        }
        NetViewer.out.println();
      }
    } // if msg is search corner
    else if (msg == SEARCH_BOUNDARY) {
      if (getType() == BOUNDARY) {
        become(general.State.PASSIVE);
        broadcast(SEARCH_CORNER, linkMsgArrivedOn); // start searching for a corner. Sends everywhere BUT linkMsgArrivedOn
        neverSendHereAgain = linkMsgArrivedOn; // the msg came from an internal node, so never send there again.
        NetViewer.out.println("Boundary node "+nodeId+" woken up by "+msg+". Became PASSIVE and sent '"+SEARCH_CORNER+"'");
      }
      else /* my type is INTERNAL */ {
	  		become(general.State.PASSIVE);
        broadcast(SEARCH_BOUNDARY, linkMsgArrivedOn); // keep searching for a boundary node. Sends everywhere BUT linkMsgArrivedOn.
        NetViewer.out.println("Internal node "+nodeId+" woken up by "+msg+". Became passive and sent '"+SEARCH_BOUNDARY+"'");
      }
    }
    else if (msg == "notification") /* my type is INTERNAL */ {
			become(general.State.FOLLOWER);
			broadcast(msg, linkMsgArrivedOn); // broadcast everywhere except linkMsgArrivedOn
    }
   } // catch
 } // process msg ASLEEP


	/* Process message received while state = CANDIDATE.
	 * Only corner nodes receives this if they have already begun the ring algorithm.
	 */
	private void candidate(String msg, Link linkMsgArrivedOn) {
        try {
          int msgInt = Integer.parseInt(msg);
          // if we get this far, we know we received an ID because it parsed to an integer
          if (nodeId < msgInt) {
            // do nothing (stop the message)
            NetViewer.out.println("Corner node "+nodeId+" remains candidate - defeats message "+msg);
          }
          else if (msgInt < nodeId) {
            become(general.State.PASSIVE);
            broadcast(msg, linkMsgArrivedOn); // will find opposite corner link
            NetViewer.out.println("Corner node "+nodeId+" forwarded "+msg+" and became PASSIVE.");
          }
          else { /* received id */
            become(general.State.LEADER);
            broadcast("notification", null); // send notification in both directions
            NetViewer.out.println("**** LEADER FOUND **** Node "+nodeId+". Sent notification.");
          }
        }
        catch (NumberFormatException e) {
          // message was a string
          // Do nothing; already in last stage (ring algorithm)
        }
	}

	/* Process message received while state = PASSIVE.
	 * Boundary nodes that have already sent their SEARCH_CORNER message.
	 * Defeated corner nodes. Internal nodes.
	 */
	private void passive(String msg, Link linkMsgArrivedOn) {
	 if (getType() == CORNER || getType() == BOUNDARY) {
        try {
          int msgInt = Integer.parseInt(msg);
          // if we get this far, we know we received an ID because it parsed to an integer
          broadcast(msg, linkMsgArrivedOn); // send everywhere BUT linkMsgArrivedOn (and any links marked "never send here again")
        }
        catch (NumberFormatException e) {
          // message was a string
          // Check if message was "never send here again".
          if (msg == NEVER_SEND_HERE_AGAIN || msg == SEARCH_BOUNDARY) {
            neverSendHereAgain = linkMsgArrivedOn;
            NetViewer.out.println("Passive node "+nodeId+" received '"+msg+"'");
          }
          else if (msg == "notification") {
            become(general.State.FOLLOWER);
            broadcastEverywhere(msg, linkMsgArrivedOn); // including links marked "never send here again"
          }
          else {
            // Do nothing
          }
        } // catch
       } // if
	 else /* my type is INTERNAL */ {
         if (msg == "notification") {
           become(general.State.FOLLOWER);
           broadcast(msg, linkMsgArrivedOn);
         }
         else if (msg != SEARCH_BOUNDARY && !alreadyNotified.contains(linkMsgArrivedOn)) /* receiving ID or search corner */ {
           send(NEVER_SEND_HERE_AGAIN, linkMsgArrivedOn); // tell sender never to send here again - this node is not on the boundary and is not interested in internal nodes
           alreadyNotified.add(linkMsgArrivedOn);
           NetViewer.out.println("Passive node "+nodeId+" sent '"+NEVER_SEND_HERE_AGAIN+"'");
         }
       } // else
	}

  /* Initialization sequence.
   */
  protected void initialize() {
	if (getType() == CORNER) { // start ring algorithm (Chang & Roberts "As Far As It Can")
        become(general.State.CANDIDATE);
        boolean notSent = true;
        int toSendOn = 0;
        while (notSent) {
          if (links.get(toSendOn) != null) {
            send(idString(), (Link)links.get(toSendOn)); // send id on first non-null link in array (so direction will be random)
            notSent = false;
          }
          else toSendOn++;
        }
        NetViewer.out.println("Node "+nodeId+" initialized. Started ring alg. Sent ID in one direction.");
      }
      else if (getType() == BOUNDARY) {
        become(general.State.PASSIVE);
        broadcast(SEARCH_CORNER, null);
        NetViewer.out.println("Boundary node "+nodeId+" initialized. Became passive and sent sent '"+SEARCH_CORNER+"'.");
      }
      else /* my type is INTERNAL */ {
        become(general.State.PASSIVE);
        broadcast(SEARCH_BOUNDARY, null);
        NetViewer.out.println("Internal node "+nodeId+" initialized. Became passive and sent '"+SEARCH_BOUNDARY+"'.");
      }
  }

  /* Broadcast a message on all links (except the one the message arrived on)
   */
  private void broadcast(String msg, Link linkMsgArrivedOn) {
    NetViewer.out.println("Node "+nodeId+" broadcasting "+msg);
    Link tmpLink;
    Enumeration allLinks = links.elements();
    while (allLinks.hasMoreElements())
    {
			tmpLink = (Link)allLinks.nextElement();
      if (tmpLink != linkMsgArrivedOn && tmpLink != neverSendHereAgain)
        tmpLink.receive(msg, this);
    }
  }

  /* Broadcast a message (notification) on absolutely all links (except the one the message arrived on)
   * In particular, send a link even if it is marked "never send here again"
   */
  private void broadcastEverywhere(String msg, Link linkMsgArrivedOn) {
    NetViewer.out.println("Node "+nodeId+" broadcasting "+msg+" everywhere.");
    Link tmpLink;
    Enumeration allLinks = links.elements();
    while (allLinks.hasMoreElements())
    {
			tmpLink = (Link)allLinks.nextElement();
      if (tmpLink != null && tmpLink != linkMsgArrivedOn)
        tmpLink.receive(msg, this);
    }
  }

	public void setType() {
		int counter = links.size();
		switch (counter)
		{
			case 2: myType = CORNER;   break;
			case 3: myType = BOUNDARY; break;
			case 4: myType = INTERNAL; break;
		}
	}

	// Set the type when creating a new node to run the same algorithm again.
	public void setType(int type) {
		myType = type;
	}

	public int getType() {
		if (myType == -1) setType();
		return myType;
	}

	/* Decided not to use - broadcast takes care of it.
	 * Left as reference.
	 */
/*	private Link getOtherLink(Link link) {
			int otherLink = 0;
			while (true) {
				if (links.get(otherLink) != null && links.get(otherLink) != link) // find corner's other link
					return (Link)links.get(otherLink);
				else otherLink++;
			}
		}
*/

	/*------------- CASES (best/worst/average) -------------*/

	/* Arrange ids into average case configuration.
	 */
	public static boolean average()  {
		return false; // not implemented
	}

	/* Arrange ids into best case configuration.
	 */
	public static boolean best()  {
		return false; // not implemented
	}

	/* Arrange ids into worst case configuration.
	 */
	public static boolean worst()  {
		return false; // not implemented
	}

}
