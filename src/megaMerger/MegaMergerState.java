package megaMerger;

import general.State;
import netViewer.Link;

public interface MegaMergerState extends State{
	static void defaultHandle() {
		assert false;
	}

	// Message visitor

	default public void handle(LetUsMergeMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(WhereMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(MakeMergeRequestMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(InsideMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(OutsideMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(FoundMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(NotifyDoneMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(UpdateMessage m, Link sender) {
		defaultHandle();
	};

	default public void handle(UpdateAndFindMessage m, Link sender) {
		defaultHandle();
	}

	public void changeState(MegaMergerState nextState);;
}
