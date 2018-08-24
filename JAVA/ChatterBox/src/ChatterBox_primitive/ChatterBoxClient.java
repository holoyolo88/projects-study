package ChatterBox_primitive;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatterBoxClient extends Application {
	Socket socket;

	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress("10.156.147.211", 5000));

					Platform.runLater(() -> {
						displayText("연결 완료 : " + socket.getRemoteSocketAddress());
						btnConn.setText("Stop");
						btnSend.setDisable(false);
					});

				} catch (Exception e) {
					Platform.runLater(() -> displayText("서버 통신 안됨"));
					if (!socket.isClosed()) {
						stopClient();
					}
					return;
				}
				receive();
			}

		};
		thread.start();
	}

	void stopClient() {
		try {
			Platform.runLater(() -> {
				displayText("연결 끊음");
				btnConn.setText("Start");
				btnSend.setDisable(true);
			});

			// 언제든지 소켓 연결이 끊어질 수 있음을 명심하여 처리
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}

	void receive() {

		while (true) {
			try {
				byte[] byteArr = new byte[100];
				InputStream is = socket.getInputStream();
				// read() : byte를 한번에 보낼 경우 연결이 끊어지면 보낼 수 없으므로 사용
				int readByteCount = is.read(byteArr);

				if (readByteCount == -1) {
					throw new IOException();
				}
				String data = new String(byteArr, 0, readByteCount, "UTF-8");
				Platform.runLater(() -> displayText("받기 완료 : " + data));
			} catch (Exception e) {
				Platform.runLater(() -> displayText("서버 통신 불가"));
				if (!socket.isClosed())
					stopClient();
				break;
			}
		}
	}

	void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream os = socket.getOutputStream();
					os.write(byteArr);
					os.flush();
					Platform.runLater(() -> displayText("보내기 완료"));
				} catch (Exception e) {
					Platform.runLater(() -> displayText("서버 통신 안됨"));
					stopClient();
				}
			}
		};
		thread.start();
	}

	TextArea txtDisplay;
	TextField txtInput;
	Button btnConn, btnSend;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
		txtDisplay = new TextArea();

		txtDisplay.setEditable(false);
		BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
		root.setCenter(txtDisplay);

		BorderPane bottom = new BorderPane();
		txtInput = new TextField();
		txtInput.setPrefSize(60, 30);
		BorderPane.setMargin(txtInput, new Insets(0, 1, 1, 1));

		btnConn = new Button("Start");
		btnConn.setPrefSize(60, 30);
		btnConn.setOnAction(e -> {
			if (btnConn.getText().equals("Start")) {
				startClient();
			} else if (btnConn.getText().equals("Stop"))
				stopClient();
		});

		btnSend = new Button("Send");
		btnSend.setPrefSize(60, 30);
		btnSend.setDisable(true);
		btnSend.setOnAction(e -> send(txtInput.getText()));

		bottom.setCenter(txtInput);
		bottom.setLeft(btnConn);
		bottom.setRight(btnSend);
		root.setBottom(bottom);

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("app.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Client");
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
	}

	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
