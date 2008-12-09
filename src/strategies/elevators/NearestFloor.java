package strategies.elevators;

import java.util.ArrayList;
import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

public class NearestFloor extends ElevatorStrategy {

	public NearestFloor() {
		super();
	}

	public NearestFloor(Elevator elevator) {
		super(elevator);
	}

	@Override
	public String getName() {
		return "NearestFloor : Comportement qui choisit un Žtage (ou il y a quelque chose ˆ faire) au hasard.";
	}

	@Override
	public Class getType() {
		return ElevatorStrategy.class;
	}
	
	@Override
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
			elevator.incrementStoppedTime(1);
			if((elevator.getStoppedTime() > elevator.getStopTime()) || must_leave_now) elevator.leaveThisFloor();

			// Etage au hazard
			ArrayList<Integer> tabWaiting = elevator.getBuilding().getFloorWithWaitingPassengers();
			ArrayList<Integer> tabWanted = elevator.getFloorWithWaitingToGoOut();

			// Mix des deux listes
			for (Integer i : tabWaiting) {
				if(!tabWanted.contains(i)){
					tabWanted.add(i);
				}
			}
			
			if(!tabWanted.isEmpty()){
				int nearestFloor = Integer.MAX_VALUE;
				for (Integer floor : tabWanted) {
					if(Math.sqrt((elevator.getCurrentFloor() - floor)*(elevator.getCurrentFloor() - floor)) < nearestFloor) nearestFloor = floor;
				}
System.out.println("nearestFloor java : "+nearestFloor);
				if(nearestFloor < elevator.getCurrentFloor()){
					elevator.setGoingToTop(false);
				}else{
					elevator.setGoingToTop(true);
				}
				elevator.setTargetFloor(nearestFloor);
			}else {
				elevator.setMoving(false);
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