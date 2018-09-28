package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

import java.util.*;

public class DisplayQuizToStudentController implements Initializable{
	/** The corresponding scene of this class is only accessible to the Student account
	 * It is used by the student to solve questions
	 * Each question type needs to display different kinds of data
	 * Thus, each question type demanded a separate controller class and fxml scene
	 * This scene allows the user to select the quiz they'd like to solve
	 * Then it loads the questions on a queue
	 * It checks the question-type of the first question and opens the relevant fxml scene*/
	@FXML
	ChoiceBox<String> quizidCB;
	// the quizID's extracted from the SQL database that are relevant to the current user
	@FXML
	Button enterBtn;
	// the <enterBtnPressed()> method is called when this button is pressed
	@FXML
	Button backBtn;
	// the <backBtnPressed()> method is called when this button is pressed
	
	public static QuizInProgress qip = new QuizInProgress();
	// A public static object of the QuizInProgress that can be accessed by the controller classes of all the scenes that display different kinds of questions
	// it is declared as public so that it can easily be accessed by other classes
	// It is declared as static so that data modified by one class is visible to another class as well
	public static String quizID = "";
	// A public static string that holds the quizID of the Quiz that the user is currently solving/has selected to solve
	
	
	public void enterBtnPressed(){
		//TODO:- Once the student has completed the quiz, remember to update checkQuizDone.txt
		/**Create a list of type <Question>
		 * This list will contain all the questions for the relevant quiz id
		 * Make this list a public static variable
		 * Check the questionType of the first question and pass the control to the relevant controller file
		 * Each questionType is displayed in a separate manner
		 * Thus, each one has a separate FXML/Controller pair*/
		//TODO:- Also, when the quiz is completed, reset the static queue of questions
		List<String> lines = new ArrayList<String>();
		// this list will contain all the individual lines in the quizzes.txt file
		
		try{
			// reads the lines in the quizzes.txt file and adds them one by one to the <lines> List using the while loop declared later in this block
			BufferedReader in = new BufferedReader(new FileReader("quizzes.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
			    //System.out.println(str);
			}
			
			in.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		/**FORMAT in file:
		 * QuizID
		 * marks
		 * nmarks
		 * Question
		 * QuestionType
		 * Correct Answer
		 * All Answers
		 * "*** end ***" */
		
		//Initialize all the objects in the Questions list
		
		for(int i = 0;i<lines.size();i++){
			Question question = new Question();
			// data from the lines from the text file is loaded using the format I had stored it there in
			// it is now stored attribute-by-attribute in a temporary object of the Question class
			
			i += 1;//starts with i = 1 essentially since we don't need the quizids (abstraction of data due to relevance)
			question.marks = Double.parseDouble(lines.get(i).substring(8));
			
			i += 1;
			System.out.println(lines.get(i).length());
			System.out.println(lines.get(i));
			question.nmarks = Double.parseDouble(lines.get(i).substring(17));
			
			i += 1;
			question.question = lines.get(i).substring(11);
			
			i+= 1;
			question.questionType = lines.get(i).substring(16);
			
			i += 1;
			question.correctAnswer = lines.get(i).substring(16);
			
			while(true){
				i += 1;
				// the "*** end ***" line is indicative of the end of the current Question
				if(lines.get(i).equals("*** end ***")){
					break;
				}
				else{
					question.Answers.add(lines.get(i).substring(10));
				}
			}
			//System.out.println(i);
			qip.questions.add(question);
			// all the data has been extracted for the current question and it is now loaded onto the <questions> Queue of datatype <Question> in the <QuizInProgress> class
		}
		
		quizID = quizidCB.getValue();
		//the queue containing questions has been initialized
		
		gotoNextQuestion();
		
	}
	
	public void gotoNextQuestion(){
		// this method checks the question type of the Question object at the front of the <qip.questions> Queue
		// It then sets it to the <currentQuestion> attribute in the static object of the QuizInProgress class
		qip.currentQuestion = qip.questions.remove();//sets the element at position 0 in the queue to the object <question>
		
		/**There are four fxml files that i need to create for the four different kinds of questions
		 * mcq
		 * laq
		 * saq
		 * rvq*/
		
		if(qip.questions.size()>0){// checks if there is a question remaining in the queue to display
			
			if(qip.currentQuestion.questionType.equals("mcq")){
				// the question type of the <currentQuestion> is multiple choice question
				// the relevant scene is loaded and displayed to the user
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayMCQ.fxml"));
			    	Stage stage = (Stage) enterBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
			else if(qip.currentQuestion.questionType.equals("saq")){
				// the question type of the <currentQuestion> is short answer question
				// the relevant scene is loaded and displayed to the user
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplaySAQ.fxml"));
			    	Stage stage = (Stage) enterBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			else if(qip.currentQuestion.questionType.equals("laq")){
				// the question type of the <currentQuestion> is long answer question
				// the relevant scene is loaded and displayed to the user
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayLAQ.fxml"));
			    	Stage stage = (Stage) enterBtn.getScene().getWindow();
			    	Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					stage.setScene(scene);
			    	
					stage.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			if(qip.currentQuestion.questionType.equals("rvq")){
				// the question type of the <currentQuestion> is random variable question
				// the relevant scene is loaded and displayed to the user
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("DisplayRVQ.fxml"));
			    	Stage stage = (Stage) enterBtn.getScene().getWindow();
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
			// certain things need to be done once the quiz is finished like storing the score
			
			// an Information Alert is displayed to the user to inform them that the quiz has been completed
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("The quiz is over! Congratulations!");

			alert.showAndWait();
			
			try
			{
				// the scene is changed back to the StudentMenu.fxml file
				Parent root = FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
		    	Stage stage = (Stage) enterBtn.getScene().getWindow();
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
		//Once the student has completed the quiz, update checkQuizDone.txt
		//Also, when the quiz is completed, reset the static queue of questions
		//format: quizid:studentid (for checkQuizDone.txt)
		//also need to save the score of the student in the score table in the SQL database
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		 
		try{
			// updates the checkQuizDone.txt file to reflect the current quiz being done by the user
			FileWriter fileWriter = new FileWriter("checkQuizDone.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(DisplayQuizToStudentController.quizID+":"+LoginController.account.idAccount);
			printWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		// the date on which the quiz was solved is also stored ("today's" date)
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strDate = dateFormat.format(date);
		try{
			// relevant data is stored in the SQL database
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			int check = 0;//setting it by default to false (or 0)
			/*To maintain integrity and privacy standards, before posting the name of the user publicly to the database, the user is first asked if they'd 
			 * be fine with their score being posted with the name
			*/
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
			
			
			//'allowedOnLead' allows it on the leaderboard if it's 1 else it posts it as anonymous
			String query = "insert into Score (studentID, quizID, score, date, allowedOnLead) values ('"+LoginController.account.idAccount+"', '"+DisplayQuizToStudentController.quizID+"', '"+DisplayQuizToStudentController.qip.score+"', '"+strDate+"', '"+check+"')";
			st.executeUpdate(query); 
			// a new row with the data is inserted into the Score table in the database
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		// the queue is reset
		DisplayQuizToStudentController.qip.questions = new LinkedList<Question>();
		// the static object of the QuizInProgress class is reset
		DisplayQuizToStudentController.qip = new QuizInProgress();
		
		
	}
	
	public void backBtnPressed(){
		try
		{
			// the user has chosen not to continue with solving a quiz
			// the scene is changed back to the StudentMenu.fxml file
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// the first method that is called in this controller class (right before the scene is loaded)
		Account a = LoginController.account;
		// a temporary object of the Account class is produced 
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		//get the id of the class from the name using sql
		//use this classid to get all the associated quizids and show them on the 
		/**I just created a file called checkQuizDone.txt
		 * In this file, if a student has completed a quiz, add a line of data in the following format: quizid:studentid
		 * In this method, before displaying the quizids, check if the student has completed the quiz
		 * If he/she has completed it already, do NOT display the relevant quiz id*/
		
		//List<String> ids = new ArrayList<String>();//list of all the quizids for the relevant studentid/accountid
		
		Map<String, Boolean> ids = new HashMap<String, Boolean>();//the string is the quizid and boolean is whether the student has done it or not
		try{
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query1 = "select class_id from class where name='"+a.className+"'";
			ResultSet rs1 = st.executeQuery(query1);
			String class_id = "";
			if(rs1.next()){
				class_id = rs1.getString("class_id");
			}
			
			String query = "select idQuiz from Quiz where class_id='"+class_id+"'";
			ResultSet rs = st.executeQuery(query);
			
			
			while(rs.next()){
				ids.put(rs.getString("idQuiz"), false);//by default, we assume that the student hasn't done the quiz yet
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		List<String> lines = new ArrayList<String>();// all the lines in the checkQuizDone.txt file
		
		try{
			/*This block of code opens the checkQuizDone.txt file to read it
			 * It then stores each individual line to the <lines> List*/
			
			BufferedReader in = new BufferedReader(new FileReader("/Users/inderdeepbhatia/Documents/workspace/School Quiz System/bin/application/checkQuizDone.txt"));
			String str;
			
			while((str = in.readLine()) != null){
				// this loop iterates through each line in the text file. It assigns each line one-by-one to the <str> variable which is then added each time to the <lines> List
			    lines.add(str);
			}
			
			in.close();
			// the instance of BufferedReader is closed
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		for(String str: lines){
			/*This for-each style loop runs through each String element in the <lines> list
			 * and identifies the quizid and studentid individually*/
			
			String qid = "";//quizid
			String sid = "";//studentid
			
			char[] chArr = str.toCharArray();
			// the current element in the <lines> list is converted to an array of characters (as that is what a String essentially is)
			
			int i = -1;// <i> is indicative of the position of the colon (its use is described below)
			/*The format that the data is stored in the checkQuizDone.txt file is quizID:studentID
			 * The following loop iterates through each element in the String element of <lines> until a colon (:) is found.
			 * Every character in the current line before the colon must be part of the quizID String.
			 * Every character in the current line after the colon must be part of the studentID String.*/
			for(char ch: chArr){
				i += 1;
				if(ch != ':'){
					qid += ch;
				}
				else{
					break;
				}
			}
			
			// the following loop starts once the previous one has discovered the colon
			// it carries on adding characters to the studentID until the end of the line
			for(int x = i;x<chArr.length;x++){
				sid += chArr[x];
			}
			
			for(int x = 0;x<ids.size();x++){
				// checks for all the entries in the text file and tells the algorithm whether any of the quizzes have been done by the student
				if((ids.containsKey(qid)) && (sid.equals(a.idAccount))){
					//the student has already completed the quiz
					//Thus, it should not be displayed
					ids.replace(qid, true);
				}
			}
			
		}
		
		List<String> displayIDs = new ArrayList<String>();//quizids that have not yet been completed by the user
		Object[] s = ids.keySet().toArray();//collection of all the keys in ids as an array of type string
		

		
		for(int i = 0;i<ids.size();i++){
			if(ids.get(s[i]) == false){//goes through each key and checks if the corresponding value is true or false. DO NOT display if true
				displayIDs.add((String) s[i]);
			}
		}
		
		quizidCB.setItems(FXCollections.observableArrayList(displayIDs));
	}
}
