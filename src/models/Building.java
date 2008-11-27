package models;

import java.util.ArrayList;
import controllers.MainController;
import views.BuildingView;

public class Building {
	
	private MainController controller;
	
	private BuildingView view;
	
	// Liste des ascenseurs du batiment
	private ArrayList<Elevator> elevators = null;
	// Liste des passagers du batiment
	public ArrayList<Passenger> passengers = null;
	
	// Nombre d'etage du batiment
	private int floorCount;
	
	public Building(int floor_count, ArrayList<Elevator> elevators_list, ArrayList<Passenger> passengers_list, MainController controller) {
		constructor(floor_count, elevators_list, passengers_list, controller);
	}
	
	public Building(int floor_count, MainController mainController) {
		constructor(floor_count, new ArrayList<Elevator>(floor_count), new ArrayList<Passenger>(100), controller);
	}

	public void constructor(int floor_count, ArrayList<Elevator> elevators_list, ArrayList<Passenger> passengers_list, MainController controller) {
		this.controller = controller;
		this.floorCount = floor_count;
		this.elevators = elevators_list;
		this.passengers = passengers_list;
	}

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}

	public void setElevators(ArrayList<Elevator> elevators) {
		this.elevators = elevators;
	}

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}
	
	public void setPassengers(ArrayList<Passenger> passengers) {
		this.passengers = passengers;
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

	public int getWaitingPassengerCountAtElevatorAndFloor(Elevator elevator, int floor_index) {
		int i = 0;
		for (Passenger p : passengers) {
			if(p.getElevator() == elevator && p.getCurrentFloor() == floor_index
					&& !p.isArrived() && !p.isInTheElevator()) i++;
		}
		return i;
	}
	
	public int getArrivedPassengerCountAtElevatorAndFloor(Elevator elevator, int floor_index) {
		int i = 0;
		for (Passenger p : passengers) {
			if(p.getElevator() == elevator && p.getCurrentFloor() == floor_index
					&& p.isArrived() && !p.isInTheElevator()) i++;
		}
		return i;
	}
	
}