package general;


public interface State {

	// perché ci sono classi che li usano
	public static final int ASLEEP = 0;
	public static final int AWAKE = 1;
	public static final int CANDIDATE = 2;
	public static final int PASSIVE = 3;
	public static final int LEADER = 4;
	public static final int FOLLOWER = 5;
	public static final int WAITING_FOR_ANSWER = 6;
	public static final int FINDING_MERGE_EDGE = 7;
	public static final int INTERNAL = 8;
	public static final int SINK = 9;
	public static final int SOURCE = 10;
	public static final int FINDING_COLOR=11;
	
	
	public static final int COLORED_OFFSET=100;
	public static final int COLORED_0 = COLORED_OFFSET+0;
	public static final int COLORED_1 = COLORED_OFFSET+1;
	public static final int COLORED_2 = COLORED_OFFSET+2;
	public static final int COLORED_3 = COLORED_OFFSET+3;
	public static final int COLORED_4 = COLORED_OFFSET+4;
	public static final int COLORED_5 = COLORED_OFFSET+5;

	
	public static final int WAITING_FOR_TOKEN_NEEDING= 20;
	public static final int WAITING_FOR_TOKEN_NON_NEEDING =21;
	public static final int TOKEN_HOLDER=22;
	
	public int intValue();

	public void spontaneously();

}
