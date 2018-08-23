package ChatterBox_revised;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
						btnConn.setText("Start");
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

	
			try {
				byte[] byteArr = new byte[100];
				InputStream is = socket.getInputStream();
				// read() : byte를 한번에 보낼 경우 연결이 끊어지면 보낼 수 없으므로 사용
				int readByteCount = is.read(byteArr,0,byteArr.length);

				if (readByteCount == -1) {
					throw new IOException();
				}
				String data = new String(byteArr, 0, readByteCount, "UTF-8");
				Platform.runLater(() -> displayText("받기 완료 : " + data));
			} catch (Exception e) {
				Platform.runLater(() -> displayText("서버 통신 불가"));
				if (!socket.isClosed())
					stopClient();
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
					Platform.runLater(() -> //displayText("보내기 완료"));
					//연결 끊음 수정하기
					{
						try {
							displayText_send(new String(byteArr,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					});
				} catch (Exception e) {
					Platform.runLater(() -> displayText("서버 통신 안됨"));
					stopClient();
				}
			}
		};
		thread.start();
	}

	TextArea receivedTxtDisplay;
	TextArea sentTxtDisplay;
	TextField txtInput;
	Button btnConn, btnSend, btnCnt;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
		receivedTxtDisplay = new TextArea();
		sentTxtDisplay = new TextArea();

		receivedTxtDisplay.setEditable(false);
		sentTxtDisplay.setEditable(false);
		
		BorderPane.setMargin(receivedTxtDisplay, new Insets(0, 0, 2, 0));
		BorderPane.setMargin(sentTxtDisplay, new Insets(0, 0, 2, 0));
		
		root.setCenter(receivedTxtDisplay);
		root.setRight(sentTxtDisplay);

		HBox hBox = new HBox();
		
		txtInput = new TextField();
		txtInput.setPrefSize(350, 30);
		HBox.setMargin(txtInput, new Insets(0, 1, 1, 1));

		btnConn = new Button("Start");
		btnConn.setPrefSize(60, 30);
		btnConn.setOnAction(e -> {
			if (btnConn.getText().equals("Start")) {
				startClient();
			} else if (btnConn.getText().equals("Stop"))
				stopClient();
		});

		btnCnt = new Button("0/10");
		btnCnt.setPrefSize(60,30);
		btnCnt.setDisable(true);
		
		btnSend = new Button("Send");
		btnSend.setPrefSize(60, 30);
		btnSend.setDisable(true);
		btnSend.setOnAction(e -> send(txtInput.getText()));

		hBox.getChildren().add(btnConn);
		hBox.getChildren().add(txtInput);
		hBox.getChildren().add(btnCnt);
		hBox.getChildren().add(btnSend);
		
		root.setBottom(hBox);

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("app.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Client");
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
	}

	void displayText(String text) {
		receivedTxtDisplay.appendText(text + "\n");
	}
	void displayText_send(String text) {
		
		receivedTxtDisplay.appendText(text + "\n");
	}
	public class controller implements Initializable{
		@FXML private Button btnConn;
		@FXML private Button btnSend;
		@FXML private Label txtCnt;
		
		@Override
		public void initialize(URL url, ResourceBundle resources) {
			public void handlebtnConnAction(ActionEvent e){
				if (btnConn.getText().equals("Start")) {
					startClient();
				} else if (btnConn.getText().equals("Stop")) {
					stopClient();
				}
			}
			
			
			private StringProperty propertyTxt = new SimpleStringProperty();
			public void setText(String newValue) {propertyTxt.set(newValue);}
			public String getText() {return propertyTxt.get();}
			public StringProperty textProperty(){return propertyTxt;}
			
			txtInput.valueProperty.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable,Number oldValue, Number newValue) {
				txtCnt.setText(newValue.toString());
			}});
			
		}
			
			
			public void handlebttxtCntAction(ActionEvent e){
				send(txtInput.getText());}
			}
	}
			
			
		
	public static void main(String[] args) {
		launch(args);
	}

}
