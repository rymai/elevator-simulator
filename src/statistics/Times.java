package statistics;

import java.util.ArrayList;

public class Times {

	private ArrayList<Long> waitingTimes;
	private ArrayList<Long> tripTimes;
	
	public Times() {
		waitingTimes = new ArrayList<Long>();
		tripTimes = new ArrayList<Long>();
	}
	
	public void resetWaitingTimes() {
		waitingTimes.clear();
	}
	
	public void resetTripTimes() {
		tripTimes.clear();
	}
	
	public void addWaitingTime(long l) {
		waitingTimes.add(l);
	}
	
	public void addTripTime(long l) {
		tripTimes.add(l);
	}
	
	public float averageWaitingTime() {
		float sum = 0;
		for (Long i : waitingTimes) {
			sum += i;
		}
		return sum / waitingTimes.size();
	}
	
	public float averageTripTime() {
		float sum = 0;
		for (Long i : tripTimes) {
			sum += i;
		}
		return sum / tripTimes.size();
	}
	
}