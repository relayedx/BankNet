package dev;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
	// private static int count = 0;
	private final int id;
	private final LocalDateTime date;
	private final String user;
	private final float amount;
	private final TranType tranType;
	
	// added constructor to build Transaction objects during start up
	public Transaction(int id, LocalDateTime date, String user, float amt, TranType type) {
		this.id = id; // ++count;
		this.date = date;
		this.user = user;
		amount = amt;
		tranType = type;
	}
	
	public Transaction(int id, String user, float amt, TranType type) {
		this.id = id; // ++count;
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
		String temp = "" + date + "|"+ user + "|" + amount + "|" + tranType;
		return temp;
	}
	
}
