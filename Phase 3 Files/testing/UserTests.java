package testing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.User;
import dev.UserInfo;

class UserTests {
	private User user;
	
	private String username = "user";
	private String password = "pass";
	private UserInfo info = new UserInfo("first", "last", "add", LocalDate.now(), "phone");
	private Boolean isTeller = false;
	private List<Integer> authAcctIDs = new ArrayList<Integer>() {
		{
			add(1);
			add(2);
			add(3);
		}
	};
	private Boolean isLoggedIn = false;
	
	@BeforeEach
	public void setUp() {
		user = new User(username, password, info, isTeller, authAcctIDs, isLoggedIn);
	}
	
	@Test
	public void testConstructor() {
		User newUser = new User("shadow05", "iamallofme", null, true, null, true);
		Assertions.assertNotNull(newUser);
	}

	@Test
	public void testGetUsername() {
		Assertions.assertTrue(username.equals(user.getUsername()));
	}
	
	@Test
	public void testGetPassword() {
		Assertions.assertTrue(password.equals(user.getPassword()));
	}
	
	@Test
	public void testGetUserInfo() {
		Assertions.assertSame(info, user.getUserInfo());
	}
	
	@Test
	public void testGetRole() {
		Assertions.assertTrue(isTeller.equals(user.getRole()));
	}
	
	@Test
	public void testGetAuthAcctIDs() {
		Assertions.assertTrue(authAcctIDs.equals(user.getAuthAcctIDs()));
	}
	
	@Test
	public void testGetIsLoggedIn() {
		Assertions.assertTrue(isLoggedIn.equals(user.getIsLoggedIn()));
	}
	
	@Test
	public void testSetPassword() {
		String newPassword = "newPass";
		user.setPassword(newPassword);
		Assertions.assertTrue(newPassword.equals(user.getPassword()));
	}
	
	@Test
	public void testSetIsLoggedIn() {
		boolean newIsLoggedIn = true;
		user.setIsLoggedIn(newIsLoggedIn);
		Assertions.assertTrue(newIsLoggedIn == user.getIsLoggedIn());
	}
	
	@Test
	public void testAddAuthAcct() {
		int newAuthAcct = 4;
		user.addAuthAcct(newAuthAcct);
		Assertions.assertTrue(user.getAuthAcctIDs().contains(newAuthAcct));
	}
	
}
