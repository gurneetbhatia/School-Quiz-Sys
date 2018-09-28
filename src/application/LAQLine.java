package application;

public class LAQLine {
	//format:- (studentid+":"+question+":"+ownerid+":"+answer+":"+done);
	/** Objects of this class are used in the TeacherMenu_ReviewLAQController class when the program is reading lines from the reviewLAQ.txt file.
	 * Data in that file is stored in objects of this class as attributes.*/
	String studentid, question, ownerid, answer, quizid;
	boolean done;
}
