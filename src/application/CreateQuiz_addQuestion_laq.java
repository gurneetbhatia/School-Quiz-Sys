package application;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateQuiz_addQuestion_laq {
	/**This question requires to be corrected manually by the teacher
	 * It also requires the teacher to enter the marks when they correct it
	 * In the correctAnswer attribute of this question, the accountid of the teacher who created the question is stored
	 * This eases the sorting of questions when it comes to distributing the laq questions among the teacher accounts*/
	
	@FXML
	TextArea questionTA;
	// the question string
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when this button is pressed
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this button is pressed
	
	public void doneBtnPressed(){
		// need to save the question and the idAccount of the user who created this question
		CreateQuiz_addQuestionController.question.question = questionTA.getText();
		CreateQuiz_addQuestionController.question.correctAnswer = LoginController.account.idAccount;
		
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
		// the user doesn't wish to proceed with creating a Long Answer question
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
	
}
