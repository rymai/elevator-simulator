/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strategies.elevators;

import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

/**
 *
 * @author x_nem
 */

/*Manoeuvre à blocage:
Aucune régulation, l’ascenseur se déplace directement vers l’étage appelé en 1er, 
puis en direction de l’unique étage de destination, sans aucun arrêt intermédiaire. 
Ce mode de fonctionnement est très consommateur d’énergie et n’est utilisé que sur les 
ascenseurs mono-vitesse.*/

public class OperateWithBlocking  implements ElevatorStrategy {
	
	private Elevator elevator;
	
	private boolean hasAlreadyTakeAPerson = false;
	
	private int stopTime = 0;
	public void incrementStopTime() {
		this.stopTime+=10;
	}

	private int stoppedTime = 0;
	
	public OperateWithBlocking() {
	}
	
	public OperateWithBlocking(Elevator elevator) {
		this.elevator = elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public void acts() {
		Console.debug("\tStrategy Operate With Blocking - Manoeuvre a blocage");
		
                //if the elevator is not call at all
		if(!elevator.noCallAtAll()) {
			Console.debug_variable("\tstopTime", stopTime);
			Console.debug_variable("\tstoppedTime", stoppedTime);
			// On est arrive a un etage, on ouvre les portes car cet ascenseur s'arrete a chaque etage appele sur sa route!
			
                        if((elevator.isThereCallsAtThisFloor()&&(!hasAlreadyTakeAPerson))) {
				Console.debug("\tStop => "+elevator.getCurrentFloor());
				elevator.resetCurrentFloorCalls();
				elevator.setChangedAndNotifiyObservers();
				elevator.getView().refresh();
				elevator.getView().refreshFloor(elevator.getCurrentFloor());
				hasAlreadyTakeAPerson = true;
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
			for (int i = elevator.getBuilding().getAskedFloors().size()-1; i >= 0; i--) {
				demande += i+" -> "+elevator.getBuilding().getAskedFloors().get(i)+"\n";
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
                hasAlreadyTakeAPerson = false;
		if(index != -1) {
			elevator.getPassengers().remove(index);
			elevator.removeOfCurrentWeight(passenger.getTotalMass());
			incrementStopTime();
		}
                
		else passenger = null;
		return passenger;
	}
	
}