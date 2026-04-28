package dev;
import java.time.LocalDate;

public class UserInfo {
	private String firstName;
	private String lastName;
	private String address;
	private LocalDate dob;
	private String phoneNum;
	
	public UserInfo(String first, String last, String add, LocalDate dob, String num) {
		this.firstName = first;
		this.lastName = last;
		this.address = add;
		this.dob = dob;
		this.phoneNum = num;
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
	
	public void setDOB(LocalDate newDOB) {
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
	
	public LocalDate getDOB() {
		return dob;
	}
	
	public String getPhone() {
		return phoneNum;
	}
}
