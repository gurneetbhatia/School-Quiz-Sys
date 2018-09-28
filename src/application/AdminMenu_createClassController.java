package application;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.security.SecureRandom;
import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminMenu_createClassController implements Initializable{ // adding the 'implements Initializable' part in this line calls the <initialize(URL location, ResourceBundle resources)> method by default when the scene is loaded
	@FXML
	TextField nameField;
	// the name of the class
	@FXML
	Button cancelBtn;
	// when this button is pressed by the user, the <cancelBtnPressed> method is called
	@FXML
	Button createBtn;
	// when this button is pressed by the user, the <createBtnPressed> method is called
	@FXML
	ChoiceBox<String> subjectCb;
	// a ChoiceBox that contains all the subject names that are pre-existent in the database
	
	public void createBtnPressed(){
		String name = nameField.getText();
		// Creates and initializes the String variable <name> to the text held in the TextField <nameField>
		String subject = subjectCb.getValue();
		// Creates and initializes a String variable <subject> to the text held in the selected value from the <subjectCb> ChoiceBox
		if((!name.equals("")) || (!subject.equals(""))){
			// checks if the variables <name> and <subject> are not empty
			String url = Data.server.url;
			String user = Data.server.username;
			String password = Data.server.password;
			
			try{
				
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				
				int id = generateUID();
				Statement st = con.createStatement();
				
				String query1 = "SELECT subjectid FROM subject WHERE name='"+subject+"'";
				// selects the <subjectid> of the selected subject name
				int subid = 0;
				ResultSet rs = st.executeQuery(query1);
				if(rs.next()){
					subid = rs.getInt("subjectid");
				}
				
				String query2 = "INSERT INTO class (class_id, name, subject_id) VALUES ('"+id+"', '"+name+"', '"+subid+"');";
				// updates the class table with the data collected from the user
				
				st.executeUpdate(query2);
				st.close();
				con.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
		    	Stage stage = (Stage) createBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
				
				// changes the scene back to the AdminMenu by opening the relevant fxml file
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			// Control of the program enters here if the variables <name> OR <subject> were empty
			// If so, it displays an appropriate error
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide valid inputs for name, email and account type");

			alert.showAndWait();
			
		}
	}
	public void cancelBtnPressed(){
		// this method is called if and when the user clicks the <cancelBtn? Button
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
		
		// changes the scene back to the AdminMenu by opening the relevant fxml file
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
			
			String query = "SELECT * FROM class WHERE class_id='"+accountID+"';";
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT name FROM subject";
			// selects all the items in the <name> column of the <subject> table in the database
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			List<String> subjectNames = new ArrayList<String>();
			// an empty list is initialized that eventually ends up holding all the subject names in the <subject> table
			while(rs.next()){
				subjectNames.add(rs.getString("name"));
			}
			st.close();
			subjectCb.setItems(FXCollections.observableArrayList(subjectNames));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
