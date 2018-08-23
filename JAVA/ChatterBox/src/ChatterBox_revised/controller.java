package ChatterBox_revised;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class controller implements Initializable{
	@FXML private Button btnConn;
	@FXML private Button btnSend;
	@FXML private Label txtCnt;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		public void handlebtnConnAction(ActionEvent e){
			send(ChatterBoxClient.txtInput.getText();}
		
		public void handlebtnSendAction(ActionEvent e){
			
		if (btnConn.getText().equals("Start")) {
			startClient();
		} else if (btnConn.getText().equals("Stop"))
			stopClient();}
		
		public void handlebttxtCntAction(ActionEvent e){
			send(txtInput.getText();}
		}
		
		
		@Override
		
		
	}

}
