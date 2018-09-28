package application;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreateQuizController implements Initializable{ // adding the 'implements Initializable' part in this line calls the <initialize(URL location, ResourceBundle resources)> method by default when the scene is loaded
	/** The corresponding scene of this controller class is only accessible to the Teacher account-holders
	 * It is used to create and distribute to students new quizzes*/
	@FXML
	ListView<String> questionsView;
	// a ListView that displays only the questions of each of the <Question> objects created by the user for this particular quiz
	@FXML
	ChoiceBox<String> classCB;
	// contains a choice of all the classes. Students of this class can access the quiz
	@FXML
	ChoiceBox<String> subjectCB;
	// contains a choice of all the subjects
	@FXML
	Button addQuestionBtn;
	// the <addQuestionsBtnPressed()> method is called when this button is pressed
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	@FXML
	Button saveBtn;
	// the <saveBtnPressed()> method is called when this button is pressed
	
	public void addQuestionBtnPressed(){
		// changes the scene to the CreateQuiz_addQuestion.fxml file
		// the user desires to add a question to the current quiz he/she is creating
		// they will be required to choose the type of question they wish to add
		// i.e. Multiple Choice/Long Answer/Short Answer/Random Variable
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion.fxml"));
	    	Stage stage = (Stage) addQuestionBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void  cancelBtnPressed(){
		// reset the quiz and change scene to TeacherMenu.fxml
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
			e.printStackTrace();
		}
	}
	
	public void saveBtnPressed(){
		// the user is done adding questions and now wants to save the quiz
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		String subject_name, class_name = "";
		
		subject_name = subjectCB.getValue();
		class_name = classCB.getValue();
		
		//save data from the quiz object in the correct format to the file and then update the sql database with required data
		//After this delete the object's data by setting it to null and changing the scene
		//write to sql database
		//first check if the id is unique
		//also send an email to all the students in the class about a new quiz
		/**Format to save in file
		 * QuizID
		 * Question
		 * QuestionType
		 * Correct Answer
		 * All Answers*/
		if((subject_name.isEmpty()) || (class_name.isEmpty())){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("INVALID INPUT ERROR!");
			alert.setContentText("Please provide valid inputs for class and subject!");

			alert.showAndWait();
		}
		else{
			String idQuiz = produceUID();
			try{
				
				/** Relevant data needs to be added to the Quiz table in the database:
				 * quizid
				 * classid
				 * subjectid
				 * teacherid
				 * number of questions*/
				
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				
				
				String subjectIDquery = "select subjectid from subject where name='"+subject_name+"'";
				ResultSet rs1 = st.executeQuery(subjectIDquery);
				// using the name of the selected subject, the subjectid is extracted from the subject table
				String subjectid = "";
				if(rs1.next()){
					subjectid = rs1.getString("subjectid");
				}
				
				
				String classIDquery = "select class_id from class where name='"+class_name+"';";
				ResultSet rs2 = st.executeQuery(classIDquery);
				// using the name of the selected class, the classid is extracted from the class table
				String classid = "";
				if(rs2.next()){
					classid = rs2.getString("class_id");
				}
				int noq = Data.quiz.questions.size();//number of questions
				String query = "insert into Quiz (idQuiz, class_id, subjectid, ownerid, numberOfQuestions) values ('"+idQuiz+"', '"+classid+"', '"+subjectid+"', '"+LoginController.account.idAccount+"', '"+noq+"')";
				st.executeUpdate(query);
				// the Quiz table in the database is updated with all the relevant data
				
				// the user is informed that the quiz was created successfully
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
	    			try{
	    				Mailgun.SendSimple(r.getString("email"), subject, text);
	    			} catch(Exception e){
	    				
	    			}
	    		}
	    		
	    		st.close();
	    		con.close();
	    		//TODO:- Close all statements like this wherever you've used sql
	    		
			} catch(Exception e){
				e.printStackTrace();
			}
	
			//write to quiz file to store data like the question, answer choices, etc.
			writeToQuizFile(idQuiz);
			
			try
			{
				// once the data has been stored in the quiz file, change the scene back to the TeacherMenu.fxml file
				Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
		    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
		    	Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
		    	
				stage.show();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public String produceUID(){//uses recursion to produce a unique quiz id
		// TODO:- use recursion everywhere for producing unique IDs
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		String uid = "";
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			SecureRandom random = new SecureRandom();
			int id = random.nextInt(100000);
			// generates a random id 5 digit integer
			String idFormatted = String.format("%05d", id);
			// converts the integer to a String
			String query = "select * from Quiz where idQuiz='"+idFormatted+"'";
			// checks if the randomly generated id is already present in the Quiz table of the database
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				//the id is not unique
				// starts over by calling the <produceUID()> method again
				produceUID();
			}
			else{
				uid = idFormatted; // the string that gets returned
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return uid;
		//returns the unique uid string to the place where it was called from
		
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
		    	System.out.println(Data.quiz.questions.get(i).question);
		    	// goes through each of the questions in that have been added to this quiz
		    	printWriter.println("QuizID = "+quizID);
		    	// the unique quizID is associated with the question so that when it needs to be displayed along with the other questions in the quiz, it can be easily extracted
		    	printWriter.println("Marks = "+Data.quiz.questions.get(i).marks);
		    	// the marks that are awarded to the user for answering the question correctly
		    	printWriter.println("Negative Marks = "+Data.quiz.questions.get(i).nmarks);
		    	// the marks that are deducted from the user's score for an incorrect answer
		    	printWriter.println("Question = "+Data.quiz.questions.get(i).question);
		    	// the question as a string
		    	printWriter.println("Question Type = "+Data.quiz.questions.get(i).questionType);
		    	// the type of question that this is. This helps in determining the controller class that it should be opened with since each different kind of question needs to display data differently
		    	printWriter.println("Correct Answer: "+Data.quiz.questions.get(i).correctAnswer);
		    	// the correct answer that the algorithm looks for
		    	for(int x = 0;x<Data.quiz.questions.get(i).Answers.size();x++){
		    		// each of the answer choices is added to the file
		    		printWriter.println("Answer "+(x+1)+": "+Data.quiz.questions.get(i).Answers.get(x));
		    	}
		    	// indicative of the end of the current question
		    	printWriter.println("*** end ***");
		    	printWriter.close();
		    }
		} catch(Exception e){
			e.printStackTrace();
		}
		Data.quiz = new Quiz();//resetting data.quiz for new quizzes
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// this method is called by default every time the CreateQuiz.fxml file is loaded
		// it sets the choices available to the user to choose from in the <subjectCB> and <classCB> ChoiceBoxes
		
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		
		try{
		
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			
			
			String query = "select name from subject";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			// selects every item in the name column of the subject table in the database
			
			List<String> subjectNames = new ArrayList<String>();
			//declares and initializes an empty List
			
			while(rs.next()){
			
				subjectNames.add(rs.getString("name"));
				// adds each of the subject names to the <subjectNames> List
			
			}
			
			subjectCB.setItems(FXCollections.observableArrayList(subjectNames));
			// the user may now choose any item from the <subjectNames> List through the <subjectNames> ChoiceBox
			
			
			String query1 = "select name from class";
			ResultSet rs1 = st.executeQuery(query1);
			// selects every item in the name column of the class table in the database
			
			List<String> classes = new ArrayList<String>();
			// declares and initializes an empty List
			
			while(rs1.next()){
				
				classes.add(rs1.getString("name"));
				// adds each of the class names to the <classes> List
			
			}
			
			classCB.setItems(FXCollections.observableArrayList(classes));
			// the user may now choose any item from the <classes> List through the <classCB> ChoiceBox
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		/**When the CreateQuiz.fxml file is loaded after returning from a 'sub-view' (i.e. something like CreateQuiz_addQuestion):
		 * There may already have been some questions that the user had added to the quiz
		 * Instead of being added to an external source, they were saved locally in a static object called Data.quiz
		 * The questions are loaded on to the <questionsView> ListView for the user to see from this source*/
		
		List<String> qs = new ArrayList<String>();
		for(int i = 0;i<Data.quiz.questions.size();i++){
			
			qs.add(Data.quiz.questions.get(i).question);
			
		}
		questionsView.setItems(FXCollections.observableArrayList(qs));
		// the user should now be able to see any questions that he/she had added before saving the entire quiz to an external source
		
	}
}
