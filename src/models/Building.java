package models;

import java.util.ArrayList;
import java.util.LinkedList;
import controllers.MainController;
import views.BuildingView;

public class Building {

	private MainController controller;

	private BuildingView view;

	// Liste des ascenseurs du batiment
	private ArrayList<Elevator> elevators = null;
	// Liste des passagers du batiment dans leur ordre d'arrivee
	private LinkedList<Passenger> passengers = null;

	private ArrayList<Integer> askedFloors;
	// Can be used by the "intelligent strategies" !
	private ArrayList<Integer> intelligentAskedFloors;

	// Nombre d'etage du batiment
	private int floorCount;

	public Building(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, MainController controller) {
		constructor(floor_count, elevators_list, passengers_list, controller);
	}

	public Building(int floor_count, MainController mainController) {
		constructor(floor_count, new ArrayList<Elevator>(floor_count), new LinkedList<Passenger>(), controller);
	}

	public void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, MainController controller) {
		this.controller = controller;
		this.floorCount = floor_count;
		this.elevators = elevators_list;
		this.passengers = passengers_list;
		this.askedFloors = new ArrayList<Integer>(getFloorCountWithGround());
		this.intelligentAskedFloors = new ArrayList<Integer>(getFloorCountWithGround());
		for (int i = 0; i <= floorCount; i++) {
			askedFloors.add(i, 0);
			intelligentAskedFloors.add(i, 0);
		}
	}

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}

	public void setElevators(ArrayList<Elevator> elevators) {
		this.elevators = elevators;
	}

	public LinkedList<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(LinkedList<Passenger> passengers) {
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

	public boolean callElevator(int wanted_floor) {
		askedFloors.set(wanted_floor, askedFloors.get(wanted_floor)+1);
		return true;
	}
	
	public boolean callElevator(int current_floor, int wanted_floor) {
		askedFloors.set(current_floor, askedFloors.get(current_floor)+1);
		intelligentAskedFloors.set(wanted_floor, intelligentAskedFloors.get(wanted_floor)+1);
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

	private LinkedList<Passenger> getWaitingPassengersAtFloor(int floor) {
		LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
		for (Passenger p : passengers) {
			if(p.isWaitingAtFloor(floor)) ret_list.add(p);
		}
		return ret_list;
	}
	public int getWaitingPassengersCountAtFloor(int floor) {
		return getWaitingPassengersAtFloor(floor).size();
	}

	private LinkedList<Passenger> getArrivedPassengersAtFloor(int floor) {
		LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
		for (Passenger p : passengers) {
			if(p.isArrivedAtFloor(floor)) ret_list.add(p);
		}
		return ret_list;
	}
	public int getArrivedPassengersCountAtFloor(int floor) {
		return getArrivedPassengersAtFloor(floor).size();
	}
	
	public int getCallCountAtFloor(int floor_index) {
		return askedFloors.get(floor_index);
	}

	public int getFloorCountWithGround() {
		return floorCount+1;
	}

	public Passenger getFirstPassengerWaitingAtFloor(int floor) {
		for (Passenger passenger : passengers) {
			if(passenger.isWaitingAtFloor(floor)) return passenger;
		}
		return null;
	}

	public boolean allPassengersAreArrived() {
		for (Passenger passenger : passengers) {
			if(!passenger.isArrived()) return false;
		}
		return true;
	}

	public int getPassengerIndexAtHisFloor(Passenger passenger) {
		LinkedList<Passenger> list = getPassengersAtFloor(passenger);
		return list.indexOf(passenger);
	}

	public LinkedList<Passenger> getPassengersAtFloor(Passenger passenger) {
		if(passenger.isArrived()) {
			return getArrivedPassengersAtFloor(passenger.getCurrentFloor());
		}
		else {
			return getWaitingPassengersAtFloor(passenger.getCurrentFloor());
		}
	}

	public int getWaitingPassengersCount() {
		int i = 0;
		for (Passenger p : passengers) {
			if(!p.isArrived() && !p.isInTheElevator()) i++;
		}
		return i;
	}
	
}