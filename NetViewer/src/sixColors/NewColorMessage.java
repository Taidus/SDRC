package sixColors;

import netViewer.Link;

public class NewColorMessage implements SixColorsMessage {
	
	private Color color;
	
	

	public NewColorMessage(Color color) {
		super();
		this.color = color;
	}

	@Override
	public String printString() {
		return "Color: "+color;
	}

	@Override
	public void accept(SixColorsState state, Link sender) {
		state.handle(this, sender);
		
	}

	public Color getColor() {
		return color;
	}
	
	

}
