package views.graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Class abstraite parente de FixedObject et AnimatedObject
 * Un objet GraphicObject possede deux attributs : x et y
 * Une classe non-abstraite heritant de GraphicObject doit implementer une methode dessineToi
 * 
 * @author remy
 *
 */
public abstract class GraphicObject {

	protected int x, y;
	protected Color color;
	
	public GraphicObject(int x, int y) {
		this.x = x;
        this.y = y;
	}
    
	public abstract void drawYourself(Graphics g);
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}