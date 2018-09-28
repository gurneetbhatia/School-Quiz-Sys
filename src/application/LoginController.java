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

public class LoginController{
	/** The FXML Scene of this class is the first one to be opened if there is at least one entry in the Account table in the SQL database.*/
	@FXML
	TextField usernameField;
	// the username of the user trying to login.
	@FXML
	PasswordField passwdField;
	// the password of the user trying to login.
	@FXML
	Button loginBtn;
	// the loginBtnPressed() method is called when this button is pressed
	@FXML
	Button registerBtn;
	// the registerBtnPressed() method is called when this button is pressed
	public static Account account;
	// a static object of the Account class that holds attributes/data of the currently logged in user
	// it is accessible to all other classes in this application since it is static.
	public void registerBtnPressed(){
		account = new Account();
		// resets the the account object.
		try{
			// Opens the Register.fxml Scene
			Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
	    	Stage stage = (Stage) registerBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loginBtnPressed()
	{
		account = new Account();
		// resets the account object.
		String username = usernameField.getText();// the input username
		String passwd = passwdField.getText();// the input password
		if((username.equals("null")) || (passwd.equals("null"))){
			// this would be a way of hacking the system since once the admin has initiated the registration process but the user hasn't finished
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide valid inputs for username and password (can't enter 'null')");

			alert.showAndWait();
		}
		else{
			String url = Data.server.url;
			String user = Data.server.username;
			String password = Data.server.password;
			try
			{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				String query = "SELECT * FROM Account WHERE username='"+username+"' AND password='"+passwd+"'";
				// query the Account table with the provided credentials
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);//getClass();
				if(rs.next())
				{
					//login credentials verified
					//go to relevant menu
					//fetch the usertype for that
					
					
					
					
					Account a = new Account();
					// assign all the properties to attributes of the Account class.
					a.idAccount = rs.getString("idAccount");
					a.className = rs.getString("class");
					a.email = rs.getString("email");
					a.name = rs.getString("name");
					a.password = rs.getString("password");
					a.registrationID = rs.getString("registrationID");
					a.username = rs.getString("username");
					a.accountType = rs.getString("accountType");
					account = a;
					
					
					// determine the account type of the user and display the appropriate Menu Scene
					if(account.accountType.equals("admin")){
						Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
				    	Stage stage = (Stage) loginBtn.getScene().getWindow();
				    	Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						stage.setScene(scene);
				    	
						stage.show();
					}
					else if(account.accountType.equals("Teacher")){
						Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
				    	Stage stage = (Stage) loginBtn.getScene().getWindow();
				    	Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						stage.setScene(scene);
				    	
						stage.show();
					}
					else if(account.accountType.equals("Student")){
						Parent root = FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
				    	Stage stage = (Stage) loginBtn.getScene().getWindow();
				    	Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						stage.setScene(scene);
				    	
						stage.show();
					}
				}
				else {
					//popup that says invalid username/password
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("Incorrect username/password!");
					alert.setContentText("The username/password you provided were incorrect. Make sure that you're providing the username"
							+ "and not the email address.");
	
					alert.showAndWait();
				}
				st.close();
				con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
