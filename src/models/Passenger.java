package models;
import java.util.Observer;

import main.Console;
import controllers.MainController;
import views.PassengerView;

public abstract class Passenger implements Observer {
	
	protected MainController controller;
	
	// Compteur perso
	protected long beginTime;
	
	protected Elevator elevator;
	
	protected PassengerView view;
	protected int currentFloor;
	protected int wantedFloor;
	
	protected boolean elevatorCalled;
	protected boolean inTheElevator;
	
	public Passenger(int current_floor, int  wanted_floor, MainController controller) {
		this.controller = controller;
//		this.setElevator(elevator);
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
		if(!isArrived()) {
			if(!elevatorCalled || getTime() > 8) {
				askForElevator();
			}
		}
	}

	private void askForElevator() {
		if(inTheElevator) elevatorCalled = this.controller.getBuilding().callElevator(wantedFloor);
		else elevatorCalled = this.controller.getBuilding().callElevator(currentFloor);
		if(elevatorCalled) beginTime = System.currentTimeMillis();
//		Console.debug("Demande ascenseur "+elevator.getIdentifier()+" (Žtage courant "+currentFloor+" -> "+wantedFloor+")");
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

	public long getTime() { 
		 return (System.currentTimeMillis() - beginTime) / 1000;  
	} 
	
	public abstract void actsAfterEnteredTheElevator();
	public abstract void actsAfterBeRejectedFromElevator();

}