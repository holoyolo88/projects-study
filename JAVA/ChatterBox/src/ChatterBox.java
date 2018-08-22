
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA_2_3.portable.OutputStream;

public class ChatterBox extends Application {

	ExecutorService executorService;
	ServerSocket serverSocket;
	// Vector : 쓰레드에 안전한 컬렉션
	List<Client> connections = new Vector<Client>();

	void startServer() {
		// newFixedThreadPool : 클라이언트의 폭증으로 인해 서버의 과도한 스레드 생성 방지를 위해 스레드풀 생성
		// : 스레드가 작업을 처리하지 않고 놀고 있더라도 스레드 개수가 줄지 않음.
		// newCachedThreadPool : 1개 이상의 스레드가 추가되었을 경우 60초동안 추가된 스레드가 아무 작업을 하지 않으면 추가된
		// 스레드를 종료하고 풀에서 제거
		executorService = Executors.newFixedThreadPool(
				// CPU 코어의 수만큼 최대 스레드를 사용하는 스레드풀 생성
				Runtime.getRuntime().availableProcessors());
		try {
			serverSocket = new ServerSocket();
			// 포트 바인딩 : 서버에 멀티 IP가 할당된 경우 해당 IP로 접속 시에만 연결 수락
			// new ServerSocket(5000)도 가능하나 IP 제한 불가
			serverSocket.bind(new InetSocketAddress("10.156.147.211", 5000));

		} catch (IOException e) {
			if (!serverSocket.isClosed()) {
				e.printStackTrace();
				stopServer();
				return;
			}
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					displayText("서버 시작");
					btnStartStop.setText("start");
				});
				while (true) {
					try {// accept : 블로킹
						Socket socket = serverSocket.accept();
						String message = "연결 수락 : " + socket.getRemoteSocketAddress()
								+ Thread.currentThread().getName();
						Platform.runLater(() -> displayText(message));

						Client client = new Client(socket);
						connections.add(client);
						Platform.runLater(() -> displayText("연결 개수 : " + connections.size()));
					} catch (Exception e) {
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		executorService.submit(runnable);
	}

	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}

			for (Client client : connections)
				client.socket.close();
			// serverSocket NullPointerE
			if (serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			if (executorService != null && !executorService.isShutdown())
				executorService.shutdownNow();

			Platform.runLater(() -> {
				displayText("서버 시작");
				btnStartStop.setText("Stop");
			});
		} catch (Exception e) {
		}
	}

	class Client {
		Socket socket;

		Client(Socket socket) {
			this.socket = socket;
			receive();
		}

	void receive() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						byte[] byteArr = new byte[100];
						InputStream is = socket.getInputStream();
						
						int readByteCount= is.read(byteArr);
						
						if(readByteCount == -1)
							{throw new IOException();}
						String message = "요청 처리 : "+socket.getRemoteSocketAddress() + Thread.currentThread().getName();
						Platform.runLater(()->displayText(message));
						System.out.println(readByteCount);
						String data = new String(byteArr, 0,readByteCount);
						
						for(Client client : connections) {
							client.send(data);
						}
					}
				}catch(IOException e) {
					try {
						//why?
						connections.remove(Client.this);
						String message = "클라이언트 통신 불가 : "+ socket.getRemoteSocketAddress()+Thread.currentThread().getName();
						Platform.runLater(()->
						displayText(message));
						socket.close();
					}catch(IOException e2) {}
				}
			};
		};
		executorService.submit(runnable);
	}

	void send(String data) {
		Runnable runnable = new Runnable() {
		@Override
		public void run(){
			byte[] byteArr = data.getBytes("UTF-8");
		
		OutputStream os = socket.getOutputStream();
		
		
		os.write(data.getBytes());
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
