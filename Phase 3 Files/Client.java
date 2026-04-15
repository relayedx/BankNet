
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
	static ClientController controller;
	static ObjectOutputStream out; 
	static ObjectInputStream in;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
        System.out.print("Enter the port number to connect to: <1234>");
        int port = sc.nextInt();
        System.out.print("Enter the host address to connect to: <localhost> ");
        String host = sc.next();
        
        Client ref = new Client(); 
        controller = new ClientController(ref);
        // Connect to the ServerSocket at host:port
        Socket socket = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);

        // Output stream socket.
        OutputStream outputStream = socket.getOutputStream();

        // Create object output stream from the output stream to send an object through it
        out = new ObjectOutputStream(outputStream);
        // Create a input stream to read these objects as well
        in = new ObjectInputStream(socket.getInputStream());

        
        // TODO: Call for login GUI here (Fosa)
        
        /// SIMULATED LOGIN
        controller.login("test", "test"); // This will be called from GUI
        
        // 
        // List of Message objects
        //List<Message> messages = new ArrayList<>();
       // messages.add(new Message("This is a test message!"));




        
        
        System.out.println("Closing socket");
        socket.close();
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
    
    public ArrayList<Message> getAccts(String user){
    	
    }
}


