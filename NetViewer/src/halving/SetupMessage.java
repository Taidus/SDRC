package halving;

public class SetupMessage implements HalvingMessage{

	private int id;
	private int n;
	
	
	
	public SetupMessage(int id, int n) {
		super();
		this.id = id;
		this.n = n;
	}

	@Override
	public String printString() {
		return String.format("N: %d, Id: %d", id,n);
	}

	@Override
	public void accept(HalvingState state) {
		state.handle(this);
		
	}

	public int getId() {
		return id;
	}

	public int getN() {
		return n;
	}
	
	

}
