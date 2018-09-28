package application;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

public class AdminMenu_createAccountController implements Initializable{ // adding the 'implements Initializable' part in this line calls the <initialize(URL location, ResourceBundle resources)> method by default when the scene is loaded
	@FXML
	ChoiceBox<String> createCh;
	//accountType to be created
	@FXML
	TextField nameField;
	// the name (not username as this is set by the user himself/herself/itself) of the user
	@FXML
	TextField emailField;
	// the email of the user. A registration email is sent on this
	@FXML
	ChoiceBox<String> classesCb; 
	//the class that the user belongs to (this is an optional since the user may or may not belong to a class)
	@FXML
	Button createBtn; 
	// when this button is pressed, the <createBtnPressed()> method is called
	@FXML
	Button cancelBtn;
	// when this button is pressed, the <cancelBtnPressed()> method is called
	public void createBtnPressed(){
		//this method is called when the <createBtn> Button is pressed by the user
		
		String name = nameField.getText(); 
		// Creates and initializes the String variable <name> to the text held in the TextField <nameField>
		String email = emailField.getText();
		// Creates and initializes the String variable <email> to the text held in the TextField <emailField>
		String accountType = createCh.getValue();
		// Creates and initializes the String variable <accountType> to the text held in the selected value from the ChoiceBox <createCh>
		String selClass = classesCb.getValue();
		// Creates and initializes the String variable <selClass> to the text held in the selected value from the ChoiceBox <classesCb>
		
		if((name.equals("")) || (email.equals("")) || (accountType.equals(""))){ 
			// checks if <name>, <email> OR <accountType> are empty strings
			// If so, it displays an appropriate error
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide valid inputs for name, email and account type");

			alert.showAndWait();
			
		}
		else{
		
			String url = Data.server.url;
			String user = Data.server.username;
			String password = Data.server.password;
			
			SecureRandom random = new SecureRandom(); 
			// the randomly generated registration key that will be used by the user to register to the database
			int regKey = random.nextInt(100000);
			String regKeyFormatted = String.format("%05d", regKey);
			 
			// the randomly generated accountID of the newly created account
			String accountIdFormatted = generateUID(); 
			// the <generateUID()> method is called to produce a unique accountID
			
			try{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				
				String query1 = "INSERT INTO Account (idAccount, name, password, email, class, accountType, registrationID, username) VALUES ('"+accountIdFormatted+"', '"+name+"', ' ', '"+email+"', '"+selClass+"', '"+accountType+"', '"+regKeyFormatted+"', ' ')";
				
				st.executeUpdate(query1); 
				// updates the Account table in the database to accommodate for the newly created account
	    		st.close();
	    		con.close();
	    		// closes the object of the Connection class
	    		String subject = "Register to School Quiz System";
	    		String content = "Register to the School Quiz System by opening the software, selecting 'Register' on the login screen and entering"
	    				+ " this registration key: "+regKeyFormatted;
	    		try{
	    			Mailgun.SendSimple(email, subject, content);
	    			// an email is sent to the emailID of the newly registered account with instructions for their first login
	    			
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Information Dialog");
	    		alert.setHeaderText("Account Created Successfully!");
	    		alert.setContentText("The account has been created succesfully and an email with the instructions to register have been sent to"
	    				+ "the provided email address!");
	
	    		alert.showAndWait();
	    		
	    		// an information dialog is displayed to the Admin to indicate that the account has been created
	    		
	    		Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
		    	Stage stage = (Stage) createBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
				
				// the program changes the scene back to the AdminMenu by opening the relevant fxml file
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void cancelBtnPressed(){
		// this method is called when the user pressed the <cancelBtn> Button
		try{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) createBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		// the method causes the program to the change the scene back to the AdminMenu by opening the relevant fxml file
		
	}
	
	public String generateUID(){ // example of a recursive algorithm
		// this method is used to check if the randomly generated accountID is unique (if there is already an account with the same ID in the database)
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		SecureRandom random = new SecureRandom(); 
		ResultSet rs;
		
		int accountID = random.nextInt(100000); 
		// the randomly generated accountID of the newly created account
		String accountIdFormatted = String.format("%05d", accountID);
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "SELECT * FROM Account WHERE idAccount='"+accountIdFormatted+"';";
			rs = st.executeQuery(query); 
			// searches the Account table in the database for an account with the same idAccount as the on randomly generated
			
			if(rs.next()){
				generateUID(); 
				// if there is already an account, it calls itself. Essentially, the process starts over
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return accountIdFormatted; 
		// if there was no pre-existent account with the same id, it returns the randomly generated id as a String
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// this method is called by default every time the AdminMenu_createAccount.fxml is opened
		//it loads up the data held in the ChoiceBoxes <createCh> and <classesCb>
		List<String> accountTypes = new ArrayList<String>();
		// contains all the possible account types that the admin may select from
		accountTypes.add("admin");
		accountTypes.add("Teacher");
		accountTypes.add("Student");
		accountTypes.add("Parent");
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try{
			createCh.setItems(FXCollections.observableArrayList(accountTypes));
			// the <createCh> ChoiceBox now contains every element in the <accountTypes> List
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT name FROM class;";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query); // selects all the data held in the <name> column of the table <class> in the database
			
			List<String> classNames = new ArrayList<String>();
			// an empty list is initialized
			while(rs.next()){
				// the program loops through each name of the class that was returned by the query
				classNames.add(rs.getString("name"));
				// the name of each class is added one by one to the list created previously
			}
			st.close();
			con.close();
			classesCb.setItems(FXCollections.observableArrayList(classNames)); 
			// the <classesCb> ChoiceBox now contains every element in the <classNames> List
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
