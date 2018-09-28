package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MyAccountController {
	/**This Controller class is common to all account types. It is essentially a menu that allows the users to navigate to scenes that would involve them
	 * modifying the settings for their personal accounts (like changing password)*/
	@FXML
	Button changeEmailBtn;
	// calls the changeEmailBtnPressed() method
	@FXML
	Button changePasswordBtn;
	// calls the changePasswordBtnPressed() method
	@FXML
	Button forgotPasswordBtn;
	// calls the forgotPasswordBtnPressed() method
	@FXML
	Button changeUsernameBtn;
	// calls the changeUsernameBtnPressed() method
	@FXML
	Button backBtn;
	// calls the backBtnPressed() method
	public void changeEmailBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("ChangeEmail.fxml"));
	    	Stage stage = (Stage) changeEmailBtn.getScene().getWindow();
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
	    	Stage stage = (Stage) forgotPasswordBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void changeUsernameBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("ChangeUsername.fxml"));
	    	Stage stage = (Stage) changeUsernameBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void backBtnPressed(){
		/*Since the scene corresponding to this Controller class is common to all account types, the account type of the currently logged in user
		 * must be determined through the accountType attribute of the static object of Account class in the LoginController class.
		 * Once the account type is determined, the relevant menu scene is opened.*/
		if(LoginController.account.accountType.equals("admin")){
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
		    	Stage stage = (Stage) backBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(LoginController.account.accountType.equals("Student")){
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
		    	Stage stage = (Stage) backBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(LoginController.account.accountType.equals("Teacher")){
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
		    	Stage stage = (Stage) backBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
