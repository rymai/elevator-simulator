package views;

import models.Elevator;

public abstract class FloorView implements InterfaceView {
	
	public static final int START_FLOOR = 0;
	public static final int END_FLOOR = 1;
	
	protected Elevator elevator = null;
	protected int floorIndex;
	protected int floorKind;
	
	public FloorView(Elevator elevator, int floor_index, int floor_kind){
		this.elevator = elevator;
		this.floorKind = floor_kind;
		this.floorIndex = floor_index;
	}
	
}