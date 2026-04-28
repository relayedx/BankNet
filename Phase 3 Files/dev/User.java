package dev;
import java.util.List;

public class User {
	private String username;
	private String password;
	private UserInfo info;
	private Boolean isTeller;
	private List<Integer> authAcctIDs;
	private Boolean isLoggedIn; // probably dont need this attribute since...
	// ...we handle and track loggedIn users from clientHandler
	
	public User(String username, String password, UserInfo info, Boolean isTeller, List<Integer> authAcctIDs, Boolean isLoggedIn) {
		this.username = username;
		this.password = password;
		this.info = info;
		this.isTeller = isTeller;
		this.authAcctIDs = authAcctIDs;
		this.isLoggedIn = isLoggedIn;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public UserInfo getUserInfo() {
		return info;
	}
	
	public Boolean getRole() {
		return isTeller;
	}
	
	public List<Integer> getAuthAcctIDs() {
		return authAcctIDs;
	}
	
	public void addAuthAcct(int id) {
		authAcctIDs.add(id);
	}
}
