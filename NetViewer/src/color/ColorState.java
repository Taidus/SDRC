package color;

import netViewer.Link;
import general.State;

public interface ColorState extends State {

	// Message visitor

	public void handle(NewColorMessage m, Link sender);

	void changeState(ColorState nextState);

}
