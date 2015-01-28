package uniAlternate;


public class NotifyMessage implements UniAlternateMessage {
	

		private final int value;

		public NotifyMessage(int value) {
			super();
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String printString() {
			return "Notify:" + value;
		}

		@Override
		public void accept(UniAlternateState state) {
			state.handle(this);
			
		}


}
