package sixColors;


public class Color {
	
	private int id;

	public Color(int id) {
		super();
		this.id = id;
	}
		
	
	public Color shrink(Color c){
		//calcola il nuovo colore in base a quello ricevuto (c)
		
		
		int a = id;
		int b = c.getId();
		
		int limit = (int) Math.floor(Math.log(b));
		int j=0;
		while((a%2)==(b%2) && j<=limit){
			j++;
			a=Math.floorDiv(a, 2);
			b=Math.floorDiv(b, 2);
		}
		
		int jBit=a%2;
		
		System.out.println("my binary id: "+ toBinaryString());
		System.out.println("received binary id: "+ c.toBinaryString());		
		
		int new_id = j*2+jBit;
		
		System.out.println("j: "+j+" jbit: "+jBit);
	 
		System.out.println("NewColor: "+new_id);
		return new Color(new_id);
	}
	
	public int getId(){
		return id;
	}
	
	public boolean isLeqFive(){
		return id<=5;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
	public String toBinaryString(){
		return Integer.toBinaryString(id);
	}
	
	
	

}
