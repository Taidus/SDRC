package color;

import netViewer.Link;

public class NewColorMessage implements ColorMessage {
	
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
	public void accept(ColorState state, Link sender) {
		state.handle(this, sender);
		
	}

	public Color getColor() {
		return color;
	}
	
	

}
