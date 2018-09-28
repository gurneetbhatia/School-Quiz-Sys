package application;

import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class TeacherMenuController {
	@FXML
	Button createNewQuizBtn;
	@FXML
	Button viewQuizzesBtn;
	@FXML
	Button performanceStatisticsBtn;
	@FXML
	Button messagesBtn;
	@FXML
	Button notificationsBtn;
	@FXML
	Button leaderboardBtn;
	@FXML
	Button settingsBtn;
	@FXML
	Button logoutBtn;
	
	
	public void createNewQuizBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz.fxml"));
	    	Stage stage = (Stage) createNewQuizBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void viewQuizzesBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu_ViewQuiz.fxml"));
	    	Stage stage = (Stage) createNewQuizBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void performanceStatisticsBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("StudentStats.fxml"));
	    	Stage stage = (Stage) createNewQuizBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void messagesBtnPressed(){
		
	}
	
	public void notificationsBtnPressed(){//these are only to review long answer questions (laq)
		//format:- (studentid+":"+question+":"+ownerid+":"+answer+":"+quizid+":"+done);
		
		List<String> lines = new ArrayList<String>();
		
		try{
			BufferedReader in = new BufferedReader(new FileReader("/Users/inderdeepbhatia/Documents/workspace/School Quiz System/bin/application/reviewLAQ.txt"));
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
		
		if(classifiedLines.size() > 0){
			//check if at least one of the questions has the same ownerid as the logged in account (the teacher)
			//only the lines that are relevant to this teacher. Determined by checking the ownerid are added to the laqQuestions queue
			for(LAQLine l: classifiedLines){
				if(l.ownerid.equals(LoginController.account.idAccount)){
					TeacherMenu_ReviewLAQController.laqQuestions.add(l);
				}
			}
			if(TeacherMenu_ReviewLAQController.laqQuestions.size()>0){
				//there is at least one question pending for this teacher
				//start with the question at the head of the queue
				
				try{
					Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu_ReviewLAQ.fxml"));
			    	Stage stage = (Stage) logoutBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
				
			}
			else{
				//this teacher has no pending questions
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("No pending questions that require to be reviewed!");

				alert.showAndWait();
			}
			
		}
		else{
			//all the questions have been checked already
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("No pending questions that require to be reviewed!");

			alert.showAndWait();
		}
		
		
	}
	
	public void leaderboardBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("Leaderboard.fxml"));
	    	Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void settingsBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("MyAccount.fxml"));
	    	Stage stage = (Stage) settingsBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logoutBtnPressed(){
		LoginController.account = new Account();
		try{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	    	Stage stage = (Stage) logoutBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
}
