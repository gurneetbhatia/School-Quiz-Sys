package application;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreateQuiz_addQuestion_mcqController implements Initializable{
	@FXML
	TextArea questionTF;
	@FXML
	TextField choice1TF;
	@FXML
	TextField choice2TF;
	@FXML
	TextField choice3TF;
	@FXML
	TextField choice4TF;
	@FXML
	ChoiceBox<String> correctAnswerCB;
	@FXML
	Button doneBtn;
	@FXML
	Button cancelBtn;
	@FXML
	TextField marksTF;
	@FXML
	TextField nmarksTF;
	
	public void doneBtnPressed(){
		
		String question = questionTF.getText();
		String choice1 = choice1TF.getText();
		String choice2 = choice2TF.getText();
		String choice3 = choice3TF.getText();
		String choice4 = choice4TF.getText();
		String correctAnswer = correctAnswerCB.getValue();
		double marks = 0;
		double nmarks = 0;
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

		CreateQuiz_addQuestionController.question.marks = marks;
		CreateQuiz_addQuestionController.question.nmarks = nmarks;
		CreateQuiz_addQuestionController.question.question = question;
		CreateQuiz_addQuestionController.question.Answers.add(choice1);
		CreateQuiz_addQuestionController.question.Answers.add(choice2);
		CreateQuiz_addQuestionController.question.Answers.add(choice3);
		CreateQuiz_addQuestionController.question.Answers.add(choice4);
		if(correctAnswer.equals("Choice 1")){
			CreateQuiz_addQuestionController.question.correctAnswer = choice1;
		}
		else if(correctAnswer.equals("Choice 2")){
			CreateQuiz_addQuestionController.question.correctAnswer = choice2;
		}
		else if(correctAnswer.equals("Choice 3")){
			CreateQuiz_addQuestionController.question.correctAnswer = choice3;
		}
		else{
			CreateQuiz_addQuestionController.question.correctAnswer = choice4;
		}
		
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
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try{
			ArrayList<String> choices = new ArrayList<String>();
			choices.add("Choice 1");
			choices.add("Choice 2");
			choices.add("Choice 3");
			choices.add("Choice 4");
			
			correctAnswerCB.setItems(FXCollections.observableArrayList(choices));
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
