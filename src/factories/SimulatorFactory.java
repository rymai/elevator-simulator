package factories;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import strategies.ElevatorStrategy;
import strategies.elevators.*;
import controllers.MainController;
import models.*;

public class SimulatorFactory {

	public Building getBuilding(int floor_count, ArrayList<Elevator> elevator_list, LinkedList<Passenger> passengers_list, MainController controller) {
		//		Console.debug("Creation d'un batiment avec "+floorCount+" etages et "+elevatorList.size()+" ascenseurs.");
		return new Building(floor_count, elevator_list, passengers_list, controller);
	}

	public Building getBuilding(int floor_count, MainController controller) {
		return new Building(floor_count, controller);
	}

	public Person getPerson(int max_floor, MainController controller) {
		//		Console.debug("Person sex: "+Person.getTextForSex(sex)+" | mass: "+mass+"kg | qi: "+qi+ ". "+
		//				"Ascenseur: "+elevator.getIdentifier()+". "+
		//				"Actuellement a l'etage "+current_floor+", veux aller a l'etage "+wanted_floor+". ");
		int sex, mass, qi, current_floor, wanted_floor;
		Random rand = new Random();
		sex = rand.nextInt(2);
		mass = (rand.nextInt(70))+40;
		qi = rand.nextInt(140);
		current_floor = rand.nextInt(max_floor);
		do {
			wanted_floor = rand.nextInt(max_floor);
		} while (wanted_floor == current_floor);

		return new Person(current_floor, wanted_floor, sex, mass, qi,controller);
	}

	public Group getGroup(int current_floor, int wanted_floor, int personCount, MainController controller, Elevator elevator) {
//		Console.debug("Creation d'un groupe initialis� � une taille de "+personCount+".");
		return new Group(current_floor, wanted_floor, personCount, controller);
	}

	public Elevator getElevator(MainController controller, String type, int max_person) {
		//		Console.debug("Creation d'un ascenseur de type "+type);
		ElevatorStrategy strategy = null;
		if(type.equals("LINEAR"))
			strategy = loadPlugin("strategies.elevators.Linear");
		else if(type.equals("LINEAR_IN_THE_DIRECTION"))
			strategy = new LinearInTheDirection();
		else if(type.equals("NAWAK"))
			strategy = new Nawak();
        //else if(type.equals("OPERATEWITHBLOCKING"))
		//	strategy = new OperateWithBlocking();
		return new Elevator(controller, max_person, strategy);
	}
	
	private static ElevatorStrategy loadPlugin(String className) {
		Class c = null;   
		try {        
			URLClassLoader cl = new URLClassLoader (new URL[] { 
//					new URL("file:///Users/remy/Documents/Development/Java/M1 Miage/elevator-behaviour-simulator-in-java/plugins")
					new URL("file:///Users/remy/Desktop/plugins/")
			});
			c = cl.loadClass(className);
		} 
		catch(ClassNotFoundException e) {
			System.err.println("Classe " + className + " non trouvee");
			e.printStackTrace();   
			return null;
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}        

		try {   
			Object o = c.newInstance();      
			return (ElevatorStrategy) o;
		} 
		catch(InstantiationException e) {  
			System.err.println("Erreur dans l'instantiation de la classe "+ className);
			e.printStackTrace();
			return null;
		}    
		catch(IllegalAccessException e) {  
			System.err.println("Erreur dans l'instantiation de la classe "+ className);
			e.printStackTrace();
			return null;
		}    
	}

}