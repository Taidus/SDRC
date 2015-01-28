import java.awt.Color;
import java.util.Vector;


public class ArbitraryNodeShortestPT extends Node{

	boolean source;
	protected int myDistance;
	private int ackcount;
	private Vector<Link> unvisited;
	private int iteration;
	private int minpath;
	private Boolean b;
	
	private Vector<Link> childLink;
	private int childcount;

	private Link parentLink;
	
	private Link mychoice;
	private Link exit;
	
	static final int WAITING_FOR_ACK = 10;
	static final int COMPUTING = 11;
	static final Color COLOUR_WAITING_FOR_ACK = Color.magenta;
	static final Color COLOUR_COMPUTING = Color.orange;
	static {
		coloursMap.put(new Integer(WAITING_FOR_ACK), COLOUR_WAITING_FOR_ACK);
		coloursMap.put(new Integer(COMPUTING), COLOUR_COMPUTING);
	}

	
	ArbitraryNodeShortestPT(Integer ID) {
		
		super(ID);
		b=false;
		childLink = new Vector<Link>();
		if(ID == ids.firstElement()) {
			become(INITIATOR);
			source = true;
			setWakeUpDelay(1); // instant wakeup
			setWakeUpPosition(1); // instant wakeup		
		}
		else {
			become(IDLE);
			source = false;
		}
	}
	
	
	
	public synchronized void receive(String msg, Link link) {
		switch (state) {
		case INITIATOR:
				init(msg,link);
			case IDLE: 
				idle(msg, link);
				break;
			case ACTIVE:  
				active(msg, link);
				break;
			case AWAKE:  
				awake(msg, link);
				break;
			case WAITING_FOR_ACK:
				waiting_for_ack(msg, link);
				break;
			case COMPUTING:
				computing(msg, link);
				break;
				
		}
	}

	protected void initialize(){
		if(source){
			unvisited = new Vector<Link>();
			for(int i=0;i<links.size();i++)
				unvisited.add((Link)links.get(i));
			b = true;
			myDistance= 0;
			ackcount=links.size();
			for(int i=0;i<links.size();i++)			//Notifico ai nodi del grafo che sono io la radice
				send("Notify",(Link)links.get(i));
			NetViewer.out.println("Source sent a NOTIFY msg");
		}
	}
	
	
	/**
	 * Operazioni eseguite dal nodo che si trova nello stato INITIATOR al momento della ricezione di un msg 
	 */
	private void init(String msg, Link link) {
		
		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];
		
