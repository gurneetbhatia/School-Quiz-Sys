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
	/**The corresponding scene of this Controller class serves the purpose of displaying a Long Answer type question*/
	@FXML
	Label questionLbl;
	// the question String
	@FXML
	TextArea answerTA;
	// the input TextArea for the answer
	@FXML
	Button nextBtn;
	// the <nextBtnPressed()> method is called when this button is pressed
	
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
		// fetches the input from the <answerTA> TextArea as a String and assigns it to <answer>
		String studentid = LoginController.account.idAccount;
		// a local reference is made to the idAccount of the currently logged in account
		String question = DisplayQuizToStudentController.qip.currentQuestion.question;
		// the current question is fetched as a string
		String ownerid = "";
		// the owner id needs to be found using a SQL query
		
		String quizid = DisplayQuizToStudentController.quizID;
		// quizID of the quiz in progress
		
		String url = Data.server.url;
		String user = Data.server.username;
		String password = Data.server.password;
		
		try{
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String query = "select ownerid from Quiz where idQuiz='"+quizid+"'";
			// finds the accountID of the owner of the current quiz
			ResultSet rs = st.executeQuery(query);
			
			if(rs.next()){
				ownerid = rs.getString("ownerid");
				// if found, it is assigned to the <ownerid> String variable
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			// data is stored in the reviewLAQ.txt file stored so that it can be made visible to the teacher who created this quiz
			//format: studentid:question:ownerid:answer:quizID:corrected by the teacher? (boolean)
			//where done is a boolean value that tells the program if the question has been checked
			
			FileWriter fileWriter = new FileWriter("reviewLAQ.txt", true);//the true parameter tells the FileWriter that we want to append the file if it's already there
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(studentid+":"+question+":"+ownerid+":"+answer+":"+quizid+":false");
			
			printWriter.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		// goes to the next question
		gotoNextQuestion();
	}
	
	public void gotoNextQuestion(){
		// this method checks the question type of the Question object at the front of the <qip.questions> Queue
				// It then sets it to the <currentQuestion> attribute in the static object of the QuizInProgress class
				DisplayQuizToStudentController.qip.currentQuestion = DisplayQuizToStudentController.qip.questions.remove();
				//sets the element at position 0 in the queue to the object <question>
				
				/**There are four fxml files that i need to create for the four different kinds of questions
				 * mcq
				 * laq
				 * saq
				 * rvq*/
				
				if(DisplayQuizToStudentController.qip.questions.size()>0){// checks if there is a question remaining in the queue to display
					
					if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("mcq")){
						// the question type of the <currentQuestion> is multiple choice question
						// the relevant scene is loaded and displayed to the user
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
						// the question type of the <currentQuestion> is short answer question
						// the relevant scene is loaded and displayed to the user
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
						// the question type of the <currentQuestion> is long answer question
						// the relevant scene is loaded and displayed to the user
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
					else if(DisplayQuizToStudentController.qip.currentQuestion.questionType.equals("rvq")){
						// the question type of the <currentQuestion> is random variable question
						// the relevant scene is loaded and displayed to the user
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// the question is displayed through the <questionLbl> Label
		questionLbl.setText(DisplayQuizToStudentController.qip.currentQuestion.question);
		
	}
	
}
