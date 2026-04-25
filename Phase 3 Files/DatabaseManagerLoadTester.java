import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// make sure the test files are in tact and formatted correctly
// if not, run the save tests first
public class DatabaseManagerLoadTester {
	DatabaseManager database;
	
	@BeforeEach
	public void setUp() throws Exception {
		database = new DatabaseManager();
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testLoadData() {
		database.loadData("db\\TestAllUsers.txt", "db\\TestAllAccounts.txt",
				"db\\TestUsers\\", "db\\TestAccounts\\");
		
		fail("Not yet implemented");
	}
}
