package halving;

import general.Message;
import netViewer.Link;

public interface HalvingMessage extends Message {
	
	public void accept(HalvingState state, Link sender); 


}
