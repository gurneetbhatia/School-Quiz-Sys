package application;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

public class DisplayLAQController implements Initializable {

	@FXML
	Label questionLbl;
	@FXML
	TextArea answerTA;
	@FXML
	Button nextBtn;
	
	public void nextBtnPressed(){
		
		//how do i send the answer to this question to the teacher to mark?
		//how do i get a response on the marks gained for the question from the teacher?
		//add an option in the teacher menu to review long answer questions
		//The teacher inputs the mark and that's added to the total gained by the student
		
		/**Create a new file called "reviewLAQ"
		 * This file should store data in the following format: studentid:question:ownerid:answer
		 * When a quiz is created, you are storing the accountid of the owner in the Quiz table
		 * Load it from there
		 * Add an option in the teacher menu to review laq answers
		 * In this display all the answers relevant to them without showing the name of the student to avoid partiality
		 * Ask them to enter the marks for each answer and add it to the total score of the student for the particular quiz*/
		
		String answer = answerTA.getText();
		String studentid = LoginController.account.idAccount;
		String question = DisplayQuizToStudentController.qip.currentQuestion.question;
		String ownerid = "";
		
		String quizid = DisplayQuizToStudentController.quizID;
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "select ownerid from Quiz where idQuiz='"+quizid+"'";
			ResultSet rs = st.executeQuery(query);
			
			if(rs.next()){
				ownerid = rs.getString("ownerid");
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			//format: studentid:question:ownerid:answer
			
			FileWriter fileWriter = new FileWriter("reviewLAQ.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(studentid+":"+question+":"+ownerid+":"+answer);
			
			printWriter.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		gotoNextQuestion();
	}
	
	public void gotoNextQuestion(){
		
		DisplayQuizToStudentController.qip.currentQuestion = DisplayQuizToStudentController.qip.questions.remove();//sets the element at position 0 in the queue to the object <question>
		
		/**There are four fxml files that i need to create for the four different kinds of questions
		 * mcq
		 * laq
		 * saq
		 * rvq*/
		
		if(DisplayQuizToStudentController.qip.questions.size()>0){
			
			if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("mcq")){
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayMCQ.fxml"));
			    	Stage stage = (Stage) nextBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
			else if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("saq")){
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplaySAQ.fxml"));
			    	Stage stage = (Stage) nextBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			else if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("laq")){
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayLAQ.fxml"));
			    	Stage stage = (Stage) nextBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("rvq")){
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayRVQ.fxml"));
			    	Stage stage = (Stage) nextBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else{
			//there is no "next question" and the quiz is over
			
			quizOver();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("The quiz is over! Congratulations!");

			alert.showAndWait();
			
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
		    	Stage stage = (Stage) nextBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void quizOver(){
		//Once the student has completed the quiz, remember to update checkQuizDone.txt
		//Also, when the quiz is completed, reset the static queue of questions
		//format: quizid:studentid
		//also need to save the score of the student in the score table
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		 
		try{
			FileWriter fileWriter = new FileWriter("checkQuizDone.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(DisplayQuizToStudentController.quizID+":"+LoginController.account.idAccount);
			printWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strDate = dateFormat.format(date);
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			int check = 0;//setting it by default to false (or 0)
			List<String> choices = new ArrayList<>();
			choices.add("Yes");
			choices.add("No");
			
			ChoiceDialog<String> dialog = new ChoiceDialog<>("No", choices);
			dialog.setTitle("Choice Dialog");
			dialog.setHeaderText("Are you fine with your score being posted to the leaderboard?");
			dialog.setContentText("Choice:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    if(result.get().equals("Yes")){
			    	check = 1;//don't need an else because it's already set to zero for the other scenario by default
			    }
			}
			
			
			//'allowedOnLead' allows it on the leaderboard if it's 0 else it posts it as anonymous
			String query = "insert into Score (studentID, quizID, score, date, allowedOnLead) values ('"+LoginController.account.idAccount+"', '"+DisplayQuizToStudentController.quizID+"', '"+DisplayQuizToStudentController.qip.score+"', '"+strDate+"', '"+check+"')";
			st.executeUpdate(query);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		DisplayQuizToStudentController.qip.questions = new LinkedList<Question>();
		DisplayQuizToStudentController.qip = new QuizInProgress();
		
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		questionLbl.setText(DisplayQuizToStudentController.qip.currentQuestion.question);
		
	}
	
}
