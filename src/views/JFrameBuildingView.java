package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Console;
import models.Building;
import controllers.MainController;

public class JFrameBuildingView extends BuildingView implements ActionListener {

	private JFrame window;
	private ArrayList<ElevatorView> elevatorViews;
	private static int FRAME_WIDTH = 800;
	private static int FRAME_HEIGHT = 600;
	
	public JFrameBuildingView(MainController controller, Building building) {
		super(controller, building);
		this.window = new JFrame("Batiment");
		
		window.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.CYAN);
		window.setResizable(false);
		System.out.println("fenetre lancée");
		
		// 1 row, x cols (x is the number of elevators)		
		window.setLayout(new GridLayout(1, building.getElevatorCount()));
		
		Insets insets = window.getInsets(); 
		FRAME_WIDTH = window.getWidth()+insets.left+insets.right;
		FRAME_HEIGHT = window.getHeight()+insets.bottom+insets.top;
		window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		System.out.println(window.getLayout().toString());
	}
	
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

	@Override
	public void refresh() {
		window.validate();
	}

}