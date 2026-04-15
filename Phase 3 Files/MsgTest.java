import java.time.LocalDateTime;

public class MsgTest {
	public static void main(String[] args) {
		LocalDateTime test = LocalDateTime.now();
		System.out.println(test.getHour() + ":" + test.getMinute());
		String well = "100@help me@well";
		System.out.println(well.replaceAll("@", ","));
		
		LocalDateTime help = LocalDateTime.parse(test.toString());
		System.out.println(help.getHour() + ":" + help.getMinute());
		Transaction l = new Transaction("me",10,TranType.SYSTEM);
		System.out.println(l);
		
		
	}
}
