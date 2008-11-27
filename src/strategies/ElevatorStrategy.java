package strategies;

import models.Elevator;
import models.Passenger;

public interface ElevatorStrategy {

	public void acts();

	public boolean takePassenger(Passenger passenger);

	public void setElevator(Elevator elevator);

	public Passenger releasePassenger(Passenger passenger);
	
}