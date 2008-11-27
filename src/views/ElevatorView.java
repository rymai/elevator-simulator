package views;

import models.Elevator;

public abstract class ElevatorView implements InterfaceView {
	
	protected Elevator elevator = null;
	protected int identifier;
	
	public ElevatorView(Elevator elevator, int identifier){		
		this.elevator = elevator;
		this.identifier = identifier;
	}
		
//	public abstract Object getElevator();
	public abstract void move(float new_y_position);

	public abstract void refreshFloor(int floor_index);
	
}