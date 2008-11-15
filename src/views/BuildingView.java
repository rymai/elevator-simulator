package views;

import models.Building;
import controllers.MainController;

public abstract class BuildingView {
	
	private MainController controller = null;
	protected Building building;
	
	public BuildingView(MainController controller, Building building){
		this.controller = controller;
		this.building = building;
	}
	
	public final MainController getController(){
		return controller;
	}
		
	public abstract void display();
	public abstract void close();
	public abstract void addElevator(ElevatorView elevatorView);

	public Building getBuilding() {
		return building;
	}
	
}