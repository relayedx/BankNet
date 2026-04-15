import java.io.*;
import java.net.*;

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
}
