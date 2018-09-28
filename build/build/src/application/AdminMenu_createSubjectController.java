package application;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

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
	@FXML
	Button cancelBtn;
	@FXML
	Button createBtn;
	public void createBtnPressed(){
		//creating the subject
		//need to generate a list of subject ids and subject names to make sure they're unique
		String name = nameField.getText();
		SecureRandom random = new SecureRandom();
		int id = random.nextInt(100000);
		
		
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		while(true)
		{
			try
			{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				String query = "SELECT subjectid, name FROM subject WHERE subjectid='"+id+"' OR name='"+name+"';";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);//getClass();
				if(rs.next()){
					String n = rs.getString(2);
					if(n.equals(name)){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setHeaderText("A subject by this name already exists!");
						alert.setContentText("Please choose a unique subject name!");
						st.close();
						alert.showAndWait();
						//continue;
					}
				}
				else{
					String query1 = "INSERT INTO subject (subjectid, name) VALUES ('"+id+"', '"+name+"');";
					st.executeUpdate(query1);
					st.close();
					break;
				}
			} catch(Exception e) {
				
			}
		}
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
	    	Stage stage = (Stage) createBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
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
