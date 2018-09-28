package application;

import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreateQuiz_addQuestion_saq {
	//One word answers only
	@FXML
	TextArea questionTA;
	// the question string 
	@FXML
	TextArea answersTA;
	// the keywords that should be a part of the correct answer
	@FXML
	TextField marksTF;
	// the marks that are awarded to the student's score if their answer is correct
	@FXML
	TextField nmarksTF;
	// the marks that are deducted from the student's score if their answer is incorrect
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when the user presses this button
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when the user presses this button
	
	public void doneBtnPressed(){
		
		String question = questionTA.getText();
		// the question is is retrieved as a string from the <questionTA> TextArea
		String answers = answersTA.getText();
		// the keywords are retrieved as  a single string from the <answerTA> TextArea
		double marks = 0;
		// the marks that are to be awarded for the correct answer are initialized to zero since the algorithm will later check if the input was of the data type Double
		double nmarks = 0;
		// the marks that are to be deducted for the incorrect answer are initialized to zero since the algorithm will later check if the input was of the data type Double
		
		String word = "";
		// the keyword is initialized as an empty string
		List<String> answersList = new ArrayList<String>();
		//the empty list of keywords
		
		for(int i = 0;i<answers.length();i++){
			// this for-loop runs through each character in the <answers> String
			if(i == answers.length()-1){
				// if the current character is the last word in the String, it must be the end of the current word
				// Also, it must be the last word
				word += answers.charAt(i); 
				// the current character is added to the word string
				answersList.add(word);
				// the word is added to the list of keywords
			}
			else if(answers.charAt(i) == ','){
				// if the current character is a comma, it must indicate the end of the current word
				i += 1;
				// the next character is skipped with the 'i += 1' since the next character is supposed to be a whitespace
				answersList.add(word);
				// the current word is added to the list without adding the current character since the current character is a comma which is not a part of the word
				word = "";
				// the word string is reset
			}
			else{
				// the current word has not yet ended
				word += answers.charAt(i); // the current character is just added to the word
			} 
		}
		
		try{
			marks = Double.parseDouble(marksTF.getText());
			nmarks = Double.parseDouble(nmarksTF.getText());
		} catch(Exception e){
			//the input for the marks values weren't integers
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide numeric values only for the marks!");

			alert.showAndWait();
		}
		
		
		// the data provided by the user is stored locally in the program for now in the static object of class <Question> in the <CreateQuiz_addQuestionController> class
		CreateQuiz_addQuestionController.question.question = question;
		CreateQuiz_addQuestionController.question.Answers = answersList;
		CreateQuiz_addQuestionController.question.correctAnswer = "~~~ all ~~~";
		CreateQuiz_addQuestionController.question.marks = marks;
		CreateQuiz_addQuestionController.question.nmarks = nmarks;
		
		
		Data.quiz.questions.add(CreateQuiz_addQuestionController.question);
		CreateQuiz_addQuestionController.question = new Question();//resetting the object
		try
		{
			// the scene is changed back to the CreateQuiz.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz.fxml"));
	    	Stage stage = (Stage) doneBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cancelBtnPressed(){
		// the user doesn't wish to add a Short Answer Question to the quiz
		// the scene is changed back to the CreateQuiz.fxml file
		CreateQuiz_addQuestionController.question = new Question();//resetting the object
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz.fxml"));
	    	Stage stage = (Stage) doneBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
