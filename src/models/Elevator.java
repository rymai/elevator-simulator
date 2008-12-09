package models;

import java.util.LinkedList;

import main.Console;
import controllers.MainController;
import statistics.Times;
import strategies.ElevatorStrategy;
import strategies.elevators.Linear;
import views.ElevatorView;
import views.graphics.AnimatedElevator;

public class Elevator {

	private static final int TO_TOP = 1;
	private static final int TO_BOTTOM = -1;

	// Pointer to his controller
	private MainController controller;
	private Building building;
//	protected ElevatorView view;

	private ElevatorStrategy strategy;
	
	private Times waitingTime;

	// C'est mieux si l'identifier est unique.
	private int identifier;
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	/**
	 *  Nombre de personnes max pouvant physiquement entrer
	 *  Au dela, l'ascenseur refuse forcement les passagers
	 */ 
	private int maxPersons = 7;

	/**
	 *  Poids au dela duquel l'ascenseur refuse de bouger, il est physiquement bloqué.
	 */
	private int maxWeight;
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
	private int alertWeight;
	public void setAlertWeight(int alert_weight) {
		this.alertWeight = alert_weight;
	}

	// Position courante
	private int currentFloor;

	private int currentWeight = 0;

	// Booleen indiquant si l'ascenseur se dirige vers le haut
	private boolean goingToTop;
	
	private int stopTime = 0;
	private int stoppedTime = 0;

	/**
	 * This array has a length equal to the number of floor of the elevator's building
	 * Each index represents a floor (ie : index 0 represents the ground, and so...)
	 * If a passenger call the elevator from the floor 3, the value at the index 3 increments.
	 */
	private LinkedList<Passenger> passengers;
	private boolean moving;
	private AnimatedElevator animatedElevator;

	public Elevator() {
		constructor(500, 400, new Linear(this));
	}

	public Elevator(int max_persons, ElevatorStrategy strategy) {
		this.maxPersons = max_persons;
		constructor(max_persons*80, (max_persons*80)-100, strategy);
	}

	public void constructor(int max_weight, int alert_weight, ElevatorStrategy strategy) {
		this.controller = MainController.getInstance();
		this.building = controller.getBuilding();
		this.maxWeight = max_weight;
		this.alertWeight = alert_weight;
		this.strategy = strategy;
		this.strategy.setElevator(this);
		this.currentFloor = 0;
		this.goingToTop = true;
		this.passengers = new LinkedList<Passenger>();
		this.moving = false;
		this.waitingTime = new Times();
	}

	// All is done here
	public void acts() {
//		Console.debug("Eleva "+identifier+" : Floor => "+currentFloor+", prochain deplacement : "+getStep());
		strategy.acts();
	}

	public boolean isInAlert() {
		return currentWeight > alertWeight;
	}

	public synchronized boolean takePassenger(Passenger passenger) {
		if(willBeBlockedWithThisMass(passenger.getTotalMass())) {
			return false;
		}
		else {
			passengers.add(passenger);
			addToCurrentWeight(passenger.getTotalMass());
			passenger.setElevator(this);
			waitingTime.addWaitingTime(passenger.getTime());
			passenger.resetTime();
			strategy.takePassenger(passenger);
			Console.debug("Un passager monte. "+passenger.getCurrentFloor()+" -> "+passenger.getWantedFloor());
			return true;
		}
	}

	public Times getWaitingTime() {
		return waitingTime;
	}
	
	public void releasePassenger(Passenger passenger) {
		if(passengers.contains(passenger)) {
			passengers.remove(passenger);
			removeOfCurrentWeight(passenger.getTotalMass());
			passenger.setElevator(null);
			waitingTime.addTripTime(passenger.getTime());
			strategy.releasePassenger(passenger);
		}
	}

	//	public boolean isAtAFloor() {
	//		if(currentPosition == 0) return true;
	//		else return currentPosition%(currentPosition.intValue()) <= 0.1f;
	//	}

	public boolean isThereCallsAtThisFloor() {
		return building.getAskedFloors().get(currentFloor) > 0;
	}

	public boolean atTop() {
		return currentFloor >= controller.getBuilding().getFloorCount(); 
	}

	public boolean atBottom() {
		return currentFloor <= 0;
	}

	/**
	 * Return the step (-1 or +1, for going to bottom or to top), regarding the variable goingToTop 
	 * @return -1 or +1
	 */
	public int getStep() {
		return goingToTop ? TO_TOP : TO_BOTTOM;
	}

//	public boolean noCallAtAll() {
////		for (int i = 0; i < building.getFloorCount(); i++) {
////			if(building.getAskedFloors().get(i) > 0) return false;
////		}
////		return true;
//		return building.allPassengersAreArrived();
//	}

	public boolean noCallOnTheWay() {
		if(goingToTop) {
			for (int i = currentFloor; i <= building.getFloorCountWithGround(); i++) {
				if(building.getWaitingPassengersCountAtFloor(i) > 0) return false;
				
				for (Passenger p : passengers) {
					if(p.getWantedFloor() == i) return false;
				}
			}
			return true;
		}
		else {
			for (int i = currentFloor; i >= 0; i--) {
				if(building.getWaitingPassengersCountAtFloor(i) > 0) return false;
				for (Passenger p : passengers) {
					if(p.getWantedFloor() == i) return false;
				}
			}
			return true;
		}
	}

	public boolean isFull() {
		return getPassengerCount() >= maxPersons;
	}

	public boolean isBlocked() {
		return currentWeight >= maxWeight;
	}

	public int getPassengerCount() {
		return passengers.size();
	}

	public LinkedList<Passenger> getPassengers() {
		return passengers;
	}

	public int getCurrentWeight() {
		return currentWeight;
	}

	public void changeDirection() {
		goingToTop = !goingToTop;
	}

	public void resetCurrentFloorCalls() {
		building.getAskedFloors().set(currentFloor, 0);
	}

	public boolean isGoingToTop() {
		return goingToTop;
	}

	public Passenger getLastPassenger() {
		return passengers.get(passengers.size()-1);
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public Building getBuilding() {
		return building;
	}

	public void setToNextFloor() {
		currentFloor += getStep();
		for (Passenger p : passengers) {
			p.setCurrentFloor(currentFloor);
		}
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
	public ElevatorStrategy getStrategy() {
		return strategy;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean move) {
		moving = move;
	}
	public boolean getMoving() {
		return moving;
	}
	public int getPassengerIndex(Person person) {
		return passengers.indexOf(person);
	}
	
	public AnimatedElevator getAnimatedElevator() {
		return animatedElevator;
	}
	
	public void setAnimatedElevator(AnimatedElevator e) {
		animatedElevator = e;
	}
	public boolean willBeBlockedWithThisMass(int mass) {
		return currentWeight + mass >= maxWeight;
	}
	
	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}
	
	public int getStoppedTime() {
		return stoppedTime;
	}

	public void setStoppedTime(int stoppedTime) {
		this.stoppedTime = stoppedTime;
	}
	
	public void incrementStopTime(int increment_amount) {
		this.stopTime += increment_amount;
	}
	public void incrementStoppedTime(int increment_amount) {
		this.stoppedTime += increment_amount;
	}
	
	public void leaveThisFloor() {
		setStopTime(0);
		setStoppedTime(0);
		// The elevator moves
		setMoving(true);
		strategy.leaveThisFloor();
	}
	
}