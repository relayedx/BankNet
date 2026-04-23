import java.time.LocalDateTime;

public class UserInfo {
	private String firstName;
	private String lastName;
	private String address;
	private LocalDateTime dob;
	private String phoneNum;
	
	UserInfo(String first, String last, String add, String dob, String num) {
		// format established in transaction MM/dd/yy HH:mm
		
	}
	
	public void setFirstName(String newFirst) {
		this.firstName = newFirst;
	}
	
	public void setLastName(String newLast) {
		this.lastName = newLast;
	}
	
	public void setAddress(String newAddress) {
		this.address = newAddress;
	}
	
	public void setDOB(LocalDateTime newDOB) {
		this.dob = newDOB;
	}
	
	public void setPhone(String newPhone) {
		this.phoneNum = newPhone;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public LocalDateTime getDOB() {
		return dob;
	}
	
	public String getPhone() {
		return phoneNum;
	}
}
