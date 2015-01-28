package uniAlternate;

import general.Message;

public interface UniAlternateMessage extends Message {

	public void accept(UniAlternateState state); 

}
