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

public class ChangeUsernameController {
	// the corresponding scene of this controller class is accessible to the Admin, Student and Teacher accounts
	@FXML
	TextField usernameLbl;
	// the new selected username of the user
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when this button is pressed
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	
	public void doneBtnPressed(){
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		String username = usernameLbl.getText();
		// the username entered by the user is stored as a String in the <username> variable
		
		try{
			
			// the user is asked to confirm their decision of changing their username
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Confirm username change?");
			alert.setContentText("For the change to take effect, you will be logged out of your account and will have to login again.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				//the user chose "OK"
				
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				
				/**For security reasons, an email is sent out to the registered email address of the account-holder
				 * This email contains a secure randomly generated pin
				 * Only if the pin entered by the user is correct is the change of username updated in the database*/
				SecureRandom random = new SecureRandom();
				int pin = random.nextInt(100000);
				String pinFormatted = String.format("%05d", pin);
				
				String subject = "Pin for change of username";
				String text = "Enter this pin in the textfield: "+pinFormatted;
				try{
					Mailgun.SendSimple(LoginController.account.email, subject, text); // the email is sent out to the user
				} catch(Exception e){
					
				}
				
				// A TextInputDialog is opened informing the user about the email and prompting them to enter the pin
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Email Confirmation");
				dialog.setHeaderText("An email has been sent to you with a pin. Please enter it here.");
				dialog.setContentText("Pin:");
				
				Optional<String> result1 = dialog.showAndWait();
				if (result1.isPresent()){
				    if(result1.get().equals(pinFormatted)){
				    	//the pin is correct
				    	
				    	String query = "update Account set username='"+username+"' where idAccount='"+LoginController.account.idAccount+"'";
				    	Statement st = con.createStatement();
				    	st.executeUpdate(query);
				    	// the Account table in the database is updated with the new username
						st.close();
						con.close();
						
						try
						{
							// the user is logged out following the change and is returned to the login screen
							Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
					    	Stage stage = (Stage) doneBtn.getScene().getWindow();
					    	Scene scene = new Scene(root);
							scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							stage.setScene(scene);
					    	
							stage.show();
						} catch(Exception e){
							e.printStackTrace();
						}
				    }
				    else{
				    	// incorrect pin
				    	// an appropriate error is displayed to the suer
				    	Alert alert1 = new Alert(AlertType.ERROR);
						alert1.setTitle("ERROR");
						alert1.setHeaderText("Incorrect Pin!");

						alert1.showAndWait();
				    }
				
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void cancelBtnPressed(){
		// the user pressed the cancel button and is returned to the MyAccount.fxml scene
		try
		{
			
			Parent root = FXMLLoader.load(getClass().getResource("MyAccount.fxml"));
	    	Stage stage = (Stage) doneBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
