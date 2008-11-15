package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Console;
import models.Building;
import controllers.MainController;

public class JFrameBuildingView extends BuildingView implements ActionListener {

	private JFrame window;
	private ArrayList<ElevatorView> elevatorViews;
	private static int FRAME_WIDTH = 1000;
	private static int FRAME_HEIGHT = 700;
	
	public JFrameBuildingView(MainController controller, Building building) {
		super(controller, building);
		this.window = new JFrame("Batiment");
		
		window.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.CYAN);
		window.setResizable(false);
//		window.validate();
		
		Insets insets = window.getInsets(); 
		FRAME_WIDTH = window.getWidth()+insets.left+insets.right;
		FRAME_HEIGHT = window.getHeight()+insets.bottom+insets.top;
		window.setSize(window.getWidth(), window.getHeight());
		System.out.println(window.getLayout().toString());
		
		// 1 row, x cols (x is the number of elevators)		
		window.setLayout(new GridLayout(1, building.getElevatorCount()));
	}
	
//	public void insertElevatorsView(ArrayList<Elevator> elevators) {
//		System.out.println("Nbre ascenseurs : "+elevators.size());
//		int elevator_count = building.getElevatorCount();
//		int floor_count = building.getFloorCount();
//		int elevator_width = (int)LARGEUR_FENETRE/elevator_count;
//		int floor_height = (int)HAUTEUR_FENETRE/floor_count;
//		
//		Dimension elevator_dimensions = new Dimension();
//		elevator_dimensions.height = floor_height;
//		elevator_dimensions.width = elevator_width;
//		
//
//		JPanel background = null;
//		Elevator elevator;
//		ElevatorView elevator_view;
//		for (int i = 0; i < elevator_count; i++) {
//			Console.debug("i : "+i);
//			elevator = elevators.get(i);
//			elevator_view = elevator.getView();
//			
//			background = new JPanel();
//			background.setLayout(null);
//			background.setBackground(Color.RED);
//			background.add(((JPanelElevatorView) elevator_view).getElevator(), null);
//			((JPanelElevatorView) elevator_view).getElevator().setBackground(Color.BLUE);
//			System.out.println(HAUTEUR_FENETRE-floor_height);
//			((JPanelElevatorView) elevator_view).getElevator().setBounds(0, HAUTEUR_FENETRE-floor_height, elevator_width, floor_height);
//			window.add(background, i);
//		}
//	}
	
	@Override
	public void close() {
		this.window.setVisible(false);
	}

	@Override
	public void display() {
		this.window.setVisible(true);
	}

	@Override
	public void addElevator(ElevatorView elevatorView) {
		this.window.getContentPane().add((Component)elevatorView.getElevator());
	}
	
	public JFrame getBuildingView() {
		return window;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed : " + e.getSource());
	}

}