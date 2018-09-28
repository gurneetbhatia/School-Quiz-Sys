package application;
import java.io.PrintWriter;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

import org.glassfish.jersey.client.ClientResponse;

import com.mysql.jdbc.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
public class StartController {
	/**The corresponding scene of this controller class is launched by the Main class if the Account table is discovered to be empty when the application
	 * is launched. It allows the user to create an Admin account. Login credentials of this account are then updated to Account table of the SQL
	 * database and every time the application is loaded after this, Login.fxml is the first scene to be loaded.
	 * Thus, this scene is only ever used once (when the application is loaded for the first time locally).*/
	@FXML
	Button createAdminBtn;
	// the createAdminBtnPressed(ActionEvent e) method is called when this button is pressed
	@FXML
	TextField emailField;
	// the email id of the admin account 
	@FXML
	PasswordField passwd1Field;
	// the password
	@FXML
	PasswordField passwd2Field;
	// the password is entered again to avoid typos
	public void createAdminBtnPressed(ActionEvent e)
	{
		try 
		{
			
			String email = emailField.getText();
			String passwd1 = passwd1Field.getText();
			String passwd2 = passwd2Field.getText();
			if(passwd1.equals(passwd2))
			{
				// check if the passwords provided are equal
				if(checkStrength(passwd1))
				{
					// check if the password is strong enough
					SecureRandom random = new SecureRandom();
					int pin = random.nextInt(100000);
					String pinFormatted = String.format("%05d", pin);
					
					//String recipient, String subject, String text
					String subject = "Admin Login Pin";
					try
					{
						Mailgun.SendSimple(email, subject, pinFormatted);
					} catch(Exception e1)
					{
						//e1.printStackTrace();
					}
					// send an email to the provided email address with a pin to confirm if it's a valid email id.
					
					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Email Confirmation");
					dialog.setHeaderText("An email has been sent to you with a pin. Please enter it here.");
					dialog.setContentText("Pin:");
					
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()){
					    if(result.get().equals(pinFormatted))
					    {
					    	//email confirmed
					    	//move to login screen
					    	//add account to database
					    	
					    	try
					    	{
					    		//Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://35.194.201.205:3306/quizdb", "root", "O3fub27LLmFc52JA");
					    		String url = Data.server.url;
					    		String user = Data.server.username;
					    		String password = Data.server.password;
					    		Connection con = (Connection) DriverManager.getConnection(url, user, password);
					    		int accountID = random.nextInt(100000);
					    		String accountIdFormatted = String.format("%05d", accountID);
					    		
					    		String query = "INSERT INTO Account (idAccount, name, password, email, class, accountType, registrationID, username) VALUES ('"+accountIdFormatted+"', '-', '"+passwd1+"', '"+email+"', '-', 'admin', '-', 'admin');";
					    		// update the Account table with the new admin account
					    		Statement st = con.createStatement();
					    		st.executeUpdate(query);
					    		st.close();
					    		con.close();
					    	} catch(Exception e1)
					    	{
					    		e1.printStackTrace();
					    	}
					    	//move to login screen
					    	Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
					    	Stage stage = (Stage) createAdminBtn.getScene().getWindow();
					    	Scene scene = new Scene(root);
							scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							stage.setScene(scene);
					    	
							stage.show();
					    }
					    else
					    {
					    	//incorrect pin entered
					    	Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERROR");
							alert.setHeaderText("Incorrect Pin!");
							alert.setContentText("Please check if you've provided the right email address and try again or"
									+ "re-enter the pin.");

							alert.showAndWait();
					    }
					}
				}
				else
				{
					// password isn't strong enough
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("Your password is too weak");
					alert.setContentText("To make a strong enough password, please include at least one lowercase and one "
							+ "uppercase character along with a digit. Also, make sure that the password is at least "
							+ "8 characters long.");

					alert.showAndWait();
				}
			}
			else
			{
				// the passwords provided aren't equal to each other
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("The passwords don't match");
				alert.setContentText("The passwords don't match! Please re-enter them");

				alert.showAndWait();
			}
			
		} catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	public static boolean checkStrength(String p)
	{
		/**Must have:
		 * lowercase
		 * uppercase
		 * int
		 * symbol
		 * length>8*/
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
