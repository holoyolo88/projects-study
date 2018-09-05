package ChatterBox_2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import java.sql.*;

public class ChatterBoxServer extends Application {
	// ExecutorService : 쓰레드풀 참조 변수
	ExecutorService executorService;
	// ServerSocket : 서버 소켓 참조 변수
	ServerSocket serverSocket;
	// Vector : 쓰레드에 안전한 컬렉션
	List<Client> connections = new Vector<Client>();

	// 드라이버 명 지정
	static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
	// 데이터베이스명 지정
	static final String DBNAME = "mydb";
	// URL 지정
	// ?serverTimezone=UTC : server time zone value '' is unrecognized or represents
	// more than one time zone
	static final String DBURL = "jdbc:mysql://127.0.0.1:3306/" + DBNAME + "?serverTimezone=UTC" +"&allowPublicKeyRetrieval=true"+"&useSSL=false";
	Connection connect = null;
	Statement statement = null;

	void startServer() {
		// newFixedThreadPool : 클라이언트의 폭증으로 인해 서버의 과도한 스레드 생성 방지를 위해 스레드풀 생성
		// : 스레드가 작업을 처리하지 않고 놀고 있더라도 스레드 개수가 줄지 않음.
		// newCachedThreadPool : 1개 이상의 스레드가 추가되었을 경우 60초동안 추가된 스레드가 아무 작업을 하지 않으면 추가된
		// 스레드를 종료하고 풀에서 제거
		executorService = Executors.newFixedThreadPool(
				// CPU 코어의 수만큼 최대 스레드를 사용하는 스레드풀 생성
				Runtime.getRuntime().availableProcessors());
		try {

			// mysql에서 제공하는 Driver클래스를 JVM method area에 로드(객체 반환)
			Class.forName(DRIVERNAME);

			// change id into mysql id
			String userId = "id";
			// change password into mysql password
			String userPassword = "password";
			// DriverManeger로 Connection 객체 생성
			connect = DriverManager.getConnection(DBURL, userId, userPassword);

			// 서버 소켓 생성
			serverSocket = new ServerSocket();
			// 포트 바인딩 : 서버에 멀티 IP가 할당된 경우 해당 IP로 접속 시에만 연결 수락
			// new ServerSocket(5000)도 가능하나 IP 제한 불가
			serverSocket.bind(new InetSocketAddress("localhost", 5001));

			Platform.runLater(() -> {
				displayText("Status | DB : connecting with MySql");
				displayText("Status | server : running");
				btnStartStop.setText("Stop");
			});
		} catch (Exception e) {
			e.printStackTrace();
			if (connect != null || !serverSocket.isClosed())
				stopServer();
			return;
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// accept : 연결 수락 전까지 블로킹
						Socket socket = serverSocket.accept();
						String message = "Status | server : connecting with" + socket.getRemoteSocketAddress()
								+ Thread.currentThread().getName();
						// UI 변경
						Platform.runLater(() -> displayText(message));

						Client client = new Client(socket);
						connections.add(client);
						Platform.runLater(() -> displayText("Status | server : connectings" + connections.size()));
					} catch (Exception e) {
						try {
							if (!connect.isClosed() || !serverSocket.isClosed()) {
								stopServer();
							}
						} catch (Exception e1) {
							e.printStackTrace();
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
			// connect close : 수행 시간이 조금 더 김
			if (connect != null && !connect.isClosed())
				connect.close();
			if (serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			if (executorService != null && !executorService.isShutdown())
				executorService.shutdownNow();

			Platform.runLater(() -> {
				displayText("Status | server : stopped");
				displayText("Status | DB : stopped");
				btnStartStop.setText("Start");
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class Client {
		// Socket : 소켓 참조 변수
		Socket socket;
		String id;

		Client(Socket socket) {
			this.socket = socket;
			receiveData();
		}

		void receiveData() {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						statement = connect.createStatement();

						byte[] byteArr = new byte[51];
						InputStream is = socket.getInputStream();

						int readByteCount = is.read(byteArr);

						if (readByteCount == -1) {
							throw new IOException();
						}
						String message = "Status | server : login request from" + socket.getRemoteSocketAddress()
								+ Thread.currentThread().getName();
						Platform.runLater(() -> displayText(message));
						String data = new String(byteArr, 0, readByteCount);

						String[] userData = data.split("/");
						String classifier = userData[0];
						String dataId = userData[1];
						String dataPassword = userData[2];
						ResultSet resultSet;
						
						if (classifier.equals("signup")) {
							String SQL_INSERT = "INSERT INTO chatterbox_user VALUES('" + dataId + "','" + dataPassword + "')";
							// executeUpdate : UPDATE, INSERT, DELETE
							statement.executeUpdate(SQL_INSERT);
							SQL_INSERT = "INSERT INTO chatterbox_record (ChatterBox_user_id) VALUES('"+dataId+"')";
							statement.executeUpdate(SQL_INSERT);
						}
						// executeQuery : SELECT
							String SQL_SELECT = "SELECT * FROM chatterbox_user WHERE id='" + dataId + "' AND password='"+ dataPassword + "'";
							resultSet = statement.executeQuery(SQL_SELECT);
							
							if (resultSet.next()) {
								id = dataId;
								sendResult("true");
								sendData(dataId);
								receiveMessage(dataId);
							} else {
								sendResult("false");
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								connections.remove(Client.this);
								String failmessage = "Status | server : connecting failed with "
										+ socket.getRemoteSocketAddress() + Thread.currentThread().getName();
								Platform.runLater(() -> displayText(failmessage));
								socket.close();
							}
					} catch (Exception e) {
						e.printStackTrace();
						sendResult("false");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						connections.remove(Client.this);
						String message = "Status | server : connecting failed with "
								+ socket.getRemoteSocketAddress() + Thread.currentThread().getName();
						Platform.runLater(() -> displayText(message));
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} 
					

				}
			};
			executorService.submit(runnable);
		}

		void sendResult(String result) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {

					try {
						byte[] byteArrResult = result.getBytes("UTF-8");
						OutputStream os = socket.getOutputStream();
						os.write(byteArrResult);
						os.flush();
						Platform.runLater(() -> displayText("sendResult"));
					} catch (IOException e) {
						try {
							connections.remove(Client.this);
							String message = "Status | server : connecting failed" + socket.getRemoteSocketAddress()
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
		void sendData(String dataId) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						
						String SQL_SELECT = "SELECT * FROM chatterbox_record WHERE ChatterBox_user_id='"+dataId+"'";
						ResultSet resultSet = statement.executeQuery(SQL_SELECT);
						
						if(resultSet.next()) {
							// mysql의 문자셋 utf8mb4_0900_ai_ci 
							try {
						byte[] byteArrReceived = resultSet.getBytes("received");
							
						OutputStream os = socket.getOutputStream();
						os.write(byteArrReceived);
						os.flush();}
							catch(Exception e) {
								
							}
						try {
							Thread.sleep(1500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
						byte[] byteArrSent = resultSet.getBytes("sent");
						OutputStream os = socket.getOutputStream();
						os.write(byteArrSent);
						os.flush();
						}
						catch(Exception e) {
							
						}
						}
					} catch (Exception e) {
						e.printStackTrace();
						try {
							connections.remove(Client.this);
							String message = "Status | server : connecting failed" + socket.getRemoteSocketAddress()
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
		
		
		void receiveMessage(String dataId) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							byte[] byteArr = new byte[101];
							InputStream is = socket.getInputStream();

							int readByteCount = is.read(byteArr);

							if (readByteCount == -1) {
								throw new IOException();
							}
							
							String message = "Status | server : request from" + socket.getRemoteSocketAddress()
									+ Thread.currentThread().getName();
							Platform.runLater(() -> displayText(message));
							String data = new String(byteArr, 0, readByteCount);
							
							String SQL_UPDATE = "UPDATE chatterbox_record SET sent=concat(sent,'\n"+data+"') WHERE ChatterBox_user_id = "+"'"+dataId+"'";
							statement.executeUpdate(SQL_UPDATE);
							
							for (Client client : connections) {
								// 연결된 소켓을 제외한 모든 소켓에 data 전송
								if (client.socket != socket) {
									SQL_UPDATE = "UPDATE chatterbox_record SET received=concat(received,'\n"+data+"') WHERE ChatterBox_user_id = "+"'"+client.id+"'";
									statement.executeUpdate(SQL_UPDATE);
									client.sendMessage(data);}
							}
						}
					}catch(SQLException se){
						se.printStackTrace();
				}catch (IOException e) {
						try {
							connections.remove(Client.this);
							String message = "Status | server : connecting failed with "
									+ socket.getRemoteSocketAddress() + Thread.currentThread().getName();
							Platform.runLater(() -> displayText(message));
							socket.close();
						} catch (IOException e2) {
						}
					}
				}
			};
			executorService.submit(runnable);
		}

		void sendMessage(String data) {
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
							String message = "Status | server : connecting failed" + socket.getRemoteSocketAddress()
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