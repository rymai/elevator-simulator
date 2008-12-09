package factories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import strategies.ElevatorStrategy;
import models.*;

public class SimulatorFactory {

	public Building getBuilding(int floor_count, ArrayList<Elevator> elevator_list, LinkedList<Passenger> passengers_list) {
		//		Console.debug("Creation d'un batiment avec "+floorCount+" etages et "+elevatorList.size()+" ascenseurs.");
		return new Building(floor_count, elevator_list, passengers_list);
	}

	public Building getBuilding(int floor_count) {
		return new Building(floor_count);
	}

	public Person getPerson(int max_floor) {
		int sex, mass, qi, current_floor, wanted_floor;
		Random rand = new Random();
		sex = rand.nextInt(2);
		mass = 45+(rand.nextInt(75)); // Entre 45 et 120 kg
		qi = rand.nextInt(140);
		current_floor = rand.nextInt(max_floor);
		do {
			wanted_floor = rand.nextInt(max_floor);
		} while (wanted_floor == current_floor);

		return new Person(current_floor, wanted_floor, sex, mass, qi);
	}

	public Group getGroup(int max_floor) {
		ArrayList<Person> persons = new ArrayList<Person>();
		int sex, mass, qi, current_floor, wanted_floor;
		Random rand = new Random();

		current_floor = rand.nextInt(max_floor);
		do {
			wanted_floor = rand.nextInt(max_floor);
		} while (wanted_floor == current_floor);
		
		// 2 ou 3 personnes 
		// (au dela, on risque de depasser la limite de poids avec un seul groupe)
		int person_count = 2+rand.nextInt(2);
		for (int i = 0; i < person_count; i++) {
			sex = rand.nextInt(2);
			mass = (rand.nextInt(70))+40;
			qi = rand.nextInt(140);
			persons.add(new Person(current_floor, wanted_floor, sex, mass, qi));
		}
		return new Group(current_floor, wanted_floor, persons);
	}

//	public Elevator getElevator(String type, int max_person) {
//		//		Console.debug("Creation d'un ascenseur de type "+type);
//		ElevatorStrategy strategy = null;
//		if(type.equals("LINEAR"))
//			strategy = new Linear();
//		else if(type.equals("LINEAR_IN_THE_DIRECTION"))
//			strategy = new LinearInTheDirection();
//		else if(type.equals("NAWAK"))
//			strategy = new Nawak();
//        //else if(type.equals("OPERATEWITHBLOCKING"))
//		//	strategy = new OperateWithBlocking();
//		return new Elevator(max_person, strategy);
//	}
	
	public Elevator getElevator(ElevatorStrategy elevatorStrategy, int max_person) {
//		Console.debug("Creation d'un ascenseur de type "+type);
		return new Elevator(max_person, elevatorStrategy);
	}

}