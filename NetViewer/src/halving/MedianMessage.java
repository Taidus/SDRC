package halving;

public class MedianMessage implements HalvingMessage {

	private int median;
	private int step;

	public MedianMessage(int median, int step) {
		super();
		this.median = median;
		this.step = step;
	}

	public int getMedian() {
		return median;
	}

	@Override
	public String printString() {
		if (Integer.MAX_VALUE == median) {
			return "Median: Inf";
		} else {
			return String.format("Median: %d", median);
		}
	}

	@Override
	public void accept(HalvingState state) {
		state.handle(this);

	}

	public int getStep() {
		return step;
	}

}
