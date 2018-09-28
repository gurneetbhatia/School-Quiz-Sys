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

	@FXML
	TextField usernameField;
	@FXML
	PasswordField passwdField;
	@FXML
	Button loginBtn;
	@FXML
	Button registerBtn;
	public static Account account;
	public void registerBtnPressed(){
		account = new Account();
		try{
			
			Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
	    	Stage stage = (Stage) registerBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
			
		} catch(Exception e){
			
		}
	}
	public void loginBtnPressed()
	{
		account = new Account();
		String username = usernameField.getText();
		String passwd = passwdField.getText();
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		try
		{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT * FROM Account WHERE username='"+username+"' AND password='"+passwd+"'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);//getClass();
			if(rs.next())
			{
				//login credentials verified
				//go to relevant menu
				//fetch the usertype for that
				
				
				//TODO:- Instead of using columnIndex argument with .getString, use columnName argument
				
				Account a = new Account();
				a.idAccount = rs.getString("idAccount");
				a.className = rs.getString("class");
				a.email = rs.getString("email");
				a.name = rs.getString("name");
				a.password = rs.getString("password");
				a.registrationID = rs.getString("registrationID");
				a.username = rs.getString("username");
				a.accountType = rs.getString("accountType");
				account = a;
				
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
				/*Can give a password reset option
				 * Send email with 5 digit pin
				 * if verified, let them reset
				 * update sql database*/
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
