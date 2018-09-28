package application;

public class Account {
	/** This class is used for Dynamic Generation of Objects
	 * When a user logs into the system, to make his/her accounts' data easier to access, an object of this class is created.
	 * This class contains an attribute for each of the columns in the Account table of the database used.*/
	String idAccount; // the unique of identifier of the account
	String name; // the name of the account holder
	String password; // the password of the account holder
	String email; // the email of the account holder
	String className; // the name of the class that the account holder belongs to
	String accountType; //the accountType of the account holder (student, teacher, admin, etc.)
	String registrationID; // if the user hasn't logged in a single time since the admin registered their account, registrationID holds a value which was emailed to the user
	String username; // the username of the user
}
