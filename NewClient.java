import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;


public class NewClient extends Thread implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Bootstrap bstserver = new Bootstrap();
	private Socket client = null;
	
	public NewClient(Socket socket){
		super("NewClient");
		
		this.client = socket;
	}
	
	public void run(){
		InputStream is;
		try {
			is = this.client.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			OutputStream os = this.client.getOutputStream();  
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			Peer p = null;
			while(true){
				while( (p = (Peer)ois.readObject()) != null ){
					
					if(p.getCommand().equals("join")){
						
						System.out.println("Object P :");
						
						System.out.println(p.getNodeIdentifier());
						System.out.println(bstserver.getEntryPeer());
						
						if(bstserver.getEntryPeer() == null){
							p.setCoordinates(0, 0, 10, 10);
							bstserver.setEntryPeer(p);
							bstserver.getEntryPeer().setIsPeerAddedFlag();
							System.out.print (bstserver.getEntryPeer().getNodeIdentifier() + " ");
							System.out.print (bstserver.getEntryPeer().getIP());
						}
						else if(p.getNodeIdentifier().equals(bstserver.getEntryPeer().getNodeIdentifier())){
							// change the entrypeer coordinates
							//	System.out.println("Peer :"+p );
							//	bstserver.setEntryPeer(p);
							bstserver.changeCoordinates(p);
						}
						else{
							
						}
					}
					
					
					oos.writeObject(bstserver.getEntryPeer());
					
				}
			}
			
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Peer connection with server is closed.");
			//e.printStackTrace();
		}
		finally {
			try {
				this.client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Peer connection with server is closed.");
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
