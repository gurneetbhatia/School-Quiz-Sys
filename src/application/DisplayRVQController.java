package application;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.*;

import com.mysql.jdbc.Connection;

public class DisplayRVQController implements Initializable{
	/**The corresponding scene of this Controller class serves the purpose of displaying a Random Variable Question*/
	
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
		if(answer.equals(DisplayQuizToStudentController.qip.currentQuestion.correctAnswer)){
			// the answer is correct
			DisplayQuizToStudentController.qip.score += DisplayQuizToStudentController.qip.currentQuestion.marks;
		
		}
		else{
			// the answer is incorrect s
			DisplayQuizToStudentController.qip.score -= DisplayQuizToStudentController.qip.currentQuestion.nmarks;
		}
		// moves on to the next question
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
		// this method is first called right before the scene is loaded
		
		
		//look for $, %, ^, &, !, @ or # in answers
		
		String ques = DisplayQuizToStudentController.qip.currentQuestion.question;
		String ans = DisplayQuizToStudentController.qip.currentQuestion.Answers.get(0);//since all the answers were stored as a single string at position 0
		
		/* The elements in the following List are indicative of a variable in the answer
		 * If one is found, a random number is generated between the range provided by the user who created the question*/
		List<Character> arr = new ArrayList<Character>();
		arr.add('$');
		arr.add('%');
		arr.add('^');
		arr.add('&');
		arr.add('!');
		arr.add('@');
		arr.add('#');
		
		List<Character> op = new ArrayList<Character>();//operations List
		op.add('+');
		op.add('-');
		op.add('*');
		op.add('/');
		
		Map<Character, Double> rv = new HashMap<Character, Double>();//keeps track of the variable names and the values assigned to them
		double a = 0.0;//the answer generated after all the random operations
		String q = "";//the question string constructed from the ans string
		for(int i = 0;i<ques.length();i++){
			
			if(arr.contains(ques.charAt(i))){
				char ch = ques.charAt(i);
				//variable declaration found
				//now look at the integer range
				i += 1;//start looking for a comma (which is the indicator of the end of the lower limit)
				//starts at i+1 to avoid the '<'
				String strLow = "";
				while(ques.charAt(i) != ','){
					
					strLow += ques.charAt(i);
					i += 1;
					
				}
				
				//once the comma has been found, skip one more place for the space after the comma and start looking for the upper-limit
				i += 1;
				String strUp = "";//start looking for the '>' since that is the indicator of the end of the upper-limit
				while(ques.charAt(i) != '>'){
					
					strUp += ques.charAt(i);
					i += 1;
					
				}
				
				double lower = 0.0;
				double upper = 0.0;
				
				try{
					
					lower = Double.parseDouble(strLow);
					upper = Double.parseDouble(strUp);
					
				} catch(Exception e){
					// there is a problem with the current question due to the datatypes provided by the user for the upper and lower limits of the variable range
					// the program must go on despite this error
					// thus, the program is directed to the next question
					gotoNextQuestion();
				}
				
				//now add a random number in the given range
				double r = getRandomNumberInRange(lower, upper);
				
				q += ""+r;
				rv.put(ch, r);
				
			}
			else{
				//not a variable
				q += ques.charAt(i);
			}
			
		}
		
		//find the answer using the answer string
		for(int i = 0;i<ans.length();i++){
			if(op.contains(ans.charAt(i))){
				//the character is an operation
				Character ch = ans.charAt(i);
				i += 1;//i+=1 is the variable name
				if(ch == '+'){
					a += rv.get(ans.charAt(i));
				}
				else if(ch == '-'){
					a -= rv.get(ans.charAt(i));
				}
				else if(ch == '*'){
					a = a*rv.get(ans.charAt(i));
				}
				else if(ch == '/'){
					a = a/rv.get(ans.charAt(i));
				}
				
			}
			else if((rv.containsKey(ans.charAt(i))) && (rv.containsKey(ans.charAt(i-1)))){
				//it's a variable. if it's just a variable, and the character before it was a variable too, it's multiplied, else, it's added
				a = a*rv.get(ans.charAt(i));
			}
			else{
				// it's added as described above
				a += rv.get(ans.charAt(i));
			}
		}
		
		DisplayQuizToStudentController.qip.currentQuestion.correctAnswer = ""+a;
		questionLbl.setText(ques);
		
	}
	
	public double getRandomNumberInRange(double min, double max) {
		// this method is used to generate a random number between the range that it receives as two separate arguments
		
		if (min > max) {
			// just in case there was a logical problem where <min> was greater than <max>, the two values held are exchanged among themselves
			double f = min;
			min = max;
			max = f;
		}

		Random r = new Random();
		double randomValue = min + (max - min) * r.nextDouble();
		/*The 'min + ' part makes sure that the value generated is AT LEAST equal to <min>
		 * r.nextDouble() generates a Double (decimal number) between 0 and 1
		 * This number is then multiplied by the difference between <min> and <max>
		 * This makes sure that the maximum value of <randomValue> is <max> (when r.nextDouble() = 1) and the minimum value is <min (when r.nextDouble() = 0)*/
		
		return randomValue;
		// the random value is returned as a double
	}
}
