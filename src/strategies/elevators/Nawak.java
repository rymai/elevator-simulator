package strategies.elevators;

import java.util.ArrayList;
import java.util.Random;

import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

public class Nawak extends ElevatorStrategy {

	public Nawak() {
		super();
	}

	public Nawak(Elevator elevator) {
		super(elevator);
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
			int floor;
			Random r = new Random();
			if(!tabWanted.isEmpty()){
				floor = tabWanted.get(r.nextInt(tabWanted.size()));

			//int random=0;

			//System.out.println(numberWaiting);

			System.out.println("Random :" +floor);

			if(floor < elevator.getCurrentFloor()){
				elevator.setGoingToTop(false);
			}else{
				elevator.setGoingToTop(true);
			}
			elevator.setTargetFloor(floor);
		}else {
			elevator.setMoving(false);
		}
		/*	if((elevator.isGoingToTop() && elevator.atTop()) || (!elevator.isGoingToTop() && elevator.atBottom())) {
				elevator.changeDirection(); // Changement de sens pour le prochain mouvement
			} */
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