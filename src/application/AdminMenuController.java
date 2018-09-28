package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminMenuController {
	@FXML
	Button createAccountBtn;
	// the <createAccountBtnPressed()> method is called when this button is pressed
	@FXML
	Button createSubjectBtn;
	// the <createSubjectBtnPressed()> method is called when this button is pressed
	@FXML
	Button createClassBtn;
	// the <createClassBtnPressed()> method is called when this button is pressed
	@FXML
	Button myAccountBtn;
	// the <myAccountBtnPressed()> method is called when this button is pressed
	@FXML
	Button serverSettingsBtn;
	// the <serverSettingsBtnPressed()> method is called when this button is pressed
	@FXML
	Button logoutBtn;
	// the <logoutBtnPressed()> method is called when this button is pressed
	public void createAccountBtnPressed(){
		// Changes the scene to the AdminMeny_createAccount.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_createAccount.fxml"));
	    	Stage stage = (Stage) createAccountBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void createClassBtnPressed(){
		// Changes the scene to the AdminMenu_createClass.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_createClass.fxml"));
	    	Stage stage = (Stage) createClassBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void createSubjectBtnPressed(){
		// Changes the scene to the AdminMenu_createSubject.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_createSubject.fxml"));
	    	Stage stage = (Stage) createSubjectBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void myAccountBtnPressed(){
		// Changes the scene to the MyAccount.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("MyAccount.fxml"));
	    	Stage stage = (Stage) myAccountBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void serverSettingsBtnPressed(){
		// Changes the scene to the ServerSettings.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("ServerSettings.fxml"));
	    	Stage stage = (Stage) serverSettingsBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void logoutBtnPressed(){
		// Changes the scene to the Login.fxml file
		try{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	    	Stage stage = (Stage) logoutBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
}
