package uniAlternate;

import netViewer.RingNodeUniAlternate;
import netViewer.RingNodeUniAlternate.Direction;




public class ElectionMessage implements UniAlternateMessage {


		private final int step;
		private final RingNodeUniAlternate.Direction dir;
		private final int value;

		public ElectionMessage(int step, Direction dir, int value) {
			super();
			this.step = step;
			this.dir = dir;
			this.value = value;
		}


		public int getStep() {
			return step;
		}

		public Direction getDir() {
			return dir;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String printString() {
			return "Election:(" + value + "," + step + ")" + "->" + dir;

		}

		@Override
		public void accept(UniAlternateState state) {
			state.handle(this);
			
		}

	}
	

