package dev;
import java.io.*;
import java.util.List;
import java.net.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.Scanner;

// Server class
class Server {
	/* private File UsersFile;
	private File BankAccountsFile;
	private List<User> usersDB;
	private Accounts accountsDB; */
	private static DatabaseManager db;
	
	Server() {
		String directory = System.getProperty("user.dir");
		String folderName = Path.of("").toAbsolutePath().getFileName().toString();
		
		System.out.println("Current Folder: " + folderName);
		if (folderName.equals("BankNet")) {
			directory += "//Phase 3 Files";
		}
		
		db = new DatabaseManager
				// i changed it again, does this new thing work for everyone :,D - michelle
				(directory + "//db//AllUsers.txt",
				 directory + "//db//AllAccounts.txt",
				 directory + "//db//Users//",
				 directory + "//db//Accounts//");
		
		db.loadData();

	}
	
	public static void main(String[] args)
	{
		Server ref = new Server();
		ServerSocket server = null;

		try {

			// server is listening on port 1234
			server = new ServerSocket(5512);
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
		private boolean connected = true;
		private String user;

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
				
				while(connected) { // While client is connected
					Message msg = (Message) in.readObject(); // Wait for client to send a msg
					if (msg.getType() == msgType.LOGIN_REQUEST) { // If it is a login request, we can upcast it into a LoginMessage
						LoginMessage loginMsg = (LoginMessage) msg;
						Message res = server.isUser(loginMsg.getUsername(),loginMsg.getPassword()); // Is the user an actual user?
						if (res.getStatus() == Status.SUCCESS) { // if so,
							loggedIn = true; // Mark user as logged in.
							user = loginMsg.getUsername();
							out.writeObject(res); // Send respose
						} else {
							out.writeObject(res); // send response without setting loggedIn to true
						}
						out.flush();// send msg to server
						continue;
					}
					// Here, we will never do a command as long as the user is not logged in
					if (!loggedIn) { // if the user is not logged in
						// We will skip the rest of the logic, and wait for our login message
						continue;
					}
					msgType type = msg.getType();
					if (type == msgType.LOGOUT_REQUEST) {
						Boolean logout = server.logout(user);
						if (logout) {
							loggedIn = false;
							out.writeObject(new Message(msgType.LOGOUT_REQUEST, Status.SUCCESS));
						}else {
							out.writeObject(new Message(msgType.LOGOUT_REQUEST, Status.ERROR));
						}
						out.flush();
					}
					if (type == msgType.WITHDRAWAL_REQUEST) {
						TransactionMessage tMsg = (TransactionMessage) msg;
						TransactionMessage withdraw = server.withdraw(tMsg.getID(), tMsg.getTransaction(), tMsg.getTransaction().getUser());
						// We will send back a transaction message
						out.writeObject(withdraw);
						out.flush();
					}
					if (type == msgType.DEPOSIT_REQUEST) {
						TransactionMessage tMsg = (TransactionMessage) msg;
						// We'll call the arguments
						TransactionMessage sendback = server.deposit(tMsg.getID(), tMsg.getTransaction(), tMsg.getTransaction().getUser());
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
						AccountsRequestMessage acct = (AccountsRequestMessage) server.createAcct(aMsg.getUser(), aMsg.getAcctType());
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
					if (type == msgType.AUTHUSER_DELETE) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						boolean deleted = server.deleteAuthUser(aMsg.getUser(), aMsg.getID());
						if (deleted) { // if so,
							out.writeObject(new Message(msgType.AUTHUSER_DELETE, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.AUTHUSER_DELETE, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					if (type == msgType.ACCOUNTS_FREEZE) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						boolean froze = server.freezeAcct(aMsg.getUser(),aMsg.getID());
						if (froze) { // if so,
							out.writeObject(new Message(msgType.ACCOUNTS_FREEZE, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.ACCOUNTS_FREEZE, Status.ERROR)); // Otherwise send error
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
					if (type == msgType.TRANSACTIONS_REQUEST) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						List<Message> transactions = server.getTransactions(aMsg.getUser(), aMsg.getID());
						out.writeObject(transactions);
						out.flush();
					}
					if (type == msgType.USER_EDIT) {
						CreateUserMessage aMsg = (CreateUserMessage) msg;
						boolean updated = server.updateUser(aMsg.getInfo(), aMsg.getUser());
						if (updated) { // if so,
							out.writeObject(new Message(msgType.USER_EDIT, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.USER_EDIT, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					if (type == msgType.USER_REQUEST) {
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						Message outgoing = server.getUserInfo(aMsg.getUser());
						out.writeObject(outgoing);
						out.flush();
					}
					
					if (type == msgType.TRANSACTION_ID) {
						String id = Integer.toString(db.getTransCount());
						if (id != null) { // if so,
							out.writeObject(new Message(msgType.TRANSACTION_ID, Status.SUCCESS, id)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.TRANSACTION_ID, Status.ERROR)); // Otherwise send error
						}
						out.flush();
					}
					
					
				}


			}
			catch (IOException e) {
				server.logout(user);
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
	
	public Message isUser(String username, String password) {
		// TODO: This will be where the user is looked up in the system, and whether or not they are logged in or not. (Michelle)
		// I ended up doing the implementaion, so i can test for role based GUI - fosa
		User user = db.getUser(username);
		if (user != null && password.equals(user.getPassword())) {
			if (user.getIsLoggedIn()) {  // Is the user currently logged in?
				return new Message(msgType.LOGIN_REQUEST, Status.ERROR); // If yes, then error (no user can be logged in twice)
			}
			String role = "";
			user.setIsLoggedIn(true); // The user is now currently logged in
			if (user.getRole()) {
				role = "teller";
				return new Message(msgType.LOGIN_REQUEST, Status.SUCCESS, role);
			} else {
				role = "customer";
				return new Message(msgType.LOGIN_REQUEST, Status.SUCCESS, role);
			}
		}
		
		
		return new Message(msgType.LOGIN_REQUEST, Status.ERROR);
	}
	
	public boolean logout(String username) { 
		// I don't know what would cause the logout to return an error...
		User user = db.getUser(username);
		if (user != null) {
			user.setIsLoggedIn(false);
			return true;
		}
		return false;
		
	}
	
	public TransactionMessage withdraw(int acctID, Transaction trans, String username) {
		// This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		BankAcct acct = db.getAccount(acctID);
		User user = db.getUser(username);

		if (acct == null || user == null) { // If there is no account under this ID.
			TransactionMessage msg = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR,trans, acctID);
			return msg; // Return an error message
		}
		// If the user trying this transaction is not the owner/authorized user
		if(!user.getAuthAcctIDs().contains(acct.getAcctID()) && !user.getRole()) {
			TransactionMessage msg = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR,trans, acctID);
			return msg; // Return an error message
		}
		TransactionMessage temp = acct.withdraw(trans);
		db.addTransaction(acctID, trans);
		return temp;
	}
	
	public TransactionMessage deposit(int acctID, Transaction trans, String username) {
		// This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		BankAcct acct = db.getAccount(acctID);
		User user = db.getUser(username);

		if (acct == null || user == null) { // If there is no account under this ID.
			TransactionMessage msg = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR,trans, acctID);
			return msg; // Return an error message
		}
		// If the user trying this transaction is not the owner/authorized user
		if(!user.getAuthAcctIDs().contains(acct.getAcctID()) && !user.getRole()) {
			TransactionMessage msg = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR,trans, acctID);
			return msg; // Return an error message
		}
		TransactionMessage temp = acct.deposit(trans);
		db.addTransaction(acctID, trans);
		return temp;
	}
	
	public boolean resetPassword(String user, String newPass) {
		// TODO: This is where the ArrayList of user is called, and their password is changed.
		// For now we will assume the password is changed
		boolean updated = db.updatePassword(user, newPass);
		return updated;
	}
	
	public List<Message> getAccts(String user){
		// TODO: This gets an arraylist of skeleton accounts that will be sent back to user/teller from accounts
		// There should be and if else statement if the array is empty, which will then send an error message of empty skeleton account.
		User acctOwner = db.getUser(user); // getting the user whos requesting their accts
		// List<Integer> authAcctIDs = acctOwner.getAuthAcctIDs(); // getting the acctIDs that belong to them
		List<BankAcct> userBankAccts = db.getUserAllAccts(user); // extracting all of the bankAccts with previous IDs
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
	
	
	public AccountsRequestMessage createAcct(String user, AcctType acctType) { // This will return a  acct back to user
		// TODO: This will call accounts to create an account with these details, also automatically assinging the account to the user
		// Call create account, returned is a skeleton acct msg
		User u = db.getUser(user);
		if (u == null) { // If the user was not found, we're going to return an error.
			AccountsRequestMessage msg = new AccountsRequestMessage(msgType.ACCOUNT_CREATE,Status.ERROR,-1);
			return msg;
		}
		BankAcct acct = new BankAcct(acctType, u, db.getBankCount());
		db.addAccount(acct);
		AccountsRequestMessage msg = new AccountsRequestMessage(msgType.ACCOUNT_CREATE,Status.SUCCESS,acct.getAcctID());
		return msg;
	}
	
	public boolean closeAcct(int acctID) {
		// TODO: This is where we will call Accounts to close an account 
		// Will return false if acct does not exist or already closed
		BankAcct acct = db.getAccount(acctID);
		if (acct == null) { // If account does not exist
			return false; // Return false
		}
		Boolean closed = acct.closeAcc();
		return closed;
	}
	
	public boolean addAuthUser(String user, int acctID) {
		// TODO: This is where we will call accounts and add an authorized user, and returning whether they worked or not.
		// We'll assume for now the user was added.
		boolean changed = db.addAuthUser(user, acctID);
		
		return changed;
	}
	
	public boolean deleteAuthUser(String user, int acctID) {
		boolean removed = db.removeAuthUser(user, acctID);
		return removed;
	}
	
	public boolean freezeAcct(String user, int acctID) {
		/* CHECKS TO SEE IF USER IS AUTHORIZED TO FREEZE AN ACCOUNT (TELLER/OWNER)*/
		boolean froze = db.freeze(user, acctID);
		return froze; // Return true
	}
	
	public boolean createUser(UserInfo userInfo, String username, String pass){
		// TODO: This is where we will have the ArrayList of users, check if they exist and then add.
		List<Integer> temp = new ArrayList<Integer>(); // An empty list b/c they have no accounts assigned to them yet.
		User user = new User(username, pass, userInfo, false, temp, false);
		boolean added = db.addUser(user);
		return added;
	}
	
	public boolean updateUser(UserInfo userInfo, String user) {
		boolean cond1 = db.updateDOB(user, userInfo.getDOB());
		boolean cond2 = db.updateAddress(user, userInfo.getAddress());
		boolean cond3 = db.updateName(user, userInfo.getFirstName(), userInfo.getLastName());
		boolean cond4 = db.updatePhone(user, userInfo.getPhone());
		return cond1 && cond2 && cond3 && cond4;
	}
	
	public AccountMessage getAcct(int acctID) {
		
		BankAcct acct = db.getAccount(acctID);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,acct);
		return msg;
	}
	
	public List<Message> getTransactions(String username, int acctID){
		BankAcct acct = db.getAccount(acctID);
		List<Transaction> transactions = new ArrayList<Transaction>();
		List<Message> msgs = new ArrayList<Message>();
		if (acct == null) {
			msgs.add(new Message(msgType.TRANSACTIONS_REQUEST,Status.ERROR));
			return msgs;
		}
		transactions = acct.getTrans();
		// If we get here, first msg is a success msg
		msgs.add(new Message(msgType.TRANSACTIONS_REQUEST,Status.SUCCESS));
		for (Transaction trans : transactions) {
			msgs.add(new TransactionMessage(msgType.TRANSACTIONS_REQUEST,Status.SUCCESS,trans,acctID));
		}
		return msgs;
	}
	
	public Message getUserInfo(String username) {
		User user = db.getUser(username);
		if (user != null) {
			return new CreateUserMessage(msgType.USER_REQUEST,Status.SUCCESS,user.getUserInfo(),username,null);
		}
		return new Message(msgType.USER_REQUEST,Status.ERROR);
	}
	
	
	
	

}
