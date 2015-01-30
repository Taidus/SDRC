package halving;

import general.Message;

public interface HalvingMessage extends Message {
	
	public void accept(HalvingState state); 


}
