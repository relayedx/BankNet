package testing;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.UserInfo;

class UserInfoTests {
	private UserInfo info;
	
	private String firstName = "first";
	private String lastName = "last";
	private String address = "add";
	private LocalDate dob = LocalDate.now();
	private String phone = "phone";
	
	@BeforeEach
	public void setUp() {
		info = new UserInfo(firstName, lastName, address, dob, phone);
	}
	
	@Test
	public void testConstructor() {
		UserInfo newInfo = new UserInfo("Shadow", "The Hedgehog", "Space Colony ARK", LocalDate.of(2001, 6, 19), "(050)019-4946");
		Assertions.assertNotNull(newInfo);
	}

	@Test
	public void testGetFirstName() {
		Assertions.assertTrue(firstName.equals(info.getFirstName()));
	}
	
	@Test
	public void testGetLastName() {
		Assertions.assertTrue(lastName.equals(info.getLastName()));
	}
	
	@Test
	public void testGetAddress() {
		Assertions.assertTrue(address.equals(info.getAddress()));
	}
	
	@Test
	public void testGetDOB() {
		Assertions.assertTrue(dob.equals(info.getDOB()));
	}
	
	@Test
	public void testGetPhone() {
		Assertions.assertTrue(phone.equals(info.getPhone()));
	}
	
	@Test
	public void testSetFirstName() {
		String newFirst = "newFirst";
		info.setFirstName(newFirst);
		Assertions.assertTrue(newFirst.equals(info.getFirstName()));
	}
	
	@Test
	public void testSetLastName() {
		String newLast = "newLast";
		info.setLastName(newLast);
		Assertions.assertTrue(newLast.equals(info.getLastName()));
	}
	
	@Test
	public void testSetAddress() {
		String newAddress = "newAdd";
		info.setAddress(newAddress);
		Assertions.assertTrue(newAddress.equals(info.getAddress()));
	}
	
	@Test
	public void testSetDOB() {
		LocalDate newDOB = LocalDate.of(2026, 1, 1);
		info.setDOB(newDOB);
		Assertions.assertTrue(newDOB.equals(info.getDOB()));
	}
	
	@Test
	public void testSetPhone() {
		String newPhone = "newPhone";
		info.setPhone(newPhone);
		Assertions.assertTrue(newPhone.equals(info.getPhone()));
	}
	
}
