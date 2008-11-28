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
	
	
//	public void TimeVar() { 
//		 beginTime = System.currentTimeMillis(); 
//	}
//	
	public long getTime() {
		 System.out.println("CurrentTime :"+System.currentTimeMillis());
		 System.out.println("BeginTime :"+beginTime);
		 return (System.currentTimeMillis() - beginTime) / 1000;  
	} 
	
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
		System.out.print("Passenger.askForElevator ");
		if(elevatorCalled){
			beginTime = System.currentTimeMillis();
		}
		Console.debug("Demande ascenseur "+elevator.getIdentifier()+" (Žtage courant "+currentFloor+" -> "+wantedFloor+")");
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