package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class FixedFloor extends FixedObject {

	public static final int FLOOR_WIDTH = 150;
	
	public FixedFloor(int x, int y) {
		super(x, y);
		Random rand = new Random();
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
	}

	public void drawYourself(Graphics g) {
		if (g != null)  {
			g.setColor(color);
            g.fillRect(x, y, FixedFloor.FLOOR_WIDTH, AnimatedElevator.ELEVATOR_HEIGHT);
        }
	}

}