
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

	static ObjectOutputStream out; 
	static ObjectInputStream in;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
        System.out.print("Enter the port number to connect to: <1234>");
        int port = sc.nextInt();
        System.out.print("Enter the host address to connect to: <localhost> ");
        String host = sc.next();
        
        Client ref = new Client(); 
        // Connect to the ServerSocket at host:port
        Socket socket = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);

        // Output stream socket.
        OutputStream outputStream = socket.getOutputStream();

        // Create object output stream from the output stream to send an object through it
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        // Create a input stream to read these objects as well
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // List of Message objects
        //List<Message> messages = new ArrayList<>();
       // messages.add(new Message("This is a test message!"));


        System.out.println("Sending Message Object");
        out.writeObject(new LoginMessage(msgType.LOGIN_REQUEST, Status.IN_PROGRESS, "user","pass"));
        
		Message msg = (Message) in.readObject();
		System.out.println(msg.getStatus() + " " + msg.getType());

        
        
        System.out.println("Closing socket");
        socket.close();
    }
}


