// Bootstrap Class contains all the functionality managed by Bootstrap Server
import java.net.*;
import java.io.*;
import java.util.*;

public class Bootstrap implements Serializable{

	private Peer EntryPeer;
	public static int numberofnodes = 0;
	
	public Bootstrap(){
		this.EntryPeer = null;
		
	}
	
	public Peer getEntryPeer(){
		return this.EntryPeer;
	}
	
	public void setEntryPeer(Peer peername){
		this.EntryPeer = peername;
	}
	
	public void changeCoordinates(Peer p){
		int x1 = p.getX1();
		int y1 = p.getY1();
		int x2 = p.getX2();
		int y2 = p.getY2();
		this.EntryPeer.setCoordinates(x1, y1, x2, y2);
		
		
		System.out.println(" New Entry Peer :");
		this.EntryPeer.view();
	}

	public static void main(String args[])
	{
		//String host = args[0];
		//int portnumber = Integer.parseInt(args[0]);
		Bootstrap bstserver = new Bootstrap();
		
		try(ServerSocket server = new ServerSocket(5000)){
			while (true){
				new NewClient(server.accept()).start();
			}
		}
		catch(IOException e){
			System.out.println(" Server failed to listen on port");
			System.exit(1);
		}
		
	}
		
}