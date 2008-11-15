package main;
import java.util.ArrayList;

import javax.swing.JFrame;

import controllers.MainController;

import views.*;
import models.*;

public class Lanceur extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Elevator> listeAscenseurs = new ArrayList<Elevator>();
		
		MainController controller = MainController.getInstance();
		
		JFrameConfigView cv = new JFrameConfigView();
	}
}