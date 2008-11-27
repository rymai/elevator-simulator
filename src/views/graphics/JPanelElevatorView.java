package views.graphics;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import views.ElevatorView;
import views.FloorView;
import main.Console;
import models.Elevator;

public class JPanelElevatorView extends ElevatorView implements InterfaceSwing {

	private JFrame frame;
	private JPanel elevatorView;
	private JLabel labelPassengersInElevator;
	
	private int elevatorWidth;
	public int getElevatorWidth() {
		return elevatorWidth;
	}
	
	private int floorHeight;
	public int getFloorHeight() {
		return floorHeight;
	}

	private ArrayList<JPanelFloorView> startFloors;
	private ArrayList<JPanelFloorView> endFloors;
	
	public JPanelElevatorView(Elevator elevator, int identifier) {
		super(elevator, identifier);
		
		frame = ((JFrameBuildingView)elevator.getBuilding().getView()).getBuildingView();
		elevatorWidth = (int)(frame.getWidth()/elevator.getBuilding().getElevatorCount())/3;
		floorHeight = (int)frame.getHeight()/elevator.getBuilding().getFloorCount();
		startFloors = new ArrayList<JPanelFloorView>(elevator.getBuilding().getFloorCount());
		endFloors = new ArrayList<JPanelFloorView>(elevator.getBuilding().getFloorCount());
		int floor_count = elevator.getBuilding().getFloorCount();
		
		JPanel background = new JPanel();
		background.setLayout(null);
		background.setBackground(Color.WHITE);
		
		Random rand = new Random();
		for (int i = 0; i < floor_count; i++) {
			Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			Console.debug("y de base : "+Integer.toString((frame.getHeight()-floorHeight)));
			Console.debug("y pour i="+i+" : "+Integer.toString((frame.getHeight()-floorHeight)-(i*floorHeight)));
			startFloors.add(new JPanelFloorView(elevator, i, FloorView.START_FLOOR, 0, (frame.getHeight()-floorHeight)-(i*floorHeight), elevatorWidth, floorHeight, c));
		}
		
		elevatorView = new JPanel();
		elevatorView.setBackground(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		elevatorView.setBounds(elevatorWidth, frame.getHeight()-floorHeight, elevatorWidth, floorHeight);
		
		for (int i = 0; i < floor_count; i++) {
			Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			endFloors.add(new JPanelFloorView(elevator, i, FloorView.END_FLOOR, 2*elevatorWidth, (frame.getHeight()-floorHeight)-(i*floorHeight), elevatorWidth, floorHeight, c));
		}
		
		labelPassengersInElevator = new JLabel("0", SwingConstants.CENTER);
		elevatorView.add(labelPassengersInElevator);
		
		for (int i = 0; i < floor_count; i++) {
			background.add(startFloors.get(i).getComponent(), null);
			background.add(endFloors.get(i).getComponent(), null);
		}
		background.add(elevatorView, null);
		frame.add(background, identifier);
		frame.validate();
	}
	
	public void close() {
		this.elevatorView.setVisible(false);
		for (int i = 0; i < elevator.getBuilding().getFloorCount(); i++) {
			startFloors.get(i).close();
			endFloors.get(i).close();
		}
	}

	public void display() {
		this.elevatorView.setVisible(true);
		for (int i = 0; i < elevator.getBuilding().getFloorCount(); i++) {
			startFloors.get(i).display();
			endFloors.get(i).display();
		}
		Console.debug("Affichage de la vue ascenseur "+identifier+" de taille : "+elevatorView.getWidth()+"x"+elevatorView.getHeight());
	}

	public void refresh() {
//		Console.debug("refresh d'elevator, currentPosition  : "+elevator.getCurrentPosition());
		elevatorView.repaint();
		move(elevator.getCurrentPosition());
		displayPassengerInElevator();
	}

	private void displayPassengerInElevator() {
		String t = "";
		for (int i = 0; i < elevator.getPassengerCount(); i++) {
			t += "|";
		}
		if(t == "") t = "___";
		labelPassengersInElevator.setText(t);
	}

	@Override
	public void move(float new_y_position) {
//		System.out.println("Nouvelle position : "+calculateNewPosition(newYPosition));
		elevatorView.setLocation(elevatorView.getX(), calculateNewPosition(new_y_position));
	}

	private int calculateNewPosition(float new_y_position) {
		return (int) (((elevator.getBuilding().getFloorCount()-new_y_position)*floorHeight)-floorHeight);
	}

	public Component getComponent() {
		return elevatorView;
	}

	@Override
	public void refreshFloor(int floor_index) {
		startFloors.get(floor_index).refresh();
		endFloors.get(floor_index).refresh();
	}

}
