// stole from client server dev branch (to try and avoid future conflicts)
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum TranType{
	WITHDRAWAL,
	DEPOSIT,
	SYSTEM;
	
	// added parsing method to translate from our files
	public static TranType parseTranType(String type) {
		switch(type) {
			case "WITHDRAWAL":
				return TranType.WITHDRAWAL;
			case "DEPOSIT":
				return TranType.DEPOSIT;
			case "SYSTEM":
				return TranType.SYSTEM;
			default:
		    	return null;
		}
	}
}

public class Transaction implements Serializable {
	private static int count = 0;
	private final int id;
	private final LocalDateTime date;
	private final String user;
	private final float amount;
	private final TranType tranType;
	
	// added constructor to build Transaction objects during start up
	public Transaction(LocalDateTime date, String user, float amt, TranType type) {
		this.id = ++count;
		this.date = date;
		this.user = user;
		amount = amt;
		tranType = type;
	}
	
	public Transaction(String user, float amt, TranType type) {
		this.id = ++count;
		date = LocalDateTime.now();
		this.user = user;
		amount = amt;
		tranType = type;
	}
	
	/// GETTERS / SETTERS
	public LocalDateTime getDate() {
		return date;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public String getUser() {
		return user;
	}
	
	public TranType getType() {
		return tranType;
	}
	
	public int getUID() {
		return id;
	}

	
	public String toString() {
		String temp = "" + date.format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")) + "%"+ user + "%" + amount + "%" + tranType;
		return temp;
	}
	
}