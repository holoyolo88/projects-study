package ChatterBox_changedUI_addedFunctions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ChatterBoxClient extends Application {
	// Socket 참조 변수
	Socket socket;

	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					// 소켓 생성
					socket = new Socket();
					// 
					socket.connect(new InetSocketAddress("localhost", 5001));
					// UI 변경
					Platform.runLater(() -> {
						displayStatusText("Status | connected with" +socket.getRemoteSocketAddress());
						btnConn.setText("Stop");
						btnSend.setDisable(false);
					});

				} catch (Exception e) {
					Platform.runLater(() -> displayStatusText("Status | connecting failed"));
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
				displayStatusText("Status | disconnect");
				btnConn.setText("Start");
				btnSend.setDisable(true);
			});

			// 언제든지 소켓 연결이 끊어질 수 있음
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}

	void receive() {

		while (true) {
			try {
				byte[] byteArr = new byte[101];
				InputStream is = socket.getInputStream();
				// read() : byte를 한번에 보낼 경우 연결이 끊어지면 보낼 수 없으므로 사용
				int readByteCount = is.read(byteArr);

				if (readByteCount == -1) {
					throw new IOException();
				}
				String data = new String(byteArr, 0, readByteCount, "UTF-8");
				Platform.runLater(() -> {
				displayReceivedText(data);
				displaySentText("");
				displayStatusText("Status | received message");});
			} catch (Exception e) {
				Platform.runLater(() -> displayStatusText("Status | connecting failed"));
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
					Platform.runLater(() -> {
					displaySentText(data);
					displayReceivedText("");
					displayStatusText("Status | send message");});
				} catch (Exception e) {
					Platform.runLater(() -> displayStatusText("Status | connecting failed"));
					stopClient();
				}
			}
		};
		thread.start();
	}

	Label statusTxtDisplay;
	TextArea receivedTxtDisplay;
	TextArea sentTxtDisplay;
	TextField txtInput;
	Button btnConn, btnSend;
	Label txtCnt;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPrefSize(500,300);
		
		statusTxtDisplay = new Label("Status | waiting to press Start");
		statusTxtDisplay.setPrefSize(500,30);
		
		receivedTxtDisplay = new TextArea();
		receivedTxtDisplay.setPrefSize(250, 300);
		receivedTxtDisplay.setEditable(false);
		
		sentTxtDisplay = new TextArea();
		sentTxtDisplay.setPrefSize(250, 300);
		sentTxtDisplay.setEditable(false);
		
		BorderPane.setMargin(statusTxtDisplay, new Insets(0,0,2,0));
		BorderPane.setMargin(receivedTxtDisplay, new Insets(0, 0, 2, 0));
		BorderPane.setMargin(sentTxtDisplay, new Insets(0,0,2,0));
		
		root.setTop(statusTxtDisplay);
		root.setCenter(receivedTxtDisplay);
		root.setRight(sentTxtDisplay);

		HBox hBox = new HBox();
		hBox.setSpacing(2);

		btnConn = new Button("Start");
		btnConn.setPrefSize(60, 30);
		btnConn.setOnAction(e -> {
			if (btnConn.getText().equals("Start")) {
				startClient();
			} else if (btnConn.getText().equals("Stop"))
				stopClient();
		});
		
		txtInput = new TextField();
		txtInput.setPrefSize(320, 30);
		
		txtCnt = new Label("0/100");
		txtCnt.setPrefSize(54,30);
		txtCnt.setDisable(true);

		txtInput.textProperty().addListener(new ChangeListener<String>(){
			
		@Override
		public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
			txtCnt.setStyle("-fx-text-fill: black"); 
			txtCnt.setText(newValue.length()+"/100");
			if(newValue.length()>100)
				txtCnt.setStyle("-fx-text-fill: red"); 
		}
		});

		btnSend = new Button("Send");
		btnSend.setPrefSize(60, 30);
		btnSend.setDisable(true);
		btnSend.setOnAction(e -> 
		{if(txtInput.getText().length() < 100)
			{send(txtInput.getText());}
		else	{
		send(txtInput.getText().substring(0,100));
		}});

		hBox.getChildren().add(btnConn);
		hBox.getChildren().add(txtInput);
		hBox.getChildren().add(txtCnt);
		hBox.getChildren().add(btnSend);
		root.setBottom(hBox);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Client");
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
	}

	void displayStatusText(String text) {
		statusTxtDisplay.setText(text);	
	}
	
	void displayReceivedText(String text) {
		receivedTxtDisplay.appendText(text + "\n");
	}
	
	void displaySentText(String text) {
		sentTxtDisplay.appendText(text+"\n");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
