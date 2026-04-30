package dev;
import java.util.List;

public class User {
	private String username;
	private String password;
	private UserInfo info;
	private Boolean isTeller;
	private List<Integer> authAcctIDs;
	private Boolean isLoggedIn; // prevents concurrent log ins from happening
	
	public User(String username, String password, UserInfo info, Boolean isTeller, List<Integer> authAcctIDs, Boolean isLoggedIn) {
		this.username = username;
		this.password = password;
		this.info = info;
		this.isTeller = isTeller;
		this.authAcctIDs = authAcctIDs;
		this.isLoggedIn = isLoggedIn;
	}
	
	/// SETTTERS
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setIsLoggedIn(boolean login) {
		isLoggedIn = login;
	}
	
	/// GETTERS
	public String getUsername() {
		return username;
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
	
	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}
	
	/// OTHER METHODS
	public void addAuthAcct(int id) {
		authAcctIDs.add(id);
	}
}

