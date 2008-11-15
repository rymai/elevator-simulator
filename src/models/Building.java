package models;

import java.util.ArrayList;
import controllers.MainController;
import views.BuildingView;
import views.JFrameBuildingView;

public class Building {
	
	private MainController controller;
	
	private BuildingView view;
	
	// Liste des ascenseurs du batiment
	private ArrayList<Elevator> elevators;
	
	// Nombre d'etage du batiment
	private int floorCount;
	
	public Building(int floor_count, ArrayList<Elevator> elevators_list, MainController controller) {
		constructor(floor_count, elevators_list, controller);
	}
	
	public Building(int floor_count, MainController mainController) {
		constructor(floor_count, new ArrayList<Elevator>(floor_count), controller);
	}

	public void constructor(int floor_count, ArrayList<Elevator> elevators_list, MainController controller) {
		this.controller = controller;
		this.floorCount = floor_count;
		this.elevators = elevators_list;
	}

	public ArrayList<Elevator> getAscenceurs() {
		return elevators;
	}

	public void setElevators(ArrayList<Elevator> elevators) {
		this.elevators = elevators;
	}

	public int getFloorCount() {
		return floorCount;
	}
	
	public int getElevatorCount() {
		return elevators.size();
	}

	public BuildingView getView() {
		return view;
	}
	
	public void setView(BuildingView view) {
		this.view = view;
	}
}