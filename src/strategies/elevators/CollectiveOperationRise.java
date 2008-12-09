package strategies.elevators;

import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

/**
 *
 * @author x_nem
 */
public class CollectiveOperationRise extends ElevatorStrategy {

	public CollectiveOperationRise() {
		super();
	}

	public CollectiveOperationRise(Elevator elevator) {
		super(elevator);
	}
	
	@Override
	public void acts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveThisFloor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releasePassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean takePassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return "CollectiveOperationRise";
	}

	@Override
	public Class getType() {
		return ElevatorStrategy.class;
	}

}
