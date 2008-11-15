package strategies.elevators;

import main.Console;
import models.Elevator;
import strategies.Strategy;

public class Linear extends Strategy {

	public void acts(Elevator elevator) {
//		Console.debug("Strategy Linear");
//		Console.debug("A du boulot ? : "+!elevator.noCallAtAll());
		if(!elevator.noCallAtAll()) {
			// On est arrivŽ a un Žtage, on ouvre les portes car cet ascenseur s'arrete a chaque etage appele sur sa route!
			if(elevator.isAtAFloor() && elevator.isThereCallsAtThisFloor()) {
				Console.debug("Arret a l'etage "+elevator.getCurrentFloor());
				elevator.resetCurrentFloor();
				elevator.setChangedAndNotifiyObservers();
			}

//			Console.debug("Dois redescendre ? : "+(elevator.getGoingToTop() && elevator.atTop()));
//			Console.debug("Dois remonter ? : "+(!elevator.getGoingToTop() && elevator.atBottom()));
//			Console.debug("Aucun appel sur la voie ? : "+elevator.noCallOnTheWay());
			if((elevator.getGoingToTop() && elevator.atTop()) || (!elevator.getGoingToTop() && elevator.atBottom()) || elevator.noCallOnTheWay()) {
				elevator.changeDirection(); // Changement de sens!
			}

			if(elevator.isBlocked()) {
				Console.debug("L'ascenseur "+elevator.getIdentifier()+" est bloquŽ ˆ l'Žtage "+elevator.getCurrentFloor());
				do {
					elevator.releasePassenger(elevator.getLastPassenger());
					Console.debug("Je vire un passager... |"+elevator.getPassengerCount()+"|");
				} while(elevator.isBlocked());
			}

			// The elevator moves
			elevator.setCurrentPosition(elevator.getCurrentPosition()+elevator.getStep()*elevator.getSpeedPerRound());
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