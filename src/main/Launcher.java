package main;

import java.net.MalformedURLException;
import javax.swing.JFrame;
import views.graphics.ConfigView;

public class Launcher extends JFrame {
	
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		new ConfigView();
	}
}