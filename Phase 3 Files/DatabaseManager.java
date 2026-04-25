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
	private String usersFile; // = "db/AllUsers.txt";
	private String accountsFile; // = "db/AllAccounts.txt";
	private String userInfoFolder;
	private String transactionFolder;
	private static int counting;
	String defaultDelimiter = "|";		// using String for delimiter
	String secondaryDelimiter = ",";	// so I can use .split() to parse data
	
	
	public DatabaseManager(String allUsersFileName, String allAccountsFileName,
			 			   String userInfoDirectory, String transactionDirectory) {
		this.usersDB = new ArrayList<User>();
		this.accountsDB = new ArrayList<BankAcct>();
		
		this.usersFile = allUsersFileName;
		this.accountsFile = allAccountsFileName;
		this.userInfoFolder = userInfoDirectory;
		this.transactionFolder = transactionDirectory;
	}
	
	public List<BankAcct> getAccountDB() {
		return accountsDB;
	}
	
	public List<User> getUserDB() {
		return usersDB;
	}
	
	public void loadData() {
		/* in these examples, the default delimiter is “|” and the
		 * secondary delimiter is “,”. [variable_name] refers to a
		 * parameter within one of the other text files */
		
		// date format: mm/dd/yy
		// format established in transaction MM/dd/yy HH:mm
		
		// AllUsers.txt format: username|password|isTeller|authorizedAcctIDs (id_1,id_2)
			// ex. fwaffycafecat|balls123|false|1,3,4,20
		
		/* LOADS ALL USERS FROM A FILE */
		File allUsersFile = new File(usersFile);	// open allUsers file
		
		// loads all the contents of the files into this ArrayList of ArrayLists
		// each ArrayList in the main ArrayList has a line's worth of data fields in it
		ArrayList<ArrayList<String>> allUsersData = loadFile(allUsersFile, defaultDelimiter);
		
		// for all the lines in the text file
		for (int i = 0; i < allUsersData.size(); i++) {
			System.out.println("User " + i);
			// the current data line (<-- reference point)
			ArrayList<String> userArrayList = allUsersData.get(i);
			
			// grabs each field and parses them properly into their data formats
			String username = userArrayList.get(0);
			System.out.println("Username: " + username);
			String password = userArrayList.get(1);
			System.out.println("Password: " + password);
			boolean isTeller = Boolean.parseBoolean(userArrayList.get(2));
			System.out.println("isTeller: " + isTeller);
			String acctIDs = "";
			
			// if user has no accounts
			if (userArrayList.size() >= 4) {
				acctIDs = userArrayList.get(3);
			}
			
			System.out.println("acctIDs: " + acctIDs);
			
			// turns the String of AcctIDs into a List of Integers
			List<Integer> dataAcctIDs = new ArrayList<Integer>();
			
			if (acctIDs != "") {
				ArrayList<String> splitAcctIDs = loadList(acctIDs, secondaryDelimiter);
				for (int j = 0; j < splitAcctIDs.size(); j++) {
					dataAcctIDs.add(Integer.parseInt(splitAcctIDs.get(j)));
				}
			}
			
			// extracts the user's User Info from a different file
			String userInfoFileName = userInfoFolder + username + "_info.txt";
			System.out.println("User Info File Name: " + userInfoFileName);
			File userInfoFile = new File(userInfoFileName);
			ArrayList<ArrayList<String>> userData =
			loadFile(userInfoFile, defaultDelimiter);
			
			ArrayList<String> userInfoArrayList = userData.get(0);
			
			// grabs each field from the User Info file and parses the date of birth
			// [username]_info.txt format: firstName|lastName|address|dob|phoneNum
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
			
			// turns the User Info data into a UserInfo object
			UserInfo info = new UserInfo(firstName, lastName, address, dob, phoneNum);
			
			// turns the parsed user data into a User object
			User user = new User(username, password,
			info, isTeller, dataAcctIDs);
			
			// adds it to the active database
			usersDB.add(user);
		}
		
		/* LOADS ALL ACCOUNTS FROM A FILE */
		
		// AllAccounts.txt format: id|owner|type|balance|credit|availCredit|frozen|closed|dueDate|AuthUsers (authUser1,authUser2)
			// ex. 0|user0|Checking|10.00|0.00|0.00|false|false|2026-04-22|user1,user2
		
		File allAccountsFile = new File(accountsFile); // open allAccounts file
		
		ArrayList<ArrayList<String>> allAccountsData = loadFile(allAccountsFile, defaultDelimiter);
		for (int i = 0; i < allAccountsData.size(); i++) {
			System.out.println("Account " + i);
			// the current data line (<-- reference point)
			ArrayList<String> accountArrayList = allAccountsData.get(i);
			
			int acctID = Integer.parseInt(accountArrayList.get(0));
			System.out.println("ID: " + acctID);
			User owner = getUser(accountArrayList.get(1)); // get username
			
			System.out.println("Type: " + accountArrayList.get(2));
			AcctType type = AcctType.parseAcctType(accountArrayList.get(2));
			
			List<User> authUsers = new ArrayList<User>();
			
			// if user has no authUsers
			// System.out.println("Field Size: " + accountArrayList.size());
			if (accountArrayList.size() >= 10) {
				ArrayList<String> authUsernames = loadList(accountArrayList.get(9), secondaryDelimiter);
				authUsers = getUsers(authUsernames); // get with usernames
			}
			
			float balance = Float.parseFloat(accountArrayList.get(3));
			System.out.println("Balance: " + balance);
			float creditLine = Float.parseFloat(accountArrayList.get(4));
			System.out.println("Credit Line: " + creditLine);
			float availCredit = Float.parseFloat(accountArrayList.get(5));
			System.out.println("Available Credit: " + availCredit);
			
			// date has to be formatted like this "2023-12-31" for parse to work
			LocalDate dueDate = LocalDate.parse(accountArrayList.get(8));
			System.out.println("Due Date: " + dueDate);
			
			boolean frozen = Boolean.parseBoolean(accountArrayList.get(6));
			System.out.println("Frozen: " + frozen);
			boolean closed = Boolean.parseBoolean(accountArrayList.get(7));
			System.out.println("Closed: " + closed);
			
			List<Transaction> transactions = new ArrayList<>();
			
			// extracts the account's Transaction data from a different file
			String transactionFileName = transactionFolder + acctID + "_transactions.txt";
			System.out.println("Transaction File Name: " + transactionFileName);
			File transactionFile = new File(transactionFileName);
			ArrayList<ArrayList<String>> transactionData = new ArrayList<ArrayList<String>>();
			
			// if account has transactions
			if (transactionFile.exists()) {
				transactionData = loadFile(transactionFile, defaultDelimiter);
			}
			
			// [id]_transactions.txt format: date|user|amt|tranType
			for (int j = 0; j < transactionData.size(); j++) {
				System.out.println("Transaction " + j);
				ArrayList<String> transactionLine = transactionData.get(j);
				
				// date has to be formatted like this "2024-12-31T10:15:30"
				LocalDateTime date = LocalDateTime.parse(transactionLine.get(0));
				System.out.println("Date: " + date);
				String user = transactionLine.get(1);
				System.out.println("User: " + user);
				float amt = Float.parseFloat(transactionLine.get(2));
				System.out.println("Amount: " + amt);
				TranType tranType = TranType.parseTranType(transactionLine.get(3));
				System.out.println("Type: " + transactionLine.get(3));
				
				Transaction transaction = new Transaction(date, user, amt, tranType);
				
				transactions.add(transaction);
			}
			
			BankAcct account = new BankAcct(acctID, owner, type, authUsers,
			balance, creditLine, availCredit, dueDate, frozen, closed, transactions);
			
			accountsDB.add(account);
			counting++;
			System.out.println("");
		}
		
		
	}
	
	// local delimiter makes it easy to change the format, if needed
	// returns an ArrayList of ArrayLists
	private ArrayList<ArrayList<String>> loadFile(File file, String delimiter) {
		// try to read all the contents of the file given
		ArrayList<ArrayList<String>> dataLines = new ArrayList<ArrayList<String>>();
		
		try {
			Scanner in = new Scanner(file);
			
			// while there is still content to read
			while (in.hasNextLine()) {
				String data = in.nextLine();
				System.out.println("Data Line: " + data);
				// if the content isn't just whitespace
				if (!data.trim().isEmpty()) {
					// split the line by the delimiters
					if (delimiter.equals("|")) {
						delimiter = "\\" + delimiter;
					}
					String[] dataArray = data.split(delimiter);
					ArrayList<String> dataFields = new ArrayList<String>();
					for (int i = 0; i < dataArray.length; i++) {
						dataFields.add(dataArray[i]);
						// System.out.println("Field " + i +": " + dataArray[i]);
					}
					dataLines.add(dataFields);
				}
			}
			
			in.close();
		// if it fails to find or open the file given
		} catch (IOException e0) {
			// print error message
			System.out.println(e0);
			// if the data itself is corrupted or formatted incorrectly
		} catch (ArrayIndexOutOfBoundsException e1) {
			// notifies the user and stops initializing the collection at the point of corruption
			System.out.println("The data is corrupted. Stopping load here.");
		// for any other possible error
		} catch (Exception e) {
			// print error message
			System.out.println(e);
		}
		
		return dataLines;
	}
	
	// local delimiter makes it easy to change the format, if needed
	private ArrayList<String> loadList(String list, String delimiter) {
		// if the content isn't just whitespace
		if (!list.trim().isEmpty()) {
			// split the line by the delimiter
			if (delimiter.equals("|")) {
				delimiter = "\\" + delimiter;
			}
			String[] array = list.split(delimiter);
			ArrayList<String> dataList = new ArrayList<String>();
			// put it into an array list
			for (int i = 0; i < array.length; i++) {
				dataList.add(array[i]);
			}
			
			// and return the list in data form
			return dataList;
		} else {
			return null;
		}
	}
	
	// turns the date format into a LocalDate object

	// this is a linear search algorithm to get a user from username
	// if i feel bothered to, i'll code up a better algorithm later
	// maybe use hash tables idfk
	public User getUser(String username) {
		for (int i = 0; i < usersDB.size(); i++) {
			if(usersDB.get(i).getUsername().equals(username)) {
	            return usersDB.get(i);
			}
		}
		
		// if user not found
		return null;
	}
	
	public List<User> getUsers(ArrayList<String> usernames) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < usernames.size(); i++) {
			User foundUser = getUser(usernames.get(i));
			if (foundUser != null) {
				users.add(foundUser);
			}
		}
		
		return users;
	}
	
	public BankAcct getAccount(int acctID) {
		BankAcct account = null;
		return account;
	}
	
	// idk if we need this, which is why it hasn't been implemented yet
	// + getAccounts(ArrayList<int>) : ArrayList<bankAcct>
	
	public ArrayList<BankAcct> getUserAllAccts(String user) {
		ArrayList<BankAcct> accounts = null;
		return accounts;
	}
	
	public void addUser(User user) {
		usersDB.add(user);
		// sort by username
		usersDB.sort( (a, b) -> { return -1 * b.getUsername().compareTo(a.getUsername()); } );
		
		writeToAllUsers(); // update user file method
		writeToUserInfo(user.getUsername()); // update user info file method
	}
	
	public void addAccount(BankAcct account) {
		accountsDB.add(account);
		// sort by id number
		accountsDB.sort( (a, b) -> { return -1 * Integer.compare(b.getAcctID(), a.getAcctID()); } );
		
		writeToAllAccounts(); // update account file method
	}
	
	public void addAuthUser(String user, int acctID) {
		getAccount(acctID).addAuthUser(getUser(user));
		
		writeToAllAccounts(); // update account file method
	}
	
	public void removeAuthUser(String user, int acctID) {
		getAccount(acctID).removeAuth(getUser(user));
		
		writeToAllAccounts(); // update account file method
	}
	
	public void addTransaction(int acctID, Transaction trans) {
		writeToTransactions(acctID, trans); // update transaction file method
	}
	
	// + assignAcc(String acctID, String user) : boolean
	
	public void updateName(String username, String firstName, String lastName) {
		User user = getUser(username);
		user.getUserInfo().setFirstName(firstName);
		user.getUserInfo().setFirstName(lastName);
		
		writeToUserInfo(username); // update user info file method
	}
	
	public void updateAddress(String username, String address) {
		User user = getUser(username);
		user.getUserInfo().setAddress(address);
		
		writeToUserInfo(username); // update user info file method
	}
	
	public void updateDOB(String username, LocalDate dob) {
		User user = getUser(username);
		user.getUserInfo().setDOB(dob);
		
		writeToUserInfo(username); // update user info file method
	}
	
	public void updatePhone(String username, String phone) {
		User user = getUser(username);
		user.getUserInfo().setPhone(phone);
		
		writeToUserInfo(username); // update user info file method
	}
	
	public void updatePassword(String username, String pass) {
		User user = getUser(username);
		user.setPassword(pass);
		
		writeToAllUsers(); // update user file method
	}
	
	// update user file method
	private void writeToAllUsers() {
		String fileName = usersFile;
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		// AllUsers.txt format: username|password|isTeller|authorizedAcctIDs (id_1,id_2)
			// ex. fwaffycafecat|balls123|false|1,3,4,20
		for (int i = 0; i < usersDB.size(); i++) {
			newData += usersDB.get(i).getUsername() + defaultDelimiter;
			newData += usersDB.get(i).getPassword() + defaultDelimiter;
			newData += usersDB.get(i).getRole() + defaultDelimiter;
			
			List<Integer> authAcctIDs = usersDB.get(i).getAuthAccts();
			for (int j = 0; j < authAcctIDs.size(); j++) {
				if (j == authAcctIDs.size() - 1) {
					newData += authAcctIDs.get(j);
				} else {
					newData += authAcctIDs.get(j) + secondaryDelimiter;
				}
			}
			
			newData += "\n";
		}
		
		// overwrites the previous file
		try {
			// makes a file writer
			FileWriter myWriter = new FileWriter(file, false); // <-- this boolean means don't append; just overwrite
			// writes the new content into the file
			myWriter.write(newData);
			myWriter.close();	// close the writer
		// if file operations go wrong for some reason
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	// update account file method
	private void writeToAllAccounts() {
		String fileName = accountsFile;
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		// AllAccounts.txt format: id|owner|type|balance|credit|availCredit|frozen|closed|dueDate|AuthUsers (authUser1,authUser2)
			// ex. 0|user0|Checking|10.00|0.00|0.00|false|false|2026-04-22|user1,user2
		for (int i = 0; i < accountsDB.size(); i++) {
			newData += accountsDB.get(i).getAcctID() + defaultDelimiter;
			newData += accountsDB.get(i).getOwner().getUsername() + defaultDelimiter;
			newData += accountsDB.get(i).getType() + defaultDelimiter;
			newData += accountsDB.get(i).getBalance() + defaultDelimiter;
			newData += accountsDB.get(i).getCredit() + defaultDelimiter;
			newData += accountsDB.get(i).getAvailCredit() + defaultDelimiter;
			newData += accountsDB.get(i).getFrozen() + defaultDelimiter;
			newData += accountsDB.get(i).getClosed() + defaultDelimiter;
			newData += accountsDB.get(i).getDueDate() + defaultDelimiter;
			
			List<User> authUsers = accountsDB.get(i).getAuths();
			for (int j = 0; j < authUsers.size(); j++) {
				if (j == authUsers.size() - 1) {
					newData += authUsers.get(j).getUsername();
				} else {
					newData += authUsers.get(j).getUsername() + secondaryDelimiter;
				}
			}
			
			newData += "\n";
		}
		
		// overwrites the previous file
		try {
			// makes a file writer
			FileWriter myWriter = new FileWriter(file, false); // <-- this boolean means don't append; just overwrite
			// writes the new content into the file
			myWriter.write(newData);
			myWriter.close();	// close the writer
		// if file operations go wrong for some reason
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	// update user info file method
	private void writeToUserInfo(String username) {
		String fileName = userInfoFolder + username + "_info.txt";
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		UserInfo info = getUser(username).getUserInfo();
		
		// [username]_info.txt format: firstName|lastName|address|dob|phoneNum
		newData += info.getFirstName() + defaultDelimiter;
		newData += info.getLastName() + defaultDelimiter;
		newData += info.getAddress() + defaultDelimiter;
		newData += info.getDOB() + defaultDelimiter;
		newData += info.getPhone() + "\n";
		
		
		// overwrites the previous file
		try {
			// makes a file writer
			FileWriter myWriter = new FileWriter(file, false); // <-- this boolean means don't append; just overwrite
			// writes the new content into the file
			myWriter.write(newData);
			myWriter.close();	// close the writer
		// if file operations go wrong for some reason
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	// update transaction file method
	private void writeToTransactions(int acctID, Transaction trans) {
		String fileName = transactionFolder + acctID + "_transactions.txt";
		File file = new File(fileName);		// open the file / creates a new file
		String newData = "";
		
		// [id]_transactions.txt format: date|user|amt|tranType
		newData += trans.getDate() + defaultDelimiter;
		newData += trans.getUser() + defaultDelimiter;
		newData += trans.getAmount() + defaultDelimiter;
		newData += trans.getType() + "\n";
		
		// overwrites the previous file
		try {
			// makes a file writer
			FileWriter myWriter = new FileWriter(file, true); // <-- this boolean means append; append only for these
			// writes the new content into the file
			myWriter.write(newData);
			myWriter.close();	// close the writer
		// if file operations go wrong for some reason
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
