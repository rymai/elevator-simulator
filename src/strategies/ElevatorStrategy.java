package strategies;

import models.Elevator;
import models.Passenger;

public abstract class ElevatorStrategy {

	protected Elevator elevator;
	private int stopTime = 0;
	private int stoppedTime = 0;

	public ElevatorStrategy() {
	}
	
	public ElevatorStrategy(Elevator elevator) {
		this.elevator = elevator;
	}

	public abstract void acts();

	public abstract boolean takePassenger(Passenger passenger);

	public abstract void releasePassenger(Passenger passenger);
	
	
	public Elevator getElevator() {
		return elevator;
	}
	
	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}

	public int getStoppedTime() {
		return stoppedTime;
	}

	public void setStoppedTime(int stoppedTime) {
		this.stoppedTime = stoppedTime;
	}
	
	public void incrementStopTime() {
		this.stopTime += 5;
	}
	public void incrementStoppedTime() {
		this.stoppedTime++;
	}
	
	public synchronized void releaseAllArrivedPassengers() {
		for (int i = 0; i < elevator.getPassengerCount(); i++) {
			if(elevator.getPassengers().get(i).getWantedFloor() == elevator.getCurrentFloor()) {
				elevator.releasePassenger(elevator.getPassengers().get(i));
			}
		}
		
//		for (Passenger passenger : elevator.getPassengers()) {
//			if(passenger.getWantedFloor() == elevator.getCurrentFloor()) {
//				elevator.releasePassenger(passenger);
//			}
//		}
	}
	
}