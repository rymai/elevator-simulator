package models;
import java.util.Observer;

import main.Console;
import controllers.MainController;
import views.PassengerView;

public abstract class Passenger implements Observer {
	
	protected MainController controller;
	
	protected Elevator elevator;
	
	protected PassengerView view;
	protected int currentFloor;
	protected int wantedFloor;
	
	protected boolean elevatorCalled;
	protected boolean inTheElevator;
	
	public Passenger(int current_floor, int  wanted_floor, MainController controller, Elevator elevator) {
		this.controller = controller;
		this.setElevator(elevator);
		this.currentFloor = current_floor%this.controller.getBuilding().getFloorCount();
		this.wantedFloor = wanted_floor%this.controller.getBuilding().getFloorCount();
		elevatorCalled = false;
	}
	
	public abstract int getTotalMass();
	public abstract int getPersonCount();
	
	// Definit devant quel ascenseur l'individu attend
	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
		elevator.addObserver(this);
	}

	public void acts() {
		if(!elevatorCalled && !isArrived()) {
			askForElevator();
		}
	}

	private void askForElevator() {
		if(inTheElevator)
			elevatorCalled = elevator.call(wantedFloor);
		else
			elevatorCalled = elevator.call(currentFloor);
	}
	
	public boolean isArrived() {
		return wantedFloor == Integer.MAX_VALUE;
	}
	
	public boolean isInTheElevator() {
		return inTheElevator;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public abstract void actsAfterEnteredTheElevator();
	public abstract void actsAfterBeRejectedFromElevator();

}