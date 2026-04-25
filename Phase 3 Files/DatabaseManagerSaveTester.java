import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DatabaseManagerSaveTester {
DatabaseManager database;
	@BeforeEach
	public void setUp() throws Exception {
		database = new DatabaseManager("db\\TestAllUsers.txt", "db\\TestAllAccounts.txt",
									   "db\\TestUsers\\", "db\\TestAccounts\\");
	}
	
	@Test
	void testAddUser() {
		// user 1 fields
		String username = "user1";
		String password = "pass1";
		boolean isTeller = false;
		List<Integer> authAcctIDs = new ArrayList<Integer>() {
			{
				add(0);
				add(2);
				add(6);
			}
		};
		String firstName = "test";
		String lastName = "1";
		String address = "address";
		LocalDate dob = LocalDate.now();
		String phone = "phone";
		
		
		UserInfo info = new UserInfo(firstName, lastName, address, dob, phone);
		User user = new User(username, password, info, isTeller, authAcctIDs);
		
		database.addUser(user);
		database.loadData();
		
		assertAll(
			"Adding User",
			// does user match with user added in the database
			() -> assertEquals(username, database.getUser(username).getUsername()),
			() -> assertEquals(password, database.getUser(username).getPassword()),
			() -> assertEquals(isTeller, database.getUser(username).getRole()),
			() -> assertEquals(authAcctIDs, database.getUser(username).getAuthAccts()),
			// does user info match with user info added in the database
			() -> assertEquals(firstName, database.getUser(username).getUserInfo().getFirstName()),
			() -> assertEquals(lastName, database.getUser(username).getUserInfo().getLastName()),
			() -> assertEquals(address, database.getUser(username).getUserInfo().getAddress()),
			() -> assertEquals(dob, database.getUser(username).getUserInfo().getDOB()),
			() -> assertEquals(phone, database.getUser(username).getUserInfo().getPhone())
		);
	}
	
	@Disabled
	@Test
	void testAddAccount() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testAddDeposit() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testAddWithdraw() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testAddAuthUser() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testRemoveAuthUser() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testGetAccount() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testGetUser() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testGetUserAllAccts() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testGetUsers() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testUpdateName() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testUpdateAddress() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testUpdateDOB() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testUpdatePhone() {
		fail("Not yet implemented");
	}
	
	@Disabled
	@Test
	void testUpdatePassword() {
		fail("Not yet implemented");
	}

}
