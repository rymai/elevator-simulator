package views.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

import statistics.WaitingTime;

class SimulationPanel extends JPanel implements Runnable {

	private ArrayList<AnimatedObject> listAnimatedObjects;
	private ArrayList<FixedObject> listFixedObjects;
	private Thread t;
	private MyFrame frame;
	private boolean inPause = false;
	private long framePerSecond = 10;

	public SimulationPanel(MyFrame f, int w, int h) {
		setSize(w, h);
		frame = f;
		listAnimatedObjects = new ArrayList<AnimatedObject>();
		listFixedObjects = new ArrayList<FixedObject>();

		// demarre le thread d'animation
		t = new Thread(this);
		t.start();
	}

	public void addFixedObject(FixedObject o) {
		listFixedObjects.add(o);
	}
	public void addAnimatedObject(AnimatedObject o) {
		listAnimatedObjects.add(o);
	}

	public void run() {
		while (!inPause) {
			for (AnimatedObject objetAnime : listAnimatedObjects) {
				// Ici on met toute l'intelligence...
				objetAnime.updateState();
			}
			repaint();
			try {
				Thread.sleep(1000/framePerSecond); // 10 images/secondes
			} catch (InterruptedException ex) {
				Logger.getLogger(SimulationPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(frame.getWidth(), frame.getHeight());
	}

	@Override
	public void paintComponent(Graphics g) {
		if ((g != null) && (getWidth() != 0)) {
			super.paintComponent(g);
			for (FixedObject fixedObject : listFixedObjects) {
				fixedObject.drawYourself(g);
			}
			for (AnimatedObject animatedObject : listAnimatedObjects) {
				animatedObject.drawYourself(g);
			}
		}
	}

	public void pause() {
		System.out.println("Pause!!!!!!!!!!!!!!!!!");
		inPause = !inPause;
		if(!inPause) {
			run();
		}
	}
	
	public void speedUp() {
		framePerSecond++;
		if(framePerSecond>50) framePerSecond = 50;
		System.out.println("framePerSecond : "+framePerSecond);
	}

	public void speedDown() {
		framePerSecond--;
		if(framePerSecond<=0) framePerSecond = (long) 0.1;
		System.out.println("framePerSecond : "+framePerSecond);
	}

	public void displayWaitingTime() {
		System.out.println("Temps moyen d'attente : "+WaitingTime.average());
	}
	
}