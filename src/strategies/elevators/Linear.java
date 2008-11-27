package strategies.elevators;

import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

public class Linear implements ElevatorStrategy {
	
	private Elevator elevator;
	
	private boolean hasAlreadyTakeAPerson = false;
	
	private int stopTime = 0;
	public void incrementStopTime() {
		this.stopTime++;
	}

	private int stoppedTime = 0;
	
	public Linear() {
	}
	
	public Linear(Elevator elevator) {
		this.elevator = elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public void acts() {
		Console.debug("\tStrategy Linear");
		
		if(!elevator.noCallAtAll()) {
			Console.debug_variable("\tstopTime", stopTime);
			Console.debug_variable("\tstoppedTime", stoppedTime);
			// On est arrivŽ a un Žtage, on ouvre les portes car cet ascenseur s'arrete a chaque etage appele sur sa route!
			if(elevator.isThereCallsAtThisFloor()) {
				Console.debug("\tStop => "+elevator.getCurrentFloor());
				elevator.resetCurrentFloorCalls();
				elevator.setChangedAndNotifiyObservers();
				elevator.getView().refresh();
				elevator.getView().refreshFloor(elevator.getCurrentFloor());
				hasAlreadyTakeAPerson = false;
				if(stoppedTime >= stopTime) leaveThisFloor();
			}
			else {
				leaveThisFloor();
			}
			stoppedTime++;
//			if(elevator.goingToTop() && elevator.atTop()) Console.debug("\tDois redescendre");
//			else if(!elevator.goingToTop() && elevator.atBottom()) Console.debug("\tDois remonter");
//			else Console.debug("\tPeut encore avancer");
//			Console.debug("\tAppels sur la voie ? : "+!elevator.noCallOnTheWay());
//			Console.debug("\tMonte ? : "+elevator.goingToTop());
//			Console.debug("\tEn haut ? : "+elevator.atTop());
//			Console.debug("\tEn bas ? : "+elevator.atBottom());
			
			if((elevator.goingToTop() && elevator.atTop())
				|| (!elevator.goingToTop() && elevator.atBottom())
				|| elevator.noCallOnTheWay()) {
				elevator.changeDirection(); // Changement de sens!
			}

			if(elevator.isBlocked()) {
				Console.debug("\tBlocked => "+elevator.getCurrentFloor());
				do {
					Passenger p = elevator.releasePassenger(elevator.getLastPassenger());
					p.actsAfterBeRejectedFromElevator();
					Console.debug("\tPassenger => "+p.getClass()+" : Out");
				} while(elevator.isBlocked());
				leaveThisFloor();
			}
			
			// DEBUG
			Console.debug("Demande pour l'ascenseur "+elevator.getIdentifier()+" :");
			String demande = "";
			for (int i = elevator.getAskedFloors().size()-1; i >= 0; i--) {
				demande += i+" -> "+elevator.getAskedFloors().get(i)+"\n";
			}
			Console.debug(demande);
			Console.debug("Step : "+Integer.toString(elevator.getStep()));
			Console.debug("");
			// DEBUG
		}
		else {
			elevator.setRunning(false);
		}
	}

	private void leaveThisFloor() {
		stopTime = 0;
		stoppedTime = 0;
		// The elevator moves
		if(!elevator.noCallAtAll()) elevator.moves();
	}

	public boolean takePassenger(Passenger passenger) {
		if(!hasAlreadyTakeAPerson && (elevator.getPassengerCount() < elevator.getMaxPersons())) {
			
			elevator.getPassengers().add(passenger);
			elevator.addToCurrentWeight(passenger.getTotalMass());
			incrementStopTime();
			Console.debug("Un passager monte. "+stoppedTime+"/"+stopTime);
			hasAlreadyTakeAPerson = true;
			return true;
		}
		else return false;
	}

	public Passenger releasePassenger(Passenger passenger) {
		int index = elevator.getPassengers().indexOf(passenger);
		if(index != -1) {
			elevator.getPassengers().remove(index);
			elevator.removeOfCurrentWeight(passenger.getTotalMass());
			incrementStopTime();
		}
		else passenger = null;
		return passenger;
	}
	
}