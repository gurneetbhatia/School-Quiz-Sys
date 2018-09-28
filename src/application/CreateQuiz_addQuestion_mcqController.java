package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	/**In this type of question, the teacher is able to provide 4 answer choices to a question
	 * The student must choose the correct one
	 * The correct choice is given to the program by the teacher
	 * When the student answers the question, the program checks if it is correct and awards/deducts marks appropriately*/
	@FXML
	TextArea questionTF;
	// the question string
	@FXML
	TextField choice1TF;
	@FXML
	TextField choice2TF;
	@FXML
	TextField choice3TF;
	@FXML
	TextField choice4TF;
	// The four answer choices available to the student
	@FXML
	ChoiceBox<String> correctAnswerCB;
	// The correct answer choice provided by the teacher
	@FXML
	Button doneBtn;
	// the <doneBtnPressed()> method is called when this Button is pressed
	@FXML
	Button cancelBtn;
	// the <cancelBtnPressed()> method is called when this Button is pressed
	@FXML
	TextField marksTF;
	@FXML
	TextField nmarksTF;
	
	public void doneBtnPressed(){
		// saves the question on the static object of the <Question> class
		
		String question = questionTF.getText();
		// the question is retrieved as a string from the <questionTF> TextArea
		String choice1 = choice1TF.getText();
		String choice2 = choice2TF.getText();
		String choice3 = choice3TF.getText();
		String choice4 = choice4TF.getText();
		// the four choices available to the user are stored as strings
		String correctAnswer = correctAnswerCB.getValue();
		// the correct answer among the 4 choices stored as  a string
		double marks = 0;
		double nmarks = 0;
		// <marks> and <nmarks> are initialized variables
		// The following lines are placed in a try-catch block in order to avoid errors due to data types
		try{
			marks = Double.parseDouble(marksTF.getText());
			nmarks = Double.parseDouble(nmarksTF.getText());
			// if the line preceding this comment was executed successfully, then the provided input were decimals
		} catch(Exception e){
			// the input for the marks values weren't integers
			// an appropriate error is displayed to the user
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Input!");
			alert.setContentText("Please provide numeric values only for the marks!");

			alert.showAndWait();
		}
		
		// the data collected from the user is stored on the static <Question> object in the <CreateQuiz_addQuestionController> class
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
			// the data was saved temporarily successfully
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
		// the user doesn't wish to add a multiple choice question anymore
		// They are returned to the CreateQuiz.fxml scene
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
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initializes the choices for the correct answer that are available to the user before the scene is loaded
		try{
			List<String> choices = new ArrayList<String>();
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
