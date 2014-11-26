package yoyo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import general.Message;
import netViewer.ArbitraryNodeYoYo;
import netViewer.Link;

public class Sink extends YoyoAbstractState {

	public Sink(ArbitraryNodeYoYo node) {
		super(node);
	}

	@Override
	public void handle(YoMessage m, Link sender) {
		
		addElementToMap(m.getId(),sender); //TODO: manca anche questo
		if (m.getId() < node.getMinReceivedValue())
			setMinReceivedValue(); // TODO: setMinReceivedValue()
		if (node.idReceivedFromAllLinks()) {
			for (int id : get_linksThatSentThatId_keys()) {
				if (id == node.getMinReceivedValue()) { // ID Minima
					YesMessage yesOr_yesAndPruneMessage = new YesMessage();
					int i = 0;
					for (Link link : node.getLinksById(id)) {
						if (i != 0) { // XXX:L'arco che salvo è sempre il primo,
										// ma se vogliamo poi si cambia
							yesOr_yesAndPruneMessage = new YesAndPruneMessage();
							node.pruneIncomingLink(link);
						}
						node.sendMessage(yesOr_yesAndPruneMessage, link);
						i++;
					}
				} else { // ID non Minima
					NoMessage noOr_noAndPruneMessage = new NoMessage();
					int i = 0;
					for (Link link : node.getLinksById(id)) {
						if (i != 0) {
							noOr_noAndPruneMessage = new NoAndPruneMessage();
							node.pruneIncomingLink(link);
						}
						// FIXME:per come l'ho strutturata conviene flippare un
						// arco alla volta, quindi meglio cambiare funzione
						Set<Link> singleLink = new HashSet<>();
						singleLink.add(link);
						node.flipOutgoingLinks(singleLink);

						node.sendMessage(noOr_noAndPruneMessage, link);
						i++;
					}
				}
			}
		}
		node.chooseState();
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	// TODO: Da qui in poi sono metodi che mi mancano
	private void setMinReceivedValue() {
	}

	private Set<Integer> get_linksThatSentThatId_keys() {
		return null;
	}
	
	private void addElementToMap(int id, Link link){
		
	}
}
