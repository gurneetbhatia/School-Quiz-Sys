package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StudentMenuController {
	@FXML
	Button myQuizzesBtn;
	@FXML
	Button leaderboardBtn;
	@FXML
	Button myPerformanceBtn;
	@FXML
	Button accountSettingsBtn;
	@FXML
	Button logoutBtn;
	
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
		
	}
	
	public void accountSettingsBtnPressed(){
		
	}
	
	public void logoutBtnPressed(){
		
	}
	
}
