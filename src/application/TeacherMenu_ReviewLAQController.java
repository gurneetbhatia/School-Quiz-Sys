package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

public class TeacherMenu_ReviewLAQController implements Initializable{
	
	@FXML
	Label questionLbl;
	@FXML
	Label answerLbl;
	@FXML
	TextField marksTF;
	@FXML
	Button doneBtn;
	@FXML
	Button backBtn;
	
	public static Queue<LAQLine> laqQuestions = new LinkedList<LAQLine>();//NOTE:- Using an abstract data type here
	static String studentID;
	static String quizID;
	public void doneBtnPressed(){
		//save the score from the user
		//in the reviewLAQ file, set the done "varible" to true
		//update the score table
		double marks = 0.0;
		try{
			marks = Double.parseDouble(marksTF.getText());
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Value Error!");
			alert.setContentText("Please enter a valid value for the marks!");

			alert.showAndWait();
		}
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "select score from Score where studentID='"+studentID+"' and quizID='"+quizID+"'";
			ResultSet rs = st.executeQuery(query);
			
			double currentScore = 0.0;
			
			if(rs.next()){
				//get the score and then add the marks awarded to it
				currentScore = Double.parseDouble(rs.getString("score"));
			}
			
			double newScore = currentScore + marks;
			
			String query1 = "update Score set score'"+newScore+"' where studentID='"+studentID+"' and quizID='"+quizID+"'";
			st.executeUpdate(query1);
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		updateFile();
		
		studentID = new String();
		quizID = new String();
		
		//if this was the last question, tell the user and return to TeacherMenu
		//if not, reload this same fxml file and the next question will be displayed
		
		if(laqQuestions.isEmpty()){
			//this was the last questions
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have finished checking all the questions!");

			alert.showAndWait();
			
			try{
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
		    	Stage stage = (Stage) doneBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
			
		}
		else{
			//refresh the same fxml file
			try{
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu_ReviewLAQ.fxml"));
		    	Stage stage = (Stage) doneBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void updateFile(){
		
		//format:- (studentid+":"+question+":"+ownerid+":"+answer+":"+quizid+":"+done);
		List<String> lines = new ArrayList<String>();
		
		try{
			BufferedReader in = new BufferedReader(new FileReader("reviewLAQ.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
			}
			
			in.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		List<LAQLine> classifiedLines = new ArrayList<LAQLine>();
		for(String str: lines){
			LAQLine l = new LAQLine();
			int a = 0;
			while(str.charAt(a)!=':'){
				l.studentid += str.charAt(a);
				a+=1;
			}
			while(str.charAt(a)!=':'){
				l.question += str.charAt(a);
				a += 1;
			}
			while(str.charAt(a)!=':'){
				l.ownerid += str.charAt(a);
				a += 1;
			}
			while(str.charAt(a)!=':'){
				l.answer += str.charAt(a);
				a += 1;
			}
			while(str.charAt(a)!=':'){
				l.quizid += str.charAt(a);
				a += 1;
			}
			String s = "";
			while(a<str.length()){
				s += str.charAt(a);
				a += 1;
			}
			if(s.equals("true")){
				l.done = true;
			}
			else{
				l.done = false;
			}
			classifiedLines.add(l);
		}
		//find the same studentID and quizID (and ownerid=accountid (from login controller))
		for(LAQLine l: classifiedLines){
			if((l.ownerid.equals(LoginController.account.idAccount)) && (l.quizid.equals(quizID)) && (l.studentid.equals(studentID)) &&
					(l.question.equals(questionLbl.getText()))){
				
				LAQLine l_copy = l;
				classifiedLines.remove(l);
				l_copy.done = true;
				classifiedLines.add(l_copy);
				
			}
		}
		
		try{
			
			FileWriter fileWriter = new FileWriter("reviewLAQ.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			for(LAQLine l: classifiedLines){
				printWriter.println(l.studentid+":"+l.question+":"+l.ownerid+":"+l.answer+":"+l.quizid+":"+l.done);
			}
			
			printWriter.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void backBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
	    	Stage stage = (Stage) doneBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LAQLine currentQuestion;
		if((currentQuestion = laqQuestions.poll()) != null){//sets the head of the queue to currentQuestion but using the != null part just to avoid an error if the queue is empty
			
			questionLbl.setText(currentQuestion.question);
			answerLbl.setText(currentQuestion.answer);
			studentID = currentQuestion.studentid;
			quizID = currentQuestion.quizid;
			
		}
		else{
			//queue is empty
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have finished correcting all questions!");

			alert.showAndWait();
			
			try{
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
		    	Stage stage = (Stage) doneBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
}
