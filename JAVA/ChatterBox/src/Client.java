import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("10.156.147.211",5000);			
			InputStream is = (InputStream) socket.getInputStream();
			
			byte[] readbytes = new byte[100];
			is.read(readbytes, 0, readbytes.length);
			System.out.println(new String(readbytes,"UTF-8"));
			
		} catch(UnknownHostException e1)
		{
			System.out.println("unknownhost e");
			e1.printStackTrace();
		}
		catch (Exception e) {
		
			System.out.println("exception e");
			e.printStackTrace();
		} 
	
		if(!socket.isClosed())
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("IOexception e");
				e.printStackTrace();
			}
				

	}

}
