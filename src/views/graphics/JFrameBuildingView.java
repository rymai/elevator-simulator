package views.graphics;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import views.BuildingView;
import views.ElevatorView;
import models.Building;
import controllers.MainController;

public class JFrameBuildingView extends BuildingView implements ActionListener, InterfaceSwing {

	private JFrame buildingView;
	private ArrayList<ElevatorView> elevatorViews;
	private static int frame_width;
	private static int frame_height;
	private static int ELEVATOR_WIDTH = 100;
	private static int ELEVATOR_HEIGHT = 45;
	
	public JFrameBuildingView(MainController controller, Building building) {
		super(controller, building);
		this.buildingView = new JFrame("Batiment");
		
		frame_width = building.getElevatorCount()*(ELEVATOR_WIDTH+(JFrameConfigView.MAX_ELEVATOR_COUNT-building.getElevatorCount())*12);
		frame_height = building.getFloorCount()*(ELEVATOR_HEIGHT+(JFrameConfigView.MAX_FLOOR_COUNT-building.getFloorCount())*5);
		
		buildingView.setSize(frame_width,frame_height);
		buildingView.setLocationByPlatform(true);
		buildingView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildingView.setResizable(false);
		
		// 1 row, x cols (x is the number of elevators)		
		buildingView.setLayout(new GridLayout(1, building.getElevatorCount()));
		
		Insets insets = buildingView.getInsets(); 
		frame_width = buildingView.getWidth()+insets.left+insets.right;
		frame_height = buildingView.getHeight()+insets.bottom+insets.top;
		buildingView.setSize(frame_width, frame_height);
	}
	
	public void close() {
		this.buildingView.setVisible(false);
	}

	public void display() {
		this.buildingView.setVisible(true);
		this.buildingView.validate();
	}
	
	public JFrame getBuildingView() {
		return buildingView;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed : " + e.getSource());
	}

	public void refresh() {
		buildingView.validate();
	}

	public Component getComponent() {
		return buildingView;
	}

}