		if(msgStr.equals("Ack")){
			ackcount--;
			NetViewer.out.println("Source has received an ack from node "+link.getOtherNode(this).id);
			if (ackcount == 0){
				iteration = 1;

				//calcola il miglior link usando la funzione v(x,y) nel ns. caso y e' il link (x,y) e x e' sempre "this"
				Link l = (Link)links.get(0);
				int min = l.getCost();
				for(int i=1;i<links.size();i++){
					int value = v(this, (Link)links.get(i));
					if(value < min){
						l = (Link)links.get(i);
						min = value;
					}
				}
				int path_length = min;

				childLink.add(l);
				l.setColor(Color.blue);
				send("Expand "+iteration+" "+path_length,l);
				NetViewer.out.println("Source sent an EXPAND msg");
				unvisited.remove(l);
				become(ACTIVE);
			}
		}
	}
	
	private int v(ArbitraryNodeShortestPT x,Link link){
		return x.myDistance+link.getCost();
	}

	/**
	 * Operazioni eseguite da un nodo che si trova nello stato IDLE al momento della ricezione di un msg 
	 */
	private void idle(String msg, Link link) {
		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];

		//ogni metodo relativo ad uno stato inizialmente fa questa print per visualizzare le azioni nei log        
		NetViewer.out.println("Node "+id+" in the state IDLE has received msg: "+msgStr+" from node: "+link.getOtherNode(this).id);

        if(msgStr.equals("Notify")){
        	if(!b){
    			unvisited = new Vector<Link>();
    			for(int i=0;i<links.size();i++)
    				unvisited.add((Link)links.get(i));
    			b = true;
        	}
    		unvisited.remove(link);
    		send("Ack",link);
			NetViewer.out.println("Node "+id+" sent an ACK msg");
    		become(AWAKE);        	
        }
	}

	/**
	 * Operazioni eseguite da un nodo che si trova nello stato AWAKE al momento della ricezione di un msg 
	 */	
	private void awake(String msg, Link link) {

		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];

        
		NetViewer.out.println("Node "+id+" in the state AWAKE has received msg: "+msgStr+" from node: "+link.getOtherNode(this).id);
        
		//case Notify
        if(msgStr.equals("Notify")){
			unvisited.remove(link);
			send("Ack",link);
			NetViewer.out.println("Node "+id+" sent an ACK msg");
			become(AWAKE);        	
        }

        //case Expand
        if(msgStr.equals("Expand")){
        	int msgIteration = Integer.parseInt(msgArr[1]); 	//iteration*
        	int msgPathValue = Integer.parseInt(msgArr[2]);     //path_value*
        	
			myDistance = msgPathValue;
			
			
			parentLink = link;

			childLink.clear();
			
			if (links.size() > 1){
				for(int i=0;i<links.size();i++){			
					if(!links.get(i).equals(parentLink))	// Invio il msg di notifica a tutti i miei vicini eccetto il sender
						send("Notify", (Link)links.get(i));
				}
				NetViewer.out.println("Node "+id+" sent a NOTIFY msg");
				ackcount= links.size()-1;

				become(WAITING_FOR_ACK);
			}
			else{
				send("IterationCompleted",parentLink);
				NetViewer.out.println("Node "+id+" sent an ITERATION COMPLETED msg");
				become(ACTIVE);
			}
        	        	
        }
		
	}

	
	/**
	 * Operazioni eseguite da un nodo che si trova nello stato ACTIVE al momento della ricezione di un msg 
	 */
	private void waiting_for_ack(String msg, Link link) {
		
		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];

		NetViewer.out.println("Node "+id+" in the state WAITING_FOR_ACK has received msg: "+msgStr+" from node: "+link.getOtherNode(this).id);
        
        if(msgStr.equals("Ack")){			//se riceve un ACK
        	ackcount = ackcount - 1;
        	if (ackcount == 0){				//se ho ricevuto tutti gli ack che dovevo ricevere allora lo dico al mio padre e divento ACTIVE
        		send("IterationCompleted",parentLink);
    			NetViewer.out.println("Node "+id+" sent an ITERATION COMPLETED msg");
        		become(ACTIVE);
        	}
        }
		
	}

	
	
	/**
	 * Operazioni eseguite da un nodo che si trova nello stato ACTIVE al momento della ricezione di un msg 
	 */
	private void active(String msg, Link link) {

		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];

		NetViewer.out.println("Node "+id+" in the state ACTIVE has received msg: "+msgStr+" from node: "+link.getOtherNode(this).id);
		
		if(msgStr.equals("IterationCompleted")){
			if(!source){
				send("IterationCompleted",parentLink);
				NetViewer.out.println("Source "+id+" sent an ITERATION COMPLETED msg");
			}
			else{
				iteration++;
				for(int t=0; t<childLink.size(); t++)
					send("StartIteration "+iteration, childLink.get(t));
				NetViewer.out.println("Node "+id+" sent a START ITERATION msg");
				ComputeLocalMinimum();//Invoco la procedura Compute Local Minimum
				
				childcount = 0;
				become(COMPUTING);
			}
		}
		
		if(msgStr.equals("StartIteration")){
        	int msgIteration = Integer.parseInt(msgArr[1]); 	//iteration*
        	iteration = msgIteration;
        	ComputeLocalMinimum(); //Invoco la procedura ComputeLocalMinimum
        	if (childLink.size() == 0){
        		send("MinValue "+minpath,parentLink);
    			NetViewer.out.println("Node "+id+" sent a MINVALUE msg "+minpath);
        	}
        	else{
        		for(int t=0; t<childLink.size(); t++){
            		send("StartIteration "+iteration, childLink.get(t));        			
        		}
    			NetViewer.out.println("Node "+id+" sent a START ITERATION msg");
        		childcount = 0;
        		become(COMPUTING);
        	}
		}
		
		if(msgStr.equals("Expand")){
        	int msgIteration = Integer.parseInt(msgArr[1]); 	//iteration*
        	int msgPathValue = Integer.parseInt(msgArr[2]);     //path_value*
        	
        	send("Expand "+msgIteration+" "+msgPathValue,exit);

        		if(exit.equals(mychoice) && !childLink.contains(mychoice)){
    				NetViewer.out.println("*****The "+mychoice+" is in the shortest path tree *****");
    				mychoice.setColor(Color.blue);
        			childLink.add(mychoice);		//Children = Children U {mychoice}
        			unvisited.remove(mychoice);		//Unvisited = Unvisited - {mychoice}
        		}
		}
		
		if(msgStr.equals("Notify")){
    		unvisited.remove(link);		//Unvisited = Unvisited - {sender}
    		send("Ack",link);
		}
		
		if(msgStr.equals("Terminate")){
			for(int t=0; t<childLink.size(); t++)		//send(Terminate) to children
				send("Terminate",childLink.get(t));
			NetViewer.out.println("Node "+id+" sent a TERMINATE msg");
			become(DONE);
		}
		

	}
	
	private void computing(String msg, Link link){
	
		String[] msgArr = msg.split(" ");
        String msgStr = msgArr[0];

		NetViewer.out.println("Node "+id+" in the state COMPUTING has received msg: "+msgStr+" from node: "+link.getOtherNode(this).id);
		
		if(msgStr.equals("MinValue")){
        	int msgPathValue = Integer.parseInt(msgArr[1]); 	//pathvalue*
        	
        	if (msgPathValue < minpath){
        		minpath = msgPathValue;
        		exit = link;			//exit = sender
        	}
        	childcount = childcount + 1;
        	NetViewer.out.println("Node "+id+" in the state COMPUTING childcount= "+childcount+"  |Children|="+childLink.size());
        	NetViewer.out.println("Node "+id+" in the state COMPUTING");
			
        	if (childcount == childLink.size()){		//if childcount == |Children|
        		if(!source){
        			send("MinValue "+minpath,parentLink);
        			NetViewer.out.println("Node "+id+" sent a MINVALUE msg "+minpath);
        			become(ACTIVE);
        		}else{
        			checkForTermination();
        		}
        	}
        	
		}

		
		
	}
	
	/**
	 * Questa è la procedura CheckForTermination
	 */
	private void checkForTermination(){
		if(minpath == 10000){
			for(int i=0; i<childLink.size(); i++)			//send Terminate to children
				send("Terminate",childLink.get(i));
			NetViewer.out.println("Node "+id+" sent an TERMINATE msg");
			become(DONE);
		}else{
			send("Expand "+iteration+" "+minpath,exit);
			
			//********THE TRICK*******//
    		if(exit.equals(mychoice) && !childLink.contains(mychoice)){
				NetViewer.out.println("i am the node "+id+" *****The "+mychoice+" is in the shortest path tree *****");
				mychoice.setColor(Color.blue);
    			childLink.add(mychoice);		//Children = Children U {mychoice}
    			unvisited.remove(mychoice);		//Unvisited = Unvisited - {mychoice}
    		}
			NetViewer.out.println("Node "+id+" sent an EXPAND msg");
			become(ACTIVE);
		}
		
	}

	/**
	 * Questa è la procedura Compute Local Minimum
	 */
	private void ComputeLocalMinimum(){
				
		if(source){
			NetViewer.out.println("I AM THE SOURCE,id="+id+" unvisited: "+unvisited.size());
		}
		if(unvisited.size() == 0){ //if Unvisited è vuoto
			minpath = 10000;
		}else{
			int link_length = 0;
			Link u = unvisited.get(0);	//link_length = v(x,y) = MIN{v(x,z):z appartiene ad Unvisited}
			int minU = v(this,u);
			link_length = u.getCost();
			for(int i=1; i<unvisited.size(); i++){
				int valueU = v(this, unvisited.get(i));
				if(valueU < minU){
					u = unvisited.get(i);
					minU = valueU;
					link_length = u.getCost();
				}
			}
			
			
			minpath = myDistance + link_length;
			mychoice = u;
			exit = u;
		}
	}
	
	public boolean isFinished() {
		return (state == DONE);
	}





	


}
