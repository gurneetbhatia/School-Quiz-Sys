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
	//a pin should be sent to the new email address to confirm it
	@FXML
	TextField emailField;
	@FXML
	Button cancelBtn;
	@FXML
	Button doneBtn;
	
	public void doneBtnPressed(){
		
		//send a pin to the email and confirm it
		//load the value on the SQL server
		//Tell them that they will be logged out for the changes to take effect
		String email = emailField.getText();
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
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
					} catch(Exception e){
						
					}
					
					try{
						TextInputDialog dialog = new TextInputDialog();
						dialog.setTitle("Email Confirmation");
						dialog.setHeaderText("An email has been sent to you with a pin. Please enter it here.");
						dialog.setContentText("Pin:");
						
						Optional<String> result1 = dialog.showAndWait();
						if (result1.isPresent()){
						    if(result1.get().equals(pinFormatted)){
						    	//String query2 = "UPDATE Account SET password='"+pw1+"', username='"+username+"', registrationID='-' WHERE idAccount='"+id+"';";
								String query = "UPDATE Account SET email='"+email+"' WHERE idAccount='"+LoginController.account.idAccount+"'";
								Statement st = con.createStatement();
								st.executeUpdate(query);
								st.close();
								
								try
								{
									LoginController.account = null;//deleting the object for security reasons
									Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
							    	Stage stage = (Stage) doneBtn.getScene().getWindow();
							    	Scene scene = new Scene(root);
									scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
									stage.setScene(scene);
							    	
									stage.show();
								} catch(Exception e){
									
								}
						    }
						    else{
						    	Alert alert1 = new Alert(AlertType.ERROR);
								alert1.setTitle("ERROR");
								alert1.setHeaderText("Incorrect Pin!");
								alert1.setContentText("Please check if you've provided the right email address and try again or"
										+ " re-enter the pin.");

								alert1.showAndWait();
						    }
						}
					} catch(Exception e){
						
					}
				} catch(Exception e){
					
				}
				
			} else {
			    cancelBtnPressed();
			}
		} catch(Exception e){
			e.printStackTrace();
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
		} catch(Exception e){
			
		}
	}
}
