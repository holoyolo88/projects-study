package ServerClientExample;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// change localhost into IP address 
			socket = new Socket("localhost", 5000);
			InputStream is = (InputStream) socket.getInputStream();

			byte[] readbytes = new byte[100];
			is.read(readbytes, 0, readbytes.length);
			System.out.println(new String(readbytes, "UTF-8"));

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!socket.isClosed())
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("IOexception e");
				e.printStackTrace();
			}
	}
}
