package sixColors;

import general.Message;
import netViewer.Link;

public interface SixColorsMessage extends Message{
		
		public void accept(SixColorsState state, Link sender); 

}
