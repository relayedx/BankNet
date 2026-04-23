import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class DatabaseManagerTester {
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAvgRoll() {
		int sides = 17;
		//Die die1 = new Die(sides);
		int total = 0;
		for (int i=0;i<500;i++) {
			//total += die1.roll();
		}
		int average = total/500;
		
		System.out.println("Average: " + average);
		
		assertTrue(average > (sides/2)-1 && average < (sides/2)+1);
	}
}
