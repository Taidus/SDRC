package halving;

import netViewer.Link;

public class MedianMessage implements HalvingMessage {

	private int median;

	public MedianMessage(int median) {
		super();
		this.median = median;
	}

	public int getMedian() {
		return median;
	}

	@Override
	public String printString() {
		return String.format("Median: %d", median);
	}

	@Override
	public void accept(HalvingState state, Link sender) {
		state.handle(this, sender);

	}

}
