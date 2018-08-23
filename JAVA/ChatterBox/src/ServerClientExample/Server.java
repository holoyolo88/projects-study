package ServerClientExample;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server  {
	
	public static void main(String[] args) {
		ServerSocket serversocket=null;
		try {
			serversocket = new ServerSocket(5000);
			
				while(true) {
				System.out.println("server : waiting");
				Socket socket = serversocket.accept();
				InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
				
				System.out.println("server : conneting with "+isa.getHostName());
				OutputStream os = (OutputStream)socket.getOutputStream();
				String message = new String("message from server");
				os.write(message.getBytes());
				}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(!serversocket.isClosed())
		try {
			serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
