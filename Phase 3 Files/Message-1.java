import java.io.Serializable;

enum Status{
	SUCCESS,
	IN_PROGRESS,
	ERROR
}

enum msgType{
	DEPOSIT_REQUEST,
	WITHDRAWAL_REQUEST,
	LOGIN_REQUEST,
	ACCOUNTS_REQUEST
}
// must implement Serializable in order to be sent
public class Message implements Serializable{
    private final int id;
    private final String text;
    private final String type;

    public Message(String text) {
        this.text = text;
        this.type = "default";
    }

    public String getText() {
        return text;
    }

    public int getID(){
        return id;
    }

    public String getType(){
        return type;
    }
}

