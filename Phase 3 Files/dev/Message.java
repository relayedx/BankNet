package dev;
import java.io.Serializable;



// must implement Serializable in order to be sent
public class Message implements Serializable {
	private static int count = 0;
	private final int id;
    private final msgType type;
    private final Status status;
    private final String text;

    public Message(msgType type, Status status, String text) {
    	this.id = ++count;
    	this.type = type;
    	this.status = status;
    	this.text = text;
    }
    

    public Message(msgType type, Status status) {
    	this.id = ++count;
    	this.type = type;
    	this.status = status;
    	this.text = "";
    }


    public msgType getType() {
    	return type;
    }
    
    public Status getStatus() {
    	return status;
    }
    
    public String getText() {
    	return text;
    }
    
    public int getUID() {
    	return id;
    }
}

