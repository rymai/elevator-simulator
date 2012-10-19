package strategies;

import plugin.Plugin;
import models.Elevator;
import models.Passenger;

/**
 * Classe Abstraite ElevatorStrategy
 * classe de base pour les différents comportements d'ascenseur
 * @author x_nem
 */
public abstract class ElevatorStrategy implements Plugin {

	protected Elevator elevator;
	protected boolean must_leave_now = false;

	public ElevatorStrategy() {
	}

	public ElevatorStrategy(Elevator elevator) {
		this.elevator = elevator;
	}

	public abstract void acts();
	public abstract boolean takePassenger(Passenger passenger);
	public abstract void releasePassenger(Passenger passenger);
	public abstract void leaveThisFloor();

	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}

}
