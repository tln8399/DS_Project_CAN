import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Peer extends Thread implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3180558492793098931L;
	private String node;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private ArrayList<Peer> neighbors = new ArrayList<Peer>();
	private String ip;
	private ArrayList<String> data = new ArrayList<String>();
	private int portnumber;
	private Boolean isPeerAdded;
	private String command;
	private String keyword;
	private Boolean isdisplayed;
	// a and b are coordinates of random point
	int a; 
	int b;
	
	public void view(){
		System.out.println("Node :" + this.node);
		System.out.println("ip : " + this.ip);
		System.out.println("Port Number: "+ this.portnumber);
		System.out.println("Coordinates: " + this.x1 + " " + this.y1 + " " + this.x2 + " " + this.y2);
		System.out.print("Neighbors :");
		for(Peer n : this.neighbors){
			System.out.print(n.getNodeIdentifier());
		}
		System.out.println(" IsPeerAdded flag : "+ this.isPeerAdded);
		System.out.println();
	}
	
	public void copyPeerdata(Peer peer){
		this.ip = peer.ip;
		this.node = peer.node;
		this.portnumber = peer.portnumber;
		this.x1 = peer.x1;
		this.y1 = peer.y1;
		this.x2 = peer.x2;
		this.y2 = peer.y2;
		this.neighbors = peer.neighbors;
		this.isPeerAdded = peer.isPeerAdded;
		this.command = peer.command;
	}
	
	
	public Peer() {
		// TODO Auto-generated constructor stub
		this.isPeerAdded = false;
		this.command = null;
		this.isdisplayed = false;
	}
	
	public void setIsDisplayed(){
		this.isdisplayed = true;
	}
	
	public Boolean getIsDisplayed(){
		return this.isdisplayed;
	}
	
	public void setKeyword(String key){
		this.keyword = key;
	}
	
	public String getKeyword(){
		return this.keyword;
	}
	
	public void setCommand(String com){
		this.command = com;
	}
	
	public String getCommand(){
		return this.command;
	}
	
	public void setIsPeerAddedFlag(){
		this.isPeerAdded = true;
	}
	
	public Boolean getIsPeerAddedFlag(){
		return this.isPeerAdded;
	}

	public void setIP(String IP){
		this.ip = IP;		
	}
	
	public String getIP(){
		return this.ip;		
	}
	
	public void setNodeIdentifier(String peerName){
		this.node = peerName;		
	}
	
	public String getNodeIdentifier(){
		return this.node;
	}
	
	public void setPortnumber(int number){
		this.portnumber = number;
	}
	
	public int getPortnumber(){
		return this.portnumber;
	}
	
	public void setCoordinates(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
	}
	
	public void viewCoordinates(){
		System.out.println("Coordinates of "+this.getNodeIdentifier()+":" + this.x1 + " " + this.y1 + " " + this.x2 + " " + this.y2);
	}
	
	public int getX1(){
		return this.x1;
	}
	public int getY1(){
		return this.y1;
	}
	public int getX2(){
		return this.x2;
	}
	public int getY2(){
		return this.y2;
	}
	
	public void setA(int aa){
		this.a = aa;
	}
	
	public int getA(){
		return this.a;
	}
	
	public void setB(int bb){
		this.b = bb;
	}
	
	public int getB(){
		return this.b;
	}
	/* setter getter for Neighbor */
	public ArrayList<Peer> getNeighbors(){
		return this.neighbors;
	}
	
	public void addNeighbor(Peer n){
		this.neighbors.add(n);
	}
	
	public void removeNeighbor(Peer n){
		this.neighbors.remove(n);
	}
	/* setter getter for data keywords */
	public ArrayList<String> getDataKeyword(){
		return this.data;
	}
	
	public void addDataKeyword(String key){
		this.data.add(key);
	}
	
	public void removeDataKeyword(String key){
		this.data.remove(key);
	}
	
	/* Methods to copy neighbors */
	public void copyNeighbors(Peer n){
		for(Peer i : n.neighbors){
			// It should not copy itself as onw's neighbor
			if(!this.getNodeIdentifier().equals(i.getNodeIdentifier())){
				Boolean flag = false;
				// You need to compare node identifier, because object can be changed which passing 
				for(Peer temp : this.getNeighbors()){
					if( temp.getNodeIdentifier().equals(i.getNodeIdentifier())){
						flag = true;
					}
				}
				if(flag == false){
					/* Check these 2 peers are neighbors of each other */
					if(this.isNeighbor(i)){
						this.addNeighbor(i);
					}
				}	
				 //this.neighbors.addLast(i);
			}
		}
	}
	
	/* Function to check 2 peers are neighbor or not
	 * It takes coordinates of 2 peers/nodes
	 * Based on coordinates of peers, creates 4 edges of each peer
	 * and checks any one of 4 edge of FIRST peer overlaps Any one of 4 Edges of SECOND peer*/
	public boolean isNeighbor(Peer p){
		int x1 = this.getX1();
		int y1 = this.getY1();
		int x2 = this.getX2();
		int y2 = this.getY2();
		int[] l1_x1 = {x1, x1, x2, x2};
		int[] l1_y1 = {y1, y2 ,y2, y1};
		int[] l1_x2 = {x1, x2, x2, x1};
		int[] l1_y2 = {y2, y2, y1, y1};
		
		int x11 = p.getX1();
		int y11 = p.getY1();
		int x22 = p.getX2();
		int y22 = p.getY2();
		int[] l2_x1 = {x11, x11, x22, x22};
		int[] l2_y1 = {y11, y22, y22, y11};
		int[] l2_x2 = {x11, x22, x22, x11};
		int[] l2_y2 = {y22, y22, y11, y11};
		
		if((x1 < x11 && x2 < x11) || (x1 > x22 && x2 > x22) || ( y1 < y11 && y2 < y11) || (y1 > y22 && y2 > y22)){
			return false;
		}
		
		for(int i = 0; i < 4; i++){
			int a1 = l1_x1[i];
			int b1 = l1_y1[i];
			int a2 = l1_x2[i];
			int b2 = l1_y2[i];
			for(int j=0; j < 4; j++){
				int p1 = l2_x1[j];
				int q1 = l2_y1[j];
				int p2 = l2_x2[j];
				int q2 = l2_y2[j];
				// check (a1,b1)(a2,b2) overlap with (p1,q1)(p2,q2)
				//case 1 : if both the rectangle share entire edge
				if((a1 == p1 && b1 == q1 && a2 == p2 && b2 == q2) ||
						(a1 == p2 && b1 == q2 && a2==p1 && b2 == q1)){
					return true;
				}
				//case2: 
				if(a1 == p1 || a2 == p2){
				   if(Math.abs(q2-q1) > Math.abs(b2-b1)){
						if( ((b1 > q1 && b1 < q2) || (b2 > q1 && b2 < q2)) ||
								((b1 < q1 && b1 > q2) || (b2 < q1 && b2 > q2))){
							return true;
						}
					}
					else if(Math.abs(q2-q1) < Math.abs(b2-b1)){
						if(((q1 > b1 && q1 < b2) || (q2 > b1 && q2 < b2)) ||
								((q1 < b1 && q1 > b2) || (q2 < b1 && q2 > b2)) ){
							return true;
						}
					}
				}
				
				if(b1 == q1 || b2 == q2){
					if(Math.abs(p2-p1) > Math.abs(a2-a1)){
						if(((a1 > p1 && a1 < p2) || (a2 > q1 && a2 < p2)) ||
								((a1 < p1 && a1 > p2) || (a2 < q1 && a2 > p2))){
							return true;
						}
					}
					else if(Math.abs(p2-p1) < Math.abs(a2-a1)){
						if(((p1 > a1 && p1 < a2) || (p2 > a1 && p2 < a2)) ||
								((p1 < a1 && p1 > a2) || (p2 < a1 && p2 > a2))){
							return true;
						}
					}
				}
				//case 3: one coordinate of both edge is same and they overlap
			}
		}
		return false;
	}
	
	public void spiltMySpace(Peer newPeer, int x, int y){
		System.out.println("split Peer" + newPeer);
		int x1 = this.getX1();
		int y1 = this.getY1();
		int x2 = this.getX2();
		int y2 = this.getY2();
		
		// Check how to divide node i.e. horizontally or vertically
		// i.e. check whether length is bigger or height
		if( (x2 - x1) <= (y2 - y1)){ 		// square or length is less than height
			/* divide horizontally */
			// Check y coordinate of given point(x,y) lies in which region i.e. upper or lower
			// If it lies in upper, then new Peer will be upper half divided node. lower will be entry point or original node.
			// If it lies lower, Then new Peer will be lower half divided node. Upper will be entrypoint or original node
			if( y < ((y1 +  y2) / 2) ){ 
				newPeer.setCoordinates(x1, y1, x2, ((y1+y2)/2) );
				this.setCoordinates(x1, ((y1+y2)/2) , x2, y2);
				this.addNeighbor(newPeer);
				newPeer.addNeighbor(this);
				newPeer.copyNeighbors(this);
			}
			else{
				newPeer.setCoordinates(x1, ((y1+y2)/2), x2, y2);
				this.setCoordinates(x1, y1, x2, ((y1+y2)/2) );
				this.addNeighbor(newPeer);
				newPeer.addNeighbor(this);
				newPeer.copyNeighbors(this);
			}
		}
		else{
			/* divide vertically */
			if(x < ((x1 + x2) / 2) ){ // check x lies in which region, right or left
				newPeer.setCoordinates(x1, y1, ((x1 + x2)/2), y2);
				this.setCoordinates(((x1 + x2)/2), y1, x2, y2);
				this.addNeighbor(newPeer);
				newPeer.addNeighbor(this);
				newPeer.copyNeighbors(this);
				
			}
			else{
				newPeer.setCoordinates(((x1 + x2)/2), y1, x2, y2);
				this.setCoordinates(x1, y1, ((x1 + x2)/2), y2);
				this.addNeighbor(newPeer);
				newPeer.addNeighbor(this);
				newPeer.copyNeighbors(this);
			}
		}
		
		newPeer.setIsPeerAddedFlag();
		// Check whether current peer's neighbors have been changed.
		this.changeNeighborOfCurrentPeerNeighbors(newPeer);
		
		//return newPeer;
	}
	
	/* When you add new peer to current peer,
	 * the neighbors of current peer's neighbors also change. for ex. if there are p1 and p2
	 *  and if you add p3, it will also become a neighbor of p2
	 *  check this
	 */
	public void changeNeighborOfCurrentPeerNeighbors(Peer newPeer){
		
	//	ArrayList<Peer> tempList = new ArrayList<Peer>(this.getNeighbors());
		// Add new Peer as neighbor to current Peer's neighbors if valid
		for(Peer temp : this.getNeighbors()){
			if(temp.isNeighbor(newPeer) &&
					!(temp.getNodeIdentifier().equals(newPeer.getNodeIdentifier())) ){
				
				if(!(newPeer.getNeighbors().contains(temp)))
					newPeer.addNeighbor(temp);
				
				if(!((temp.getNeighbors()).contains(newPeer))){
					/* Create socket to that Peer and add current peer to it's neighbor list  */
					int EntryPeerPort = temp.getPortnumber();
					String peerIp = temp.getIP();
					try{
						Socket peerSocket1 = new Socket(peerIp, EntryPeerPort);
						System.out.println(" Socket to Entry Peer created.. TO CHANGE NEIGHBOR ");
						OutputStream peerOs1 = peerSocket1.getOutputStream();  
						ObjectOutputStream peerOos1 = new ObjectOutputStream(peerOs1);
						
						InputStream peerIs1 = peerSocket1.getInputStream();
						ObjectInputStream peerOis1 = new ObjectInputStream(peerIs1);
						peerOos1.writeObject(newPeer);
						Peer temp1 = (Peer) peerOis1.readObject();
						peerOis1.close();
						peerOos1.close();
						peerSocket1.close();
					}
					catch(IOException | ClassNotFoundException e){
						System.out.println();
						System.out.println(" Peer connection closed.");
					} 
				}
			}
		}
		
		// change neighbor list of current peer neighbor list
		for(Peer newTemp : this.getNeighbors()){
			
			if(!(newTemp.getNodeIdentifier()).equals(newPeer.getNodeIdentifier())){
				/* Create socket to that Peer and remove current peer from it's neighbor list */
				
				int newEntryPeerPort = newTemp.getPortnumber();
				String newPeerIp = newTemp.getIP();
				try{
					Socket peerSocket2 = new Socket(newPeerIp, newEntryPeerPort);
					System.out.println(" Socket to Entry Peer created TO CHANGE NEIGHBOR..");
					OutputStream peerOs2 = peerSocket2.getOutputStream();  
					ObjectOutputStream peerOos2 = new ObjectOutputStream(peerOs2);
					
					InputStream peerIs2 = peerSocket2.getInputStream();
					ObjectInputStream peerOis2 = new ObjectInputStream(peerIs2);
					peerOos2.writeObject(this);
					Peer temp1 = (Peer) peerOis2.readObject();
					this.copyPeerdata(temp1);
					peerOis2.close();
					peerOos2.close();
					peerSocket2.close();
				}
				catch(IOException | ClassNotFoundException e){
					System.out.println();
					System.out.println(" Peer connection closed.");
				} 
			}
		}
		System.out.println("Neighbor after Addition of new Peer : ");
		for(Peer showNeighbor : this.getNeighbors()){
			System.out.print(showNeighbor.getNodeIdentifier()+"  ");
		}
		System.out.println();
	}
	
	public BigInteger firstHashValue(String keyword) {
        try {
        	MessageDigest messageDigest;
            messageDigest = MessageDigest.getInstance("SHA1"); 
            messageDigest.update(keyword.getBytes(), 0,
            		keyword.length());
            return new BigInteger(1, messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public int secondHashValue(String keyword){
		int hash2Value = keyword.hashCode();
		int x = hash2Value % (keyword.length()) ;
		return x;
	}
	
	public static void main(String args[]){
		
		 
		int serverportnumber = Integer.parseInt(args[0]); 
		try{
			Socket client = new Socket("localhost", serverportnumber);
			
			OutputStream os = client.getOutputStream();  
			ObjectOutputStream oos = new ObjectOutputStream(os);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			InputStream is = client.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			String userInput;
			
			// New peer
			Peer p = new Peer();
			Peer EntryPoint = null;
			System.out.println("Enter Any One Command : 1. join  2.insert 3. serach.");
			while((userInput = stdIn.readLine()) != null){
				String[] in= userInput.split("\\s+");
				if( in[0].equals("join")){
					System.out.println("Enter command, hostname , node, portnumber name, x coordinate, y coordinate separated by space : ");
					userInput = stdIn.readLine();
					String[] input = userInput.split("\\s+");
					p.setCommand(input[0]);
					p.setIP(input[1]);
					p.setNodeIdentifier(input[2]);
					/* Check for duplicate node identifier , HashMap can be used to check*/
					p.setPortnumber(Integer.parseInt(input[3]));
					/* get Coordinates of point for new node*/
					p.setA(Integer.parseInt(input[4]));
					p.setB(Integer.parseInt(input[5]));
				}
				else if(in[0].equals("insert")){
					System.out.println("Enter command, keyword ");
					userInput = stdIn.readLine();
					String[] data = userInput.split("\\s+");
					p.setCommand(data[0]);
					p.setKeyword(data[1]);
				}
				else if(in[0].equals("search")){
					System.out.println("Enter command, keyword ");
					userInput = stdIn.readLine();
					String[] data = userInput.split("\\s+");
					p.setCommand(data[0]);
					p.setKeyword(data[1]);
				}
				oos.writeObject(p);	// Send object to bootstrap server for entry point peer request
				
				EntryPoint = (Peer)ois.readObject();		// Server returns the entry point peer
				System.out.println("ENTRY POINT: " + EntryPoint);
				EntryPoint.view();
				
				if(EntryPoint.getNodeIdentifier().equals(p.getNodeIdentifier())){
					p = EntryPoint;
				}
				
				int peerPortNumber = p.getPortnumber();
				int EntryPeerPort = EntryPoint.getPortnumber();
				String peerIp = EntryPoint.getIP();
				
				// Create Own server socket
				try(ServerSocket server = new ServerSocket(peerPortNumber)){
					System.out.println(" Own Server Socket is created..");
					
					if(p.getCommand().equals("insert") || p.getCommand().equals("search")){
						System.out.println(" Routing Path :"+ EntryPoint.getNodeIdentifier());
					}
					
					if(!EntryPoint.getNodeIdentifier().equals(p.getNodeIdentifier()) ){
						// Create socket to EntryPoint Peer
						Socket peerSocket = new Socket(peerIp, EntryPeerPort);
						//	System.out.println(" Socket to Entry Peer created..");
						OutputStream peerOs = peerSocket.getOutputStream();  
						ObjectOutputStream peerOos = new ObjectOutputStream(peerOs);
						
						InputStream peerIs = peerSocket.getInputStream();
						ObjectInputStream peerOis = new ObjectInputStream(peerIs);
						
						//	System.out.println(" Step 1");
						peerOos.writeObject(p);
						//	System.out.println(" Step 4");
						Peer temp = (Peer) peerOis.readObject();
						while(!(temp.getNodeIdentifier().equals(p.getNodeIdentifier()))){
							int NewPeerPort = temp.getPortnumber();
							String NewPeerIp = temp.getIP();
							Socket NewPeerSocket = new Socket(NewPeerIp, NewPeerPort);
							OutputStream NewPeerOs = NewPeerSocket.getOutputStream();  
							ObjectOutputStream NewPeerOos = new ObjectOutputStream(NewPeerOs);
							
							InputStream NewPeerIs = NewPeerSocket.getInputStream();
							ObjectInputStream NewPeerOis = new ObjectInputStream(NewPeerIs);
							/* to print path for storing and retriving keyword */
							if(p.getCommand().equals("insert") || p.getCommand().equals("search")){
								System.out.println(" Routing Path :"+ EntryPoint.getNodeIdentifier());
							}	
							//	System.out.println(" Contacting peer " + temp.getNodeIdentifier());
							NewPeerOos.writeObject(p);
							temp = (Peer) NewPeerOis.readObject();
							
						}
						p = temp;
						p.view();
					}
					
					while (true){
						new PeerThread(server.accept(), p).start();
					}
				}
				catch(IOException e){
					System.out.println( EntryPoint.getNodeIdentifier() + " Server failed to listen on port");
					System.exit(1);
				}
				
				
			}
		}	
		catch(IOException | ClassNotFoundException e)
		{
			System.exit(1);
		}
		finally {
            
			
         }
	}
}