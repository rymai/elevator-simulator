package statistics;

import java.util.ArrayList;

public class WaitingTime {

	private static ArrayList<Long> waitingTimes = new ArrayList<Long>();
	
	public static void addTime(long l) {
		waitingTimes.add(l);
	}
	
	public static float average() {
		float sum = 0;
		for (Long i : waitingTimes) {
			sum += i;
		}
		return sum / waitingTimes.size();
	}
	
}
