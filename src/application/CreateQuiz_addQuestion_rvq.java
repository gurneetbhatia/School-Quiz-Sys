package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.*;

public class CreateQuiz_addQuestion_rvq {
	//to be used only for maths questions
	@FXML
	TextArea questionTA;
	// the question string
	@FXML
	TextArea answerTA;
	// the answer(s) are extracted from this TextArea using a separate algorithm
	@FXML
	TextField marksTF;
	// the marks awarded to the student for answering the question correctly
	@FXML
	TextField nmarksTF;
	// the marks deducted from the user's score for answering the question incorrectly
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when this button is pressed
	
	public void doneBtnPressed(){
		List<String> answers = new ArrayList<String>();
		// an empty list is initialized
		answers.add(answerTA.getText());
		// contains the entire input string from the <answerTA> TextArea
		// Had to be stored as the only element in the List because the data type for the relevant attribute in the Question class is a List
		
		double marks = 0;
		double nmarks = 0;
		
		try{
			marks = Double.parseDouble(marksTF.getText());
			nmarks = Double.parseDouble(nmarksTF.getText());
			// makes sure that the inputs conform to the data-type Double
		} catch(Exception e){
			// If they aren't doubles, an appropriate error is displayed to the user
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide numeric values only for the marks!");

			alert.showAndWait();
		}
		
		// the variables are stored
		CreateQuiz_addQuestionController.question.question = questionTA.getText();
		CreateQuiz_addQuestionController.question.Answers = answers;
		CreateQuiz_addQuestionController.question.correctAnswer = "~~~ all ~~~";
		CreateQuiz_addQuestionController.question.marks = marks;
		CreateQuiz_addQuestionController.question.nmarks = nmarks;
		
		// the question is added to the temporary object of the Quiz class
		Data.quiz.questions.add(CreateQuiz_addQuestionController.question);
		CreateQuiz_addQuestionController.question = new Question();//resetting the object
		try
		{
			// everything is done and the scene is changed back to the CreateQuiz.fxml class
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
		// the user doesn't wish to add a random variable question anymore
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
			
		}
	}
}
