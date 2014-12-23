package sixColors;

import netViewer.Link;
import general.State;

public interface SixColorsState extends State {

	// Message visitor

	public void handle(NewColorMessage m, Link sender);

	void changeState(SixColorsState nextState);

}
