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

	public int intValue();

	default public void spontaneously(){}

}
