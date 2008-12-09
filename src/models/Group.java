package models;

import java.util.ArrayList;
import java.util.Observable;

import main.Console;
import controllers.MainController;

public class Group extends Passenger {
	
	private ArrayList<Person> persons;
	
	public Group(int current_floor, int wanted_floor, int personCount) {
		super(current_floor, wanted_floor);
		this.persons = new ArrayList<Person>(personCount);
	}
	
	public Group(int current_floor, int wanted_floor, ArrayList<Person> persons) {
		super(current_floor, wanted_floor);
		this.persons = persons;
	}

	@Override
	/**
	 * Retourne la masse totale du groupe
	 */
	public int getTotalMass() {
		int somme = 0;
		for (Person i : persons) {
			somme += i.getTotalMass();
		}
		return somme;
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}
	
	public void addPerson(Person person) {
		this.persons.add(person);
	}

	public int getPersonCount() {
		return persons.size();
	}

	@Override
	public boolean canEnterElevator(Elevator elevator) {
		// Si l'ascenseur indique qu'il est en alerte de poids
		if(!elevator.isInAlert()) {
			if(elevator.takePassenger(this)) {
				Console.debug("Nous montons dans ascenseur "+elevator.getIdentifier()+", nous allons a l'etage "+wantedFloor+"! |"+elevator.getPassengerCount()+"|");
			}
			else {
				Console.debug("Nous sommes trop lourd pour monter dans ascenseur "+elevator.getIdentifier()+".");
			}
		}
		return isInTheElevator();
	}
	
}