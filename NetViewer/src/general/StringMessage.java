package general;


public class StringMessage implements Message {

	private String msg;

	public StringMessage(String msg) {
		this.msg = msg;
	}

	@Override
	public String printString() {
		return msg;
	}

	public String getMsg() {
		return msg;
	}
	
	

}
