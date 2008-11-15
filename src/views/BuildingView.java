package views;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import models.Building;
import models.Elevator;

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
	
//	public abstract void insertElevatorsView(ArrayList<Elevator> elevators);
	
	public abstract void display();
	public abstract void close();

	public abstract void addElevator(ElevatorView elevatorView);

	public Building getBuilding() {
		return building;
	}
	
}