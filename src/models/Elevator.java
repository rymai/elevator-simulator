package models;

import java.util.ArrayList;
import java.util.Observable;

import com.sun.tools.javac.tree.Tree.TopLevel;

import main.Console;
import controllers.MainController;
import strategies.ElevatorStrategy;
import strategies.elevators.Linear;
import views.ElevatorView;

public class Elevator extends Observable implements Runnable {

	private static final int TO_TOP = 1;
	private static final int TO_BOTTOM = -1;
	private static int SLEEPING_DURATION = 100;
	// Elevator move by <speed_per_round>*floor each <SLEEPING_DURATION>ms
	protected float speed_per_round = 0.5f;

	// Pointer to his controller
	protected MainController controller;
	protected Building building;
	protected ElevatorView view;

	protected ElevatorStrategy strategy;

	// C'est mieux si l'identifier est unique.
	protected String identifier;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Indique si l'ascenseur est en service
	 */
	private boolean running = false;

	/**
	 *  Nombre de personnes max pouvant physiquement entrer
	 *  Au dela, l'ascenseur refuse forcement les passagers
	 */ 
	protected int maxPersons = 7;

	/**
	 *  Poids au dela duquel l'ascenseur refuse de bouger, il est physiquement bloqué.
	 */
	protected int maxWeight;
	public int getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(int max_weight) {
		this.maxWeight = max_weight;
	}

	// 
	/**
	 * Poids au dela duquel, l'ascenseur commence a avertir que la limite est bientot atteinte
	 * Ce qui est interessant ici est la prise en compte du comportement des usagers.
	 * En effet, un usager simple d'esprit montera meme si l'ascenseur indique qu'il est en alerte.
	 */
	protected int alertWeight;
	public void setAlertWeight(int alert_weight) {
		this.alertWeight = alert_weight;
	}

	// Position courante (nombre a virgule si l'ascenseur est entre 2 etages)
	protected Float currentPosition;

	protected int currentWeight = 0;

	// Booleen indiquant si l'ascenseur se dirige vers le haut
	public boolean goingToTop;

	private Thread theThread;

	/**
	 * This array has a length equal to the number of floor of the elevator's building
	 * Each index represents a floor (ie : index 0 represents the ground, and so...)
	 * If a passenger call the elevator from the floor 3, the value at the index 3 increments.
	 */
	protected ArrayList<Integer> askedFloors;
	protected ArrayList<Passenger> passengers;

	public Elevator(MainController controller) {
		constructor(controller, 500, 400, new Linear(this));
	}

	public Elevator(MainController controller, int max_persons, ElevatorStrategy strategy) {
		this.maxPersons = max_persons;
		constructor(controller, max_persons*80, (max_persons*80)-100, strategy);
	}

	public void constructor(MainController controller, int max_weight, int alert_weight, ElevatorStrategy strategy) {
		this.controller = controller;
		this.building = controller.getBuilding();
		this.maxWeight = max_weight;
		this.alertWeight = alert_weight;
		this.strategy = strategy;
		this.strategy.setElevator(this);
		this.currentPosition = 0f;
		this.goingToTop = true;
		this.askedFloors = new ArrayList<Integer>(controller.getBuilding().getFloorCount());
		this.passengers = new ArrayList<Passenger>();
		for (int i = 0; i < controller.getBuilding().getFloorCount(); i++) {
			askedFloors.add(i, 0);
		}
		theThread = new Thread(this);
	}

	// All is doing here
	public void acts() {
		// Repaint the elevator
		getView().refresh();
		Console.debug("Eleva "+identifier+" : Position => "+currentPosition);
		strategy.acts();
	}

	public void setChangedAndNotifiyObservers() {
		// On previent les eventuels ecouteurs que les valeurs du tableau
		setChanged();
		notifyObservers();
	}

	public boolean isInAlert() {
		return currentWeight > alertWeight;
	}

	public boolean call(int floor) {
		askedFloors.set(floor, askedFloors.get(floor)+1);
		//		Console.debug("Demande etage "+floor+" : "+askedFloors.get(floor));
		return true;
	}

	public boolean takePassenger(Passenger passenger) {
		return strategy.takePassenger(passenger);
	}

