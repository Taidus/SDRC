package yoyo;

import general.State;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Sink extends Receiver {

	public Sink(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		handleYoMessage(m, sender);
		node.chooseState();
	}
	
	@Override
	protected void whenReceivedIdOnAllLinks() {
		respondToAll();
		/* XXX mi sembra che quello che faccia sia identico al respondToAll() di Receiver
		for (int id : get_linksThatSentThatId_keys()) {
			if (id == getMinReceivedId()) {
//				YesMessage response = new YesMessage();
//				int i = 0;
//				for (Link link : getLinksById(id)) {
//					if (i != 0) { // XXX:L'arco che salvo è sempre il primo,
//									// ma se vogliamo poi si cambia
//						response = new YesAndPruneMessage();
//						node.pruneIncomingLink(link);
//					}
//					node.send(response, link);
//					i++;
//				}
				Link yesLink = pruneAndRespondToAllButRandomOne(new YesAndPruneMessage(), getLinksById(id));
				node.send(new YesMessage(), yesLink);
			}
			else {
//				NoMessage response = new NoMessage();
//				int i = 0;
//				for (Link link : getLinksById(id)) {
//					if (i != 0) {
//						response = new NoAndPruneMessage();
//						node.pruneIncomingLink(link);
//					}
//					// FIXME:per come l'ho strutturata conviene flippare un
//					// arco alla volta, quindi meglio cambiare funzione
//					Set<Link> singleLink = new HashSet<>();
//					singleLink.add(link);
//					node.flipOutgoingLinks(singleLink);
//
//					node.send(response, link);
//					i++;
//				}
				Link noLink = pruneAndRespondToAllButRandomOne(new NoAndPruneMessage(), getLinksById(id));
				node.send(new NoMessage(), noLink);
				node.flipIncomingLinks(getLinksById(id));
			}
		}*/
	}
	
	

	@Override
	public int intValue() {
		return State.SINK;
	}
	
	//XXX se non serve il metodo di prima rimuovere
//	private Link pruneAndRespondToAllButRandomOne(ResponseMessage message, Set<Link> links) {
//		Link chosenOne = links.iterator().next();
//
//		Set<Link> allButOne = new HashSet<Link>(links);
//		allButOne.remove(chosenOne);
//
//		pruneAndRespond(message, allButOne);
//		return chosenOne;
//	}
//	
//	private void pruneAndRespond(ResponseMessage message, Set<Link> links) {
//		node.sendToAll(message, links);
//		node.pruneIncomingLinks(links);
//	}

}
