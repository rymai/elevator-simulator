package main;
import java.util.ArrayList;

import javax.swing.JFrame;

import controllers.MainController;

import views.*;
import models.*;

public class Launcher extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717947917966144726L;
	private static ArrayList<Elevator> listeAscenseurs;
	private static MainController controller;
	private static JFrameConfigView cv;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		listeAscenseurs = new ArrayList<Elevator>();
		
		controller = MainController.getInstance();
		
		cv = new JFrameConfigView();
	}
}