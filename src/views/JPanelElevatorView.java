package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Console;
import models.Building;
import models.Elevator;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import controllers.MainController;

public class JPanelElevatorView extends ElevatorView {

//	private static final int ELEVATOR_WIDTH = 100;
//	private static final int ELEVATOR_HEIGHT = 100;
	private JPanel elevatorView;
	
	public JPanelElevatorView(MainController controller, Elevator elevator, Building building, int identifier) {
		super(controller, elevator, building, identifier);
		
		JFrame frame = ((JFrameBuildingView)building.getView()).getBuildingView();
		System.out.println(building.getView().toString());
		int elevator_width = (int)frame.getWidth()/building.getElevatorCount();
		int floor_height = (int)frame.getHeight()/building.getFloorCount();
		Console.debug_variable("elevator_width", elevator_width);
		Console.debug_variable("floor_height", floor_height);
		
		JPanel background = new JPanel();
		background.setLayout(null);
		background.setBackground(Color.RED);
		
		elevatorView = new JPanel();
		Random rand = new Random();
		elevatorView.setBackground(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		elevatorView.setBounds(0, frame.getHeight()-floor_height, elevator_width, floor_height);
		
		background.add(elevatorView, null);
		frame.add(background, identifier);
		frame.validate();
	}
	
	@Override
	public void close() {
		this.elevatorView.setVisible(false);
	}

	@Override
	public void display() {
		this.elevatorView.setVisible(true);
		Console.debug("Affichage de la vue ascenseur "+identifier+" de taille : "+elevatorView.getWidth()+"x"+elevatorView.getHeight());
	}

	public Component getElevator() {
		return elevatorView;
	}

	@Override
	public void moveBy(int x, int y) {
//		this.elevator.setLocation(this.elevator.getLocation().x + x, this.elevator.getLocation().y + y);
	}

}
