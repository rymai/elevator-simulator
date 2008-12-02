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
	private ArrayList<Passenger> passengers = null;

	private ArrayList<Integer> askedFloors;

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
		this.askedFloors = new ArrayList<Integer>(floorCount+1);
		for (int i = 0; i <= floorCount; i++) {
			askedFloors.add(i, 0);
		}
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

	public boolean callElevator(int wantedFloor) {
		askedFloors.set(wantedFloor, askedFloors.get(wantedFloor)+1);
		return true;
	}

	public ArrayList<Integer> getAskedFloors() {
		return askedFloors;
	}

	public boolean isThereElevatorAtFloor(int floor) {
		for (Elevator e : elevators) {
			if(e.getCurrentFloor() == floor) return true;
		}
		return false;
	}

	public Elevator getElevatorAtFloor(int floor) {
		for (Elevator e : elevators) {
			if(e.getCurrentFloor() == floor) return e;
		}
		return null;
	}

	public int getWaitingPassengerCountAtFloor(int floor_index) {
		int i = 0;
		for (Passenger p : passengers) {
			if(p.getCurrentFloor() == floor_index && !p.isArrived() && !p.isInTheElevator()) i++;
		}
		return i;
	}

	public int getArrivedPassengerCountAtFloor(int floor_index) {
		int i = 0;
		for (Passenger p : passengers) {
			if(p.getCurrentFloor() == floor_index && p.isArrived() && !p.isInTheElevator()) i++;
		}
		return i;
	}
}