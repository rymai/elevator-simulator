package models;

import java.util.ArrayList;
import java.util.Observable;
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
	
	/**
	 * Methode implementant le comportement du groupe lorsque son ascenceur s'arrete
	 */
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	}

	public int getPersonCount() {
		return persons.size();
	}

	@Override
	public boolean canEnterElevator(Elevator elevator) {
		// TODO Auto-generated method stub
		return false;
	}
	
}