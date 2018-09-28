package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateQuiz_addQuestionController {
	//NOTE:- Random variable questions are only recommended for calculation-based questions
	@FXML
	Button multipleChoiceBtn;
	@FXML
	Button shortAnswerBtn;
	@FXML
	Button longAnswerBtn;
	@FXML
	Button randomVariableBtn;
	@FXML
	Button cancelBtn;
	
	static Question question = new Question();
	
	public void multipleChoiceBtnPressed(){
		question.questionType = "mcq";
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_mcq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets() .add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void shortAnswerBtnPressed(){
		question.questionType = "saq";
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_saq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}

	}
	
	public void longAnswerBtnPressed(){
		question.questionType = "laq";
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_laq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void randomVariableBtnPressed(){
		question.questionType = "rvq";
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_rvq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
	
	public void cancelBtnPressed(){
		
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
}
