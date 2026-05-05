package dev;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class Client {
	private Socket socket;
	private ObjectOutputStream out; 
	private ObjectInputStream in;
	private CountDownLatch latch;
	
	public Client(Socket socket, CountDownLatch latch) throws IOException {
		this.latch = latch;
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			System.out.print("Error: " + e);
		}
	}
	
	public void disconnect() throws IOException {
		socket.close();
		latch.countDown();
	}
	
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
    	// creating latch counter to stop main from terminating 
    	CountDownLatch latch = new CountDownLatch(1);
    	
    	// automatically connects the client to the server
    	int serverPort = 5512;
    	String serverIP = "134.154.64.106";
    	
        int port = serverPort; 
        String host = serverIP; 
        
        // Connect to the ServerSocket at host:port
        Socket socket = null;
        try {
        	socket = new Socket(serverIP, port);
        	System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Failed to connect to " + host + ":" + port);
            System.err.println("Is the server running?");
            System.exit(1);
        }
      
        Client clientRef = new Client(socket, latch);
        ClientController clientController = new ClientController(clientRef);
        latch.await();
    }
    
    public Message login(String username, String password) throws IOException, ClassNotFoundException { // Called from ClientController
        out.writeObject(new LoginMessage(msgType.LOGIN_REQUEST, Status.IN_PROGRESS, username,password)); // Client makes msg to send to the server
        out.flush(); // Sends the message
		Message res = (Message) in.readObject(); // We are expecting a message back
		return res; // sending response from server back to clientController
		
    }
    
    public List<Message> getAccts(String user) throws IOException, ClassNotFoundException{ // TODO: Finish this server side this can be for teller
    	out.writeObject(new AccountsRequestMessage(msgType.ACCOUNTS_REQUEST, Status.SUCCESS, user));
    	out.flush();
    	List<Message> msgs = (List<Message>) in.readObject(); // We are expecting the list of their accts
    	return msgs;
    }
    
    public boolean logout() throws ClassNotFoundException, IOException{
    	out.writeObject(new Message(msgType.LOGOUT_REQUEST,Status.IN_PROGRESS));
    	out.flush();
    	Message msg = (Message) in.readObject(); // We are expecting a msg back
    	if (msg.getStatus() == Status.SUCCESS) { // If logout is successful
    		return true; // We were able to logout
    	}
    	return false; // otherwise, return error
    }
   
    
    public TransactionMessage withdraw(int acctID, Transaction trans) throws IOException, ClassNotFoundException{
    	out.writeObject(new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.IN_PROGRESS,trans,acctID));
    	out.flush();
    	TransactionMessage msg = (TransactionMessage) in.readObject(); // We're expecting a transaction msf back
    	return msg;
    }
    
    public TransactionMessage deposit(int acctID, Transaction trans) throws IOException, ClassNotFoundException{
    	out.writeObject(new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.IN_PROGRESS,trans,acctID));
    	out.flush();
    	TransactionMessage msg = (TransactionMessage) in.readObject(); // We're expecting a transaction msf back
    	return msg;
    }
    
    public boolean resetPass(String user, String pass) throws IOException, ClassNotFoundException {
    	out.writeObject(new LoginMessage(msgType.PWRESET_REQUEST,Status.IN_PROGRESS,user,pass));
    	out.flush();
    	Message msg = (Message) in.readObject();
    	if (msg.getStatus() == Status.SUCCESS){
    		return true;
    	}
    	return false;
    	
    }
    
    // When user log into GUI and has al
    public void requestAccount(String user, int acctID)throws ClassNotFoundException, IOException{
    	out.writeObject(new AccountsRequestMessage(msgType.USER_REQUEST,Status.IN_PROGRESS,user, acctID));
    	out.flush();
    	
    }
    
	public int createAccount(String user, AcctType acctType) throws ClassNotFoundException, IOException{ // TODO: swap this w/ actual account type
    	out.writeObject(new CreateAccountMessage(msgType.ACCOUNT_CREATE,Status.IN_PROGRESS,user,acctType));
    	out.flush();
    	AccountsRequestMessage msg = (AccountsRequestMessage) in.readObject();
    	return msg.getID();
    }
	
	public boolean closeAccount(int acctID) throws ClassNotFoundException, IOException{
		out.writeObject(new AccountsRequestMessage(msgType.ACCOUNT_CLOSE,Status.IN_PROGRESS,acctID));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public boolean addAuthUser(String user, int acctID) throws ClassNotFoundException, IOException {
		out.writeObject(new AccountsRequestMessage(msgType.AUTHUSER_REQUEST, Status.IN_PROGRESS,user,acctID));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public boolean freezeAccount(String user, int acctID) throws ClassNotFoundException, IOException{
		out.writeObject(new AccountsRequestMessage(msgType.ACCOUNTS_FREEZE,Status.IN_PROGRESS,user,acctID));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public boolean createUser(UserInfo userInfo, String user, String pass) throws ClassNotFoundException, IOException {
		out.writeObject(new CreateUserMessage(msgType.USER_CREATE,Status.IN_PROGRESS,userInfo,user,pass));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public AccountMessage getAccount(String acctID) throws ClassNotFoundException, IOException{
		out.writeObject(new AccountsRequestMessage(msgType.ACCOUNT_REQUEST,Status.IN_PROGRESS,acctID));
		out.flush();
		AccountMessage msg = (AccountMessage) in.readObject();
		return msg;
	}
	
	public TransactionMessage transfer(int outgoingAcctID, int incomingAcctID, float amt, String user) throws ClassNotFoundException, IOException {
		int transCount = getTransID();
		
		Transaction outgoing = new Transaction(transCount, user, amt, TranType.TRANSFER);
		Transaction incoming = new Transaction(transCount, user, amt, TranType.TRANSFER);
		TransactionMessage d = deposit(incomingAcctID,incoming);
		
		// We could have this be returned in a list, idk
		if (d.getStatus() == Status.ERROR) { // If withdrawing comes up with an error
			return d;  // Don't deposit, return
		}TransactionMessage w = withdraw(outgoingAcctID,outgoing);
		
		return w;
	}
	
	public List<Transaction> getTransactions(int acctID, String username) throws IOException, ClassNotFoundException {
		// TODO: needs to get transactions from corresponding acctID and username
		out.writeObject(new AccountsRequestMessage(msgType.TRANSACTIONS_REQUEST,Status.IN_PROGRESS,username,acctID));
		out.flush();
		List<Message> response = (List<Message>) in.readObject();
		List<Transaction> myTransactions = new ArrayList<Transaction>();
		if (response.getFirst().getStatus() != Status.ERROR) { // If the first message tells us it was a success
			response.removeFirst(); // remove the msg 
			for (Message msg : response) {
				TransactionMessage tMsg = (TransactionMessage) msg;
				myTransactions.add(tMsg.getTransaction());
				
			}
		} // Otherwise, myTransactions will just return an empty list of transactions
		
		// 
		return myTransactions;
	}
	
	public boolean deleteAuthUser(String user, int acctID) throws ClassNotFoundException, IOException{
		out.writeObject(new AccountsRequestMessage(msgType.AUTHUSER_DELETE, Status.IN_PROGRESS,user,acctID));
		out.flush();
		Message response = (Message) in.readObject();
		if (response.getStatus() == Status.ERROR) {
			return false;
		}
		return true;
	}
	
	public boolean updatedUser(UserInfo userInfo, String user) throws ClassNotFoundException, IOException {
		out.writeObject(new CreateUserMessage(msgType.USER_EDIT,Status.IN_PROGRESS,userInfo,user,""));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public UserInfo getUserInfo(String user) throws ClassNotFoundException, IOException {
		out.writeObject(new AccountsRequestMessage(msgType.USER_REQUEST,Status.IN_PROGRESS, user, -1));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			CreateUserMessage cMsg = (CreateUserMessage) msg;
			return cMsg.getInfo();
		}
		return null;
	}
	/* GETS WHAT THE TRANSACTION ID SHOULD BE FOR TRANSACTION REQUEST */
	public int getTransID() throws ClassNotFoundException, IOException {
		// request transaction id from server
		out.writeObject(new Message(msgType.TRANSACTION_ID, Status.IN_PROGRESS));
		out.flush();
		
		// reads the transaction id
		Message msg = (Message) in.readObject();
		int transCount = Integer.parseInt(msg.getText());
		
		return transCount;
	}
}