	public Passenger releasePassenger(Passenger passenger) {
		return strategy.releasePassenger(passenger);
	}

	public boolean isAtAFloor() {
		if(currentPosition == 0.0f) return true;
		else return currentPosition%(currentPosition.intValue()) == 0.0f;
	}

	public boolean isThereCallsAtThisFloor() {
		return !isAtAFloor() ? false : (askedFloors.get(getCurrentFloor()) > 0);
	}

	public boolean atTop() {
		return getCurrentFloor() == controller.getBuilding().getFloorCount()-1; 
	}

	public boolean atBottom() {
		return getCurrentFloor() == 0.0f;
	}

	/**
	 * Return the step (-1 or +1, for going to bottom or to top), regarding the variable goingToTop 
	 * @return -1 or +1
	 */
	public int getStep() {
		return goingToTop ? TO_TOP : TO_BOTTOM;
	}

	public boolean noCallAtAll() {
		for (int i = 0; i <= floorCount(); i++) {
			if(askedFloors.get(i) > 0) return false;
		}
		return true;
	}

	public boolean noCallOnTheWay() {
		if(goingToTop) {
			for (int i = currentPosition.intValue(); i <= floorCount(); i++) {
				if(askedFloors.get(i) > 0) return false;
			}
			return true;
		}
		else {
			for (int i = currentPosition.intValue(); i >= 0; i--) {
				if(askedFloors.get(i) > 0) return false;
			}
			return true;
		}
	}

	/**
	 * Protected methods
	 */
	//	protected boolean isInBound(float position) {
	//		if(goingToTop) {
	//			return position <= floorCount();
	//		}
	//		else {
	//			return position >= 0;
	//		}
	//	}

	public boolean isFull() {
		return getPassengerCount() >= maxPersons;
	}

	public boolean isBlocked() {
		return currentWeight >= maxWeight;
	}

	public int getPassengerCount() {
		return passengers.size();
	}

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}

	public int getCurrentWeight() {
		return currentWeight;
	}

	public void changeDirection() {
		goingToTop = !goingToTop;
	}

	public void resetCurrentFloorCalls() {
		askedFloors.set(getCurrentFloor(), 0);
	}

	public boolean goingToTop() {
		return goingToTop;
	}

	public Passenger getLastPassenger() {
		return passengers.get(passengers.size()-1);
	}

	public float getCurrentPosition() {
		return currentPosition;
	}

	public int getCurrentFloor() {
		return isAtAFloor() ? currentPosition.intValue() : Integer.MAX_VALUE;
	}

	public Building getBuilding() {
		return building;
	}

	public float getSpeedPerRound() {
		return speed_per_round;
	}

	public void moves() {
		currentPosition = getCurrentPosition()+getStep()*getSpeedPerRound();
	}

	public ArrayList<Integer> getAskedFloors() {
		return askedFloors;
	}

	private int floorCount() {
		return askedFloors.size()-1;
	}

	public void setView(ElevatorView elevator_view) {
		this.view = elevator_view;
	}

	public ElevatorView getView() {
		return view;
	}

	public void setRunning(boolean running) {
		this.running = running;
		if(running && !theThread.isAlive()) {
			System.out.println("Demarrage du thread de l'elevator "+getIdentifier());
			theThread.start();
		}
		else if(!running && theThread.isAlive()) {
			synchronized (theThread) {
				try {
					theThread.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void run() {
		while (isRunning()) {			
			Console.debug("...");
			acts();
			//			displayPassengersPerFloor(floor_count);
			try {
				java.util.concurrent.TimeUnit.MILLISECONDS.sleep(SLEEPING_DURATION);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int getWaitingPassengerCountAtFloor(int floor_index) {
		return building.getWaitingPassengerCountAtElevatorAndFloor(this, floor_index);
	}

	public int getArrivedPassengerCountAtFloor(int floor_index) {
		return building.getArrivedPassengerCountAtElevatorAndFloor(this, floor_index);
	}

	public int getMaxPersons() {
		return maxPersons;
	}
	public void addToCurrentWeight(int mass) {
		currentWeight += mass;
	}
	public void removeOfCurrentWeight(int mass) {
		currentWeight -= mass;
	}
}