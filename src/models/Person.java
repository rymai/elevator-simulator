package models;

import main.Console;
import java.util.Observable;
import controllers.MainController;

public class Person extends Passenger {
	
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	
	private int sex;
	private int mass;
	
	// Si la personne est trop lourde pour l'ascenceur, et qu'elle monte quand meme par exemple
	// Pour simuler les cons.
	private int qi;
	
	public Person(int current_floor, int wanted_floor, int sex, int mass, int qi, MainController controller) {
		super(current_floor, wanted_floor, controller);
		this.sex = sex;
		this.mass = mass;
		this.qi = qi;
		this.inTheElevator = false;
	}

	@Override
	public int getTotalMass() {
		return mass;
	}

	public int getSex() {
		return sex;
	}
	
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
	 * Methode implementant le comportement de la personne lorsque son ascenceur s'arrete
	 */
	public void update(Observable o, Object arg) {
		// L'individu est avise par l'ascenseur qu'il est possible de monter dedans
		
		// La personne n'est pas arrive a destination
		if(!isArrived()) {
			// La personne n'est pas deja dans un ascenseur
			if(!inTheElevator) {
				// L'ascenseur est bien a mon etage
				if(elevatorIsAtMyFloor()) {
					elevator = controller.getBuilding().getElevatorAtFloor(currentFloor);
					// L'ascenseur est en alerte
					if(elevator.isInAlert()) {
						// QI faible = la personne essaye de monter quand meme
						if(qi < 70) {
							// Maintenant, le passager fait une demande pour l'etage ou il veut aller
							this.inTheElevator = elevator.takePassenger(this);
							// La personne a reussi a forcer et a monter
							if(inTheElevator) {
								// Une fois la personne dans l'ascenseur, elle appelle l'etage ou elle veut aller
								System.out.println("Duree de l'attente : "+getTime());
								actsAfterEnteredTheElevator();
								Console.debug("Boulet monte dans ascenseur "+elevator.getIdentifier()+", je vais a l'etage "+wantedFloor+"! |"+elevator.getPassengerCount()+"|");
							}
							else {
								Console.debug("Je n'ai pas pu monter dans ascenseur "+elevator.getIdentifier()+"! Je suis un gros boulet!");
								actsAfterBeRejectedFromElevator();
							}
								
						}
						else
							Console.debug("L'ascenseur "+elevator.getIdentifier()+" est en alerte, je ne monte pas...");
					}
					// L'asenseur n'est pas en alerte
					else {
						this.inTheElevator = elevator.takePassenger(this);
						if(inTheElevator) {
							// Une fois la personne dans l'ascenseur, elle appelle l'etage ou elle veut aller
							actsAfterEnteredTheElevator();
							Console.debug("Je monte dans ascenseur "+elevator.getIdentifier()+", je vais a l'etage "+wantedFloor+"! |"+elevator.getPassengerCount()+"|");
						}
						else {
							actsAfterBeRejectedFromElevator();
						}
					}
				}
			}
			// Deja dans l'ascenseur
			else {
				currentFloor = elevator.getCurrentFloor(); // Update current floor
				// Arrive a son etage de destination
				if(itsMyDestinationFloor()) {
					this.inTheElevator = false;
					elevator.releasePassenger(this);
//					Console.debug("Je sors ici! |"+elevator.getPassengerCount()+"|");
					// L'etage desire est mis a une valeur qui indique que la personne est arrivee
					this.wantedFloor = Integer.MAX_VALUE;
				}
			}
		}
	}
	
	private boolean elevatorIsAtMyFloor() {
		return controller.getBuilding().isThereElevatorAtFloor(currentFloor);
//		return Float.compare(elevator.getCurrentFloor(), currentFloor) == 0;
	}
	
	private boolean itsMyDestinationFloor() {
		return currentFloor == wantedFloor;
	}

	@Override
	public int getPersonCount() {
		return 1;
	}

	@Override
	public void actsAfterEnteredTheElevator() {
		elevatorCalled = false;
		acts();
	}
	
	@Override
	public void actsAfterBeRejectedFromElevator() {
		elevatorCalled = false;
		acts();
	}
	
}