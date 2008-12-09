package models;

import main.Console;
import controllers.MainController;

/**
 * Class Person
 * @author x_nem
 */
public class Person extends Passenger {
	
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	
	private int sex;
	private int mass;
	
	// Si la personne est trop lourde pour l'ascenceur, et qu'elle monte quand meme par exemple
	// Pour simuler les cons.
	private int qi;
	
        /**
         * Constructeur Person 
         * @param current_floor         étage actuel
         * @param wanted_floor          étage souhaité
         * @param sex                   sex de la personne
         * @param mass                  masse (poids) de la personne
         * @param qi                    qi de la personne
         */
	public Person(int current_floor, int wanted_floor, int sex, int mass, int qi) {
		super(current_floor, wanted_floor);
		this.sex = sex;
		this.mass = mass;
		this.qi = qi;
//		this.inTheElevator = false;
	}

	@Override
	public int getTotalMass() {
		return mass;
	}

	public int getSex() {
		return sex;
	}
	
        /**
         * Fonction renvoyant le sexe de la personne en chaine de caractère
         * male ou female
         * @param sex
         * @return
         */
	public static String getTextForSex(int sex) {
		switch (sex) {
		case MALE:
			return "male";
		case FEMALE:
			return "female";
		}
		return "unknow";
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public int getQi() {
		return qi;
	}

	public void setQi(int qi) {
		this.qi = qi;
	}

	/**
	 * Methode implementant le comportement de la personne lorsque son ascenceur lui propose de monter
	 */
	@Override
	public boolean canEnterElevator(Elevator elevator) {
		// Si l'ascenseur indique qu'il est en alerte de poids
		if(elevator.isInAlert()) {
			// 2 possiblitees
			// QI faible = la personne est faible d'esprit, elle essaye de monter
			if(qi < 70) {
				if(elevator.takePassenger(this)) {
					// Une fois la personne dans l'ascenseur, elle appelle l'etage ou elle veut aller
//					Console.debug("Duree de l'attente : "+waitingTime());
					Console.debug("Boulet monte dans ascenseur "+elevator.getIdentifier()+", je vais a l'etage "+wantedFloor+"! |"+elevator.getPassengerCount()+"|");
				}
				else {
					Console.debug("Je n'ai pas pu monter dans ascenseur "+elevator.getIdentifier()+"! Je suis un gros boulet!");
//					actsAfterBeRejectedFromElevator();
				}
					
			}
			// QI "normal", la personne attendra...
			else
				Console.debug("L'ascenseur "+elevator.getIdentifier()+" est en alerte, je ne monte pas...");
		}
		// L'asenseur n'est pas en alerte
		else {
			if(elevator.takePassenger(this)) {
//				Console.debug("Duree de l'attente : "+waitingTime());
				Console.debug("Je monte dans ascenseur "+elevator.getIdentifier()+", je vais a l'etage "+wantedFloor+"! |"+elevator.getPassengerCount()+"|");
			}
			else {
				Console.debug("Je suis trop lourd pour monter dans ascenseur "+elevator.getIdentifier()+".");
			}
		}
		return isInTheElevator();
	}
	
	private boolean elevatorIsAtMyFloor() {
		return controller.getBuilding().isThereElevatorAtFloor(currentFloor);
	}
	
	private boolean itsMyDestinationFloor() {
		return currentFloor == wantedFloor;
	}

	@Override
	public int getPersonCount() {
		return 1;
	}
	
}