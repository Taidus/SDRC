package general;

import megaMerger.FoundMessage;
import megaMerger.InsideMessage;
import megaMerger.LetUsMergeMessage;
import megaMerger.MakeMergeRequestMessage;
import megaMerger.NotifyDoneMessage;
import megaMerger.OutsideMessage;
import megaMerger.UpdateAndFindMessage;
import megaMerger.UpdateMessage;
import megaMerger.WhereMessage;
import netViewer.Link;

public interface State {
	// perché ci sono classi che li usano
	public static final int ASLEEP = 0;
	public static final int AWAKE = 1;
	public static final int CANDIDATE = 2;
	public static final int PASSIVE = 3;
	public static final int LEADER = 4;
	public static final int FOLLOWER = 5;
	public static final int WAITING_FOR_ANSWER = 6;
	public static final int FINDING_MERGE_EDGE = 7;

	static void defaultHandle() {
		assert false;
	}

	public int intValue();

	public void changeState(State nextState);

	default public void spontaneously() {
	};

	// Message visitor (XXX bisogna metterli tutti -> implementazione di default)
	default public void handle(StringMessage m, Link sender) {
		defaultHandle();
	};

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
	};
}
