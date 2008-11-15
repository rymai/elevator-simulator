package models;

import java.util.ArrayList;
import java.util.Observable;
import main.Console;
import controllers.MainController;
import strategies.Strategy;
import strategies.elevators.Linear;
import views.ElevatorView;

public class Elevator extends Observable implements Runnable {
	
	private static final int TO_TOP = 1;
	private static final int TO_BOTTOM = -1;
	private static int SLEEPING_DURATION = 1000;
	

	// Pointeur to his controller
	protected MainController controller;
	protected Building building;
	protected ElevatorView view;
	
	protected Strategy strategy;
	
	protected float speed_per_round = 0.5f;
	
	// C'est mieux si l'identifier est unique.
	protected String identifier;
	
	private boolean running = false;
	
	// Nombre de personnes max pouvant entrer (entre en compte dans le comportement des usagers,
	// un usager simple d'esprit montera meme si l'ascenseur ˆ atteint sa limite)
	protected int maxPerson = 5;
	
	// Poids au dela duquel l'ascenseur refuse de bouger.
	protected int maxWeight;
	
	// Poids au dela duquel, l'ascenseur commence a avertir que la limite est bientot atteinte
	protected int alertWeight;
	
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
		constructor(controller, 500, 400, new Linear());
	}
	
	public Elevator(MainController controller, int max_persons, Strategy strategy) {
		this.maxPerson = max_persons;
		constructor(controller, max_persons*80, (max_persons*80)-100, strategy);
	}
	
	public void constructor(MainController controller, int max_weight, int alert_weight, Strategy strategy) {
		this.controller = controller;
		this.building = controller.getBuilding();
		this.maxWeight = max_weight;
		this.alertWeight = alert_weight;
		this.strategy = strategy;
		this.currentPosition = 0f;
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
		
		Console.debug("Ascenseur "+identifier+" arrive a la position: "+currentPosition);
//		Console.debug("strategy : "+strategy.getClass().toString());
		strategy.acts(this);
	}
	
	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int max_weight) {
		this.maxWeight = max_weight;
	}

	public void setAlertWeight(int alert_weight) {
		this.alertWeight = alert_weight;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public float getCurrentFloor() {
		if(isAtAFloor()) return currentPosition.intValue();
		else return currentPosition;
	}
	
	public boolean isInAlert() {
		return currentWeight > alertWeight;
	}
	
	public void setChangedAndNotifiyObservers() {
		// On previent les eventuels ecouteurs que les valeurs du tableau
		setChanged();
		notifyObservers();
	}

	public boolean call(int floor) {
		askedFloors.set(floor, askedFloors.get(floor)+1);
		Console.debug("Demande etage "+floor+" : "+askedFloors.get(floor));
		return true;
	}
	
	public boolean takePassenger(Passenger passenger) {
			passengers.add(passenger);
			Console.debug((currentWeight+passenger.getTotalMass())+" = "+currentWeight+" + "+passenger.getTotalMass());
			currentWeight += passenger.getTotalMass();
//			if(isInAlert())
//				Console.debug("L'ascenseur "+getIdentifier()+" est en alerte, le passager monte.");
//			if(isBlocked())
//				Console.debug("L'ascenseur "+getIdentifier()+" est bloquŽ, le passager monte.");
			return true;
	}

	public void releasePassenger(Passenger passenger) {
		int index = passengers.indexOf(passenger);
		if(index != -1) {
			passengers.remove(index);
			Console.debug((currentWeight-passenger.getTotalMass())+" = "+currentWeight+" - "+passenger.getTotalMass());
			currentWeight -= passenger.getTotalMass();
		}
	}
	
	/**
	 * Protected methods
	 */
	public boolean isAtAFloor() {
		return (currentPosition.intValue() == 0) || (currentPosition%(currentPosition.intValue()) == 0);
	}
	
	public boolean atTop() {
		return isAtAFloor() && currentPosition == controller.getBuilding().getFloorCount()-1; 
	}
	
	public boolean atBottom() {
		return isAtAFloor() && currentPosition == 0.0f; 
	}
	
	public boolean isThereCallsAtThisFloor() {
		if(!isAtAFloor()) return false;
		return askedFloors.get(currentPosition.intValue()) > 0;
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

	protected boolean isInBound(float position) {
		if(goingToTop) {
			return position <= floorCount();
		}
		else {
			return position >= 0;
		}
	}

	/**
	 * Return the step (-1 or +1, for going to bottom or to top), regarding the variable goingToTop 
	 * @return -1 or +1
	 */
	public int getStep() {
		return goingToTop ? TO_TOP : TO_BOTTOM;
	}
	
	protected boolean isFull() {
		return getPassengerCount() >= maxPerson;
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

	public void resetCurrentFloor() {
		askedFloors.set((int)getCurrentFloor(), 0);
	}

	public boolean getGoingToTop() {
		return goingToTop;
	}

	public Passenger getLastPassenger() {
		return passengers.get(passengers.size()-1);
	}

	public float getCurrentPosition() {
		return currentPosition;
	}

	public float getSpeedPerRound() {
		return speed_per_round;
	}

	public void setCurrentPosition(float f) {
		currentPosition = f;
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
			System.out.println("Demarrage du thread.");
			theThread.start();
		}
		else if(!running && theThread.isAlive()) {
			try {
				theThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
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
			}
		}
	}
	
}