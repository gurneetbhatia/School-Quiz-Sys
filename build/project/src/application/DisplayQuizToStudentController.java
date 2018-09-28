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
	@FXML
	ChoiceBox<String> quizidCB;
	@FXML
	Button enterBtn;
	@FXML
	Button backBtn;
	
	public static QuizInProgress qip = new QuizInProgress();
	public static String quizID = "";
	
	//public static Queue<Question> questions = new LinkedList<Question>();//NOTE:- Using an abstract data type here
	//I used this due to the first in first out policy of queues and would be easier to use than a list since i don't have to initialize an additional counter
	
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
		
		try{
			BufferedReader in = new BufferedReader(new FileReader("quizzes.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
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
		
		for(int i = 0;i<lines.size();){//TODO:- Check if removing the 'i++' caused any kind of problem with loading the questions
			Question question = new Question();
			
			i += 1;//starts with i = 1 essentially since we don't need the quizids (abstraction of data due to relevance)
			question.marks = Double.parseDouble(lines.get(i).substring(8));
			
			i += 1;
			question.nmarks = Double.parseDouble(lines.get(i).substring(17));
			
			i += 1;
			question.question = lines.get(i).substring(11);
			
			i+= 1;
			question.questionType = lines.get(i).substring(16);
			
			i += 1;
			question.correctAnswer = lines.get(i).substring(16);
			
			while(true){
				i += 1;
				if(lines.get(i).equals("*** end ***")){
					break;
				}
				else{
					question.Answers.add(lines.get(i).substring(10));
				}
			}
			
			qip.questions.add(question);
		}
		
		quizID = quizidCB.getValue();
		//the queue containing questions has been initialized
		
		gotoNextQuestion();
		
	}
	
	public void gotoNextQuestion(){
		
		qip.currentQuestion = qip.questions.remove();//sets the element at position 0 in the queue to the object <question>
		
		/**There are four fxml files that i need to create for the four different kinds of questions
		 * mcq
		 * laq
		 * saq
		 * rvq*/
		
		if(qip.questions.size()>0){
			
			if(qip.currentQuestion.questionType.equals("mcq")){
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
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("The quiz is over! Congratulations!");

			alert.showAndWait();
			
			try
			{
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
	
	public void backBtnPressed(){
		try
		{
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
		
		Account a = LoginController.account;
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
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
			
			String query = "select idQuiz from Quiz where ownerid='"+a.idAccount+"'";
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()){
				ids.put(rs.getString("idQuiz"), false);//by default, we assume that the student hasn't done it yet
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		List<String> lines = new ArrayList<String>();
		
		try{
			
			/*URL path = DisplayQuizToStudentController.class.getResource("checkQuizDone.txt");
			File f = new File(path.getFile());
			BufferedReader in = new BufferedReader(new FileReader(f));*/
			
			BufferedReader in = new BufferedReader(new FileReader("/Users/inderdeepbhatia/Documents/workspace/School Quiz System/bin/application/checkQuizDone.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
			}
			
			in.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//boolean check = false;
		
		for(String str: lines){
			
			String qid = "";//quizid
			String sid = "";//studentid
			
			char[] chArr = str.toCharArray();
			
			int i = -1;
			
			for(char ch: chArr){
				i += 1;
				if(ch != ':'){
					qid += ch;
				}
				else{
					break;
				}
			}
			
			for(int x = i;x<chArr.length;x++){
				sid += chArr[x];
			}
			
			for(int x = 0;x<ids.size();x++){
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
