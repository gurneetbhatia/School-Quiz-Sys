package application;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class StudentStatsController implements Initializable{
	/**The corresponding scene of this controller class displays to the user a graph showing the scores on quizzes of the student with the date on 
	 * which the quiz was solved.*/
	@FXML
	LineChart<String, Double> chart;//score on y-axis and date on x-axis
	@FXML
	ChoiceBox<String> studentidCB;
	@FXML
	Button selectBtn;
	@FXML
	Button backBtn;
	
	@SuppressWarnings("unchecked")
	public void selectBtnPressed(){
		Map<Date, Double> data = new HashMap<Date, Double>();// date, score
		//dd/MM/yyyy
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		
		String studentID = studentidCB.getValue();
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
    		Statement st = con.createStatement();
    		
    		String query = "select score, date from Score where studentID='"+studentID+"'";
    		ResultSet rs = st.executeQuery(query);
    		
    		while(rs.next()){
    			Date date = formatter.parse(rs.getString("date"));
    			Double score = Double.parseDouble(rs.getString("score"));
    			data.put(date, score);
    		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		Map<Date, Double> sortedData = new TreeMap<Date, Double>(data);//NOTE:- Using another abstract data type (TreeMap)
		
		
		XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
		
		series.setName("Scores");
		for(Date d: sortedData.keySet()){
			String str = formatter.format(d);
			series.getData().add(new XYChart.Data<String, Double>(str, sortedData.get(d)));
		}
		chart.getData().add(series);
		
		
	}
	public void backBtnPressed(){
		/**The method first needs to determine the account type of the currently logged in user by checking the accountType attribute of the static object
		 * of the Account class in the LoginController class.
		 * Then it needs to open the relevant meny scene.*/
		if(LoginController.account.accountType.equals("Teacher")){
			try{
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
		    	Stage stage = (Stage) backBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(LoginController.account.accountType.equals("Student")){
			try{
				Parent root = FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
		    	Stage stage = (Stage) backBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
		chart.setTitle("Student Performance Over Time");
		// the title of the chart is set
		chart.getXAxis().setLabel("Date Completed");
		// the x-axis label of the chart is set
		chart.getYAxis().setLabel("Score");
		// the y-axis label of the chart is set
		
		if(LoginController.account.accountType.equals("Teacher")){
			// if the accountType of the currently logged in user is "Teacher", then the idAccount column of all the accounts in the Account table are fetched
			// whose accountType is "Student" so that the user may select a studentid and his/her/its statistics can be displayed with the use of a LineChart
			try{
				
				String url = Data.server.url;
				String user = Data.server.username;
				String password = Data.server.password;
	    		
	    		Connection con = (Connection) DriverManager.getConnection(url, user, password);
	    		Statement st = con.createStatement();
	    		
	    		String query = "select idAccount from Account where accountType='Student'";
	    		ResultSet rs = st.executeQuery(query);
	    		List<String> ids = new ArrayList<String>();
	    		while(rs.next()){
	    			ids.add(rs.getString("idAccount"));
	    		}
	    		studentidCB.setItems(FXCollections.observableArrayList(ids));
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(LoginController.account.accountType.equals("Student")){
			
			
			selectBtn.setDisable(true);
			studentidCB.setDisable(true);
			// a student is not to have access to the feature that would allow them to check the statistics of other students.
			// this is done by disabling the selectBtn Button and the studentidCB ChoiceBox.
			
			
			
			
			Map<Date, Double> data = new HashMap<Date, Double>();// date, score
			//dd/MM/yyyy
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			
			String studentID = LoginController.account.idAccount;
			String url = Data.server.url;
			String user = Data.server.username;
			String password = Data.server.password;
			
			try{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
	    		Statement st = con.createStatement();
	    		
	    		String query = "select score, date from Score where studentID='"+studentID+"'";
	    		ResultSet rs = st.executeQuery(query);
	    		
	    		while(rs.next()){
	    			Date date = formatter.parse(rs.getString("date"));
	    			Double score = Double.parseDouble(rs.getString("score"));
	    			data.put(date, score);
	    		}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Map<Date, Double> sortedData = new TreeMap<Date, Double>(data);//NOTE:- Using another abstract data type (TreeMap)
			
			XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
			
			series.setName("Scores");
			for(Date d: sortedData.keySet()){
				String str = formatter.format(d);
				series.getData().add(new XYChart.Data<String, Double>(str, sortedData.get(d)));
			}
			chart.getData().add(series);
		}
		
	}
}