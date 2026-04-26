package dev;
import java.io.*;
import java.util.List;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.Scanner;

// Server class
class Server {
	private File UsersFile;
	private File BankAccountsFile;
	private List<User> usersDB;
	private Accounts accountsDB;
	
	Server() {
		this.UsersFile = new File("Users.txt");
		this.BankAccountsFile = new File("BankAccounts.txt");
		this.usersDB = new ArrayList<>();
		loadUsers();
		loadAccounts();
	}
	
	public static void main(String[] args)
	{
		Server ref = new Server();
		ServerSocket server = null;

		try {

			// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			var pool = Executors.newFixedThreadPool(20);
			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected "
								+ client.getInetAddress()
										.getHostAddress());
				
				// using pool of threads to execute new client thread
				// This thread will handle the client separately
				pool.execute(new ClientHandler(client,ref));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	// ClientHandler class
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final Server server;
		private ObjectOutputStream out = null;
		private ObjectInputStream in = null;
		private boolean loggedIn = false;

		// Constructor
		public ClientHandler(Socket socket, Server server)
		{
			this.clientSocket = socket;
			this.server = server;
		}

		public void run() {
			try {
					
				// get the outputstream of client
				out = new ObjectOutputStream(clientSocket.getOutputStream());

				// get the inputstream of client
				in = new ObjectInputStream(clientSocket.getInputStream());
				
				while(true) { // While client is connected
					Message msg = (Message) in.readObject(); // Wait for client to send a msg
					if (msg.getType() == msgType.LOGIN_REQUEST) { // If it is a login request, we can upcast it into a LoginMessage
						LoginMessage loginMsg = (LoginMessage) msg;
						Message res = server.isUser(loginMsg.getUsername(),loginMsg.getPassword()); // Is the user an actual user?
						if (res.getStatus() == Status.SUCCESS) { // if so,
							loggedIn = true; // Mark user as logged in.
							out.writeObject(res); // Send respose
						} else {
							out.writeObject(res); // send response without setting loggedIn to true
						}
						out.flush();// send msg to server
					}
					// Here, we will never do a command as long as the user is not logged in
					if (!loggedIn) { // if the user is not logged in
						System.out.println("ERROR: USER IS NOT LOGGED IN AND INCORRECT MSG SENT");
						// We will skip the rest of the logic, and wait for our login message
						continue;
					}
					msgType type = msg.getType();
					if (type == msgType.WITHDRAWAL_REQUEST) {
						TransactionMessage tMsg = (TransactionMessage) msg;
						TransactionMessage withdraw = server.withdraw(tMsg.getID(), tMsg.getTransaction());
						// We will send back a transaction message
						out.writeObject(withdraw);
						out.flush();
					}
					if (type == msgType.DEPOSIT_REQUEST) {
						TransactionMessage tMsg = (TransactionMessage) msg;
						// We'll call the arguments
						TransactionMessage sendback = server.deposit(tMsg.getID(), tMsg.getTransaction());
						out.writeObject(sendback);
						out.flush();
						
					}
					
					if (type == msgType.PWRESET_REQUEST) {
						LoginMessage lMsg = (LoginMessage) msg;
						boolean reset = server.resetPassword(lMsg.getUsername(), lMsg.getPassword());
						if (reset) { // if so,
							out.writeObject(new Message(msgType.PWRESET_REQUEST, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.PWRESET_REQUEST, Status.ERROR)); // Otherwise send error
						}
						out.flush();// send msg to server
					}
					if (type == msgType.ACCOUNT_REQUEST) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						AccountMessage returningMsg = server.getAcct(aMsg.getID());
						out.writeObject(returningMsg);
						out.flush();
						
					}
					if (type == msgType.ACCOUNTS_REQUEST) { 
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						List<Message> msgs = server.getAccts(aMsg.getUser());
						out.writeObject(msgs);
						out.flush();
						 
					}
					if (type == msgType.ACCOUNT_CREATE) {
						CreateAccountMessage aMsg =(CreateAccountMessage) msg; 
						SkeletonAccountMessage acct = (SkeletonAccountMessage) server.createAcct(aMsg.getUser(), aMsg.getAcctType());
						out.writeObject(acct);
						out.flush();
						
					}
					if (type == msgType.ACCOUNT_CLOSE) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						boolean close = server.closeAcct(aMsg.getID());
						if (close) { // if so,
							out.writeObject(new Message(msgType.ACCOUNT_CLOSE, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.ACCOUNT_CLOSE, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					if (type == msgType.AUTHUSER_REQUEST) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						boolean add = server.addAuthUser(aMsg.getUser(), aMsg.getID());
						if (add) { // if so,
							out.writeObject(new Message(msgType.AUTHUSER_REQUEST, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.AUTHUSER_REQUEST, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					if (type == msgType.ACCOUNTS_FREEZE) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						boolean close = server.closeAcct(aMsg.getID());
						if (close) { // if so,
							out.writeObject(new Message(msgType.ACCOUNT_CLOSE, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.ACCOUNT_CLOSE, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					if (type == msgType.USER_CREATE) {
						CreateUserMessage aMsg = (CreateUserMessage) msg;
						boolean add = server.createUser(aMsg.getInfo(), aMsg.getUser(), aMsg.getPass());
						if (add) { // if so,
							out.writeObject(new Message(msgType.USER_CREATE, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.USER_CREATE, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					
					
					
				}


			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	
	public void loadUsers() {
		try {
			Scanner scanner = new Scanner(UsersFile);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split("\\|");
				// 0 -> username, 1 -> password, 2 -> acctType
				// 3 -> authAccts, 4 -> isLoggedIn
				String username = data[0];
				String password = data[1];
				Boolean isTeller = Boolean.parseBoolean(data[2]);
				List<Integer> authAcctIDs = new ArrayList<>();
				String[] AcctIDsAsString = data[3].split(",");
				for (String id : AcctIDsAsString) {
					authAcctIDs.add(Integer.parseInt(id));
				}
				Boolean isLoggedIn = Boolean.parseBoolean(data[4]);
				usersDB.add(new User(username, password,
					isTeller, authAcctIDs, isLoggedIn));
			}
			
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void loadAccounts() {
		try {
			// List of BankAccts that will be used to initialize accountsDB
			List<BankAcct> BankAccts = new ArrayList<>();
			Scanner scanner = new Scanner(BankAccountsFile);
			while (scanner.hasNextLine() ) {
				// parsing each line by '|'
				String line = scanner.nextLine();
				String[] data = line.split("\\|");
				// getting acctID
				int acctID = Integer.parseInt(data[0]);
				// getting account type
				AcctType type = AcctType.Credit;
				if (data[1].equals("checking")) {type = AcctType.Checking;}
				else if (data[1].equals("savings")) {type = AcctType.Savings;}
				else if (data[1].equals("credit")) {type = AcctType.Credit;}
				// getting acct balance
				String bal = data[2].replace("$", "");
				float balance = Float.parseFloat(bal);
				// getting acct status (frozen & closed)
				boolean frozen = Boolean.parseBoolean(data[3]);
				boolean closed = Boolean.parseBoolean(data[4]);
				String dueDate = data[5];
				// getting owner of acct
				String ownerUsername = data[6];
				// finding account owner obj from usersDB
				User owner = findUser(ownerUsername);
				// parsing authUsers in file to add to List for authUsers
				List<User> authUsers = new ArrayList<>(); // creating new authUsers array so i can just pass in as param
				String[] authUsersInFile = data[7].split(",");
				for (String username : authUsersInFile) {
					User authUser = findUser(username);
					authUsers.add(authUser);
				}
				// manually creating temporary transactions list
				List<Transaction> transactions = new ArrayList<>();
				transactions.add(new Transaction("johndoe", 350.22f, TranType.WITHDRAWAL));
				// creating BankAccts with parsed info and adding to list
				BankAccts.add(new BankAcct(acctID, type, owner, balance, frozen,
					closed, dueDate, authUsers, transactions));
			}
			// initializing accountsDB with all list of all BankAccts we parsed
			accountsDB = new Accounts(BankAccts);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
	public User findUser(String username) {
		 for (User user : usersDB) {
		        if (user.getUsername().equals(username)) {
		            return user;
		        }
		    }
		    return null;
	}
	
	public Message isUser(String username, String password) {
		// TODO: This will be where the user is looked up in the system, and whether or not they are logged in or not. (Michelle)
		// I ended up doing the implementaion, so i can test for role based GUI - fosa
		for (User user : usersDB) {
			if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
				String role = "";
				if (user.getRole()) {
					role = "teller";
					return new Message(msgType.LOGIN_REQUEST, Status.SUCCESS, role);
				} else {
					role = "customer";
					return new Message(msgType.LOGIN_REQUEST, Status.SUCCESS, role);
					
					// List<Integer> authAcctIDs = user.getAuthAcctIDs();
					// List<BankAcct> customerBankAccts = accountsDB.getAccts(authAcctIDs);
					// return new AccountMessage(msgType.ACCOUNT_REQUEST, Status.SUCCESS,
					//	customerBankAccts.get(0));
				}
			}
		}
		
		return new Message(msgType.LOGIN_REQUEST, Status.ERROR);
	}
	
	public TransactionMessage withdraw(int acctID, Transaction trans) {
		// TODO: This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		// For now, we will assume they have enough funds
		TransactionMessage temp = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.SUCCESS,trans,acctID,trans.getAmount()-10);
		return temp;
	}
	
	public TransactionMessage deposit(int acctID, Transaction trans) {
		// TODO: This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		// For now, we will assume they don't have enough funds
		TransactionMessage temp = new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.SUCCESS,trans,acctID,trans.getAmount()+10);
		return temp;
	}
	
	public boolean resetPassword(String user, String newPass) {
		// TODO: This is where the ArrayList of user is called, and their password is changed.
		// For now we will assume the password is changed
		return true;
	}
	
	public List<Message> getAccts(String user){
		// TODO: This gets an arraylist of skeleton accounts that will be sent back to user/teller from accounts
		// There should be and if else statement if the array is empty, which will then send an error message of empty skeleton account.
		User acctOwner = findUser(user); // getting the user whos requesting their accts
		List<Integer> authAcctIDs = acctOwner.getAuthAcctIDs(); // getting the acctIDs that belong to them
		List<BankAcct> userBankAccts = accountsDB.getAccts(authAcctIDs); // extracting all of the bankAccts with previous IDs
		List<Message> acctsRequested = new ArrayList<>();
		for (BankAcct currBankAcct : userBankAccts) {
			acctsRequested.add(new AccountMessage(msgType.ACCOUNTS_REQUEST, Status.SUCCESS, currBankAcct));
		}
		return acctsRequested;
		
		/*
		ArrayList<Message> msgs = new ArrayList<Message>();
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,1,100,"credit"));
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,2,200,"savings"));
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,3,100,"checkings"));
		return msgs;
		*/

	}
	
	
	public SkeletonAccountMessage createAcct(String user, String acctType) { // This will return a skeleton acct back to user
		// TODO: This will call accounts to create an account with these details, also automatically assinging the account to the user
		// Call create account, returned is a skeleton acct msg
		SkeletonAccountMessage test = new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,3,100,"checkings");
		return test;
	}
	
	public boolean closeAcct(int acctID) {
		// TODO: This is where we will call Accounts to close an account 
		// We'll assume for now the closing worked (would return false if acct does not exist or already closed
		return true;
	}
	
	public boolean addAuthUser(String user, int acctID) {
		// TODO: This is where we will call accounts and add an authorized user, and returning whether they worked or not.
		// We'll assume for now the user was added.
		return true;
	}
	
	public boolean freezeAcct(int acctID) {
		// TODO: This is where we will call Accounts to freeze an account 
		// We'll assume for now the freeze worked
		return true;
	}
	
	public boolean createUser(String userInfo, String user, String pass){
		// TODO: This is where we will have the ArrayList of users, check if they exist and then add.
		// Create user object
		// Add to array
		// We assume that this process is succesful (error would be if user already exists
		return true;
	}
	
	public AccountMessage getAcct(int acctID) {
		// TODO: This is where we'll get a singular account using Accounts
		// For now, we'll send back a static manual account message
		
		
		List<Integer> authAccts = new ArrayList<>();
		authAccts.add(1);
		authAccts.add(2);
		User user = new User("jerrick", "pass", false,authAccts, true);
		BankAcct test = new BankAcct(AcctType.Checking,user);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,test);
		return msg;
	}
}
