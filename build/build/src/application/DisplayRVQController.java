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
	
	@FXML
	Label questionLbl;
	@FXML
	TextArea answerTA;
	@FXML
	Button nextBtn;
	
	
	
	public void nextBtnPressed(){
		String answer = answerTA.getText();
		if(answer.equals(DisplayQuizToStudentController.qip.currentQuestion.correctAnswer)){
			
			DisplayQuizToStudentController.qip.score += DisplayQuizToStudentController.qip.currentQuestion.marks;
		
		}
		else{
			DisplayQuizToStudentController.qip.score -= DisplayQuizToStudentController.qip.currentQuestion.nmarks;
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
		
		//questionLbl.setText(DisplayQuizToStudentController.qip.currentQuestion.question);
		
		//look for $, %, ^, &, !, @ or # in answers
		
		String ques = DisplayQuizToStudentController.qip.currentQuestion.question;
		String ans = DisplayQuizToStudentController.qip.currentQuestion.Answers.get(0);//since all the answers were stored as a single string at position 0
		
		List<Character> arr = new ArrayList<Character>();
		arr.add('$');
		arr.add('%');
		arr.add('^');
		arr.add('&');
		arr.add('!');
		arr.add('@');
		arr.add('#');
		
		List<Character> op = new ArrayList<Character>();//operations
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
					//the two values weren't in a double format
					//TODO- handle this is quiz creation part for random variable questions
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
				a += rv.get(ans.charAt(i));
			}
		}
		
		DisplayQuizToStudentController.qip.currentQuestion.correctAnswer = ""+a;
		questionLbl.setText(ques);
		
	}
	
	public double getRandomNumberInRange(double min, double max) {

		if (min >= max) {
			double f = min;
			min = max;
			max = f;
		}

		Random r = new Random();
		double randomValue = min + (max - min) * r.nextDouble();
		
		return randomValue;
	}
}
