package models;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * @author x_nem
 */
public class Building {

	// Liste des ascenseurs du batiment
	private ArrayList<Elevator> elevators = null;
	// Liste des passagers du batiment dans leur ordre d'arrivee
	private LinkedList<Passenger> passengers = null;

	private ArrayList<Integer> askedFloors;
	// Can be used by the "intelligent strategies" !
	private ArrayList<Integer> intelligentAskedFloors;

	// Nombre d'etage du batiment
	private int floorCount;

	public Building(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list) {
		constructor(floor_count, elevators_list, passengers_list);
	}

	public Building(int floor_count) {
		constructor(floor_count, new ArrayList<Elevator>(floor_count), new LinkedList<Passenger>());
	}

	public void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list) {
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
	
	/**
	 * Retourne le nombre d'étages du batîment + le rez de chaussée
	 * @return
	 */
	public int getFloorCountWithGround() {
		return floorCount+1;
	}

	public int getElevatorCount() {
		return elevators.size();
	}

	/**
	 * Retournet le premier ascenseur qui est à l'étage passé en paramètre
	 * si il y n'en a pas la fonction retourne null
	 * @param floor
	 * @return
	 */
	public Elevator getElevatorAtFloor(int floor) {
		for (Elevator e : elevators) {
			if(e.getCurrentFloor() == floor) return e;
		}
		return null;
	}

	public LinkedList<Passenger> getWaitingPassengersAtFloor(int floor) {
		LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
		for (Passenger p : passengers) {
			if(p.isWaitingAtFloor(floor)) ret_list.add(p);
		}
		return ret_list;
	}

	public int getWaitingPassengersCountAtFloor(int floor) {
		int sum = 0;
		for (Passenger passenger : getWaitingPassengersAtFloor(floor)) {
			sum += passenger.getPersonCount();
		}
		return sum;
	}
	/**
	 * Retourne une Liste Chainé de Passenger pour connaîre les passagers arrivant à l'étage
	 * @param floor 
	 * @return
	 */
	public LinkedList<Passenger> getArrivedPassengersAtFloor(int floor) {
		LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
		for (Passenger p : passengers) {
			if(p.isArrivedAtFloor(floor)) ret_list.add(p);
		}
		return ret_list;
	}
	/**
	 * Retourne le nombre de passagers arrivant à l'étage
	 * @param floor
	 * @return
	 */
	public int getArrivedPassengersCountAtFloor(int floor) {
		int sum = 0;
		for (Passenger passenger : getArrivedPassengersAtFloor(floor)) {
			sum += passenger.getPersonCount();
		}
		return sum;
	}

	/**
	 * Retourne le premiet passager attendant à l'étage passé en paramètre
	 * @param floor paramètre étage
	 * @return retourne le premier passager attendant à l'étage 
	 */
	public Passenger getFirstPassengerWaitingAtFloor(int floor) {
		for (Passenger passenger : passengers) {
			if(passenger.isWaitingAtFloor(floor)) return passenger;
		}
		return null;
	}
	
	/**
	 * Retourne un booléen permettant de savoir si tous les passagers sont arrivés à destination
	 * @return retourne true si les passagers sont bien tous arrivés
	 * retourne false si les passagers ne sont tous pas tous arrivés
	 * 
	 */
	public boolean allPassengersAreArrived() {
		for (Passenger passenger : passengers) {
			if(!passenger.isArrived()) return false;
		}
		return true;
	}

	/**
	 * Retourne l'index d'un passager passé en paramètre (utile pour dessiner les passager)
	 * @param passenger
	 * @return
	 */
	public int getPassengerIndexAtHisFloor(Passenger passenger) {
		LinkedList<Passenger> list = getPassengersAtFloor(passenger);
		return list.indexOf(passenger);
	}

	/**
	 * Retourne une liste chainé des passagers 
	 * @param passenger
	 * @return
	 */
	public LinkedList<Passenger> getPassengersAtFloor(Passenger passenger) {
		if(passenger.isArrived()) {
			return getArrivedPassengersAtFloor(passenger.getCurrentFloor());
		}
		else {
			return getWaitingPassengersAtFloor(passenger.getCurrentFloor());
		}
	}

	/**
	 * Retourne le nombre de passagers totals attendant un ascenseur
	 * @return
	 */
	public int getWaitingPassengersCount() {
		int sum = 0;
		for (Passenger p : passengers) {
			if(!p.isArrived() && !p.isInTheElevator()) sum += p.getPersonCount();
		}
		return sum;
	}

	/**
	 * Retourne le premier passager de l'étage passé en paramètre qui va dans une certaine direction
	 * @param floor étage passé en paramètre
	 * @param going_to_top direction de l'ascenseur
	 * @return un Passenger ou null selon si il y a au moins une personne qui va dans la direction passé en paramètre
	 */
	public Passenger getFirstWaitingPassengerAtFloorInThisDirection(int floor, boolean going_to_top) {

		LinkedList<Passenger> ps = getWaitingPassengersAtFloor(floor);
		for (Passenger p : ps) {
			if(going_to_top) {
				if(p.getWantedFloor() > p.getCurrentFloor()) return p;
			}
			else {
				if(p.getWantedFloor() < p.getCurrentFloor()) return p;
			}
		}
		return null;
	}

	/**
	 * Retourne l'etage ou il y a le maximum de passagers en attente
	 * @return
	 */
	public int getMaximumWaitingFloor(){
		int maxCrowdedFloor = 0;
		int numberOfPeople = 0;
		for(int i=0; i <= floorCount; i++){
			if(getWaitingPassengersCountAtFloor(i) > numberOfPeople){
				maxCrowdedFloor = i;
				numberOfPeople = getWaitingPassengersCountAtFloor(i);
			}
		}
		System.out.println("Etage le plus blindé : "+maxCrowdedFloor);
		return maxCrowdedFloor;
	}

	/**
	 * Retourne les etages ou il y a des passagers qui attendent
	 * @return
	 */
	public ArrayList<Integer> getFloorWithWaitingPassengers(){
		ArrayList<Integer> numberWaiting = new ArrayList<Integer>();
		for(int i=0; i <= floorCount; i++){
			if(getWaitingPassengersCountAtFloor(i) > 0){
				numberWaiting.add(i);
			}
		}
		return numberWaiting;
	}
	
}