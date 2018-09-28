package application;

import javafx.application.Application;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.glassfish.jersey.client.ClientResponse;

import com.mysql.jdbc.Connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {
	//Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://35.194.201.205:3306/quizdb", "root", "O3fub27LLmFc52JA");
	@Override
	public void start(Stage primaryStage) throws IOException {
		String b = "";
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		try
		{
			//if login_credentials is empty, there are no users. Thus, go to admin registration else to login screen
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "SELECT * FROM Account";
			Statement st = con.createStatement();
			String q = "select username, password from Account";
			ResultSet r = st.executeQuery(q);
			while(r.next()){
				System.out.println(r.getString("username")+" : "+r.getString("password"));
			}
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
			else{
				Parent root = FXMLLoader.load(getClass().getResource("Start.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
		/*String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "create table Score (studentID varchar(45), quizID varchar(45), score varchar(45), date char(10), allowedOnLead tinyint(1))";//dd/mm/yyyy
			//String query = "drop table score";
			st.executeUpdate(query);
			
			System.out.println("Done");
			
		} catch(Exception e){
			e.printStackTrace();
		}*/
		
	}
}
