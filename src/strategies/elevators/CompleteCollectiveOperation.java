package strategies.elevators;

import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

/**
 *
 * @author x_nem
 */
public class CompleteCollectiveOperation extends ElevatorStrategy {

	public CompleteCollectiveOperation() {
		super();
	}

	public CompleteCollectiveOperation(Elevator elevator) {
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
		return "CompleteCollectiveOperation";
	}

	@Override
	public Class getType() {
		return ElevatorStrategy.class;
	}

}
