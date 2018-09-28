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
	@FXML
	TextArea answerTA;
	@FXML
	TextField marksTF;
	@FXML
	TextField nmarksTF;
	@FXML
	Button cancelBtn;
	@FXML
	Button doneBtn;
	
	public void doneBtnPressed(){
		List<String> answers = new ArrayList<String>();
		answers.add(answerTA.getText());
		
		double marks = 0;
		double nmarks = 0;
		
		try{
			marks = Integer.parseInt(marksTF.getText());
			nmarks = Integer.parseInt(nmarksTF.getText());
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide numeric values only for the marks!");

			alert.showAndWait();
		}
		
		CreateQuiz_addQuestionController.question.question = questionTA.getText();
		CreateQuiz_addQuestionController.question.Answers = answers;
		CreateQuiz_addQuestionController.question.correctAnswer = "~~~ all ~~~";
		CreateQuiz_addQuestionController.question.marks = marks;
		CreateQuiz_addQuestionController.question.nmarks = nmarks;
		
		Data.quiz.questions.add(CreateQuiz_addQuestionController.question);
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
	
	public void cancelBtnPressed(){
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
