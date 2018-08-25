package ChatterBox_changedUI_addedFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatterBoxServer extends Application {
	// ExecutorService : 쓰레드풀 참조 변수
	ExecutorService executorService;
	// ServerSocket : 서버 소켓 참조 변수
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
			// 서버 소켓 생성
			serverSocket = new ServerSocket();
			// 포트 바인딩 : 서버에 멀티 IP가 할당된 경우 해당 IP로 접속 시에만 연결 수락
			// new ServerSocket(5000)도 가능하나 IP 제한 불가
			serverSocket.bind(new InetSocketAddress("localhost", 5001));

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
					displayText("Status | run server");
					btnStartStop.setText("Stop");
				});
				while (true) {
					try {
						// accept : 연결 수락 전까지 블로킹
						Socket socket = serverSocket.accept();
						String message = "Status | connecting with" + socket.getRemoteSocketAddress()
								+ Thread.currentThread().getName();
						// UI 변경
						Platform.runLater(() -> displayText(message));

						Client client = new Client(socket);
						connections.add(client);
						Platform.runLater(() -> displayText("Status | connectings" + connections.size()));
					} catch (Exception e) {
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		// submit : 쓰레드 풀의 작업 큐에 객체를 넣어 작업 처리를 요청
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
			if (serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			if (executorService != null && !executorService.isShutdown())
				executorService.shutdownNow();

			Platform.runLater(() -> {
				displayText("Status | stop server");
				btnStartStop.setText("Start");
			});
		} catch (Exception e) {
		}
	}

	class Client {
		// Socket : 소켓 참조 변수
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
						while (true) {
							byte[] byteArr = new byte[100];
							InputStream is = socket.getInputStream();

							int readByteCount = is.read(byteArr);

							if (readByteCount == -1) {
								throw new IOException();
							}
							String message = "Status | request from" + socket.getRemoteSocketAddress()
									+ Thread.currentThread().getName();
							Platform.runLater(() -> displayText(message));
							String data = new String(byteArr, 0, readByteCount);

							for (Client client : connections) {
								// 연결된 소켓을 제외한 모든 소켓에 data 전송
								if (client.socket != socket)
									client.send(data);
							}
						}
					} catch (IOException e) {
						try {
							connections.remove(Client.this);
							String message = "Status | connecting failed with " + socket.getRemoteSocketAddress()
									+ Thread.currentThread().getName();
							Platform.runLater(() -> displayText(message));
							socket.close();
						} catch (IOException e2) {
						}
					}
				}
			};
			executorService.submit(runnable);
		}

		void send(String data) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {

					try {
						byte[] byteArr = data.getBytes("UTF-8");
						OutputStream os = socket.getOutputStream();
						os.write(byteArr);
						os.flush();
					} catch (IOException e) {
						try {
							String message = "Status | connecting failed" + socket.getRemoteSocketAddress()
									+ Thread.currentThread().getName();
							Platform.runLater(() -> displayText(message));
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}

				}
			};
			executorService.submit(runnable);
		}
	}

	TextArea txtDisplay;
	Button btnStartStop;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
		txtDisplay = new TextArea();

		txtDisplay.setEditable(false);
		BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
		root.setCenter(txtDisplay);

		btnStartStop = new Button("Start");
		btnStartStop.setPrefHeight(30);
		btnStartStop.setMaxWidth(Double.MAX_VALUE);

		btnStartStop.setOnAction(e -> {
			if (btnStartStop.getText().equals("Start")) {
				startServer();
			} else if (btnStartStop.getText().equals("Stop")) {
				stopServer();
			}
		});

		root.setBottom(btnStartStop);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Server");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.show();
	}

	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}

	public static void main(String[] args) {
		launch(args);
	}
}