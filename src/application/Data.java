package application;


public class Data {
	/** contains a temporary static object of the Quiz class that can store the Quiz while the user is still working on it (i.e. adding questions)
	 * It is declared static so that it the same data can be accessed from multiple classes like CreateQuizController and CreateQuiz_addQuestion_laq
	 */
	public static Quiz quiz = new Quiz();
	public static CurrentServer server = new CurrentServer();
}
