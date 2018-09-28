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
	@FXML
	Button createAdminBtn;
	@FXML
	TextField usernameField;
	@FXML
	TextField emailField;
	@FXML
	PasswordField passwd1Field;
	@FXML
	PasswordField passwd2Field;	
	public void createSchoolBtnPressed(ActionEvent e)//TODO:- Rename this method
	{
		try 
		{
			
			String email = emailField.getText();
			//String username = usernameField.getText();
			String passwd1 = passwd1Field.getText();
			String passwd2 = passwd2Field.getText();
			if(passwd1.equals(passwd2))
			{
				if(checkStrength(passwd1))
				{
					SecureRandom random = new SecureRandom();
					int pin = random.nextInt(100000);
					String pinFormatted = String.format("%05d", pin);
					
					//String recipient, String subject, String text
					String subject = "Admin Login Pin";
					try
					{
						//TODO: remove comment from the line below
						Mailgun.SendSimple(email, subject, pinFormatted);
					} catch(Exception e1)
					{
						//e1.printStackTrace();
					}
					
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
					    		String url = "jdbc:mysql://localhost/db";
					    		String user = "root";
					    		String password = "bUrp@107";
					    		Connection con = (Connection) DriverManager.getConnection(url, user, password);
					    		int accountID = random.nextInt(100000);
					    		String accountIdFormatted = String.format("%05d", accountID);
					    		//String query = "INSERT INTO Account (username, email, password, type) VALUES ('admin', '"+email+"', '"+passwd1+"', 'admin');";
					    		String query = "INSERT INTO Account (idAccount, name, password, email, class, accountType, registrationID, username) VALUES ('"+accountIdFormatted+"', '-', '"+passwd1+"', '"+email+"', '-', 'admin', '-', 'admin');";
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
		//strength is out of 5.
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
