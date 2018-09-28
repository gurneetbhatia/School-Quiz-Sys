package application;

import java.io.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.Stage;

public class TeacherMenu_ViewQuizController implements Initializable{
	@FXML
	ListView<String> questionLV;
	@FXML
	ChoiceBox<String> subjectCB;
	@FXML
	ChoiceBox<String> classCB;
	@FXML
	Button searchBtn;
	@FXML
	Button backBtn;
	
	public void searchBtnPressed(){
		//questionsView.setItems(FXCollections.observableArrayList(qs));
		/**Have to read this data from quizzes.txt
		 * Let the user select a class and subject
		 * run a sql query to find all the quizid's that match the parameters
		 * Produce a popup that lets the user select one of these quizids
		 * If there is no available quizid, display an appropriate error message*/
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		String subjectname = subjectCB.getValue();
		String classname = classCB.getValue();
		
		if((classname.equals("")) || (subjectname.equals(""))){
			try{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Value for subject/class not found!");
				alert.setContentText("Please select valid options for subject and class");

				alert.showAndWait();
			} catch(Exception e){
				
			}
		}
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			
			String classid = "";
			String q1 = "select class_id from class where name='"+classname+"'";
			ResultSet rs1 = st.executeQuery(q1);
			if(rs1.next()){
				classid = rs1.getString("class_id");
			}
			
			String subjectid = "";
			String q2 = "select subjectid from subject where name='"+subjectname+"'";
			ResultSet rs2 = st.executeQuery(q2);
			if(rs2.next()){
				subjectid = rs2.getString("subjectid");
			}
			
			List<String> ids = new ArrayList<String>();
			String query = "select idQuiz from Quiz where class_id='"+classid+"' and subjectid='"+subjectid+"'";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				ids.add(rs.getString("idQuiz"));
			}
			
			String choiceid = getChoiceID(ids);
			//using this choiceid, get all the questions from the text file.
			get_and_display_questions(choiceid);
			
		} catch(Exception e){
			
		}
	}
	
	public void get_and_display_questions(String quizid){
		//this method reads data from the quizzes.txt and posts questions to the listview that belong to the same quizid
		/**FORMAT:
		 * QuizID
		 * marks
		 * nmarks
		 * Question
		 * QuestionType
		 * Correct Answer
		 * All Answers
		 * "*** end ***" */
		
		List<String> lines = new ArrayList<String>();
		
		try{
			BufferedReader in = new BufferedReader(new FileReader("quizzes.txt"));
			String str;
	
			while((str = in.readLine()) != null){
			    lines.add(str);
			}
			
			in.close();
			
		} catch(Exception e){
			
		}
		Map<String, String> dictionary = new HashMap<String, String>();
		
		
		for(int i = 0;i<lines.size();i++){
			String qid_check = lines.get(i).substring(0, 6);
			if(qid_check.equals("QuizID")){
				//the identifier is a quiz
				String qid = lines.get(i).substring(9);
				//now find the question
				String ques = "";
				for(int x = i;x<lines.size();x++){
					String ques_check = lines.get(x).substring(0, 8);
					if(ques_check.equals("Question")){
						i = x;
						ques = lines.get(i).substring(11);
						break;
					}
				}
				dictionary.put(qid, ques);
			}
			
		}
		
		
		/*Scanner sc = new Scanner(System.in);
		for(int i = 0;i<lines.size();i++){
			if(i % 8 == 0){
				
				String qid = lines.get(i).substring(9);
				System.out.println("i1 = "+i);
				i += 3;
				String ques = lines.get(i).substring(11);
				System.out.println("i2 = "+i);
				System.out.println("ID: "+qid);
				System.out.println("Question: "+ques);
				sc.next();
				dictionary.put(qid, ques);
			}
		}*/
		
		ArrayList<String> validQuestions = new ArrayList<String>();
		for(String key: dictionary.keySet()){
			if(key.equals(quizid)){
				validQuestions.add(dictionary.get(key));
			}
		}
		//all the valid questions have been fetched
		//now display them on the listview
		try{
			questionLV.setItems(FXCollections.observableArrayList(validQuestions));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getChoiceID(List<String> ids){
		String choiceid = "";
		ChoiceDialog<String> dialog = new ChoiceDialog<>("", ids);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Choose a Quiz ID");
		dialog.setContentText("Choice:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    choiceid = result.get();
		}
		else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("No value found for choice");
			alert.setContentText("Please choose a value for quizid");
			alert.showAndWait();
			
			getChoiceID(ids);
		}
		
		return choiceid;
		
	}
	
	public void backBtnPressed(){
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
	    	Stage stage = (Stage) backBtn.getScene().getWindow();
	    	Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
	    	
			stage.show();
		} catch(Exception e){
			
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		try{
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			String query = "select * from subject";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<String> subjectNames = new ArrayList<String>();
			while(rs.next()){
				subjectNames.add(rs.getString(2));
			}
			
			subjectCB.setItems(FXCollections.observableArrayList(subjectNames));
			
			String query1 = "select * from class";
			ResultSet rs1 = st.executeQuery(query1);
			ArrayList<String> classes = new ArrayList<String>();
			while(rs1.next()){
				classes.add(rs1.getString(2));
			}
			
			classCB.setItems(FXCollections.observableArrayList(classes));
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
