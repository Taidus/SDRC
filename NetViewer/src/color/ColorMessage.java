package color;

import general.Message;
import netViewer.Link;

public interface ColorMessage extends Message{
		
		public void accept(ColorState state, Link sender); 

}
