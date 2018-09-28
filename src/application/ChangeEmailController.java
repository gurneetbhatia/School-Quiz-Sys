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

public class ChangeEmailController {
	/**The corresponding scene of this controller (ChangeEmail.fxml) is accessible to the Admin, Student and Teacher accounts
	 * It allows them to change the email registered with their account in the database (the Account table)
	 * For security reasons, a pin is sent out to the newly registered email before it the Account table is updated in the SQL database
	 * */
	
	@FXML
	TextField emailField;
	// the new email of the user
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when this button is pressed
	
	public void doneBtnPressed(){
		
		// Send a pin to the email and confirm it
		// Update the value on the SQL server
		// Inform the user that they will be logged out for the changes to take effect
		
		String email = emailField.getText();
		// the text entered in the <emailField> TextField is retrieved as a String and assigned to <email>
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try{
			// the user is first asked to confirm that they are sure about this change
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Confirm email change?");
			alert.setContentText("For the change to take effect, you will be logged out of your account and will have to login again.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    //user chose OK
				
				try{
					Connection con = (Connection) DriverManager.getConnection(url, user, password);
					
					SecureRandom random = new SecureRandom();
					int pin = random.nextInt(100000);
					String pinFormatted = String.format("%05d", pin);
					
					String subject = "Pin for change of email";
					String text = "Enter this pin in the textfield: "+pinFormatted;
					try{
						Mailgun.SendSimple(email, subject, text);
						// a randomly generated pin is sent via email to the new email that was provided by the user
					} catch(Exception e){
						
					}
					
					try{
						// The user enters the pin
						TextInputDialog dialog = new TextInputDialog();
						dialog.setTitle("Email Confirmation");
						dialog.setHeaderText("An email has been sent to you with a pin. Please enter it here.");
						dialog.setContentText("Pin:");
						
						Optional<String> result1 = dialog.showAndWait();
						if (result1.isPresent()){
							// the pin entered by the user is not null
							// result1.isPresent() avoids an error in the event that the user did not provide an input for the pin in the TextInputDialog
						    if(result1.get().equals(pinFormatted)){
								String query = "UPDATE Account SET email='"+email+"' WHERE idAccount='"+LoginController.account.idAccount+"'";
								Statement st = con.createStatement();
								st.executeUpdate(query);
								st.close();
								// the email is updated for the user's account
								try
								{
									
									Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
							    	Stage stage = (Stage) doneBtn.getScene().getWindow();
							    	Scene scene = new Scene(root);
									scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
									stage.setScene(scene);
							    	
									stage.show();
									// the user is returned to the login screen
								} catch(Exception e){
									e.printStackTrace();
								}
						    }
						    else{
						    	// the pin entered by the user was incorrect
						    	// an appropriate error is displayed
						    	Alert alert1 = new Alert(AlertType.ERROR);
								alert1.setTitle("ERROR");
								alert1.setHeaderText("Incorrect Pin!");
								alert1.setContentText("Please check if you've provided the right email address and try again or"
										+ " re-enter the pin.");

								alert1.showAndWait();
						    }
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				
			} else {
				// if the user decides that they do not wish to proceed with a change in username, they are returned to their MyAccount menu
			    cancelBtnPressed();
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
}
