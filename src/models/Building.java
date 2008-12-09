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

	/**
	 * Constructeur Building
	 * @param floor_count Nombre d'étage
	 * @param elevators_list Liste d'Elevator
	 * @param passengers_list Liste de passager
	 */
	public Building(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list) {
		constructor(floor_count, elevators_list, passengers_list);
	}

	/**
	 * Constructeur Building avec un paramètre
	 * @param floor_count Nombre d'étage
	 */
	public Building(int floor_count) {
		constructor(floor_count, new ArrayList<Elevator>(floor_count), new LinkedList<Passenger>());
	}

	/**
	 * Méthode privée utilisé par les constructeurs pour initialiser les variables
	 * @param floor_count       Nombre d'étage
	 * @param elevators_list    Liste d'Elevator  (liste d'Elevator)
	 * @param passengers_list   Liste de Passengers
	 */
	private void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list) {
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

	/**
	 * Retourne le nombre d'étages (sans compter le RDC)
	 * @return
	 */
	public int getFloorCount() {
		return floorCount;
	}

	/**
	 * Retourne le nombre d'étages + le RDC
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
	 * Retourne la liste des passagers qui attendent à l'étage floor
	 * @param floor
	 * @return
	 */
	public LinkedList<Passenger> getWaitingPassengersAtFloor(int floor) {
		LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
		for (Passenger p : passengers) {
			if(p.isWaitingAtFloor(floor)) ret_list.add(p);
		}
		return ret_list;
	}

	/**
	 * Retourne le ieme passagers qui attend à l'étage floor
	 * @param floor
	 * @param i Index du passager dans la LinkedList de passagers qui attendent à l'étage floor
	 * @return
	 */
	public Passenger getWaitingPassengerAtFloorWithIndex(int floor, int i) {
		LinkedList<Passenger> ps = getWaitingPassengersAtFloor(floor);
		if(i >= ps.size()) {
			return null;
		}
		else {
			return ps.get(i);
		}
	}

	/**
	 * Retourne le ieme passagers qui attend à l'étage floor et qui va dans
	 * la direction going_to_top
	 * @param floor
	 * @param i Index du passager dans la LinkedList de passagers qui attendent à l'étage floor
	 * @return
	 */
	public Passenger getWaitingPassengerAtFloorWithIndexInThisDirection(int floor, int i, boolean going_to_top) {
		Passenger p = getWaitingPassengerAtFloorWithIndex(floor,i);

		if(p == null) return null;
		else {
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
	 * Retourne le nombre de passagers qui attendent à l'étage floor
	 * @param floor
	 * @return
	 */
	public int getWaitingPersonsCountAtFloor(int floor) {
		int sum = 0;
		for (Passenger passenger : getWaitingPassengersAtFloor(floor)) {
			sum += passenger.getPersonCount();
		}
		return sum;
	}

	/**
	 * Retourne une liste chainée des passagers 
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
	 * Retourne l'index d'un passager passé en paramètre (utile pour dessiner les passager)
	 * @param passenger
	 * @return
	 */
	public int getPassengerIndexAtHisFloor(Passenger passenger) {
		LinkedList<Passenger> list = getPassengersAtFloor(passenger);
		return list.indexOf(passenger);
	}

	/**
	 * Retourne le nombre de personnes attendant un ascenseur
	 * @return
	 */
	public int getWaitingPersonsCount() {
		int sum = 0;
		for (Passenger p : passengers) {
			if(!p.isArrived() && !p.isInTheElevator()) sum += p.getPersonCount();
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
	 * Retourne l'etage ou il y a le maximum de passagers en attente
	 * @return L'indice de l'etage ou il y a le maximum de passagers en attente
	 */
	public int getMaximumWaitingFloor(){
		int maxCrowdedFloor = 0;
		int numberOfPeople = 0;
		for(int i=0; i <= floorCount; i++){
			if(getWaitingPersonsCountAtFloor(i) > numberOfPeople){
				maxCrowdedFloor = i;
				numberOfPeople = getWaitingPersonsCountAtFloor(i);
			}
		}
		System.out.println("Etage le plus blindé : "+maxCrowdedFloor);
		return maxCrowdedFloor;
	}

	/**
	 * Retourne les indices des etages ou il y a des passagers qui attendent
	 * @return Une ArrayList<Integer> des etages ou il y a des passagers
	 */
	public ArrayList<Integer> getFloorWithWaitingPassengers(){
		ArrayList<Integer> numberWaiting = new ArrayList<Integer>();
		for(int i=0; i <= floorCount; i++){
			if(getWaitingPersonsCountAtFloor(i) > 0){
				numberWaiting.add(i);
			}
		}
		return numberWaiting;
	}

}