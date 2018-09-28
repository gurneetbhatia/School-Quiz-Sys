package application;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DisplaySAQController implements Initializable{
	/**The corresponding scene of this Controller class serves the purpose of displaying a Short Answer type question*/
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
		String answer = answerTA.getText();
		
		// compare each word in the answers list to each word in the answer (since all the answers are correct in this case)
		// a drawback here is that we don't know the syntax (sentence structure/context) in which the word is being used
		// generally speaking, we should be expecting single word or short phrases for answers so this shouldn't be a problem
		
		List<String> words = new ArrayList<String>();
		String word = "";
		boolean isPresent = false;// a boolean variable that indicates whether the keyword from the answers list has been found in the answer
		
		for(int i = 0;i<answer.length();i++){
			// each word in the String separated individually in thus for loop by iterating over every character in the String
			if(answer.charAt(i) == ' '){
				words.add(word);
			}
			else if(i == answer.length()){
				word += answer.charAt(i);
				words.add(word);
			}
			else{
				word += answer.charAt(i);
			}
		}
		
		for(String w: words){
			//check if w is in the correct answer list
			for(String x: DisplayQuizToStudentController.qip.currentQuestion.Answers){
				if(w.equals(x)){
					isPresent = true;
				}
			}
		}
		
		if(isPresent){
			//the answer is present and thus, it's correct
			
			DisplayQuizToStudentController.qip.score += DisplayQuizToStudentController.qip.currentQuestion.marks;
		}
		else{
			//the answer is incorrect
			
			DisplayQuizToStudentController.qip.score -= DisplayQuizToStudentController.qip.currentQuestion.nmarks;
		}
		//  moves on to the next question 
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
		
		questionLbl.setText(DisplayQuizToStudentController.qip.currentQuestion.question);
		
	}
}
