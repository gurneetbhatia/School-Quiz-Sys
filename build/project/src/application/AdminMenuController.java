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
	@FXML
	Button createSubjectBtn;
	@FXML
	Button createClassBtn;
	@FXML
	Button viewAccountsBtn;
	@FXML
	Button myAccountBtn;
	@FXML
	Button serverSettingsBtn;
	@FXML
	Button logoutBtn;
	public void createAccountBtnPressed(){
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
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_createClass.fxml"));
	    	Stage stage = (Stage) createClassBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			
		}
	}
	public void createSubjectBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_createSubject.fxml"));
	    	Stage stage = (Stage) createSubjectBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e) {
			
		}
	}
	public void viewAccountsBtnPressed(){
		
	}
	public void myAccountBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_MyAccount.fxml"));
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
		
	}
	public void logoutBtnPressed(){
		LoginController.account = new Account();
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
