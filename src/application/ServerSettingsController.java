package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ServerSettingsController{
	/**The corresponding scene of this controller class allows the Admin user to change the SQL connection information by modifying the URL, username and password for the connection.
	 */
	@FXML
	TextField urlTF;
	@FXML
	TextField usernameTF;
	@FXML
	TextField passwdTF;
	@FXML
	Button cancelBtn;
	@FXML
	Button doneBtn;
	
	public void doneBtnPressed(){
		/* format data is stored in in the ServerDetails.txt file:
		 * URL
		 * username
		 * password*/
		
		String url = urlTF.getText();
		String username = usernameTF.getText();
		String passwd = passwdTF.getText();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Are you sure you want to change your server settings?");
		alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // the user chose OK
			// need to update the text file and the static object of the CurrentServer class
			Data.server = new CurrentServer();
			// resets the server object
			Data.server.url = url;
			Data.server.password = passwd;
			Data.server.username = username;
			
			//update the text file now
			try{
				String filename = "ServerDetails.txt";
				FileWriter fw = new FileWriter(filename, false);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write(Data.server.url+"\n");
				bw.write(Data.server.username+"\n");
				bw.write(Data.server.password);
				
				bw.close();
				fw.close();
				
			} catch(Exception e){
				e.printStackTrace();
			}
			// the scene is changed back to the AdminMenu.fxml file
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
		    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);

				stage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} else {
			// the user chose cancel
			// the scene is changed back to the AdminMenu.fxml file
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
		    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);

				stage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void cancelBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
