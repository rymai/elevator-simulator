package views;

import models.Building;
import controllers.MainController;

public abstract class BuildingView implements InterfaceView {
	
	protected MainController controller = null;
	public final MainController getController(){
		return controller;
	}
	
	protected Building building = null;
	public Building getBuilding() {
		return building;
	}
	
	public BuildingView(MainController controller, Building building){
		this.controller = controller;
		this.building = building;
	}
	
//	public abstract void addElevator(ElevatorView elevatorView);
}