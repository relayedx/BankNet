import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// delete the test files for the best effect
class DatabaseManagerSaveTester {
DatabaseManager database;
	@BeforeEach
	public void setUp() throws Exception {
		database = new DatabaseManager("db\\TestAllUsers.txt", "db\\TestAllAccounts.txt",
									   "db\\TestUsers\\", "db\\TestAccounts\\");
	}

	@Test
	void testAddAccount() {
		fail("Not yet implemented");
	}
	
	@Test
	void testAddAuthUser() {
		fail("Not yet implemented");
	}
	
	@Test
	void testAddDeposit() {
		fail("Not yet implemented");
	}
	
	@Test
	void testAddUser() {
		fail("Not yet implemented");
	}
	
	@Test
	void testAddWithdraw() {
		fail("Not yet implemented");
	}
	
	@Test
	void testGetAccount() {
		fail("Not yet implemented");
	}
	
	@Test
	void testGetUser() {
		fail("Not yet implemented");
	}
	
	@Test
	void testGetUserAllAccts() {
		fail("Not yet implemented");
	}
	
	@Test
	void testGetUsers() {
		fail("Not yet implemented");
	}
	
	@Test
	void testRemove() {
		fail("Not yet implemented");
	}

}
