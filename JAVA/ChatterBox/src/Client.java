import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("10.156.147.211",5000);			
			InputStream is = (InputStream) socket.getInputStream();
			
//			byte[] readbytes = new byte[100];
//			System.out.println(is.read(readbytes));
			socket.close();
		} catch (UnknownHostException e) {
			System.out.println("unknown");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO");
			e.printStackTrace();
		}
		
		
		
				

	}

}
