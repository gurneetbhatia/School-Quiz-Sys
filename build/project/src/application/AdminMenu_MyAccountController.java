package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminMenu_MyAccountController {
	@FXML
	Button changeEmailBtn;
	@FXML
	Button changePasswordBtn;
	@FXML
	Button forgotPasswordBtn;
	@FXML
	Button changeUsernameBtn;
	@FXML
	Button backBtn;
	public void changeEmailBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("ChangeEmail.fxml"));
	    	Stage stage = (Stage) backBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void changePasswordBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));
	    	Stage stage = (Stage) changePasswordBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void forgotPasswordBtnPressed(){
		//send pin to user's registered email
		//check if the correct pin has been entered
		//change password and log the user out
		try{
			Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));
	    	Stage stage = (Stage) changePasswordBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void changeUsernameBtnPressed(){
		
	}
	
	public void backBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) backBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
}
