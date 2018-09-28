package application;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterController {
	/**The corresponding scene of this controller class is displayed when the Account table of the SQL database is empty, implying that this is the first
	 * time this application is being launched locally (on the present device).*/
	@FXML
	TextField regKeyField;
	// the registration key
	@FXML
	TextField usernameField;
	// the username selected by the user
	@FXML
	PasswordField pw1Field;
	// the password
	@FXML
	PasswordField pw2Field;
	// same password but entered again to avoid typos
	@FXML
	Button cancelBtn;
	// the cancelBtnPressed() method is called when this button is pressed
	@FXML
	Button registerBtn;
	// the registerBtnPressed() method is called when this button is pressed
	public void registerBtnPressed(){
		//TODO:- check if regKey is being checked for being unique in the createAccountController class.
		String regKey = regKeyField.getText();//check if this is anywhere in the database. once account is created, set it to null
		String username = usernameField.getText();//make sure this is unique
		String pw1 = pw1Field.getText();//make sure this is strong and matches with pw2
		String pw2 = pw2Field.getText();
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		if(!regKey.equals("-")){ //making sure that the registration key is unused
			try{
				
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				String query = "SELECT * FROM Account WHERE registrationID='"+regKey+"';";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				
				if(rs.next()){
					//the regKey is valid
					String id = rs.getString(1);
					String query1 = "SELECT * FROM Account WHERE username='"+username+"';";
					ResultSet rs1 = st.executeQuery(query1);
					if(!rs1.next()){
						//the username is unique and hasn't been selected yet
						//check the strength of the passwords and check if they're equal
						if(pw1.equals(pw2)){
							//the passwords match
							//check the password strength now
							if(checkStrength(pw1)){
								//password is strong enough
								//Update the SQL table
								//Also, set the registration key to '-' so that it can't be reused
								String query2 = "UPDATE Account SET password='"+pw1+"', username='"+username+"', registrationID='-' WHERE idAccount='"+id+"';";
								st.executeUpdate(query2);
					    		st.close();
					    		con.close();
					    		//return to login controller after confirming registration
					    		Alert alert = new Alert(AlertType.INFORMATION);
					    		alert.setTitle("Information Dialog");
					    		alert.setHeaderText("Account Registered Successfully!");
					    		alert.setContentText("Your account has been successfully registered and you may now login to the system.");
	
					    		alert.showAndWait();
					    		
					    		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
						    	Stage stage = (Stage) registerBtn.getScene().getWindow();
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
					else{
						//the selected username isn't unique
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setHeaderText("Select a unique username!");
						alert.setContentText("The selected username isn't unique! Please choose a new one.");
	
						alert.showAndWait();
					}
				}
				else{
					//the registration key isn't valid
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("Invalid Registration Key!");
					alert.setContentText("Please enter a valid registration key!");
	
					alert.showAndWait();
				}
			} catch(Exception e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Invalid Registration Key!");
				alert.setContentText("Please enter a valid registration key!");

				alert.showAndWait();
			}
		}
		else{
			
		}
	}
	public void cancelBtnPressed(){
		
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
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
		/*Checks if the entered password is strong enough.
		 * A strong password must contain at least one uppercase and one lowercase character along with one symbol and a digit.
		 * It must also have a minimum length of 8 characters.*/
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
