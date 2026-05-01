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
    	int serverPort = 1234;
    	String serverIP = "localhost";
    	
        // Scanner sc = new Scanner(System.in); //System.in is a standard input stream.
        // System.out.println("Enter the port number to connect to: <1234>");
        int port = serverPort; // sc.nextInt();
        // sc.nextLine(); // Flushes out the stream to get ready for asking for host address
        // System.out.println("Enter the host address to connect to: <localhost> ");
        String host = serverIP; // sc.nextLine();
        
        // Connect to the ServerSocket at host:port
        Socket socket = null;
        try {
        	socket = new Socket(host, port);
        	System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Failed to connect to " + host + ":" + port);
            System.err.println("Is the server running?");
            System.exit(1);
        }
      
        Client clientRef = new Client(socket, latch);
        ClientController clientController = new ClientController(clientRef);
        latch.await();
        // Output stream socket.
     
        //OutputStream outputStream = socket.getOutputStream();

        // Create object output stream from the output stream to send an object through it
        //out = new ObjectOutputStream(outputStream);
        // Create a input stream to read these objects as well
        //in = new ObjectInputStream(socket.getInputStream());
        
        // instantiation of client and clientController
        // once a clientController is instantiated it will call LoginGUI on its own!
        //Client ref = new Client();
        //controller = new ClientController(ref);
        
        /// SIMULATED LOGIN
        /*
        controller.login("test", "test"); // This will be called from GUI
        controller.withdraw(1, 10, "User");
        controller.resetPassword("test", "test");
        controller.deposit(1, 120, "user");
        controller.getSkelAccts("user");
        */
        // List of Message objects
        //List<Message> messages = new ArrayList<>();
       // messages.add(new Message("This is a test message!"));
        
        //System.out.println("Closing socket");
        //socket.close();
    }
    
    public Message login(String username, String password) throws IOException, ClassNotFoundException { // Called from ClientController
        System.out.println("Sending Login Message Object");
        out.writeObject(new LoginMessage(msgType.LOGIN_REQUEST, Status.IN_PROGRESS, username,password)); // Client makes msg to send to the server
        out.flush(); // Sends the message
		Message res = (Message) in.readObject(); // We are expecting a message back
		return res; // sending response from server back to clientController
		
		/*if (msg.getType() != msgType.LOGIN_REQUEST) { // If the server sends back anything else other than a login_request
			return new LoginMessage(msgType.LOGIN_REQUEST, Stat); // Something is wrong, return false.
		}
		if (msg.getStatus() == Status.SUCCESS) { // If the login_request is true
			return true;
		}else {
			return false;
		}
		*/
    }
    
    public List<Message> getAccts(String user) throws IOException, ClassNotFoundException{ // TODO: Finish this server side this can be for teller
    	// System.out.println("Sending Skeleton Accts Msgs");
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
    	System.out.println("Sending deposit request");
    	out.writeObject(new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.IN_PROGRESS,trans,acctID));
    	out.flush();
    	TransactionMessage msg = (TransactionMessage) in.readObject(); // We're expecting a transaction msf back
    	System.out.println("Got response back" + msg.getStatus());
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
    
	public AccountMessage createAccount(String user, AcctType acctType) throws ClassNotFoundException, IOException{ // TODO: swap this w/ actual account type
    	out.writeObject(new CreateAccountMessage(msgType.ACCOUNT_CREATE,Status.IN_PROGRESS,user,acctType));
    	out.flush();
    	AccountMessage msg = (AccountMessage) in.readObject();
    	return msg;
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
		Transaction outgoing = new Transaction(user, amt, TranType.TRANSFER);
		Transaction incoming = new Transaction(user, amt, TranType.TRANSFER);
		// TODO: When transferring, do we want both updated balances, or just where we're making the transfer from?
		TransactionMessage w = withdraw(outgoingAcctID,outgoing);
		// We could have this be returned in a list, idk
		if (w.getStatus() == Status.ERROR) { // If withdrawing comes up with an error
			return w;  // Don't deposit, return
		}
		TransactionMessage d = deposit(incomingAcctID,incoming);
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
}




