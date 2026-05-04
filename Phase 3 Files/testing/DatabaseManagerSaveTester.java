package testing;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.AcctType;
import dev.BankAcct;
import dev.DatabaseManager;
import dev.TranType;
import dev.Transaction;
import dev.User;
import dev.UserInfo;

class DatabaseManagerSaveTester {
	private DatabaseManager database;
	
	// user 1 fields
	private String username1 = "user1";
	private String password1 = "pass1";
	private boolean isTeller1 = false;
	private List<Integer> authAcctIDs1 = new ArrayList<Integer>() {
		{
			add(0);
			add(2);
			add(6);
		}
	};
	private boolean isLoggedIn1 = true;
	
	// user info 1 fields
	private String firstName1 = "first1";
	private String lastName1 = "last1";
	private String address1 = "address1";
	private LocalDate dob1 = LocalDate.of(2026, 1, 1);
	private String phone1 = "phone1";
	
	private UserInfo info1 = new UserInfo(firstName1, lastName1, address1, dob1, phone1);
	private User user1 = new User(username1, password1, info1, isTeller1, authAcctIDs1, isLoggedIn1);
	
	// user 2 fields
	private String username2 = "user2";
	private String password2 = "pass2";
	private boolean isTeller2 = false;
	private List<Integer> authAcctIDs2 = new ArrayList<Integer>() {
		{
			add(1);
			add(3);
			add(5);
		}
	};
	private boolean isLoggedIn2 = false;
	
	// user info 2 fields
	private String firstName2 = "first2";
	private String lastName2 = "last2";
	private String address2 = "address2";
	private LocalDate dob2 = LocalDate.of(2026, 2, 2);
	private String phone2 = "phone2";
	
	private UserInfo info2 = new UserInfo(firstName2, lastName2, address2, dob2, phone2);
	private User user2 = new User(username2, password2, info2, isTeller2, authAcctIDs2, isLoggedIn2);
	
	// account 1 fields
	private int acctID = 0;
	private User owner = user1;
	private AcctType type = AcctType.Checking;
	private List<User> authUser = new ArrayList<User>() {
		{
			add(user2);
		}
	};
	private float balance = 100f;
	private float creditLine = 0f;
	private float availCredit = 0f;
	private LocalDate dueDate = LocalDate.of(2026, 4, 22);
	private boolean frozen = false;
	private boolean closed = false;
	private List<Transaction> trans = new ArrayList<Transaction>() {
		{
			add(new Transaction(0, LocalDateTime.of(2026, 3, 25, 13, 3, 27), username1, 116.18f, TranType.DEPOSIT));
			add(new Transaction(1, LocalDateTime.of(2026, 4, 14, 10, 15, 30), username1, 184.27f, TranType.WITHDRAWAL));
			add(new Transaction(2, LocalDateTime.of(2026, 4, 24, 0, 0, 0), username2, 7.00f, TranType.SYSTEM));
		}
	};
	
	BankAcct account = new BankAcct(acctID, owner, type, authUser,
			balance, creditLine, availCredit, dueDate, frozen, closed, trans);

	@BeforeEach
	public void setUp() throws Exception {
		database = new DatabaseManager("db\\TestAllUsers.txt", "db\\TestAllAccounts.txt",
									   "db\\TestUsers\\", "db\\TestAccounts\\");
	}
	
	@Test
	void testAddUser() {		
		database.addUser(user1);
		database.loadData();
		
		assertAll(
			"Adding User",
			// does user match with user added in the database
			() -> assertTrue(user1.equals(database.getUser(username1))),
			// does user info match with user info added in the database
			() -> assertTrue(info1.equals(database.getUser(username1).getUserInfo()))
		);
	}
	
	@Test
	void testAddAccount() {
		// BankAcct account = new BankAcct(AcctType.Checking, user1, database.getBankCount());
		
		database.addUser(user1);
		database.addAccount(account);
		database.loadData();
		//BankAcct account = new BankAcct(acctID, owner, type, authUser,
			//	balance, creditLine, availCredit, dueDate, frozen, closed, trans);
		
		assertAll(
			"Adding Account",
			// does account match with account added in the database
			() -> assertTrue(account.equals(database.getAccount(account.getAcctID()))),
			() -> assertEquals(trans, database.getAccount(account.getAcctID()).getTrans())
			
			/*() -> assertEquals(acctID, database.getAccount(acctID).getAcctID()),
			() -> assertTrue(user1.equals(database.getAccount(acctID).getOwner())),
			() -> assertEquals(type, database.getAccount(acctID).getType())
			
			() -> assertEquals(username1, database.getUser(username1).getUsername()),
			() -> assertEquals(password1, database.getUser(username1).getPassword()),
			() -> assertEquals(isTeller1, database.getUser(username1).getRole()),
			() -> assertEquals(authAcctIDs1, database.getUser(username1).getAuthAcctIDs()),
			// does user info match with user info added in the database
			() -> assertEquals(firstName1, database.getUser(username1).getUserInfo().getFirstName()),
			() -> assertEquals(lastName1, database.getUser(username1).getUserInfo().getLastName()),
			() -> assertEquals(address1, database.getUser(username1).getUserInfo().getAddress()),
			() -> assertEquals(dob1, database.getUser(username1).getUserInfo().getDOB()),
			() -> assertEquals(phone1, database.getUser(username1).getUserInfo().getPhone())*/
		);
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
