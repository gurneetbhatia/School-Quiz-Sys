package application;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminMenu_createSubjectController {
	@FXML
	TextField nameField;
	// the name of the subject
	@FXML
	Button cancelBtn;
	// when the user presses this button, the <cancelBtnPressed()> method is called
	@FXML
	Button createBtn;
	// when the user presses this button, the <createBtnPressed()> method is called
	
	
	public void createBtnPressed(){
		// this method is called when the <createBtn> Button is pressed
		
		//creating the subject
		//need to generate a list of subject ids and subject names to make sure they're unique
		
		String name = nameField.getText();
		int id = generateUID(); // generates a unique subjectid
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try
		{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT name FROM subject WHERE name='"+name+"';"; // selects a name from the <subject> table where the name entered by the user is already there
			// this avoids a repetition of names since each subject is supposed to have a unique name
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);//getClass();
			if(rs.next()){
				// if there is already a subject by the provided name, an appropriate error is displayed to the user
				
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("A subject by this name already exists!");
					alert.setContentText("Please choose a unique subject name!");
					st.close();
					alert.showAndWait();
					//continue;
			}
			else{
				// if the name is unique, the <subject> table in the database is updated using the following query
				String query1 = "INSERT INTO subject (subjectid, name) VALUES ('"+id+"', '"+name+"');";
				st.executeUpdate(query1);
				st.close();
				con.close();
				try
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Subject successfully created!");

					alert.showAndWait();
					
					//at the end of it all, the AdminMenu scene is opened up again
					
					Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
			    	Stage stage = (Stage) createBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	public void cancelBtnPressed(){
		//if the user clicks on the <cancelBtn> Button, the program goes back to the AdminMenu by opening the relevant fxml file
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public int generateUID(){ // example of a recursive algorithm
		// this method is used to check if the randomly generated accountID is unique (if there is already an account with the same ID in the database)
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		SecureRandom random = new SecureRandom(); 
		ResultSet rs;
		
		int accountID = random.nextInt(100000); 
		// the randomly generated accountID of the newly created account
		//String accountIdFormatted = String.format("%05d", accountID);
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "SELECT * FROM subject WHERE subjcetid='"+accountID+"';";
			rs = st.executeQuery(query); 
			// searches the Account table in the database for an account with the same idAccount as the on randomly generated
			
			if(rs.next()){
				generateUID(); 
				// if there is already an account, it calls itself. Essentially, the process starts over
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return accountID; 
		// if there was no pre-existent account with the same id, it returns the randomly generated id as a String
	}
}
