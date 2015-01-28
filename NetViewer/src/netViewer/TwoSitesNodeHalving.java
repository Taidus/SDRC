package netViewer;

import java.util.List;

public class TwoSitesNodeHalving extends Node {

	private List<Integer> data;
	private int k;
	
	public TwoSitesNodeHalving(Integer ID) {
		super(ID);
		become(general.State.IDLE);
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public void setData(List<Integer> data) {
		this.data = data;
	}
	
	//TODO: defensive copies
	public List<Integer> getData() {
		return data;
	}

}
