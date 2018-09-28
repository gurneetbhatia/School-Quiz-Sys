package application;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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

public class AdminMenu_createAccountController implements Initializable{
	@FXML
	ChoiceBox<String> createCh;//accountType
	@FXML
	TextField nameField;
	@FXML
	TextField emailField;
	@FXML
	//TODO:- change this to a ChoiceBox too
	ComboBox<String> classesCb;
	@FXML
	Button createBtn;
	@FXML
	Button cancelBtn;
	public void createBtnPressed(){
		
		String name = nameField.getText();
		String email = emailField.getText();
		String accountType = createCh.getValue();
		String selClass = classesCb.getValue();
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		SecureRandom random = new SecureRandom();
		int regKey = random.nextInt(100000);
		String regKeyFormatted = String.format("%05d", regKey);
		
		int accountID = random.nextInt(100000);
		String accountIdFormatted = String.format("%05d", accountID);
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			while(true) {
				String query = "SELECT * FROM Account WHERE idAccount='"+accountIdFormatted+"';";
				ResultSet rs1 = st.executeQuery(query);
				if(rs1.next()){
					accountID = random.nextInt(100000);
					accountIdFormatted = String.format("%05d", accountID);
				}
				else{
					break;
				}
			}
			//TODO:- In the login class, add a method to make sure that the inputs aren't "null" (as a string)
			//String query1 = "INSERT INTO Account (idAccount, name, password, email, class, accountType, registrationID) VALUES ('"+accountIdFormatted+"', 'NULL', 'NULL', '"+email+"', '"+selClass+", '"+accountType+"', '"+regKeyFormatted+"');";
			//INSERT INTO class (class_id, name, subject_id) VALUES ('"+id+"', '"+name+"', '"+subid+"');
			
			String query1 = "INSERT INTO Account (idAccount, name, password, email, class, accountType, registrationID, username) VALUES ('"+accountIdFormatted+"', '"+name+"', ' ', '"+email+"', '"+selClass+"', '"+accountType+"', '"+regKeyFormatted+"', ' ')";
			
			st.executeUpdate(query1);
    		st.close();
    		con.close();
    		String subject = "Register to School Quiz System";
    		String content = "Register to the School Quiz System by opening the software, selecting 'Register' on the login screen and entering"
    				+ " this registration key: "+regKeyFormatted;
    		try{
    			Mailgun.SendSimple(email, subject, content);
    			
    		} catch(Exception e) {
    			
    		}
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText("Account Created Successfully!");
    		alert.setContentText("The account has been created succesfully and an email with the instructions to register have been sent to"
    				+ "the provided email address!");

    		alert.showAndWait();
    		
    		Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) createBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void cancelBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) createBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ArrayList<String> accountTypes = new ArrayList<String>();
		accountTypes.add("admin");
		accountTypes.add("Teacher");
		accountTypes.add("Student");
		accountTypes.add("Parent");
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			createCh.setItems(FXCollections.observableArrayList(accountTypes));
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT * FROM class;";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			ArrayList<String> classNames = new ArrayList<String>();
			while(rs.next()){
				classNames.add(rs.getString(2));
			}
			st.close();
			classesCb.setItems(FXCollections.observableArrayList(classNames));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
