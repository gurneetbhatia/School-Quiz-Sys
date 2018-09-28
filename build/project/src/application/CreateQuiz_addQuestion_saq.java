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
	@FXML
	TextArea answersTA;
	@FXML
	TextField marksTF;
	@FXML
	TextField nmarksTF;
	@FXML
	Button cancelBtn;
	@FXML
	Button doneBtn;
	
	public void doneBtnPressed(){
		
		String question = questionTA.getText();
		String answers = answersTA.getText();
		double marks = 0;
		double nmarks = 0;
		
		String word = "";
		List<String> answersList = new ArrayList<String>();
		
		for(int i = 0;i<answers.length();i++){
			if(i == answers.length()-1){
				word += answers.charAt(i);
				answersList.add(word);
			}
			else if(answers.charAt(i) == ','){
				i += 1;
				answersList.add(word);
				word = "";
			}
			else{
				word += answers.charAt(i);
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
		
		CreateQuiz_addQuestionController.question.question = question;
		CreateQuiz_addQuestionController.question.Answers = answersList;
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
