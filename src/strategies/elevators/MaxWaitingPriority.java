package strategies.elevators;

import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

/**
 * 
 * @author francois
 *
 */
public class MaxWaitingPriority extends ElevatorStrategy {

	public MaxWaitingPriority() {
		super();
	}

	public MaxWaitingPriority(Elevator elevator) {
		super(elevator);
	}
	
	@Override
	public synchronized void acts() {
		elevator.setTargetFloor(elevator.getBuilding().getMaximumWaitingFloor());
		int haveToEmpty = 0;
		
		if(haveToEmpty == 0){
			if(!elevator.getBuilding().allPassengersAreArrived() && (elevator.getBuilding().getWaitingPassengersCount() != 0 || elevator.getPassengerCount() != 0)) {
				boolean must_leave_now = false;
				releaseAllArrivedPassengers();
				
					if(elevator.getCurrentFloor() == elevator.getTargetFloor()) {
						if(!elevator.isFull()) {
							Passenger p = elevator.getBuilding().getFirstPassengerWaitingAtFloor(elevator.getCurrentFloor());
							if(p != null) {
								if(!((elevator.getPassengerCount()+p.getPersonCount()) > elevator.getMaxPersons())) {
									must_leave_now = !p.canEnterElevator(elevator);
								}
							}
						}
					}
					elevator.setMoving(false);
					elevator.incrementStoppedTime(1);
					if((elevator.getStoppedTime() > elevator.getStopTime()) || must_leave_now){
						haveToEmpty = 1;
						elevator.leaveThisFloor();
					}
					else {
						elevator.setMoving(false);
						elevator.incrementStoppedTime(1);
						if((elevator.getStoppedTime() > elevator.getStopTime()) || must_leave_now){
							haveToEmpty = 1;
							elevator.leaveThisFloor();
						}
						System.out.println("entré dans le else");
						if(elevator.getCurrentFloor() > elevator.getTargetFloor()) {
							System.out.println("Descendre");
							elevator.setGoingToTop(false);
							elevator.setMoving(true);
						}else{
							System.out.println("Monter");
							elevator.setGoingToTop(true);
							elevator.setMoving(true);
						}
					}
			}
		}else{
			if(haveToEmpty == 1){ elevator.setTargetFloor(0); haveToEmpty = 2;}
			else{
				if(elevator.getCurrentFloor() == 0){
					elevator.setTargetFloor(elevator.getBuilding().getFloorCount()); haveToEmpty = 0;
				}
			}
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

	@Override
	public String getName() {
		return "L'ascenseur va se remplir ˆ l'Žtage ou il y a le plus de personne qui attendent.";
	}

	@Override
	public Class getType() {
		return ElevatorStrategy.class;
	}

}
