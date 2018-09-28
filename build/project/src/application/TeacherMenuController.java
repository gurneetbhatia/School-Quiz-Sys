package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TeacherMenuController {
	@FXML
	Button createNewQuizBtn;
	@FXML
	Button viewQuizzesBtn;
	@FXML
	Button performanceStatisticsBtn;
	@FXML
	Button messagesBtn;
	@FXML
	Button notificationsBtn;
	@FXML
	Button leaderboardBtn;
	@FXML
	Button settingsBtn;
	@FXML
	Button logoutBtn;
	
	
	public void createNewQuizBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("CreateQuiz.fxml"));
	    	Stage stage = (Stage) createNewQuizBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void viewQuizzesBtnPressed(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu_ViewQuiz.fxml"));
	    	Stage stage = (Stage) createNewQuizBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void performanceStatisticsBtnPressed(){
		
	}
	
	public void messagesBtnPressed(){
		
	}
	
	public void notificationsBtnPressed(){
		
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
	
	public void settingsBtnPressed(){
		
	}
	
	public void logoutBtnPressed(){
		LoginController.account = new Account();
		try{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	    	Stage stage = (Stage) logoutBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}
}
