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
	
	@Override
	public String getName() {
		return "Comportement lineaire sans optimisation des durees d'attente.";
	}

	@Override
	public Class getType() {
		return ElevatorStrategy.class;
	}
	
	@Override
	public void acts() {
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
			elevator.incrementStoppedTime(1);
			if((elevator.getStoppedTime() > elevator.getStopTime()) || must_leave_now) elevator.leaveThisFloor();
			
			if((elevator.isGoingToTop() && elevator.atTop()) || (!elevator.isGoingToTop() && elevator.atBottom()) || (elevator.noCallOnTheWay())) {
				elevator.changeDirection(); // Changement de sens pour le prochain mouvement
			}
		}
		else {
			elevator.setMoving(false);
		}
	}

	@Override
	public boolean takePassenger(Passenger passenger) {
		elevator.incrementStopTime(5);
		return true;
	}

	@Override
	public void releasePassenger(Passenger passenger) {
		elevator.incrementStopTime(5);
		Console.debug("Un passager descend. "+passenger.getCurrentFloor()+" -> "+passenger.getWantedFloor()+" "+passenger.isInTheElevator()+" "+passenger.isArrived());
	}

	@Override
	public void leaveThisFloor() {
	}

}