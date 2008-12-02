package views;

import models.Building;

public abstract class FloorView implements InterfaceView {
	
	public static final int START_FLOOR = 0;
	public static final int END_FLOOR = 1;
	
	protected Building building = null;
	protected int floorIndex;
	protected int floorKind;
	
	public FloorView(Building building, int floor_index, int floor_kind){
		this.building = building;
		this.floorKind = floor_kind;
		this.floorIndex = floor_index;
	}
	
}