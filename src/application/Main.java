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
import java.util.*;


public class Main extends Application {
	//Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://35.194.201.205:3306/quizdb", "root", "O3fub27LLmFc52JA");
	
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		try
		{
			//if Account is empty, there are no users. Thus, go to admin registration else to login screen
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			String q = "select username, password from Account";
			// query the Account table in the SQL database to check if it's empty
			ResultSet rs = st.executeQuery(q);
			if(rs.next()){
				// the Account table has one or more non-empty rows inside it
				Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
			else{
				// the Account table is empty and the first Admin user must now be registered.
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
		
		List<String> lines = new ArrayList<String>();
		
		try{
			BufferedReader in = new BufferedReader(new FileReader("/Users/inderdeepbhatia/Documents/workspace/School Quiz System/bin/application/ServerDetails.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
			}
			
			in.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		/*Data.server.url = "jdbc:mysql://localhost/db";
		Data.server.username = "root";
		Data.server.password = "bUrp@107";*/
		
		Data.server.url = lines.get(0);
		Data.server.username = lines.get(1);
		Data.server.password = lines.get(2);
		
		
		
		
		launch(args);
		
	}
}
