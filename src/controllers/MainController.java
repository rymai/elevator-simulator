package controllers;

import java.util.ArrayList;
import factories.SimulatorFactory;
import main.Console;
import models.*;

/**
 *  MainController est construit sur le modele du design pattern Singleton
 *  En effet, il ne peut y avoir qu'une seule instance de ce controleur a la fois.
 *  
 * @author remy
 *
 */
public class MainController {
	
	private static MainController INSTANCE = null;

	// Le point d'acces a tous les modeles (le batiment a acces direct aux elevators et aux passagers)
	public static Building building = null;
	public Building getBuilding() {
		return building;
	}

	/**
	 * La présence d'un constructeur privé supprime
	 * le constructeur public par défaut.
	 */
	private MainController() {}

	/**
	 * Le mot-clé synchronized sur la méthode de création
	 * empêche toute instanciation multiple même par
	 * différents threads.
	 * @return L'unique instance du singleton.
	 */
	public synchronized static MainController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainController();
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
		ArrayList<Elevator> elevators = new ArrayList<Elevator>(elevator_count);
		Elevator elevator;
		for (int i = 0; i < elevator_count; i++) {
			elevator = sf.getElevator(INSTANCE, "LINEAR", 5);
			elevator.setIdentifier(Integer.toString(i));
			elevators.add(elevator);
		}
		// Add elevators to the building
		building.setElevators(elevators);

		// Constructs the passengers (only persons for now)
		ArrayList<Passenger> passengers = new ArrayList<Passenger>(person_count);
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
		// Add passengers to the building
		building.setPassengers(passengers);
		
		for (Elevator e : elevators) {
			for (Passenger p : passengers) {
				e.addObserver(p);
			}
		}

		// Creating the building view
		building.setView(sf.getBuildingView(INSTANCE, building));
		building.getView().addStartsFloors();
		
		// Creating a view for each elevator
		Elevator temp_elevator;
		for (int i = 0; i < elevators.size(); i++) {
			temp_elevator = elevators.get(i);
			temp_elevator.setView(sf.getElevatorView(temp_elevator, i+1));
			temp_elevator.getView().display();
		}
		
		building.getView().addEndsFloors();
		building.getView().display();
		
		// Actions
		for (Passenger p : passengers) {
			p.acts();
		}

		for (Elevator e : elevators) {
			e.setRunning(true);
		}		
	}

	private void displayPassengersPerFloor(int floor_count) {
		/**/
		ArrayList<Passenger> passengers_per_floor = new ArrayList<Passenger>();
		ArrayList<Passenger> temp_passengers = new ArrayList<Passenger>();
		temp_passengers = (ArrayList<Passenger>) building.getPassengers().clone();

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
		for (Elevator e : building.getElevators()) {
			for(Passenger q : e.getPassengers()) {
				mectons += q.isArrived() ? "A " : "W ";
			}
			Console.debug("|"+mectons+"|("+e.getCurrentWeight()+" kg) Elevator "+e.getIdentifier());
		}
		/**/
	}

}