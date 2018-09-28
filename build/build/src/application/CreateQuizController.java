package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CreateQuizController implements Initializable{
	
	@FXML
	ListView<String> questionsView;
	@FXML
	ChoiceBox<String> classCB;
	@FXML
	ChoiceBox<String> subjectCB;
	@FXML
	Button addQuestionBtn;
	@FXML
	Button previousPageBtn;
	@FXML
	Button cancelBtn;
	@FXML
	Button saveBtn;
	@FXML
	Button nextPageBtn;
	//TODO:- Delete the previousPageBtn and the nextPageBtn
	//static Quiz quiz = new Quiz();
	String pinFormatted;
	/**Format to save in file
	 * QuizID
	 * Question
	 * QuestionType
	 * Correct Answer
	 * All Answers*/
	public void addQuestionBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion.fxml"));
	    	Stage stage = (Stage) addQuestionBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void previousPageBtnPressed(){
		
	}
	
	public void  cancelBtnPressed(){
		//set the quiz object to null and change scene
		Data.quiz = new Quiz();
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void saveBtnPressed(){
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		String subject_name = subjectCB.getValue();
		String class_name = classCB.getValue();
		//save data from the quiz object in the correct format to the file and then update the sql database with required data
		//After this delete the object's data by setting it to null and changing the scene
		//write to sql database
		//first check if the id is unique
		//also send an email to all the students in the class about a new quiz
		String idQuiz = produceUID();
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			//quizid
			//classid
			//subjectid
			//teacherid
			
			
			String subjectIDquery = "select subjectid from subject where name='"+subject_name+"'";
			ResultSet rs1 = st.executeQuery(subjectIDquery);
			String subjectid = "";
			if(rs1.next()){
				subjectid = rs1.getString("subjectid");
			}
			
			
			String classIDquery = "select class_id from class where name='"+class_name+"' and subject_id='"+subjectid+"'";
			ResultSet rs2 = st.executeQuery(classIDquery);
			String classid = "";
			if(rs2.next()){
				classid = rs2.getString("class_id");
			}
			int noq = Data.quiz.questions.size();//number of questions
			String query = "insert into Quiz (idQuiz, class_id, subjectid, ownerid, numberOfQuestions) values ('"+idQuiz+"', '"+classid+"', '"+subjectid+"', '"+LoginController.account.idAccount+"', '"+noq+"')";
			st.executeUpdate(query);
			Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText("Quiz Successfully saved!");
    		alert.setContentText("The quiz has been successfully saved to the database");

    		alert.showAndWait();
    		
    		//Send a notification via email to every student in the relevant class about this quiz now
    		String q = "select email from Account where accountType='Student' and class='"+classid+"'";
    		ResultSet r = st.executeQuery(q);
    		
    		//String recipient, String subject, String text
    		String subject = "New Quiz!";
    		String text = "Please login to the School Quiz System as a new quiz has been added to your class. "
    				+ "To complete this particular quiz, use Quiz ID: "+idQuiz;
    		
    		while(r.next()){
    			Mailgun.SendSimple(r.getString("email"), subject, text);
    		}
    		
    		st.close();
    		//TODO:- Close all statements like this wherever you've used sql
    		
		} catch(Exception e){
			
		}

		//write to quiz file
		writeToQuizFile(idQuiz);
		
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
		
	}
	
	public String produceUID(){//uses repercussion to produce a unique quiz id
		// TODO:- use repercussion everywhere for producing unique IDs
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		String uid = "";
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			SecureRandom random = new SecureRandom();
			int id = random.nextInt(100000);
			String idFormatted = String.format("%05d", id);
			String query = "select * from Quiz where idQuiz='"+idFormatted+"'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				//the id is not unique
				produceUID();
			}
			else{
				uid = idFormatted;
			}
			
		} catch(Exception e){
			
		}
		return uid;
		
	}
	
	public void writeToQuizFile(String quizID){
		/**FORMAT:
		 * QuizID
		 * marks
		 * nmarks
		 * Question
		 * QuestionType
		 * Correct Answer
		 * All Answers
		 * "*** end ***" */
		try{
			
			FileWriter fileWriter = new FileWriter("quizzes.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
		    for(int i = 0;i<Data.quiz.questions.size();i++){
		    	printWriter.println("QuizID = "+quizID);
		    	printWriter.println("Marks = "+Data.quiz.questions.get(i).marks);
		    	printWriter.println("Negative Marks = "+Data.quiz.questions.get(i).nmarks);
		    	printWriter.println("Question = "+Data.quiz.questions.get(i).question);
		    	printWriter.println("Question Type = "+Data.quiz.questions.get(i).questionType);
		    	printWriter.println("Correct Answer: "+Data.quiz.questions.get(i).correctAnswer);
		    	for(int x = 0;x<Data.quiz.questions.get(i).Answers.size();x++){
		    		printWriter.println("Answer "+(x+1)+": "+Data.quiz.questions.get(i).Answers.get(x));
		    	}
		    	printWriter.println("*** end ***");
		    	printWriter.close();
		    }
		} catch(Exception e){
			e.printStackTrace();
		}
		Data.quiz = new Quiz();//resetting data.quiz for new quizzes
	}
	
	public void nextPageBtnPressed(){
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "select * from subject";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<String> subjectNames = new ArrayList<String>();
			while(rs.next()){
				subjectNames.add(rs.getString(2));
			}
			
			subjectCB.setItems(FXCollections.observableArrayList(subjectNames));
			
			String query1 = "select * from class";
			ResultSet rs1 = st.executeQuery(query1);
			ArrayList<String> classes = new ArrayList<String>();
			while(rs1.next()){
				classes.add(rs1.getString(2));
			}
			
			classCB.setItems(FXCollections.observableArrayList(classes));
			
		} catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<String> qs = new ArrayList<String>();
		for(int i = 0;i<Data.quiz.questions.size();i++){
			
			qs.add(Data.quiz.questions.get(i).question);
			
		}
		questionsView.setItems(FXCollections.observableArrayList(qs));
		
	}
}
