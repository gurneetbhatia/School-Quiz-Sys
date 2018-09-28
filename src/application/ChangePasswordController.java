package application;

import java.sql.DriverManager;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ChangePasswordController {
	/**The corresponding scene of this controller class is accessible to the Admin, Student and Teacher accounts
	 * It permits them to change their password by entering their old password*/
	@FXML
	PasswordField currentPasswordField;
	@FXML
	PasswordField passwd1Field;
	@FXML
	PasswordField passwd2Field;
	@FXML
	Button cancelBtn;
	@FXML
	Button doneBtn;
	public void doneBtnPressed(){
		String oldPasswd = currentPasswordField.getText();
		String pw1 = passwd1Field.getText();
		String pw2 = passwd2Field.getText();
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		
		try{
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			if(LoginController.account.password.equals(oldPasswd)){ 
				// the password of the user was stored in the LoginController.account object as an attribute to make it easily accessible without the need for an SQL query every time data needed to be pulled
				// password is correct
				// now check the strength of the new passwords
				// also check if they're equal
				if(pw1.equals(pw2)){
					//passwords are equal
					if(checkStrength(pw1)){
						//password is strong enough
						String query = "UPDATE Account SET password='"+pw1+"' WHERE idAccount='"+LoginController.account.idAccount+"'";
						Statement st = con.createStatement();
						st.executeUpdate(query);
						st.close();
						con.close();
						
						// the user is informed that the password has been changed
						Alert alert = new Alert(AlertType.INFORMATION);
			    		alert.setTitle("Information Dialog");
			    		alert.setHeaderText("Password Changed successfully");
			    		alert.setContentText("Your password has been changed successfully! You will now have to logout and log back in for "
			    				+ "this effect to take place.");

			    		alert.showAndWait();
			    		
			    		// The user is logged out and the scene changes back to the Login.fxml file
			    		
			    		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
				    	Stage stage = (Stage) doneBtn.getScene().getWindow();
				    	Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						stage.setScene(scene);
				    	
						stage.show();
					}
					else{
						
						// the password isn't strong enough
						// an appropriate error is displayed with instructions regarding how the user could make their password stronger
						
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setHeaderText("Your password is too weak");
						alert.setContentText("To make a strong enough password, please include at least one lowercase and one "
								+ "uppercase character along with a digit and a symbol. Also, make sure that the password is at least "
								+ "8 characters long.");

						alert.showAndWait();
					}
				}
				else{
					//the passwords don't match
					// an appropriate error is displayed to the user
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("The passwords don't match");
					alert.setContentText("The passwords don't match! Please re-enter them");

					alert.showAndWait();
				}
			}
			else{
				// the currentPassword is incorrect
				// an appropriate error is displayed to the user
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Incorrect Password!");
				alert.setContentText("The password entered is incorrect! If you have forgotten your password, you may reset your password "
						+ "through your account settings");

				alert.showAndWait();
			}
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelBtnPressed(){
		// changes the scene back to the MyAccount.fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("MyAccount.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public static boolean checkStrength(String p)
	{
		/* This method checks the strength of the new password (which it receives as an argument)
		 * It returns true if the password is strong enough and false if it is not
		 * A strong password must contain the following:
		 * at least one lowercase character
		 * at least one uppercase character
		 * at least one digit
		 * at least one symbol
		 * at least 8 characters*/
		
		boolean l = false;
		boolean u = false;
		boolean d = false;
		boolean s = false;
		for(int i = 0;i<p.length();i++){
			if(Character.isUpperCase(p.charAt(i))){
				u = true;
			}
			else if(Character.isLowerCase(p.charAt(i))){
				l = true;
			}
			else if(Character.isDigit(p.charAt(i))){
				d = true;
			}
			else{
				s = true;
			}
		}
		if((p.length()>=8) && (l) && (u) && (d) && (s))
		{
			return true;
		}
		return false;
	}
}
