package application;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ForgotPasswordController {
	@FXML
	PasswordField passwd1Field;
	@FXML
	PasswordField passwd2Field;
	@FXML
	Button doneBtn;
	@FXML
	Button cancelBtn;
	public void doneBtnPressed(){
		String pw1 = passwd1Field.getText();
		String pw2 = passwd2Field.getText();
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			//send a pin to the user to confirm identity for security reasons before proceeding
			
			SecureRandom random = new SecureRandom();
			int pin = random.nextInt(100000);
			String pinFormatted = String.format("%05d", pin);
			try{
				Mailgun.SendSimple(LoginController.account.email, "Identity Verification", "Enter this pin: "+pinFormatted);
			} catch(Exception e){
				
			}
			
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Email Confirmation");
			dialog.setHeaderText("An email has been sent to you with a pin. Please enter it here.");
			dialog.setContentText("Pin:");
			
			Optional<String> result1 = dialog.showAndWait();
			if (result1.isPresent()){
			    if(result1.get().equals(pinFormatted)){
			    	Connection con = (Connection) DriverManager.getConnection(url, user, password);
			    	//identity verified
			    	//check the strength of the passwords and check if they're equal
			    	if(pw1.equals(pw2)){
						//passwords are equal
						if(checkStrength(pw1)){
							//password is strong enough
							String query = "UPDATE Account SET password='"+pw1+"' WHERE idAccount='"+LoginController.account.idAccount+"'";
							Statement st = con.createStatement();
							st.executeUpdate(query);
							st.close();
							con.close();
							
							Alert alert = new Alert(AlertType.INFORMATION);
				    		alert.setTitle("Information Dialog");
				    		alert.setHeaderText("Password Changed successfully");
				    		alert.setContentText("Your password has been changed successfully! You will now have to logout and log back in for "
				    				+ "this effect to take place.");

				    		alert.showAndWait();
				    		
				    		LoginController.account = null;
				    		
				    		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
					    	Stage stage = (Stage) doneBtn.getScene().getWindow();
					    	Scene scene = new Scene(root);
							scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							stage.setScene(scene);
					    	
							stage.show();
						}
						else{
							//the password isn't strong enough
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERROR");
							alert.setHeaderText("Your password is too weak");
							alert.setContentText("To make a strong enough password, please include at least one lowercase and one "
									+ "uppercase character along with a digit. Also, make sure that the password is at least "
									+ "8 characters long.");

							alert.showAndWait();
						}
					}
					else{
						//the passwords don't match
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setHeaderText("The passwords don't match");
						alert.setContentText("The passwords don't match! Please re-enter them");

						alert.showAndWait();
					}
			    }
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu_MyAccount.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	public static boolean checkStrength(String p)
	{
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
