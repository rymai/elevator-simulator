package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

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
	
	private Elevator elevator;
	private JPanel elevatorView;
	
	public JPanelElevatorView(MainController controller, Elevator elevator, Building building, int identifier) {
		super(controller, elevator, building, identifier);
		
		JFrame frame = ((JFrameBuildingView)building.getView()).getBuildingView();
		int elevator_width = (int)frame.getWidth()/building.getElevatorCount();
		int floor_height = (int)frame.getHeight()/building.getFloorCount();
		
		this.elevatorView = new JPanel();
		this.elevatorView.setSize(elevator_width, floor_height);
		elevatorView.setBackground(Color.YELLOW);
		frame.add(elevatorView, 1);
		frame.setBackground(Color.BLACK);
		frame.add(new JButton("test2"), 2);
		frame.getContentPane().add(new JButton("test3"), 3);
		frame.validate();
		
//		JPanel background = new JPanel();
//		background.setLayout(null);
//		background.setBackground(Color.RED);
//		elevatorView = new JPanel();
//		background.add(elevatorView, null);
//		elevatorView.setBackground(Color.BLUE);
//		System.out.println(frame.getHeight()-floor_height);
//		elevatorView.setBounds(0, frame.getHeight()-floor_height, elevator_width, floor_height);
//		Console.debug("background : "+background.getWidth()+"x"+background.getHeight());
//		frame.add(background, identifier);
		
//		JPanel JPanel = new JPanel();
//		background.setLayout(null);
//		background.setBackground(Color.RED);
//		elevatorView.setBackground(Color.BLUE);
//		elevatorView.setBounds(0, frame.getHeight()-floor_height, elevator_width, floor_height);
//		background.add(elevatorView, null);
//		Console.debug("background : "+background.getWidth()+"x"+background.getHeight());
//		
//		frame.add(background, identifier);
//		
//		System.out.println("Position de l'elevator: x => " + (parent.getSize().width-ELEVATOR_WIDTH) +
//		",y => " + (parent.getSize().height-ELEVATOR_HEIGHT));
//		this.elevator.setLocation(parent.getSize().width-ELEVATOR_WIDTH, parent.getSize().height-ELEVATOR_HEIGHT);
//		Graphics g = this.parent.getGraphics();
//		g.setColor(Color.BLUE);
//		g.fillRect(parent.getSize().width-ELEVATOR_WIDTH, parent.getSize().height-ELEVATOR_HEIGHT, ELEVATOR_WIDTH, ELEVATOR_HEIGHT);
//		g.fillRect(0, 0, 500, 500);
//		this.parent.validate();
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
