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
	// the <multipleChoiceBtnPressed()> method is called when this button is pressed
	@FXML
	Button shortAnswerBtn;
	// the <shortAnswerBtnPressed()> method is called when this button is pressed
	@FXML
	Button longAnswerBtn;
	// the <longAnswerBtnPressed()> method is called when this button is pressed
	@FXML
	Button randomVariableBtn;
	// the <randomVariableBtnPressed()> method is called when this button is pressed
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	
	static Question question = new Question();
	// a static object of the <Question> class is declared since the data is not yet stored on an external source (the .txt file or the SQL database)
	//it is static so that other classes can access the SAME data too 
	
	public void multipleChoiceBtnPressed(){
		question.questionType = "mcq";
		// the questionType of the local question object is set to 'mcq' which is short for "multiple choice question"
		try
		{
			// the scene is changed to the CreateQuiz_addQuestion_mcq.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_mcq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets() .add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void shortAnswerBtnPressed(){
		question.questionType = "saq";
		// the questionType of the local question object is set to 'saq' which is short for "short answer question"
		try
		{
			// the scene is changed to the CreateQuiz_addQuestion_saq.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_saq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void longAnswerBtnPressed(){
		question.questionType = "laq";
		// the questionType of the local question object is set to 'laq' which is short for "long answer questions"
		try
		{
			// the scene is changed to the CreateQuiz_addQuestion_laq.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_laq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void randomVariableBtnPressed(){
		question.questionType = "rvq";
		// the questionType of the local question object is set to 'rvq' which is short for "random variable question"
		try
		{
			// the scene is changed to CreateQuiz_addQuestion_rvq.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz_addQuestion_rvq.fxml"));
	    	Stage stage = (Stage) multipleChoiceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cancelBtnPressed(){
		// the user doesn't wish to add a question anymore
		// they are returned to the CreateQuiz.fxml scene
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz.fxml"));
	    	Stage stage = (Stage) cancelBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
