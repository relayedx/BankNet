package dev;
import java.io.*;
import java.util.List;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.Scanner;

// Server class
class Server {
	DatabaseManager db;
	
	Server() {
		db = new DatabaseManager
				// i changed to // b/c it wasn't working on my mac with \\ :sob: - jerrick
				// i changed it as well b/c the db package is in Phase 3 Files for me :D - fosa
				(System.getProperty("user.dir") + "//Phase 3 Files//db//AllUsers.txt",
				 System.getProperty("user.dir") + "//Phase 3 Files//db//AllAccounts.txt",
				 System.getProperty("user.dir") + "//Phase 3 Files//db//Users//",
				 System.getProperty("user.dir") + "//Phase 3 Files//db//Accounts//");
		db.loadData();
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
				
				while(true) { // While client is connected
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
						System.out.println("ERROR: USER IS NOT LOGGED IN AND INCORRECT MSG SENT");
						// We will skip the rest of the logic, and wait for our login message
						continue;
					}
					msgType type = msg.getType();
					if (type == msgType.LOGOUT_REQUEST) {
						Boolean logout = server.logout(user);
						if (logout) {
							out.writeObject(new Message(msgType.LOGOUT_REQUEST, Status.SUCCESS));
						}else {
							out.writeObject(new Message(msgType.LOGOUT_REQUEST, Status.ERROR));
						}
						out.flush();
					}
					if (type == msgType.WITHDRAWAL_REQUEST) {
						TransactionMessage tMsg = (TransactionMessage) msg;
						TransactionMessage withdraw = server.withdraw(tMsg.getID(), tMsg.getTransaction(), user);
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
						AccountMessage acct = (AccountMessage) server.createAcct(aMsg.getUser(), aMsg.getAcctType());
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
						boolean froze = server.closeAcct(aMsg.getID());
						if (froze) { // if so,
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
				System.out.println("User " + username + " is already logged in, returning fail");
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
		
		user.setIsLoggedIn(false);
		return true;
		
	}
	
	public TransactionMessage withdraw(int acctID, Transaction trans, String username) {
		// TODO: This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		// For now, we will assume they have enough funds
		BankAcct acct = db.getAccount(acctID);
		if (acct == null) { // If there is no account under this ID.
			TransactionMessage msg = new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR,trans, acctID);
			return msg; // Return an error message
		}
		User user = db.getUser(username);
		// If the user trying this transaction is not a teller or the owner/authorized user
		
		
		TransactionMessage temp = acct.withdraw(trans);
		db.addTransaction(acctID, trans);
		return temp;
	}
	
	public TransactionMessage deposit(int acctID, Transaction trans) {
		// TODO: This is where accounts is called using these params, which will send back a TransactionMessage 
		// (since we need both the updated balance, and whether or not this is a success)
		// For now, we will assume they don't have enough funds
		BankAcct acct = db.getAccount(acctID);
		if (acct == null) { // If there is no account under this ID.
			TransactionMessage msg = new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.ERROR,trans, acctID);
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
	
	
	public AccountMessage createAcct(String user, AcctType acctType) { // This will return a  acct back to user
		// TODO: This will call accounts to create an account with these details, also automatically assinging the account to the user
		// Call create account, returned is a skeleton acct msg
		User u = db.getUser(user);
		if (u == null) { // If the user was not found, we're going to return an error.
			AccountMessage msg = new AccountMessage(msgType.ACCOUNT_CREATE,Status.ERROR,null);
			return msg;
		}
		BankAcct acct = new BankAcct(acctType, u);
		db.addAccount(acct);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_CREATE,Status.SUCCESS,acct);
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
		
		return true;
	}
	
	public boolean freezeAcct(int acctID) {
		// TODO: This is where we will call Accounts to freeze an account 
		BankAcct acct = db.getAccount(acctID);
		if (acct == null) { // If account does not exist
			return false; // Return false
		}
		// Otherwise, the account is either frozen or unfrozen
		acct.freezeAcc();
		return true; // Return true
	}
	
	public boolean createUser(UserInfo userInfo, String username, String pass){
		// TODO: This is where we will have the ArrayList of users, check if they exist and then add.
		List<Integer> temp = new ArrayList<Integer>(); // An empty list b/c they have no accounts assigned to them yet.
		User user = new User(username, pass, userInfo, false, temp, false);
		boolean added = db.addUser(user);
		return added;
	}
	
	public AccountMessage getAcct(int acctID) {
		
		BankAcct acct = db.getAccount(acctID);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,acct);
		return msg;
	}
	

}
