import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Server class
class Server {
	public static void main(String[] args)
	{
		Server ref = new Server();
		ServerSocket server = null;

		try {

			// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);

			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected"
								+ client.getInetAddress()
										.getHostAddress());

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(client,ref);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
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

		public void run()
		{
			try {
					
				// get the outputstream of client
				out = new ObjectOutputStream(clientSocket.getOutputStream());

				// get the inputstream of client
				in = new ObjectInputStream(clientSocket.getInputStream());
				
				while(true) { // While client is connected
					Message msg = (Message) in.readObject(); // Wait for client to send a msg
					if (msg.getType() == msgType.LOGIN_REQUEST) { // If it is a login request, we can upcast it into a LoginMessage
						LoginMessage lMsg = (LoginMessage) msg;
						boolean auth = server.isUser(lMsg.getUser(),lMsg.getPass()); // Is the user an actual user?
						if (auth) { // if so,
							out.writeObject(new Message(msgType.LOGIN_REQUEST, Status.SUCCESS)); // Send a success msg
							loggedIn = true; // Mark user as logged in.
						}else {
							out.writeObject(new Message(msgType.LOGIN_REQUEST, Status.ERROR)); // Otherwise send error
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
						boolean reset = server.resetPassword(lMsg.getUser(), lMsg.getPass());
						if (reset) { // if so,
							out.writeObject(new Message(msgType.PWRESET_REQUEST, Status.SUCCESS)); // Send a success msg
						}else {
							out.writeObject(new Message(msgType.PWRESET_REQUEST, Status.ERROR)); // Otherwise send error
						}
						out.flush();// send msg to server
					}
					if (type == msgType.ACCOUNTS_REQUEST) { 
						AccountsRequestMessage aMsg = (AccountsRequestMessage) msg;
						ArrayList<Message> msgs = server.getSkelAccts(aMsg.getUser());
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
	
	public boolean isUser(String user, String pass) {
		// TODO: This will be where the user is looked up in the system, and whether or not they are logged in or not. (Michelle)
		// We can also check here whether or not the user is already logged into the system.
		// For now, we will assume they are a user.
		return true;
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
	
	public ArrayList<Message> getSkelAccts(String user){
		// TODO: This gets an arraylist of skeleton accounts that will be sent back to user/teller from accounts
		// There should be and if else statement if the array is empty, which will then send an error message of empty skeleton account.
		ArrayList<Message> msgs = new ArrayList<Message>();
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,1,100,"credit"));
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,2,200,"savings"));
		msgs.add(new SkeletonAccountMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,3,100,"checkings"));
		return msgs;


	}
	
	public void getAcct(String user, int acctID) { 
		// TODO: This returns an account, which is used to give to the teller when the user wants to make a transaction
		// This.... will not return anything for now!!
		
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
}
