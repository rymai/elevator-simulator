package main;

import java.net.MalformedURLException;
import javax.swing.JFrame;
import views.graphics.ConfigView;

public class Launcher extends JFrame {
	
	private static final long serialVersionUID = -6717947917966144726L;

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(final String[] args) throws MalformedURLException {
		new ConfigView();
	}
	
}