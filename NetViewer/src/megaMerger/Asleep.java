package megaMerger;

import general.State;
import netViewer.ArbitraryNodeMegaMerger;
import netViewer.Link;


public class Asleep extends MegaMergerAbstractState {
	
	public Asleep(ArbitraryNodeMegaMerger node) {
		super(node);
	}

	@Override
	public int intValue() {
		return State.ASLEEP;
	}
	
	@Override
	public void spontaneously() {
		node.megaMergerInitialize();
		node.sendMergeRequest();
	}

	@Override
	public void handle(LetUsMergeMessage m, Link sender) {
		node.megaMergerInitialize();
		node.suspendRequest(m, sender);
		node.sendMergeRequest();
	}
	
	@Override
	public void handle(WhereMessage m, Link sender) {
		node.megaMergerInitialize();
		
		// Se qualcuno mi chiede la città mentre sono Asleep vuol dire che è almeno di livello 2, quindi devo sospendere la richiesta
		node.suspendQuestion(m, sender);
		
		node.sendMergeRequest();
	}
}
