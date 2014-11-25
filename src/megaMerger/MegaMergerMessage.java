package megaMerger;

import netViewer.Link;
import general.Message;

public interface MegaMergerMessage extends Message {
	public void accept(MegaMergerState state, Link sender);
}
