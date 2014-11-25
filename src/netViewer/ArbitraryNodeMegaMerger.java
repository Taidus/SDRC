package netViewer;

import general.Message;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import megaMerger.Asleep;
import megaMerger.Awake;
import megaMerger.FindingMergeEdge;
import megaMerger.InsideMessage;
import megaMerger.LetUsMergeMessage;
import megaMerger.MakeMergeRequestMessage;
import megaMerger.MegaMergerMessage;
import megaMerger.MegaMergerState;
import megaMerger.OutsideMessage;
import megaMerger.UpdateAndFindMessage;
import megaMerger.UpdateMessage;
import megaMerger.WaitingForAnswer;
import megaMerger.WhereMessage;

public class ArbitraryNodeMegaMerger extends Node {

	private MegaMergerState nodeState;
	private String nodeName;
	private int level;
	private Set<Link> childrenEdges;
	private Link parentEdge;
	private Map<Link, LetUsMergeMessage> suspendedRequests;
	private Map<Link, WhereMessage> suspendedQuestions;

	private Set<Link> internalEdges;
	private Link mergePathNextEdge;
	private Link outsideRequest_edge;

	ArbitraryNodeMegaMerger(Integer ID) {
		super(ID);

		this.nodeState = new Asleep(this);

		this.childrenEdges = new HashSet<>();
		this.internalEdges = new HashSet<>();
		this.suspendedRequests = new HashMap<>();
		this.suspendedQuestions = new HashMap<>();
	}
	
	public void become(MegaMergerState nextState){
		this.nodeState = nextState;
	}

	@Override
	public int getNodeId() {
		return nodeId;
	}

	@Override
	protected void initialize() {
		nodeState.spontaneously();
	}

	public void megaMergerInitialize() {

		assert !links.contains(null);

		this.nodeName = "City " + nodeId;
		this.level = 1;
		this.mergePathNextEdge = getMinCostLink(links);
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		//XXX bruttura: cast. Ma se non si ristruttura la classe Node è difficile far di meglio
		((MegaMergerMessage) msg).accept(nodeState, link); 
	}

	public boolean isDowntown() {
		return null == this.parentEdge;
	}

	public String getNodeName() {
		return nodeName;
	}

	public int getLevel() {
		return level;
	}

	public Link getMergePathNextEdge() {
		return mergePathNextEdge;
	}

	public Link getOutsideRequestEdge() {
		return outsideRequest_edge;
	}

	public void setMergePathNextEdge(Link mergePathNextEdge) {
		this.mergePathNextEdge = mergePathNextEdge;
	}

	public void suspendRequest(LetUsMergeMessage message, Link sender) {
		suspendedRequests.put(sender, message);
	}

	public void suspendQuestion(WhereMessage message, Link sender) {
		suspendedQuestions.put(sender, message);
	}

	public LetUsMergeMessage processSuspendedRequest(Link requestSender) {
		assert suspendedRequests.containsKey(requestSender);
		return suspendedRequests.remove(requestSender);
	}

	public WhereMessage processSuspendedQuestion(Link questionSender) {
		assert suspendedQuestions.containsKey(questionSender);
		return suspendedQuestions.remove(questionSender);
	}

	public boolean previousRequestOnMergeEdgeFound() {
		for (Link entry : suspendedRequests.keySet()) {
			if (suspendedRequests.get(entry).getLevel() == level && entry.equals(mergePathNextEdge)) {
				return true;
			}
		}
		return false;
	}

	public void addChild(Link child) {
		assert null != child;
		childrenEdges.add(child);
	}

	public int getChildrenNumber() {
		return childrenEdges.size();
	}

	public void addInternalEdge(Link link) {
		this.internalEdges.add(link);
	}

	public void updateInternalEdges() {
		internalEdges.addAll(childrenEdges);

		if (!isDowntown()) {
			internalEdges.add(parentEdge);
		}
	}

	public Set<Link> getNonInternalLinks() {
		Set<Link> nonInternalLinks = new HashSet<>(links);
		nonInternalLinks.removeAll(internalEdges);

		assert nonInternalLinks.size() + internalEdges.size() == links.size();

		return nonInternalLinks;
	}

	public void sendMessage(Message message, Link link) {
		send(message, link);
	}

	public void sendToChildren(Message message) {
		for (Link l : childrenEdges) {
			assert null != l;
			sendMessage(message, l);
		}
	}

	public void sendToParent(Message m) {
		sendMessage(m, parentEdge);
	}

	public void sendMergeRequest() {
		sendMessage(new LetUsMergeMessage(level, nodeId, nodeName), mergePathNextEdge);

		WaitingForAnswer newState = new WaitingForAnswer(this);
		nodeState.changeState(newState);
		newState.checkForPreviousRequests();
	}

	public void delegateMergeRequest() {
		sendMessage(new MakeMergeRequestMessage(), mergePathNextEdge);
	}

	public void broadcastUpdate(String new_name, int new_level, Link sender_edge) {
		update(new_name, new_level, sender_edge);

		sendToChildren(new UpdateMessage(nodeName, level));

		become(new Awake(this));
	}

	public void broadcastUpdateAndFind(String new_name, int new_level, Link sender_edge) {
		update(new_name, new_level, sender_edge);

		sendToChildren(new UpdateAndFindMessage(nodeName, level));

		FindingMergeEdge newState = new FindingMergeEdge(this);
		become(newState);
		newState.findMinExternalEdge();
	}

	private void update(String new_name, int new_level, Link sender_edge) {
		this.nodeName = new_name;
		this.level = new_level;

		if (!isDowntown()) {
			addChild(parentEdge);
		}
		childrenEdges.remove(sender_edge);
		assert !childrenEdges.contains(sender_edge);
		parentEdge = sender_edge;
		answerQuestions();
		addWeakerRequestersAsChildren();
		assert !childrenEdges.contains(sender_edge);
	}

	private void answerQuestions() {
		Map<Link, WhereMessage> stillSuspendedQuestions = new HashMap<>();

		for (Link l : suspendedQuestions.keySet()) {
			if (suspendedQuestions.get(l).getName().equals(nodeName)) {
				sendMessage(new InsideMessage(), l);
			}
			else if (suspendedQuestions.get(l).getLevel() <= level) {
				sendMessage(new OutsideMessage(), l);
			}
			else {
				stillSuspendedQuestions.put(l, suspendedQuestions.get(l));
			}
		}

		suspendedQuestions = stillSuspendedQuestions;
	}

	private void addWeakerRequestersAsChildren() {
		Map<Link, LetUsMergeMessage> stillSuspendedRequests = new HashMap<>();

		for (Link l : suspendedRequests.keySet()) {
			if (suspendedRequests.get(l).getLevel() < level) {
				addChild(l);
			}
			else {
				stillSuspendedRequests.put(l, suspendedRequests.get(l));
			}
		}

		suspendedRequests = stillSuspendedRequests;
	}

	public static Link getMinCostLink(Collection<Link> linkSet) {
		assert !linkSet.isEmpty();
		Link result = null;

		int currentCost = Integer.MAX_VALUE;
		for (Link l : linkSet) {
			if (l.getCost() <= currentCost) {
				result = (Link) l;
				currentCost = l.getCost();
			}
		}

		assert result != null;
		return result;
	}
}
