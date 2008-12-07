package models;

import controllers.MainController;

public abstract class Passenger {
	
	protected MainController controller;
	
	// Compteur perso
	protected long beginTime;
	protected Elevator elevator;
	protected int currentFloor;
	protected int wantedFloor;
	
	public Passenger(int current_floor, int  wanted_floor, MainController controller) {
		this.controller = controller;
		this.currentFloor = current_floor%this.controller.getBuilding().getFloorCountWithGround();
		this.wantedFloor = wanted_floor%this.controller.getBuilding().getFloorCountWithGround();
		this.elevator = null;
		this.beginTime = System.currentTimeMillis();
	}
	
	public abstract int getTotalMass();
	public abstract int getPersonCount();
	public abstract boolean canEnterElevator(Elevator elevator);
	
	public Elevator getElevator() {
		return elevator;
	}
	// Definit dans quel ascenseur l'individu se trouve
	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public boolean isArrived() {
		return (wantedFloor == currentFloor) && !isInTheElevator();
	}
	
	public boolean isInTheElevator() {
		return elevator != null;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	public void setCurrentFloor(int floor) {
		currentFloor = floor;
	}

	public long waitingTime() { 
		 return (System.currentTimeMillis() - beginTime) / 1000;  
	}

	public int getWantedFloor() {
		return wantedFloor;
	}

	public boolean isWaitingAtFloor(int floor) {
		return !isArrived() && !isInTheElevator() && currentFloor == floor;
	}
	
	public boolean isArrivedAtFloor(int floor) {
		return isArrived() && currentFloor == floor;
	}

}