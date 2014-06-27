import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
//import java.net.SocketException;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PeerThread extends Thread {
	public Peer peer = new Peer();
	private Socket client = null;
	
	
	public PeerThread(Socket socket, Peer p){
		super("PeerThread");
		this.peer = p;
		this.client = socket;
	}
	
public void run(){
		
		try {
			InputStream isThread = this.client.getInputStream();
			ObjectInputStream oisThread = new ObjectInputStream(isThread);
			
			OutputStream osThread = this.client.getOutputStream();  
			ObjectOutputStream oosThread = new ObjectOutputStream(osThread);
			
			Peer clientPeer = null;
			while(true){
				while( (clientPeer = (Peer)oisThread.readObject()) != null ){
					
					// Get the coordinates of Self / Entry point
					int x1 = this.peer.getX1();
					int y1 = this.peer.getY1();
					int x2 = this.peer.getX2();
					int y2 = this.peer.getY2();
					int x = clientPeer.getA();
					int y = clientPeer.getB();
					
					if(clientPeer.getCommand().equals("join")){
					
						if(!clientPeer.getIsPeerAddedFlag() ){ // Peer not added, needs to be added, start from entry peer
							//	System.out.println(clientPeer.getNodeIdentifier() +" in  "+ this.peer.getNodeIdentifier());
							//	System.out.println(" Step 2");
							//	clientPeer.view();
							System.out.println(" In first condition "+x1+" "+y1+" "+x2+" "+y2+" "+x+" "+y);
							
							if(x >= x1 && x <= x2 && y >= y1 && y <= y2){ // Check the point of new node lies in entry point
								
								System.out.println(" Split first node..");
								this.peer.spiltMySpace(clientPeer, x, y);
									System.out.println();
								this.peer.view();
									System.out.println();
								clientPeer.view();
								oosThread.writeObject(clientPeer);
							}
							else{										 // get nearest peer
								ArrayList<Peer> list = this.peer.getNeighbors();
								int min_x = 10;
								int min_y = 10;
								Peer min_dist_peer = null;
								for(Peer neighbor : list){
									// Get the coordinates of current neighbor
									int a1 = neighbor.getX1();
									int b1 = neighbor.getY1();
									int a2 = neighbor.getX2();
									int b2 = neighbor.getY2();
									System.out.println(a1+" "+b1+" "+a2+" "+b2+" "+x+" "+y);
									if(x >= a1 && x <= a2 && y >=b1 && y <= b2){
										min_dist_peer = neighbor;
										oosThread.writeObject(min_dist_peer);
									//	System.out.println(" Min dest peer 1:" + min_dist_peer.getNodeIdentifier());
									}
									else{
										// calculate the center point of node/peer
										int center_x = (a1 + a2)/2;
										int center_y = (b1 + b2)/2;
										if(Math.abs(x - center_x) < min_x && Math.abs(y - center_y) < min_y){
											min_x = center_x;
											min_y = center_y;
											min_dist_peer = neighbor;
										//	System.out.println(" Min dest peer 2:" + min_dist_peer.getNodeIdentifier());
										} 
									}
								}
							//	System.out.println(" returning Min dest peer :" + min_dist_peer.getNodeIdentifier()+ " " + min_dist_peer.getIP() + " " + min_dist_peer.getPortnumber()+" ");
								oosThread.writeObject(min_dist_peer);
							}
							
						}
						else{
							//System.out.println(" CHANGING NEIGHBOR..");
							ArrayList<Peer> tempArraylist = this.peer.getNeighbors();
							Boolean isPeerPresentAsNeighbor = false;
							String ClientPeerName = clientPeer.getNodeIdentifier();
							//System.out.println(" Peer to be Checked "+ ClientPeerName);
							Peer peerToBeRemoved = null;
							for(Peer node : tempArraylist){
								String thisNodeName = node.getNodeIdentifier();
								if(ClientPeerName.equals(thisNodeName)){
									isPeerPresentAsNeighbor = true;
									peerToBeRemoved = node;
								}
							}
							
							if(isPeerPresentAsNeighbor){
							//	System.out.println("Neighbor present and should be removed : ");
								if(!this.peer.isNeighbor(clientPeer)){
									this.peer.removeNeighbor(peerToBeRemoved);
									
									/* Now remove this neighbor from ClientPeer i.e. vise versa */
									String thisPeerName = this.peer.getNodeIdentifier();
									Boolean isPeerPresentAsNeighborInClient = false;
									Peer removePeer =null;
									for(Peer node : clientPeer.getNeighbors()){
										String thisNodeName = node.getNodeIdentifier();
										if(thisPeerName.equals(thisNodeName)){
												isPeerPresentAsNeighborInClient = true;
												removePeer = node;
										}
									} 
									if(isPeerPresentAsNeighborInClient){
											clientPeer.removeNeighbor(removePeer);
									}
									
								}
							}
							else{
								//System.out.println("Neighbor not present and should be added : ");
								if(this.peer.isNeighbor(clientPeer)){
									this.peer.addNeighbor(clientPeer);
								}
							}
							
							System.out.println("Neighbor after Addition of new Peer : ");
							for(Peer showNeighbor : this.peer.getNeighbors()){
								System.out.print(showNeighbor.getNodeIdentifier()+"  ");
							}
							System.out.println();
							oosThread.writeObject(clientPeer); 
						} 
					}
					else if(clientPeer.getCommand().equals("insert")){
						
							String keyword = clientPeer.getKeyword();
							/* Claculate the Hash values i.e. x and y coordinates */
							BigInteger hash1Value = clientPeer.firstHashValue(keyword);
							int length = keyword.length();
							BigInteger bfSize = new BigInteger(new Integer(length).toString());
							BigInteger yBig = hash1Value.mod(bfSize) ;
							int yVal = yBig.intValue(); 							// Hash Y coordinate
							
							int hash2Value = keyword.hashCode();
							int xVal = hash2Value % (keyword.length()) ;
							xVal = Math.abs(xVal);									//Hash X coordinate
							yVal = Math.abs(yVal);
							yVal = yVal % 10;
							xVal = xVal % 10;
							//int hash2value = this.peer.secondHashValue(keyword);
							clientPeer.setA(xVal);
							clientPeer.setB(yVal);
							System.out.println(" X - coordinate of Hash :"+xVal);
							System.out.println(" Y - coordinate of Hash :"+yVal);
							
							//Check first for current Peer i.e. first peer
							int x11 = this.peer.getX1();
							int y11 = this.peer.getY1();
							int x22 = this.peer.getX2();
							int y22 = this.peer.getY2();
							if(xVal >= x11 && xVal <= x22 && yVal >= y11 && yVal <= y22){ 
								// Check the point of new node lies in current node
								System.out.println(" Keyword stored in this node..");
								this.peer.addDataKeyword(keyword);
								System.out.println(this.peer.getDataKeyword());
							//	oosThread.writeObject(this);
							}
							else{
								ArrayList<Peer> dataList = this.peer.getNeighbors();
								int min_x = 10;
								int min_y = 10;
								Peer min_dest_peer = null;
								for(Peer neighbor : dataList){
									// Get the coordinates of current neighbor
									int a1 = neighbor.getX1();
									int b1 = neighbor.getY1();
									int a2 = neighbor.getX2();
									int b2 = neighbor.getY2();
									if(xVal >= a1 && xVal <= a2 && yVal >=b1 && yVal <= b2){
										min_dest_peer = neighbor;
									//	System.out.println(" Min dest peer 1:" + min_dist_peer.getNodeIdentifier());
									}
									else{
										// calculate the center point of node/peer
										int center_x = (a1 + a2)/2;
										int center_y = (b1 + b2)/2;
										if(Math.abs(xVal - center_x) < min_x && Math.abs(yVal - center_y) < min_y){
											min_x = center_x;
											min_y = center_y;
											min_dest_peer = neighbor;
										//	System.out.println(" Min dest peer 2:" + min_dist_peer.getNodeIdentifier());
										} 
									}
								}
							//	System.out.println(" returning Min dest peer :" + min_dist_peer.getNodeIdentifier()+ " " + min_dist_peer.getIP() + " " + min_dist_peer.getPortnumber()+" ");
								oosThread.writeObject(min_dest_peer);
							}
						}
					else if(clientPeer.getCommand().equals("search")){
						
						String keyword = clientPeer.getKeyword();
						/* Calculate the Hash values i.e. x and y coordinates */
						BigInteger hash1Value = clientPeer.firstHashValue(keyword);
						int length = keyword.length();
						BigInteger bfSize = new BigInteger(new Integer(length).toString());
						BigInteger yBig = hash1Value.mod(bfSize) ;
						int yVal = yBig.intValue(); 							// Hash Y coordinate
						
						int hash2Value = keyword.hashCode();
						int xVal = hash2Value % (keyword.length()) ;
						xVal = Math.abs(xVal);									//Hash X coordinate
						yVal = Math.abs(yVal);
						yVal = yVal % 10;
						xVal = xVal % 10;
						//int hash2value = this.peer.secondHashValue(keyword);
						clientPeer.setA(xVal);
						clientPeer.setB(yVal);
						System.out.println(" X - coordinate of Hash :"+xVal);
						System.out.println(" Y - coordinate of Hash :"+yVal);
						
						//Check first for current Peer i.e. first peer
						int x11 = this.peer.getX1();
						int y11 = this.peer.getY1();
						int x22 = this.peer.getX2();
						int y22 = this.peer.getY2();
						if(xVal >= x11 && xVal <= x22 && yVal >= y11 && yVal <= y22){ 
							// Check the point of new node lies in current node
							System.out.println(" Keyword found in this node..");
							System.out.println(this.peer.getDataKeyword());
							
						//	oosThread.writeObject(this);
						}
						else{
							ArrayList<Peer> dataList = this.peer.getNeighbors();
							int min_x = 10;
							int min_y = 10;
							Peer min_dest_peer = null;
							for(Peer neighbor : dataList){
								// Get the coordinates of current neighbor
								int a1 = neighbor.getX1();
								int b1 = neighbor.getY1();
								int a2 = neighbor.getX2();
								int b2 = neighbor.getY2();
								if(xVal >= a1 && xVal <= a2 && yVal >=b1 && yVal <= b2){
									min_dest_peer = neighbor;
								//	System.out.println(" Min dest peer 1:" + min_dist_peer.getNodeIdentifier());
								}
								else{
									// calculate the center point of node/peer
									int center_x = (a1 + a2)/2;
									int center_y = (b1 + b2)/2;
									if(Math.abs(xVal - center_x) < min_x && Math.abs(yVal - center_y) < min_y){
										min_x = center_x;
										min_y = center_y;
										min_dest_peer = neighbor;
									//	System.out.println(" Min dest peer 2:" + min_dist_peer.getNodeIdentifier());
									} 
								}
							}
						//	System.out.println(" returning Min dest peer :" + min_dist_peer.getNodeIdentifier()+ " " + min_dist_peer.getIP() + " " + min_dist_peer.getPortnumber()+" ");
							oosThread.writeObject(min_dest_peer);
						}
					}
					else if(clientPeer.getCommand().equals("view")){
						
					}
						
				}
					
			}
				
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println();
			System.out.println(" Socket closed");
		}
		finally{
			try {
				this.client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println();
				System.out.println(" Socket closed");
			//	e.printStackTrace();
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
