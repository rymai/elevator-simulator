package views.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import views.BuildingView;
import views.ElevatorView;
import views.FloorView;
import models.Building;
import controllers.MainController;

public class JFrameBuildingView extends BuildingView implements ActionListener, InterfaceSwing {

	private JFrame buildingView;
	private ArrayList<JPanelFloorView> startFloors;
	private ArrayList<JPanelFloorView> endFloors;
	private ArrayList<ElevatorView> elevatorViews;
	private int frameWidth;
	private int frameHeight;
	private int floorWidth;
	private int floorHeight;
	private static int ELEVATOR_WIDTH = 100;
	private static int ELEVATOR_HEIGHT = 45;
	
	public JFrameBuildingView(MainController controller, Building b) {
		super(controller, b);
		this.buildingView = new JFrame("Batiment");
		
		frameWidth = building.getElevatorCount()*(ELEVATOR_WIDTH+(JFrameConfigView.MAX_ELEVATOR_COUNT-building.getElevatorCount())*12);
		frameHeight = building.getFloorCount()*(ELEVATOR_HEIGHT+(JFrameConfigView.MAX_FLOOR_COUNT-building.getFloorCount())*5);
		
		buildingView.setSize(frameWidth,frameHeight);
		buildingView.setLocationByPlatform(true);
		buildingView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildingView.setResizable(false);
		
		// 1 row, x cols (x is the number of elevators)		
		buildingView.setLayout(new GridLayout(1, building.getElevatorCount()+2));
		
		Insets insets = buildingView.getInsets(); 
		frameWidth = buildingView.getWidth()+insets.left+insets.right;
		frameHeight = buildingView.getHeight()+insets.bottom+insets.top;
		buildingView.setSize(frameWidth, frameHeight);
		
		startFloors = new ArrayList<JPanelFloorView>(building.getFloorCount());
		endFloors = new ArrayList<JPanelFloorView>(building.getFloorCount());
		
		floorWidth = (int)(frameWidth/building.getElevatorCount());
		floorHeight = (int)(frameHeight/building.getFloorCount());
	}
	
	public void addStartsFloors() {
		Random rand = new Random();
		JPanel startFloorsPanel = new JPanel();
		startFloorsPanel.setLayout(null);
		JPanelFloorView temp_jpanel_floor_view;
		for (int i = 0; i < building.getFloorCount(); i++) {
			Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			temp_jpanel_floor_view = new JPanelFloorView(building, i, FloorView.START_FLOOR, 0, (frameHeight-floorHeight)-(i*floorHeight), floorWidth, floorHeight, Color.GREEN); 
			startFloors.add(temp_jpanel_floor_view);
			startFloorsPanel.add(temp_jpanel_floor_view.getComponent(), null);
		}
		buildingView.add(startFloorsPanel, 0);
	}
	
	public void addEndsFloors() {
		Random rand = new Random();
		JPanel endFloorsPanel = new JPanel();
		endFloorsPanel.setLayout(null);
		JPanelFloorView temp_jpanel_floor_view;
		for (int i = 0; i < building.getFloorCount(); i++) {
			Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			temp_jpanel_floor_view = new JPanelFloorView(building, i, FloorView.END_FLOOR, 0, (frameHeight-floorHeight)-(i*floorHeight), floorWidth, floorHeight, Color.RED); 
			endFloors.add(temp_jpanel_floor_view);
			endFloorsPanel.add(temp_jpanel_floor_view.getComponent(), null);
		}
		buildingView.add(endFloorsPanel, building.getElevatorCount()+1);
	}
	
	public void close() {
		this.buildingView.setVisible(false);
	}

	public void display() {
		for (int i = 0; i < building.getFloorCount(); i++) {
			startFloors.get(i).display();
			endFloors.get(i).display();
		}
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
	
	@Override
	public void refreshFloor(int floor_index) {
		startFloors.get(floor_index).refresh();
		endFloors.get(floor_index).refresh();
	}

}