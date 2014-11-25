package megaMerger;

import general.State;
import netViewer.Link;

public interface MegaMergerState extends State {

	// Message visitor

	public void handle(LetUsMergeMessage m, Link sender);

	public void handle(WhereMessage m, Link sender);

	public void handle(MakeMergeRequestMessage m, Link sender);

	public void handle(InsideMessage m, Link sender);

	public void handle(OutsideMessage m, Link sender);

	public void handle(FoundMessage m, Link sender);

	public void handle(NotifyDoneMessage m, Link sender);

	public void handle(UpdateMessage m, Link sender);

	public void handle(UpdateAndFindMessage m, Link sender);

	public void changeState(MegaMergerState nextState);

}