package dev;

public class LoginMessage extends Message {
	private final String username;
	private final String password;
	
	public LoginMessage(msgType type, Status status, String username, String password) {
		super(type,status);
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
