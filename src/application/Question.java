package application;
import java.util.*;
public class Question extends Quiz{

	String question;
	String questionType;//mcq,saq,laq,rvq (multiple choice, short, long and random variable)
	List<String> Answers = new ArrayList<String>();
	String correctAnswer;//if this is equal to "~~~ all ~~~", all the answer choices are correct.
	/**If question type is equal to laq (long answer question),
	 * The correctAnswer variable will be set to the teacherID of the teacher that created it
	 * This will then be added to the notifications of the teacher for him/her to correct
	 * Also, there will be no mark allocations for this question
	 * When the teacher corrects the question, he/she will be prompted to enter the marks obtained for the answer*/
	double marks;//number of marks awarded for correct answer
	double nmarks = 0.0;//number of marks deducted for incorrect answer. Initialized to zero since this is an option input.
}
