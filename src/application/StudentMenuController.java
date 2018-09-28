package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StudentMenuController {
	/**The corresponding scene of this controller class is the Menu Scene of the Student account.*/
	@FXML
	Button myQuizzesBtn;
	// the myQuizzesBtnPressed() method is called when this button is pressed
	@FXML
	Button leaderboardBtn;
	// the leaderboardBtnPressed() method is called when this button is pressed
	@FXML
	Button myPerformanceBtn;
	// the myPerformanceBtnPressed() method is called when this button is pressed
	@FXML
	Button accountSettingsBtn;
	// the accountSettingsBtnPressed() method is called when this button is pressed
	@FXML
	Button logoutBtn;
	// the logoutBtnPressed() method is called when this button is pressed
	
	public void myQuizzesBtnPressed(){
		//displays all the pending quizzes
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("DisplayQuizToStudent.fxml"));
	    	Stage stage = (Stage) myQuizzesBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void leaderboardBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("Leaderboard.fxml"));
	    	Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void myPerformanceBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("StudentStats.fxml"));
	    	Stage stage = (Stage) myPerformanceBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void accountSettingsBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("MyAccount.fxml"));
	    	Stage stage = (Stage) accountSettingsBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logoutBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	    	Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
