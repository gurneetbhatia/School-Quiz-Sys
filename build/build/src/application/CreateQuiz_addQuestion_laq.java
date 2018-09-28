package application;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateQuiz_addQuestion_laq {
	@FXML
	TextArea questionTA;
	@FXML
	Button doneBtn;
	@FXML
	Button cancelBtn;
	
	public void doneBtnPressed(){
		CreateQuiz_addQuestionController.question.question = questionTA.getText();
		CreateQuiz_addQuestionController.question.correctAnswer = LoginController.account.idAccount;
		
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
			e.printStackTrace();
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
