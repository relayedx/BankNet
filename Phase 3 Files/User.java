import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private Boolean isTeller;
	// private UserInfo Info; <-- UserInfo class needs to be made
	private List<Integer> authAcctIDs;
	private Boolean isLoggedIn; // probably dont need this attribute since...
	// ...we handle and track loggedIn users from clientHandler
	
	User(String username, String password, Boolean isTeller, List<Integer> authAcctIDs, Boolean isLoggedIn) {
		this.username = username;
		this.password = password;
		this.isTeller = isTeller;
		this.authAcctIDs = authAcctIDs;
		this.isLoggedIn = isLoggedIn;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Boolean getRole() {
		return isTeller;
	}
	
}
