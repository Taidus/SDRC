package netViewer;

import general.Message;

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
	private Set<Link> childrenLinks;
	private Link parentLink;
	private Map<Link, LetUsMergeMessage> suspendedRequests;
	private Map<Link, WhereMessage> suspendedQuestions;

	private Set<Link> internalLinks;
	private Link mergePathNextLink;
	private Link whereQuestion_link;

	ArbitraryNodeMegaMerger(Integer ID) {
		super(ID);

		this.nodeState = new Asleep(this);

		this.childrenLinks = new HashSet<>();
		this.internalLinks = new HashSet<>();
		this.suspendedRequests = new HashMap<>();
		this.suspendedQuestions = new HashMap<>();
	}

	public void become(MegaMergerState nextState) {
		this.nodeState = nextState;
		become(nextState.intValue());
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
		this.mergePathNextLink = FindingMergeEdge.getMinCostLink(links);
	}

	@Override
	protected synchronized void receive(Message msg, Link link) {
		// XXX bruttura: cast. Ma se non si ristruttura la classe Node è difficile far di meglio
		((MegaMergerMessage) msg).accept(nodeState, link);
	}

	public boolean isDowntown() {
		return null == this.parentLink;
	}

	public String getNodeName() {
		return nodeName;
	}

	public int getLevel() {
		return level;
	}

	public Link getMergePathNextLink() {
		return mergePathNextLink;
	}

	public Link getWhereQuestionLink() {
		return whereQuestion_link;
	}

	public void setMergePathNextLink(Link mergePathNextLink) {
		this.mergePathNextLink = mergePathNextLink;
	}

	public void suspendRequest(LetUsMergeMessage request, Link sender) {
		suspendedRequests.put(sender, request);
	}

	public void suspendQuestion(WhereMessage question, Link sender) {
		suspendedQuestions.put(sender, question);
	}

	public LetUsMergeMessage processSuspendedRequest(Link requestSender) {
		assert suspendedRequests.containsKey(requestSender);
		return suspendedRequests.remove(requestSender);
	}

	public WhereMessage processSuspendedQuestion(Link questionSender) {
		assert suspendedQuestions.containsKey(questionSender);
		return suspendedQuestions.remove(questionSender);
	}

	public boolean previousFriendlyMergeRequestOnMergeLinkFound() {
		for (Link entry : suspendedRequests.keySet()) {
			if (suspendedRequests.get(entry).getLevel() == level && entry.equals(mergePathNextLink)) {
				return true;
			}
		}
		return false;
	}

	public void addChild(Link child) {
		assert null != child;
		childrenLinks.add(child);
	}

	private void removeChild(Link child) {
		childrenLinks.remove(child);
		assert !childrenLinks.contains(child);
	}

	public int getChildrenNumber() {
		return childrenLinks.size();
	}

	public void addInternalLink(Link link) {
		this.internalLinks.add(link);
	}

	public void updateInternalLinks() {
		internalLinks.addAll(childrenLinks);

		if (!isDowntown()) {
			internalLinks.add(parentLink);
		}
	}

	public Set<Link> getNonInternalLinks() {
		Set<Link> nonInternalLinks = new HashSet<>(links);
		nonInternalLinks.removeAll(internalLinks);

		assert nonInternalLinks.size() + internalLinks.size() == links.size();

		return nonInternalLinks;
	}

	public void sendToChildren(Message message) {
		for (Link l : childrenLinks) {
			assert null != l;
			send(message, l);
		}
	}

	public void sendToParent(Message m) {
		send(m, parentLink);
	}

	public void sendMergeRequest() {
		send(new LetUsMergeMessage(level, nodeId, nodeName), mergePathNextLink);

		WaitingForAnswer newState = new WaitingForAnswer(this);
		nodeState.changeState(newState);
		newState.checkForPreviousRequests();
	}

	public void delegateMergeRequest() {
		send(new MakeMergeRequestMessage(), mergePathNextLink);
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
			addChild(parentLink);
		}
		removeChild(sender_edge);
		parentLink = sender_edge;
		answerQuestions();
		addWeakerRequestersAsChildren();
		assert !childrenLinks.contains(sender_edge);
	}

	private void answerQuestions() {
		Map<Link, WhereMessage> stillSuspendedQuestions = new HashMap<>();

		for (Link l : suspendedQuestions.keySet()) {
			if (suspendedQuestions.get(l).getName().equals(nodeName)) {
				send(new InsideMessage(), l);
			}
			else if (suspendedQuestions.get(l).getLevel() <= level) {
				send(new OutsideMessage(), l);
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
			if (weakerCityRequest(suspendedRequests.get(l))) {
				addChild(l);
			}
			else {
				stillSuspendedRequests.put(l, suspendedRequests.get(l));
			}
		}

		suspendedRequests = stillSuspendedRequests;
	}

	private boolean weakerCityRequest(LetUsMergeMessage request) {
		return request.getLevel() < level;
	}
	
	public boolean isRequestLate(Link sender) {
		// il controllo può servire in caso di rete non-FIFO: in un Friendly merge posso ricevere il messaggio di aggiornamento della nuova
		// downtown PRIMA di aver ricevuto il Let-us-merge
		return parentLink == sender || childrenLinks.contains(sender);
	}
}
