import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		ServerSocket serversocket;
		Socket socket;
		try {
			serversocket = new ServerSocket(5000);

		
				//while(true) {
				System.out.println("waiting");
				socket = serversocket.accept();
				InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
				
				System.out.println("conneting with "+isa.getHostName());
				//}
			//OutputStream os = (OutputStream)socket.getOutputStream();
			//String message = new String("succeed");
			//os.write(message.getBytes());
			
			serversocket.close();
			socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
