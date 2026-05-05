package dev;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
	private List<User> usersDB;
	private List<BankAcct> accountsDB;
	private String usersFile;		// = "..db/AllUsers.txt";
	private String accountsFile;	// = "..db/AllAccounts.txt";
	private String userInfoFolder;
	private String transactionFolder;
	private static int bankCount;
	private static int transCount;
	String defaultDelimiter = "|";		// using String for delimiter
	String secondaryDelimiter = ",";	// so I can use .split() to parse data
	
	// Constructor! Takes in four parameters at construction:
	// 1. the file where all User details are, 2. the file where all Account details are
	// 3. the folder where all UserInfo is stored, and 4. the folder where all account transactions are stored
	// not a Singleton for now, but could be
	public DatabaseManager(String allUsersFileName, String allAccountsFileName,
			 			   String userInfoDirectory, String transactionDirectory) {
		this.usersDB = new ArrayList<User>();			// instantiates at start up
		this.accountsDB = new ArrayList<BankAcct>();	// instantiates at start up
		
		// stores these file names / directories for later saving and loading
		this.usersFile = allUsersFileName;
		this.accountsFile = allAccountsFileName;
		this.userInfoFolder = userInfoDirectory;
		this.transactionFolder = transactionDirectory;
	}
	
	// mainly for testing verification,
	// but if server needs the database for some reason, it can also grab it
	// can also be removed
	public List<BankAcct> getAccountDB() {
		return accountsDB;
	}
	
	// mainly for testing verification,
	// but if server needs the database for some reason, it can also grab it
	// can also be removed
	public List<User> getUserDB() {
		return usersDB;
	}
	
	/* LOADS ALL BANK DATA FROM FILES STORED IN THE DATABASE */
	public void loadData() {
		/* in these examples, the default delimiter is “|” and the
		 * secondary delimiter is “,”. [variable_name] refers to a
		 * parameter within one of the other text files */
		
		/* LOADS ALL USERS FROM A FILE */
		File allUsersFile = new File(usersFile);	// open allUsers file
		System.out.println(allUsersFile.exists());
		/// loads all the contents of the files into this ArrayList of ArrayLists
			// each ArrayList in the main ArrayList has a line's worth of data fields in it
		ArrayList<ArrayList<String>> allUsersData = loadFile(allUsersFile, defaultDelimiter);
		
		// for all the lines in the text file
		for (int i = 0; i < allUsersData.size(); i++) {
			/// AllUsers.txt format: username|password|isTeller|authorizedAcctIDs (id_1,id_2)
				// ex. fwaffycafecat|balls123|false|1,3,4,20
			
			System.out.println("User " + i);
			
			// the current data line (<-- reference point)
			ArrayList<String> userArrayList = allUsersData.get(i);
			
			/// grabs each field and parses them properly into their data formats
			String username = userArrayList.get(0);
			System.out.println("Username: " + username);
			String password = userArrayList.get(1);
			System.out.println("Password: " + password);
			boolean isTeller = Boolean.parseBoolean(userArrayList.get(2));
			System.out.println("isTeller: " + isTeller);
			String acctIDs = "";
			
			// if user has accounts
			if (userArrayList.size() >= 4) {
				// grabs it from file
				acctIDs = userArrayList.get(3);
			} // else, acctIDs is empty ("")
			
			System.out.println("acctIDs: " + acctIDs);
			
			/// turns the String of acctIDs into a List of Integers
			// makes a local List<Integer>
			List<Integer> dataAcctIDs = new ArrayList<Integer>();
			
			// if there are acctIDs to process
			// ex. "1,3,4,20"
			if (acctIDs != "") {
				// separate the IDs into a String ArrayList
				ArrayList<String> splitAcctIDs = loadList(acctIDs, secondaryDelimiter);
				// then turn it into an Integer ArrayList
				// (matches List<Integer> authAcctIDs in User class)
				for (int j = 0; j < splitAcctIDs.size(); j++) {
					// add the ids (in int form) to the local List<Integer>
					dataAcctIDs.add(Integer.parseInt(splitAcctIDs.get(j)));
				}
			}
			
			/// extracts the user's User Info from a different file
			String userInfoFileName = userInfoFolder + username + "_info.txt";
			System.out.println("\nUser Info File Name: " + userInfoFileName);
			File userInfoFile = new File(userInfoFileName);
			ArrayList<ArrayList<String>> userData =
					loadFile(userInfoFile, defaultDelimiter);
			
			// grabs the line of data from the User Info file
				// there should only be one line in there (can combine this data into AllUsers file if needed)
			ArrayList<String> userInfoArrayList = userData.get(0);
			
			// grabs each field from the User Info file and parses the date of birth
				/// [username]_info.txt format: firstName|lastName|address|dob|phoneNum
			String firstName = userInfoArrayList.get(0);
			System.out.println("First Name: " + firstName);
			String lastName = userInfoArrayList.get(1);
			System.out.println("Last Name: " + lastName);
			String address = userInfoArrayList.get(2);
			System.out.println("Address: " + address);
			// date has to be formatted like this "2023-12-31" for parse to work
			LocalDate dob = LocalDate.parse(userInfoArrayList.get(3));
			System.out.println("Date of Birth: " + dob);
			String phoneNum = userInfoArrayList.get(4);
			System.out.println("Phone Number: " + phoneNum);
			
			System.out.println("");
			
			// turns all the User Info data into a UserInfo object
			UserInfo info = new UserInfo(firstName, lastName, address, dob, phoneNum);
			
			// turns all the parsed User data and the UserInfo object into a User object
			User user = new User(username, password,
					info, isTeller, dataAcctIDs, false);	// dunno how to handle isLoggedIn boolean
			
			// adds the User to the active database
			usersDB.add(user);
		}
		
		// ^ this loads all users from the AllUsers file
		
		/* LOADS ALL ACCOUNTS FROM A FILE */
		File allAccountsFile = new File(accountsFile); // open allAccounts file
		
		/// loads all the contents of the files into this ArrayList of ArrayLists
			// each ArrayList in the main ArrayList has a line's worth of data fields in it
		ArrayList<ArrayList<String>> allAccountsData = loadFile(allAccountsFile, defaultDelimiter);
		
		// for all the lines in the text file
		for (int i = 0; i < allAccountsData.size(); i++) {
			/// AllAccounts.txt format: id|owner|type|balance|credit|availCredit|frozen|closed|dueDate|AuthUsers (authUser1,authUser2)
				// ex. 0|user0|Checking|10.00|0.00|0.00|false|false|2026-04-22|user1,user2
			
			System.out.println("Account " + i);
			
			// the current data line (<-- reference point)
			ArrayList<String> accountArrayList = allAccountsData.get(i);
			
			/// grabs each field and parses them properly into their data formats
			int acctID = Integer.parseInt(accountArrayList.get(0));
			System.out.println("ID: " + acctID);
			User owner = getUser(accountArrayList.get(1)); // get User object via username
			
			System.out.println("Type: " + accountArrayList.get(2));
			AcctType type = AcctType.parseAcctType(accountArrayList.get(2));
			
			/// turns the String of usernames into a List of Users
			// makes a local List<User>
			List<User> authUsers = new ArrayList<User>();
			
			// if Account has authUsers to process
			// ex. "jelly, shar"
			if (accountArrayList.size() >= 10) {
				// grabs it from file and processes it into an ArrayList of Strings
				ArrayList<String> authUsernames = loadList(accountArrayList.get(9), secondaryDelimiter);
				// gets a List<User> of the actual User objects via the parsed usernames
				authUsers = getUsers(authUsernames);
			} // else, authUsers is empty (no elements in List<User>)
			
			float balance = Float.parseFloat(accountArrayList.get(3));
			System.out.println("Balance: " + balance);
			float creditLine = Float.parseFloat(accountArrayList.get(4));
			System.out.println("Credit Line: " + creditLine);
			float availCredit = Float.parseFloat(accountArrayList.get(5));
			System.out.println("Available Credit: " + availCredit);
			
			// date has to be formatted like this "2023-12-31" for parse to work
			LocalDate dueDate = null;
			// if account is checking, has a null dueDate variable (set above)
			if (type != AcctType.Checking) {
				dueDate = LocalDate.parse(accountArrayList.get(8));
			}
			System.out.println("Due Date: " + dueDate);
			
			boolean frozen = Boolean.parseBoolean(accountArrayList.get(6));
			System.out.println("Frozen: " + frozen);
			boolean closed = Boolean.parseBoolean(accountArrayList.get(7));
			System.out.println("Closed: " + closed);
			
			/// turns the account's transaction file into a List of Transactions
			// makes a local List<Transaction>
			List<Transaction> transactions = new ArrayList<>();
			
			// tries to extract the account's Transaction data from a different file
			String transactionFileName = transactionFolder + acctID + "_transactions.txt";
			System.out.println("\nTransaction File Name: " + transactionFileName);
			File transactionFile = new File(transactionFileName);
			// instantiates ArrayList that will contain all the Transactions in the files
			// Each Transaction should be an ArrayList that's already had its data separated by a delimiter
			ArrayList<ArrayList<String>> transactionData = new ArrayList<ArrayList<String>>();
			
			// if Account has Transactions to process
			// aka if the transaction file even exists
			if (transactionFile.exists()) {
				// grabs the data from the file
				transactionData = loadFile(transactionFile, defaultDelimiter);
			}
			
			// grabs each field from the Transaction file and parses them to their appropriate data types
				/// [id]_transactions.txt format: date|user|amt|tranType
			for (int j = 0; j < transactionData.size(); j++) {
				
				System.out.println("\nTransaction " + j);
				// the current data line (<-- reference point)
				ArrayList<String> transactionLine = transactionData.get(j);
				
				// date has to be formatted like this "2024-12-31T10:15:30" for parse to work
				int id = Integer.parseInt(transactionLine.get(0));
				System.out.println("ID: " + id);
				LocalDateTime date = LocalDateTime.parse(transactionLine.get(1));
				System.out.println("Date: " + date);
				String user = transactionLine.get(2);
				System.out.println("User: " + user);
				float amt = Float.parseFloat(transactionLine.get(3));
				System.out.println("Amount: " + amt);
				TranType tranType = TranType.parseTranType(transactionLine.get(4));
				System.out.println("Type: " + transactionLine.get(4));
				
				// turns all the Transaction data into a Transaction object
				Transaction transaction = new Transaction(id, date, user, amt, tranType);
				
				// add the transactions (in Transaction form) to the local List<Transaction>
				transactions.add(transaction);
				
				transCount++;
			}
			
			// turns all the parsed Account data and parsed Transaction List into an Account object
			BankAcct account = new BankAcct(acctID, owner, type, authUsers,
			balance, creditLine, availCredit, dueDate, frozen, closed, transactions);
			
			// adds the User to the active database
			accountsDB.add(account);
			// keeps track of the current BankAcct id
			bankCount++;		// might need to alter BankAcct constructor to take in id
			System.out.println("");
		}
		System.out.println("Next Account ID: " + bankCount);
		
	}
	
	/* HELPER METHOD THAT LOADS THE DATA FROM ONE FILE */
	private ArrayList<ArrayList<String>> loadFile(File file, String delimiter) {
		// local delimiter makes it easy to change the format, if needed
		// returns an ArrayList (all data line) of ArrayLists (all fields within the data lines)
		
		// constructs object to be returned
		ArrayList<ArrayList<String>> dataLines = new ArrayList<ArrayList<String>>();
		
		// try to read all the contents of the file given
		try {
			// reads from file
			Scanner in = new Scanner(file);
			
			// while there is still content to read
			while (in.hasNextLine()) {
				String data = in.nextLine();
				// System.out.println("Data Line: " + data);
				
				// if the content isn't just whitespace
				if (!data.trim().isEmpty()) {
					
					// splits the data line into an ArrayList which contains all the data fields
					ArrayList<String> dataFields = loadList(data, delimiter);
					
					// and then adds it to an overarching ArrayList which has all the data lines
					dataLines.add(dataFields);
					
				}
			}
			in.close();	// closes the scanner
			
		// Error handling: if it fails to find or open the file given
		} catch (IOException e0) {
			// print error message
			System.out.println(e0);
			
		// Error handling: if the data itself is corrupted or formatted incorrectly
		} catch (ArrayIndexOutOfBoundsException e1) {
			// notifies the user and stops initializing the collection at the point of corruption
			System.out.println("The data is corrupted. Stopping load here.");
			
		// Error handling: for any other possible error
		} catch (Exception e) {
			// print error message
			System.out.println(e);
		}
		
		// returns a data separated version of the given file
		return dataLines;	// if can't read or file is empty, returns empty
	}
	
	/* HELPER METHOD THAT PARSES A DATA LINE OR A DATA FIELD WHICH CONTAINS A LIST */
	private ArrayList<String> loadList(String list, String delimiter) {
		// local delimiter makes it easy to change the format, if needed
		
		// constructs object to be returned
		ArrayList<String> dataList = new ArrayList<String>();
		
		// if the content isn't just whitespace
		if (!list.trim().isEmpty()) {
			
			// if the delimiter is "|" (a special character for .split())
			if (delimiter.equals("|")) {
				// makes sure to escape it when splitting by "|"
				delimiter = "\\" + delimiter;
			}
			
			// split the line by the delimiter
			String[] array = list.split(delimiter);
			
			// transfers the split data line into an ArrayList<String>
			for (int i = 0; i < array.length; i++) {
				dataList.add(array[i]);
				// System.out.println("Field " + i +": " + array[i]);
			}
			
		}
		
		// return the line or list in split data form
		return dataList;	// if line or list is empty, returns empty
	}

	/* SEARCHES FOR AND RETURNS A USER OBJECT, GIVEN A USERNAME */
	public User getUser(String username) {
		// this is a linear search algorithm to get a user from username
		// if i feel bothered to, i'll code up a better algorithm later
		// maybe use hash tables idfk
		
		// goes through the user database one by one
		for (int i = 0; i < usersDB.size(); i++) {
			// if user is found
			if(usersDB.get(i).getUsername().equals(username)) {
				// immediately return the user object
				return usersDB.get(i);
			}
		}
		
		// if user is not found
		return null;
	}
	
	/* SEARCHES FOR AND RETURNS MULTIPLE USER OBJECTS, GIVEN A LIST OF USERNAMES */
	public List<User> getUsers(List<String> usernames) {
		// constructs object to be returned
		List<User> users = new ArrayList<User>();
		
		// searches for each user one by one
		for (int i = 0; i < usernames.size(); i++) {
			// uses the getUser() method to find users
			User foundUser = getUser(usernames.get(i));
			
			// if the user is found
			if (foundUser != null) {
				// adds it to the list of users that will be returned
				users.add(foundUser);
			}
		}
		
		// return a List of User objects
		return users;	// if usernames is empty or can't find all usernames, returns empty
	}
	
	/* SEARCHES FOR AND RETURNS AN ACCOUNT OBJECT, GIVEN AN ACCOUNT ID */
	public BankAcct getAccount(int acctID) {
		// this is a linear search algorithm to get an account from id
		// if i feel bothered to, i'll code up a better algorithm later
		// maybe use hash tables idfk
		
		// goes through the account database one by one
		for (int i = 0; i < accountsDB.size(); i++) {
			// if account is found
			if(accountsDB.get(i).getAcctID() == acctID) {
				// immediately return the account object
				return accountsDB.get(i);
			}
		}
		
		// if account is not found
		return null;
	}
	
	/* SEARCHES FOR AND RETURNS MULTIPLE ACCOUNT OBJECTS, GIVEN A LIST OF ACCOUNT IDS */
	public List<BankAcct> getAccounts(List<Integer> acctIDs) {
		// constructs object to be returned
		List<BankAcct> accounts = new ArrayList<BankAcct>();
		
		// searches for each account one by one
		for (int i = 0; i < acctIDs.size(); i++) {
			// uses the getAccount() method to find accounts
			BankAcct foundAcc = getAccount(acctIDs.get(i));
			
			// if the account is found
			if (foundAcc != null) {
				// adds it to the list of accounts that will be returned
				accounts.add(foundAcc);
			}
		}
		
		// return a List of Account objects
		return accounts;	// if acctIDs is empty or can't find all acctIDs, returns empty
	}
	
	/* SEARCHES FOR A USER AND RETURNS A LIST OF THEIR ACCOUNTS, GIVEN A USERNAME */
	public List<BankAcct> getUserAllAccts(String username) {
		// constructs object to be returned
		List<BankAcct> accounts = new ArrayList<BankAcct>();
		
		// tries to find the given user
		User user = getUser(username);
		
		// if the user is found
		if (user != null) {
			// tries to find all of their accounts
			accounts = getAccounts(user.getAuthAcctIDs());
		}
		
		// return a List of the User's Account objects
		return accounts;
		// ^ if can't find User, User's authAccts are empty
		// or can't find all of User's authAccts, returns empty
	}
	
	/* GETS THE NEWEST AVAILABLE ACCOUNT ID FOR NEW ACCOUNTS */
	public int getBankCount() {
		return ++bankCount;
	}
	
	/* GETS THE NEWEST AVAILABLE TRANSACTION ID FOR NEW TRANSACTIONS */
	public int getTransCount() {
		return transCount;
	}
	
	/* ADDS A USER OBJECT TO THE DATABASE AND RECORDS THE CHANGES TO FILES */
	public boolean addUser(User user) {
		if (user != null) {
			// adds the user to the database
			usersDB.add(user);
			// sorts the database by username
			usersDB.sort( (a, b) -> { return -1 * b.getUsername().compareTo(a.getUsername()); } );
			
			// update user file method
			writeToAllUsers();
			// update user info file method (creates a user info file for the user)
			writeToUserInfo(user.getUsername());
			
			// operation success!
			return true;
		}
		
		return false;	// operation failed
	}
	
	/* ADDS AN ACCOUNT OBJECT TO THE DATABASE AND RECORDS THE CHANGES TO FILES */
	public boolean addAccount(BankAcct account) {
		User user = getUser(account.getOwner().getUsername());
		if (account != null || user != null) {
			// adds the account to the database
			accountsDB.add(account);
			// adds the account to the user's authorized accounts
			user.addAuthAcct(account.getAcctID());
			// sorts the database by id number
			accountsDB.sort( (a, b) -> { return -1 * Integer.compare(b.getAcctID(), a.getAcctID()); } );
		
			// update account file method
			writeToAllAccounts();
			writeToAllUsers();
			
			// not sure why a new account would have transactions
			// but if they do, also saves the transactions to file
			for (int i = 0; i < account.getTrans().size(); i++) {
				Transaction trans = account.getTrans().get(i);
				addTransaction(account.getAcctID(), trans);
			}
			
			
			// operation success!
			return true;
		}
		
		return false;	// operation failed
	}
	
	/* ADDS AN AUTH USER TO AN ACCOUNT AND RECORDS THE CHANGES TO FILES */
	public boolean addAuthUser(String username, int acctID) {
		// tries to find the given user
		User user = getUser(username);
		// tries to find the given account
		BankAcct account = getAccount(acctID);
		
		// if both the user and the account is found
		if (user != null && account != null) {
			// logic for adding auth users to an account
			account.addAuthUser(user);
			user.addAuthAcct(acctID);
			
			// update account file method
			writeToAllAccounts();
			writeToAllUsers();
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* REMOVES AN AUTH USER TO AN ACCOUNT AND RECORDS THE CHANGES TO FILES */
	public boolean removeAuthUser(String username, int acctID) {
		// tries to find the given user
		User user = getUser(username);
		// tries to find the given account
		BankAcct account = getAccount(acctID);
		
		// if both the user and the account is found
		if (user != null && account != null) {
			// logic for removing auth users to an account
			account.removeAuth(user);
			user.deleteAuthAcct(acctID);
			
			// update account file method
			writeToAllAccounts();
			writeToAllUsers();
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* CHANGES AN ACCOUNTS FROZEN BOOLEAN AND RECORDS THE CHANGES TO FILES */
	public boolean freeze(String username, int acctID) {
		// tries to get the user of the account
		User user = getUser(username);
		
		// tries to find the given account
		BankAcct account = getAccount(acctID);
		
		
		// if the account and user is found and they are authorized to make the change
		if ((account != null || user != null) && (user.getRole() || account.getOwner() == user)) {
			// logic for freezing account
			account.freezeAcc();
			
			// update account file method
			writeToAllAccounts();
			
			// operation success!
			return true;
			
		}
		
		// else doesn't update
		return false; // operation failed
	}
	
	/* ADDS A TRANSACTION TO AN ACCOUNT AND RECORDS THE CHANGES TO FILES */
	public boolean addTransaction(int acctID, Transaction trans) {
		// assume the logic for transactions (deposit/withdraw) have taken place already
		// tries to find the given account
		BankAcct account = getAccount(acctID);
		
		// if the account is found and transaction is not null
		if (account != null && trans != null) {
			// update transaction file method
			writeToTransactions(acctID, trans);
			// updates balance on account
			writeToAllAccounts();

			// iterates up to an available transaction id
			transCount++;
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	// idk if we need this, which is why it hasn't been implemented yet
	// + assignAcc(String acctID, String user) : boolean
	
	/* UPDATES A USER'S FULL NAME AND RECORDS THE CHANGES TO FILES */
	public boolean updateName(String username, String firstName, String lastName) {
		// tries to find the given user
		User user = getUser(username);
				
		// if the user is found
		if (user != null) {
			// changes their name
			user.getUserInfo().setFirstName(firstName);
			user.getUserInfo().setFirstName(lastName);
			
			// update user info file method
			writeToUserInfo(username);
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* UPDATES A USER'S ADDRESS AND RECORDS THE CHANGES TO FILES */
	public boolean updateAddress(String username, String address) {
		// tries to find the given user
		User user = getUser(username);
		
		// if the user is found
		if (user != null) {
			// changes their address
			user.getUserInfo().setAddress(address);
			
			// update user info file method
			writeToUserInfo(username);
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* UPDATES A USER'S DATE OF BIRTH AND RECORDS THE CHANGES TO FILES */
	public boolean updateDOB(String username, LocalDate dob) {
		// tries to find the given user
		User user = getUser(username);
		
		// if the user is found
		if (user != null) {
			// changes their date of birth
			user.getUserInfo().setDOB(dob);
			
			// update user info file method
			writeToUserInfo(username);
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* UPDATES A USER'S PHONE NUMBER AND RECORDS THE CHANGES TO FILES */
	public boolean updatePhone(String username, String phone) {
		// tries to find the given user
		User user = getUser(username);
		
		// if the user is found
		if (user != null) {
			// changes their phone number
			user.getUserInfo().setPhone(phone);
			
			// update user info file method
			writeToUserInfo(username);
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* UPDATES A USER'S PASSWORD AND RECORDS THE CHANGES TO FILES */
	public boolean updatePassword(String username, String pass) {
		// tries to find the given user
		User user = getUser(username);
		
		// if the user is found
		if (user != null) {
			// changes their password
			user.setPassword(pass);
			
			// update user file method
			writeToAllUsers();
			
			// operation success!
			return true;
		}
		
		// else doesn't update
		return false;	// operation failed
	}
	
	/* UPDATES THE FILE THAT CONTAINS ALL USER DATA WITH NEW INFORMATION */
	private void writeToAllUsers() {
		String fileName = usersFile;		// grab all users file name
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		/// records all the data from the user database
		for (int i = 0; i < usersDB.size(); i++) {
			// AllUsers.txt format: username|password|isTeller|authorizedAcctIDs (id_1,id_2)
			// ex. jelly|ultra|false|1,3,4,20
			
			// for each user
			newData += usersDB.get(i).getUsername() + defaultDelimiter;
			newData += usersDB.get(i).getPassword() + defaultDelimiter;
			newData += usersDB.get(i).getRole() + defaultDelimiter;
			
			// separates the acctIDs with the secondaryDelimiter
			List<Integer> authAcctIDs = usersDB.get(i).getAuthAcctIDs();
			for (int j = 0; j < authAcctIDs.size(); j++) {
				// if this id is the last id
				if (j == authAcctIDs.size() - 1) {
					// do not include a delimiter
					newData += authAcctIDs.get(j);
				} else {
					// otherwise, append a delimiter after each id
					newData += authAcctIDs.get(j) + secondaryDelimiter;
				}
			}
			
			// and then new line for the next user
			newData += "\n";
		}
		
		// overwrites the all users file with updated information
		saveToFile(file, newData, false);
	}
	
	/* UPDATES THE FILE THAT CONTAINS ALL ACCOUNT DATA WITH NEW INFORMATION */
	private void writeToAllAccounts() {
		String fileName = accountsFile;		// grab all accounts file name
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		/// records all the data from the account database
		for (int i = 0; i < accountsDB.size(); i++) {
			// AllAccounts.txt format: id|owner|type|balance|credit|availCredit|frozen|closed|dueDate|AuthUsers (authUser1,authUser2)
				// ex. 0|user0|Checking|10.00|0.00|0.00|false|false|2026-04-22|user1,user2
			
			// for each account
			newData += accountsDB.get(i).getAcctID() + defaultDelimiter;
			newData += accountsDB.get(i).getOwner().getUsername() + defaultDelimiter;
			newData += accountsDB.get(i).getType() + defaultDelimiter;
			newData += accountsDB.get(i).getBalance() + defaultDelimiter;
			newData += accountsDB.get(i).getCredit() + defaultDelimiter;
			newData += accountsDB.get(i).getAvailCredit() + defaultDelimiter;
			newData += accountsDB.get(i).getFrozen() + defaultDelimiter;
			newData += accountsDB.get(i).getClosed() + defaultDelimiter;
			newData += accountsDB.get(i).getDueDate() + defaultDelimiter;
			
			// separates the authUsers with the secondaryDelimiter
			List<User> authUsers = accountsDB.get(i).getAuths();
			for (int j = 0; j < authUsers.size(); j++) {
				// if this user is the last user
				if (j == authUsers.size() - 1) {
					// do not include a delimiter
					newData += authUsers.get(j).getUsername();
				} else {
					// otherwise, append a delimiter after each user
					newData += authUsers.get(j).getUsername() + secondaryDelimiter;
				}
			}
			
			// and then new line for the next account
			newData += "\n";
		}
		
		// overwrites the all accounts file with updated information
		saveToFile(file, newData, false);
	}
	
	/* UPDATES THE FILE THAT CONTAINS A USER'S USERINFO WITH NEW INFORMATION */
	private void writeToUserInfo(String username) {
		// construct user info file name
		String fileName = userInfoFolder + username + "_info.txt";
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		// gets User's user info
		UserInfo info = getUser(username).getUserInfo();
		
		// [username]_info.txt format: firstName|lastName|address|dob|phoneNum
		newData += info.getFirstName() + defaultDelimiter;
		newData += info.getLastName() + defaultDelimiter;
		newData += info.getAddress() + defaultDelimiter;
		newData += info.getDOB() + defaultDelimiter;
		newData += info.getPhone() + "\n";
		
		// overwrites the [username]_info file with updated information
		saveToFile(file, newData, false);
	}
	
	/* APPEND A NEW TRANSACTION TO AN ACCOUNT'S TRANSACTION FILE */
	private void writeToTransactions(int acctID, Transaction trans) {
		// construct account transactions file name
		String fileName = transactionFolder + acctID + "_transactions.txt";
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		// [id]_transactions.txt format: id|date|user|amt|tranType
		newData += trans.getUID() + defaultDelimiter;
		newData += trans.getDate() + defaultDelimiter;
		newData += trans.getUser() + defaultDelimiter;
		newData += trans.getAmount() + defaultDelimiter;
		newData += trans.getType() + "\n";
		
		// appends a new transaction to the [id]_transactions file
		saveToFile(file, newData, true);
	}
	
	/* HELPER METHOD THAT OVERWRITES THE PREVIOUS FILE WITH UPDATED INFORMATION OF THE FILE */
	private void saveToFile(File file, String newData, boolean append) {	
		try {
			// makes a file writer
			FileWriter myWriter = new FileWriter(file, append); // <-- this boolean means append or overwrite
																// false for overwrite; true for append to file
			// writes the new content into the file
			myWriter.write(newData);
			// close the writer
			myWriter.close();
			
		// if file operations go wrong for some reason
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
