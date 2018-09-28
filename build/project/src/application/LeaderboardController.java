package application;

import java.net.URL;

import java.sql.DriverManager;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardController implements Initializable {
	
	@FXML
	ListView<String> scoreLV;
	@FXML
	Button searchBtn;
	@FXML
	Button backBtn;
	@FXML
	Label studentNameLbl;
	@FXML
	Label scoreLbl;//displays the avearge score over all quizzes
	
	public void searchBtnPressed(){
		//only a teacher account has the permit to use this feature for privacy reasons
		if(LoginController.account.accountType.equals("Teacher")){
			
			String url = "jdbc:mysql://localhost/db";
			String user = "root";
			String password = "bUrp@107";
			
			List<String> studentNames = new ArrayList<String>();
			
			try{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				
				String query = "select name from Account where accountType='Student'";
				ResultSet rs = st.executeQuery(query);
				
				while(rs.next()){
					studentNames.add(rs.getString("name"));
				}
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			String selectedStudentName;
			
			ChoiceDialog<String> dialog = new ChoiceDialog<>(studentNames.get(0), studentNames);
			dialog.setTitle("Choice Dialog");
			dialog.setHeaderText("Choose a student name");
			dialog.setContentText("Choice:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				
			    selectedStudentName = result.get();
			    //now get the relevant studentID
			    try{
			    	Connection con = (Connection) DriverManager.getConnection(url, user, password);
					Statement st = con.createStatement();
					
				    String q = "select idAccount from Account where name='"+selectedStudentName+"'";
				    ResultSet rs = st.executeQuery(q);
				    List<String> studentIDs = new ArrayList<String>();
				    while(rs.next()){
				    	studentIDs.add(rs.getString("idAccount"));
				    }
				    if(studentIDs.size()>1){
				    	//there are more than one students with the same name
				    	//the teacher needs to select a studentid to proceed
				    	
				    	ChoiceDialog<String> d = new ChoiceDialog<>(studentIDs.get(0), studentIDs);
						d.setTitle("Choice Dialog");
						d.setHeaderText("Choose a studentID (there are more than one students with the selected name)");
						d.setContentText("Choice:");
				    	
						Optional<String> result1 = dialog.showAndWait();
						
						if(result1.isPresent()){
							
							String selectedStudentID = result1.get();
							//need to get the average score for this studentID from Score table
							
							studentNameLbl.setText(selectedStudentName);
							List<String> scores = new ArrayList<String>();
							
							String query = "select score from Score where studentID='"+selectedStudentID+"'";
							ResultSet rs1 = st.executeQuery(query);
							
							while(rs1.next()){
								scores.add(rs1.getString("score"));
							}
							
							double sum = 0;
							for(String s: scores){
								sum += Double.parseDouble(s);
							}
							
							Double avgScore = sum/scores.size();
							scoreLbl.setText(avgScore.toString());
							
						}
				    	
				    }
				    else{
				    	//there is only one student with the selected name
				    	String selectedStudentID = studentIDs.get(0);
				    	//need to get the average score for this studentID from Score table
						
						studentNameLbl.setText(selectedStudentName);
						List<String> scores = new ArrayList<String>();
						
						String query = "select score from Score where studentID='"+selectedStudentID+"'";
						ResultSet rs1 = st.executeQuery(query);
						
						while(rs1.next()){
							scores.add(rs1.getString("score"));
						}
						
						double sum = 0;
						for(String s: scores){
							sum += Double.parseDouble(s);
						}
						
						Double avgScore = sum/scores.size();
						scoreLbl.setText(avgScore.toString());
				    }
				    
			    } catch(Exception e){
			    	e.printStackTrace();
			    }
			    
			    
			}
			
			
			
			
			
		}
		else{
			//display an appropriate error message
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Unauthorized access detected!");
			alert.setContentText("Only a teacher has access to this feature");

			alert.showAndWait();
		}
	}
	
	public void backBtnPressed(){
		try
		{
			String f = "Login.fxml";
			if(LoginController.account.accountType.equals("Teacher")){
				f = "TeacherMenu.fxml";
			}
			else if(LoginController.account.accountType.equals("Teacher")){
				f = "StudentMenu.fxml";
			}
			Parent root = FXMLLoader.load(getClass().getResource(f));
	    	Stage stage = (Stage) backBtn.getScene().getWindow();
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
		
		//if the accountType is Student, display their name and average score
		//regardless of the above, display the top ten scores in the format position. name:score
		//if the student had opted to not have their name shown, format = position. "anon":score
		//show the top 25 only
		
		
		String url = "jdbc:mysql://localhost/db";
		String user = "root";
		String password = "bUrp@107";
		
		if(LoginController.account.accountType.equals("Student")){
			studentNameLbl.setText(LoginController.account.name);
			List<String> scores = new ArrayList<String>();
			
			try{
				Connection con = (Connection) DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				String query = "select score from Score where studentID='"+LoginController.account.idAccount+"'";
				ResultSet rs = st.executeQuery(query);
				
				while(rs.next()){
					scores.add(rs.getString("score"));
				}
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			double sum = 0;
			for(String s: scores){
				sum += Double.parseDouble(s);
			}
			
			Double avgScore = sum/scores.size();
			scoreLbl.setText(avgScore.toString());
			
		}
		
		List<String> lines = new ArrayList<String>();
		
		try{
			
			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			String query = "select * from Score";
			ResultSet rs = st.executeQuery(query);
			ResultSet rs1 = rs;
			
			/**Conditions to display score:
			 * If in top 25
			 * If allowed on lead, display name, else anonymous
			 * Also need to get the names of the users
			 * These are individual scores for quizzes
			 * I need the averages*/
			
			Map<String, Double> scores = new HashMap<String, Double>();//<studentid, score>
			//now find the averages of all the scores with the same studentIDs and add them to the averageScores Map as a single entry
			Map<String, Double> averageScores = new HashMap<String, Double>();
			
			while(rs.next()){
				//scores.add(Double.parseDouble(rs.getString("score")));
				scores.put(rs.getString("studentID"), Double.parseDouble(rs.getString("score")));
			}
			
			List<String> scoreKeyList = new ArrayList<String>(scores.keySet());
			int x = -1;
			for(String s: scores.keySet()){
				x += 1;
				//check if averageScores already contains the key (which would imply that the current studentID has already been done)
				if(!averageScores.containsKey(s)){
					double sum = 0;
					int i = 0;//i is the number of scores that were added together
					for(int c = x;c<scoreKeyList.size();c++){
						if(s.equals(scoreKeyList.get(c))){
							sum += scores.get(c);
							i += 1;
						}
					}
					
					double avg = sum/i;
					averageScores.put(s, avg);
				}
			}
			
			//now I have all the averageScores
			//need to find the top 25
			Map<String, Double> sortedAvgScores = scores.entrySet().stream()//sorting the average scores 
	                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));//TODO:- Ask Mrs Moine if the Lambda expression could get me some more marks
			
			//now the first 25 elements are the top 25
			Set<String> scoresSet = sortedAvgScores.keySet();
			Object[] scoresArr = scoresSet.toArray();
			
			
			if(scoresArr.length>25){
				
				for(int i = 0;i<25;i++){
					
					//name:score
					String q1 = "select allowedOnLead from Score where studentID='"+scoresArr[0].toString()+"'";
					ResultSet r1 = st.executeQuery(q1);
					if((r1.next()) && (r1.getInt("allowedOnLead") == 0)){
						//they don't want their name to be displayed
						lines.add((i+1)+". Anonymous: "+sortedAvgScores.get(scoresArr[0]));
						
					}
					else{
						//their name needs to be displayed
						String q2 = "select name from Account where idAccount='"+scoresArr[0].toString()+"'";
						ResultSet r2 = st.executeQuery(q2);
						
						if(r2.next()){
							lines.add((i+1)+". "+r2.getString("name")+": "+sortedAvgScores.get(scoresArr[0]));
						}
						else{
							//couldn't find the name for some reason and the code is having to handle the error
							lines.add((i+1)+". Anonymous: "+sortedAvgScores.get(scoresArr[0]));
						}
						
					}
				}
				
			}
			else{
				for(int i = 0;i<scoresArr.length;i++){
					
					//name:score
					String q1 = "select allowedOnLead from Score where studentID='"+scoresArr[0].toString()+"'";
					ResultSet r1 = st.executeQuery(q1);
					if((r1.next()) && (r1.getInt("allowedOnLead") == 0)){
						//they don't want their name to be displayed
						lines.add((i+1)+". Anonymous: "+sortedAvgScores.get(scoresArr[0]));
						
					}
					else{
						//their name needs to be displayed
						String q2 = "select name from Account where idAccount='"+scoresArr[0].toString()+"'";
						ResultSet r2 = st.executeQuery(q2);
						
						if(r2.next()){
							lines.add((i+1)+". "+r2.getString("name")+": "+sortedAvgScores.get(scoresArr[0]));
						}
						else{
							//couldn't find the name for some reason and the code is having to handle the error
							lines.add((i+1)+". Anonymous: "+sortedAvgScores.get(scoresArr[0]));
						}
						
					}
				}
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		scoreLV.setItems(FXCollections.observableArrayList(lines));
		
		
		
	}

}
