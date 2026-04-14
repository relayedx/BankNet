
public class MsgTest {
	public static void main() {
		LoginMessage msg = new LoginMessage(msgType.LOGIN_REQUEST,Status.IN_PROGRESS,"test","pass");
		System.out.println(msg.getUser());
	}
}
