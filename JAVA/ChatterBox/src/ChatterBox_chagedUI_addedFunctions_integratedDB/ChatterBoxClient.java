package ChatterBox_chagedUI_addedFunctions_integratedDB;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatterBoxClient extends Application {
	// Socket 참조 변수
	Socket socket;
	String id;
	String password;

	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					// 소켓 생성
					socket = new Socket();
					//
					socket.connect(new InetSocketAddress("10.156.147.211", 5001));
					// UI 변경
					Platform.runLater(() -> {
						displayStatusText("Status | server : connected with" + socket.getRemoteSocketAddress());
						btnConn.setText("Stop");
						btnSend.setDisable(false);
					});

				} catch (Exception e) {
					e.printStackTrace();
					Platform.runLater(() -> displayStatusText("Status | server : connecting failed"));
					if (!socket.isClosed()) {
						stopClient();
					}
					return;
				}
				Platform.runLater(() -> {
					loginStage.showAndWait();
				});
				receiveResult();
			}
		};
		thread.start();
	}

	void stopClient() {
		try {
			Platform.runLater(() -> {
				btnConn.setText("Start");
				btnSend.setDisable(true);
			});

			// 언제든지 소켓 연결이 끊어질 수 있음
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void receiveResult() {
		while(!socket.isClosed()) {
		try {
			byte[] byteArrResult = new byte[50];
			InputStream is = socket.getInputStream();
			// read() : byte를 한번에 보낼 경우 연결이 끊어지면 보낼 수 없으므로 사용
			int readByteCount = is.read(byteArrResult);

			if (readByteCount == -1) {
				throw new IOException();
			}
			String data = new String(byteArrResult, 0, readByteCount, "UTF-8");
			Platform.runLater(() ->
				displayStatusText("Status | checked userData"));
				System.out.println(data);
			if (data.equals("true")){
				Platform.runLater(()->{
					Alert loginsucceess = new Alert(AlertType.INFORMATION);
					loginsucceess.setTitle("INFORMATION");
					loginsucceess.setHeaderText(null);
					loginsucceess.setContentText("Login Succeed");
					loginsucceess.showAndWait();});
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				receiveDataReceived();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			receiveDataSent();
			}
			else{
				Platform.runLater(()->{
					Alert loginFail = new Alert(AlertType.WARNING);
					loginFail.setTitle("WARNING");
					loginFail.setHeaderText(null);
					loginFail.setContentText("failed, Try again");
					loginFail.showAndWait();});
				if(!socket.isClosed()) {
					stopClient();}
				
			}
		} catch (Exception e) {
			Platform.runLater(() -> displayStatusText("Status | connecting failed"));
			if (!socket.isClosed())
				stopClient();
		}
		}
		stopClient();

	}
	
	void receiveDataReceived() {
		try {
			byte[] byteArr = new byte[1200];
			InputStream is = socket.getInputStream();
			int readByteCount = is.read(byteArr);

			if (readByteCount == -1) {
				throw new IOException();
			}
			String data = new String(byteArr, 0, readByteCount, "UTF-8");
			Platform.runLater(() -> {
				displayReceivedText(data);
				displayReceivedText("");
				displayStatusText("Status | received receivdmessage");
			});
		} catch (Exception e) {
			e.printStackTrace();
			Platform.runLater(() -> displayStatusText("Status | connecting failed receiving data"));
			if (!socket.isClosed())
				stopClient();
		}
		
	}
	
	
	void receiveDataSent() {
		try {
			byte[] byteArr = new byte[1200];
			InputStream is = socket.getInputStream();
			int readByteCount = is.read(byteArr);

			if (readByteCount == -1) {
				throw new IOException();
			}
			String data = new String(byteArr, 0, readByteCount, "UTF-8");
			Platform.runLater(() -> {
				displaySentText(data);
				displaySentText("");
				
				displayStatusText("Status | received sentmessage");
			});
			receive();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.runLater(() -> displayStatusText("Status | connecting failed receiving data"));
			if (!socket.isClosed())
				stopClient();
		}
		
	}

	

	void receive() {
		while(true) {
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
				displayStatusText("Status | received message");
			});
		} catch (Exception e) {
			Platform.runLater(() -> displayStatusText("Status | connecting failed"));
			if (!socket.isClosed())
				stopClient();
			return;
		}
		}

	}

	void sendData(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream os = socket.getOutputStream();
					os.write(byteArr);
					os.flush();
					Platform.runLater(() -> {
						displayStatusText("Status | checking userData Please wait");
					});
				} catch (Exception e) {
					e.printStackTrace();
					Platform.runLater(() -> displayStatusText("Status | connecting failed"));
					stopClient();
				}
			}
		};
		thread.start();
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
						displayStatusText("Status | send message");
					});
				} catch (Exception e) {
					Platform.runLater(() -> displayStatusText("Status | connecting failed"));
					stopClient();
				}
			}
		};
		thread.start();
	}
	

	// 로그인
	Stage loginStage;
	TextField txtId;
	PasswordField txtPassword;
	Button btnLogin;
	Button btnSignUp;
	// 클라이언트
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
		{
			String message = txtInput.getText();
			if(message.length() > 100)
				message = message.substring(0, 100);
			send(message);
		});
		
	

	hBox.getChildren().add(btnConn);hBox.getChildren().add(txtInput);hBox.getChildren().add(txtCnt);hBox.getChildren().add(btnSend);root.setBottom(hBox);

	Scene scene = new Scene(
			root);primaryStage.setScene(scene);primaryStage.setTitle("Client");primaryStage.setOnCloseRequest(event->

	stopClient());
		primaryStage.show();
		
		loginStage = new Stage(StageStyle.UTILITY);
		loginStage.initModality(Modality.WINDOW_MODAL);
		loginStage.initOwner(primaryStage);
		
		GridPane gridPane = new GridPane();
		Label labelId = new Label("id");
		Label labelPassword = new Label("password");
		
		txtId = new TextField();
		txtPassword = new PasswordField();
		
		btnLogin = new Button("login");
		btnLogin.setOnAction(e->{
			
		id =txtId.getText();
		password=txtPassword.getText();
		
		if(id.length()>20)
		id = id.substring(0,20);
		if(password.length()>20)
		password=password.substring(0,20);
		String data = "login/"+id+"/"+password;
		sendData(data);
		btnLogin.getScene().getWindow().hide();
		});
		
		btnSignUp = new Button("sign up");
		btnSignUp.setOnAction(e->{
			
		id =txtId.getText();
		password=txtPassword.getText();
		
		if(id.length()>20)
		id = id.substring(0,20);
		if(password.length()>20)
		password=password.substring(0,20);
		String data = "signup/"+id+"/"+password;
		sendData(data);
		btnSignUp.getScene().getWindow().hide();});
		
	
	
		gridPane.add(labelId, 0,0,1,1);
		gridPane.add(labelPassword, 0,1,1,1);
		gridPane.add(txtId, 1,0,2,1);
		gridPane.add(txtPassword, 1, 1,2,1);
		gridPane.add(btnSignUp,1,2,1,1);
		gridPane.add(btnLogin, 2,2,1,1);
		
		gridPane.setHgap(5);
		gridPane.setVgap(2);
		gridPane.setPadding(new Insets(4,3,2,3));
		Scene loginScene = new Scene(gridPane,188,90);
		loginStage.setScene(loginScene);
		loginStage.setTitle("login");
		// 로그인 예외 처리하기
		

	}

	void displayStatusText(String text) {
		statusTxtDisplay.setText(text);
	}

	void displayReceivedText(String text) {
		receivedTxtDisplay.appendText(text + "\n");
	}

	void displaySentText(String text) {
		sentTxtDisplay.appendText(text + "\n");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
