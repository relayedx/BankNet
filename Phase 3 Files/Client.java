import java.io.*;
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
    	
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
        System.out.println("Enter the port number to connect to: <1234>");
        int port = sc.nextInt();
        System.out.println("Enter the host address to connect to: <localhost> ");
        String host = sc.nextLine();
        
        // Connect to the ServerSocket at host:port
        Socket socket = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);
 
        Client clientRef = new Client(socket, latch);
        ClientController controller = new ClientController(clientRef);
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
    
    public boolean login(String user, String pass) throws IOException, ClassNotFoundException { // Called from ClientController
   
        System.out.println("Sending LoginMessage Object");
        out.writeObject(new LoginMessage(msgType.LOGIN_REQUEST, Status.IN_PROGRESS, "user","pass")); // Client makes msg to send to the server
        out.flush(); // Sends the message
		Message msg = (Message) in.readObject(); // We are expecting a message back
		if (msg.getType() != msgType.LOGIN_REQUEST) { // If the server sends back anything else other than a login_request
			return false; // Something is wrong, return false.
		}
		if (msg.getStatus() == Status.SUCCESS) { // If the login_request is true
			return true;
		}else {
			return false;
		}
    }
    
    public ArrayList<Message> getSkelAccts(String user) throws IOException, ClassNotFoundException{ // TODO: Finish this server side this can be for teller
    	System.out.println("Sending Skeleton Accts Msgs");
    	out.writeObject(new AccountsRequestMessage(msgType.ACCOUNTS_REQUEST,Status.SUCCESS,user));
    	out.flush();
    	ArrayList<Message> msgs = (ArrayList<Message>) in.readObject(); // We are expecting the list of their accts
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
   
    
    // TODO: we can prolly combine this and then just send in a msgType as the argument but either works 
    public TransactionMessage withdraw(int acctID, Transaction trans) throws IOException, ClassNotFoundException{
    	out.writeObject(new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.IN_PROGRESS,trans,acctID));
    	out.flush();
    	TransactionMessage msg = (TransactionMessage) in.readObject(); // We're expecting a transaction msf back
    	return msg;
    }
    
    public TransactionMessage deposit(int acctID, Transaction trans) throws IOException, ClassNotFoundException{
    	out.writeObject(new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.IN_PROGRESS,trans,acctID));
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
    
	public SkeletonAccountMessage createAccount(String user, String acctType) throws ClassNotFoundException, IOException{ // TODO: swap this w/ actual account type
    	out.writeObject(new CreateAccountMessage(msgType.ACCOUNT_CREATE,Status.IN_PROGRESS,user,acctType));
    	out.flush();
    	SkeletonAccountMessage msg = (SkeletonAccountMessage) in.readObject();
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
	
	public boolean freezeAccount(int acctID) throws ClassNotFoundException, IOException{
		out.writeObject(new AccountsRequestMessage(msgType.ACCOUNTS_FREEZE,Status.IN_PROGRESS,acctID));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
	
	public boolean createUser(String userInfo, String user, String pass) throws ClassNotFoundException, IOException {
		out.writeObject(new CreateUserMessage(msgType.USER_CREATE,Status.IN_PROGRESS,userInfo,user,pass));
		out.flush();
		Message msg = (Message) in.readObject();
		if (msg.getStatus() == Status.SUCCESS) {
			return true;
		}
		return false;
	}
}




