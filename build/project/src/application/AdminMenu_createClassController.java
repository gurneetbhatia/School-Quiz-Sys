package application;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.security.SecureRandom;
import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminMenu_createClassController implements Initializable{
	@FXML
	TextField nameField;
	@FXML
	Button cancelBtn;
	@FXML
	Button createBtn;
	@FXML
	ChoiceBox<String> subjectCb;
	
	public void createBtnPressed(){
		String name = nameField.getText();
		String subject = subjectCb.getValue();
		if((!name.equals("")) && (!subject.equals(""))){
			String url = "jdbc:mysql://localhost/db";
			String user = "root";
			String password = "bUrp@107";
			
			try{
				
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				
				SecureRandom random = new SecureRandom();
				int id = random.nextInt(100000);
				Statement st = con.createStatement();
				
				String query1 = "SELECT subjectid FROM subject WHERE name='"+subject+"'";
				int subid = 0;
				ResultSet rs = st.executeQuery(query1);
				while(rs.next()){
					subid = rs.getInt(1);
				}
				
				String query2 = "INSERT INTO class (class_id, name, subject_id) VALUES ('"+id+"', '"+name+"', '"+subid+"');";
				
				st.executeUpdate(query2);
				st.close();
				
			}catch(Exception e){
				
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT * FROM subject";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<String> subjectNames = new ArrayList<String>();
			while(rs.next()){
				subjectNames.add(rs.getString(2));
			}
			st.close();
			subjectCb.setItems(FXCollections.observableArrayList(subjectNames));
		} catch(Exception e){
			
		}
	}
}
