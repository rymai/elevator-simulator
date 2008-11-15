package strategies.elevators;

import main.Console;
import models.Elevator;
import strategies.Strategy;

public class Linear extends Strategy {

	private int stopTime;
	
	public Linear() {
		stopTime = 0;
	}
	
	public void acts(Elevator elevator) {
//		Console.debug("Strategy Linear");
//		Console.debug("A du boulot ? : "+!elevator.noCallAtAll());
		if(!elevator.noCallAtAll()) {
			// On est arrivé a un étage, on ouvre les portes car cet ascenseur s'arrete a chaque etage appele sur sa route!
			if(elevator.isAtAFloor() && elevator.isThereCallsAtThisFloor() && stopTime < 3) {
				Console.debug("Arret a l'etage "+elevator.getCurrentFloor());
				elevator.resetCurrentFloor();
				elevator.setChangedAndNotifiyObservers();
				Console.debug("stopTime : "+stopTime);
				stopTime++;
			}
			else {
				stopTime = 0;
				// The elevator moves
				elevator.setCurrentPosition(elevator.getCurrentPosition()+elevator.getStep()*elevator.getSpeedPerRound());
			}

//			Console.debug("Dois redescendre ? : "+(elevator.getGoingToTop() && elevator.atTop()));
//			Console.debug("Dois remonter ? : "+(!elevator.getGoingToTop() && elevator.atBottom()));
//			Console.debug("Aucun appel sur la voie ? : "+elevator.noCallOnTheWay());
			if((elevator.getGoingToTop() && elevator.atTop()) || (!elevator.getGoingToTop() && elevator.atBottom()) || elevator.noCallOnTheWay()) {
				elevator.changeDirection(); // Changement de sens!
			}

			if(elevator.isBlocked()) {
				Console.debug("L'ascenseur "+elevator.getIdentifier()+" est bloqué à l'étage "+elevator.getCurrentFloor());
				do {
					elevator.releasePassenger(elevator.getLastPassenger());
					Console.debug("Je vire un passager... |"+elevator.getPassengerCount()+"|");
				} while(elevator.isBlocked());
			}
			
			Console.debug("|"+elevator.getPassengerCount()+"|");

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
	}
	
}