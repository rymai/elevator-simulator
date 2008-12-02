package views;

import models.Elevator;

public abstract class ElevatorView implements InterfaceView {
	
	protected Elevator elevator = null;
	protected int identifier;
	
	public ElevatorView(Elevator e, int identifier){
		this.elevator = e;
		this.identifier = identifier;
	}
		
//	public abstract Object getElevator();
	public abstract void move(float new_y_position);

	public void setElevator(Elevator e) {
		this.elevator = e;
	}
	
}