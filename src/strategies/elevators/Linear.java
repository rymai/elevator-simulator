package strategies.elevators;

import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

public class Linear extends ElevatorStrategy {

	public Linear() {
		super();
	}

	public Linear(Elevator elevator) {
		super(elevator);
	}

	public synchronized void acts() {
		//		System.out.println("id : "+elevator.getIdentifier());
		// Aucun appel, on ne fait rien, on est a l'arret, tout va bien
		if(!elevator.getBuilding().allPassengersAreArrived() && (elevator.getBuilding().getWaitingPassengersCount() != 0 || elevator.getPassengerCount() != 0)) {
			boolean must_leave_now = false;
			releaseAllArrivedPassengers();
			// On est arrive a un etage, si on a un ou des appels a cet etage
			// on ouvre les portes car cet ascenseur s'arrete a chaque etage appele sur sa route!
			if(!elevator.isFull()) {
				Passenger p = elevator.getBuilding().getFirstPassengerWaitingAtFloor(elevator.getCurrentFloor());
				if(p != null) {
					if(!((elevator.getPassengerCount()+p.getPersonCount()) > elevator.getMaxPersons())) {
						must_leave_now = !p.canEnterElevator(elevator);
					}
				}
			}
			elevator.setMoving(false);
			incrementStoppedTime();
			if((getStoppedTime() > getStopTime()) || must_leave_now) leaveThisFloor();
			
			if((elevator.goingToTop() && elevator.atTop()) || (!elevator.goingToTop() && elevator.atBottom())) {
				elevator.changeDirection(); // Changement de sens pour le prochain mouvement
			}
		}
		else {
			elevator.setMoving(false);
		}
	}



	public boolean takePassenger(Passenger passenger) {
		elevator.addToCurrentWeight(passenger.getTotalMass());

		if(elevator.isBlocked()) {
			elevator.removeOfCurrentWeight(passenger.getTotalMass());
			return false;
		}
		else {
			elevator.getPassengers().add(passenger);
			passenger.setElevator(elevator);
			incrementStopTime();
			Console.debug("Un passager monte. "+passenger.getCurrentFloor()+" -> "+passenger.getWantedFloor());
			return true;
		}
	}

	public void releasePassenger(Passenger passenger) {
		incrementStopTime();
		Console.debug("Un passager descend. "+passenger.getCurrentFloor()+" -> "+passenger.getWantedFloor()+" "+passenger.isInTheElevator()+" "+passenger.isArrived());

	}

	private void leaveThisFloor() {
		setStopTime(0);
		setStoppedTime(0);
		// The elevator moves
		elevator.setMoving(true);
	}

}