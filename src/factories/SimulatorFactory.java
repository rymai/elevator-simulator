package factories;

import java.util.ArrayList;

import strategies.Strategy;
import strategies.elevators.Linear;
import views.BuildingView;
import views.ElevatorView;
import views.JFrameBuildingView;
import views.JPanelElevatorView;
import controllers.MainController;
import main.Console;
import models.*;

public class SimulatorFactory {
	
	public Building getBuilding(int floor_count, ArrayList<Elevator> elevatorList, MainController controller) {
//		Console.debug("Creation d'un batiment avec "+floorCount+" etages et "+elevatorList.size()+" ascenseurs.");
		return new Building(floor_count, elevatorList, controller);
	}
	
	public Building getBuilding(int floor_count, MainController controller) {
		return new Building(floor_count, controller);
	}
	
	public Person getPerson(int current_floor, int wanted_floor, int sex, int mass, int qi, MainController controller, Elevator elevator) {
//		Console.debug("Person sex: "+Person.getTextForSex(sex)+" | mass: "+mass+"kg | qi: "+qi+ ". "+
//				"Ascenseur: "+elevator.getIdentifier()+". "+
//				"Actuellement a l'etage "+current_floor+", veux aller a l'etage "+wanted_floor+". ");
		return new Person(current_floor, wanted_floor, sex, mass, qi,controller, elevator);
	}
	
	public Group getGroup(int current_floor, int wanted_floor, int personCount, MainController controller, Elevator elevator) {
//		Console.debug("Creation d'un groupe initialisé à une taille de "+personCount+".");
		return new Group(current_floor, wanted_floor, personCount, controller, elevator);
	}

	public Elevator getElevator(MainController controller, String type, int max_person) {
//		Console.debug("Creation d'un ascenseur de type "+type);
		Strategy strategy = null;
		if(type.equals("LINEAR"))
			strategy = new Linear();
		return new Elevator(controller, max_person, strategy);
	}

	public BuildingView getBuildingView(MainController controller, Building building) {
		return new JFrameBuildingView(controller, building);
	}

	public ElevatorView getElevatorView(MainController controller, Elevator elevator, Building building, int identifier) {
		return new JPanelElevatorView(controller, elevator, building, identifier);
	}
	
}