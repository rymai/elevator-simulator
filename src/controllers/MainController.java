package controllers;

import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;

import factories.SimulatorFactory;
import main.Console;
import models.*;
import views.*;

// MainController est construit sur le modele du design pattern Singleton
// En effet, il ne peut y avoir qu'une seule instance de ce controleur a la fois.
public class MainController {

	private static MainController INSTANCE = null;

	// Models objects
	public static Building building = null;
	public static ArrayList<Elevator> elevators = null;
	public static ArrayList<Passenger> passengers = null;

	// Views objects lists
	//	public static BuildingView buildingView = null;
	//	public static ArrayList<ElevatorView> elevatorViews = null;
//	public static ArrayList<PassengerView> passengerViews = null;

	/**
	 * La présence d'un constructeur privé supprime
	 * le constructeur public par défaut.
	 */
	private MainController() {}

	/**
	 * Le mot-clé synchronized sur la méthode de création
	 * empêche toute instanciation multiple même par
	 * différents threads.
	 * Retourne l'instance du singleton.
	 */
	public synchronized static MainController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainController();
			//			elevatorViews = new ArrayList<ElevatorView>();
//			passengerViews = new ArrayList<PassengerView>();
		}
		return INSTANCE;
	}

	public void startSimulation(int floor_count, int elevator_count, int person_count, int group_count) {
		Console.info("Lancement d'une partie avec "+floor_count+" etages, "+elevator_count+
				" ascenseurs, "+person_count+" individus et "+group_count+" groupes.");

		SimulatorFactory sf = new SimulatorFactory();

		// Constructs the buildings.
		building = sf.getBuilding(floor_count, INSTANCE);

		// Constructs the elevators
		elevators = new ArrayList<Elevator>(elevator_count);
		Elevator elevator;
		for (int i = 0; i < elevator_count; i++) {
			elevator = sf.getElevator(INSTANCE, "LINEAR", 5);
			elevator.setIdentifier(Integer.toString(i));
			elevators.add(elevator);
		}
		// Add elevators to the building
		building.setElevators(elevators);

		// Constructs the passengers (only persons for now)
		passengers = new ArrayList<Passenger>(person_count);
		int elevator_index, sex, mass, qi, current_floor, wanted_floor;
		for (int i = 0; i < person_count; i++) {
			elevator_index = (int)(Math.random()*elevators.size());
			sex = (int)(Math.random()*2);
			mass = (int)(Math.random()*70)+40;
			qi = (int)(Math.random()*100);
			current_floor = (int)(Math.random()*floor_count);
			do {
				wanted_floor = (int)(Math.random()*floor_count);
			} while (wanted_floor == current_floor);
			passengers.add(sf.getPerson(current_floor, wanted_floor, sex, mass, qi, INSTANCE, elevators.get(elevator_index)));
		}

		// Creating the building view
		building.setView(new JFrameBuildingView(INSTANCE, building));
		building.getView().display();
		
		// Creating a view for each elevator
		for (int i = 0; i < elevators.size(); i++) {
			elevators.get(i).setView(new JPanelElevatorView(INSTANCE, elevators.get(i), building, i));
			elevators.get(i).getView().display();
		}

		// Actions
		for (Passenger p : passengers) {
			p.acts();
		}

		Thread thread = new Thread();
		thread.start();
		while (true) {
			Console.debug("...");
			for (Elevator e : elevators) {
				e.acts();
			}
			displayPassengersPerFloor(floor_count);
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void displayPassengersPerFloor(int floor_count) {
		ArrayList<Passenger> passengers_per_floor = new ArrayList<Passenger>();
		ArrayList<Passenger> temp_passengers = new ArrayList<Passenger>();
		temp_passengers = (ArrayList<Passenger>) passengers.clone();

		Passenger p;
		String mectons = "";
		int floor = 0, elevator_floor = 0;
		for (int l = floor_count-1; l >= 0; l--) {
			passengers_per_floor.clear();
			for (int k = 0; k < temp_passengers.size() ; k++) {
				p = temp_passengers.get(k);
				if(p.getCurrentFloor() == l && !p.isInTheElevator()) {
					floor = p.getCurrentFloor();
					elevator_floor = (int) p.getElevator().getCurrentFloor();
					passengers_per_floor.add(p);
					temp_passengers.remove(p);

				}
			}
			mectons = (floor == elevator_floor) ? "|X| " : "|"+Integer.toString(l)+"| ";
			for(Passenger q : passengers_per_floor) {
				mectons += q.isArrived() ? "A " : "W ";
			}
			Console.debug(mectons);
		}
		Console.debug("");

		mectons = "";
		for (Elevator e : elevators) {
			for(Passenger q : e.getPassengers()) {
				mectons += q.isArrived() ? "A " : "W ";
			}
			Console.debug("|"+mectons+"|("+e.getCurrentWeight()+" kg) Elevator "+e.getIdentifier());
		}
	}

	public Building getBuilding() {
		return building;
	}

}
