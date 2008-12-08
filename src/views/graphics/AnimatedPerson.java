package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import controllers.MainController;
import models.Person;

public class AnimatedPerson extends AnimatedObject {

	private static int head_width = 13;
	private static int head_height = 13;
	private static int eye_width = 2;
	private static int eye_height = 2;
	private static int shoulder_height = 3;
	private static int arm_width = 7;
	private static int body_width = 10;
	private static int body_height = 8;
	private static int leg_width = 10;
	private static int leg_height = 5;
	private Person person;
	private Color head_color;
	private Color eye_color;
	
	public static final int PERSON_WIDTH = arm_width*2 + 2;
	public static final int PERSON_HEIGHT = head_height + body_width + leg_height -2;
	
	public AnimatedPerson(MyFrame frame, Person person, int x, int y) {
		super(frame, x, y);
		this.person = person;
		Random rand = new Random();
		head_color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		eye_color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
	}

	@Override
	public void updateState() {
		if(person.isInTheElevator()) {
			x = person.getElevator().getAnimatedElevator().getX()+AnimatedElevator.ELEVATOR_WIDTH-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*person.getElevator().getPassengerIndex(person));
			y = person.getElevator().getAnimatedElevator().getY()+AnimatedElevator.ELEVATOR_HEIGHT-PERSON_HEIGHT;
		}
		else {
			if(person.isArrived()) { // Is arrived
				x = MyFrame.frame_width-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*MainController.getInstance().getBuilding().getPassengerIndexAtHisFloor(person));
			}
			else { // Is waiting
				x = FixedFloor.FLOOR_WIDTH-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*MainController.getInstance().getBuilding().getPassengerIndexAtHisFloor(person));
			}
		}
	}

	@Override
	public void drawYourself(Graphics g) {
		Random rand = new Random();
		
		g.setColor(head_color);
		// Head
		g.fillOval(x, y, head_width, head_height);
		
		g.setColor(eye_color);
		// Left eye
		g.fillOval(x+(head_width/2)-head_width/4, y+head_height/5+1, eye_width, eye_height);
		
		// Right eye
		g.fillOval(x+(head_width/2)+head_width/4, y+head_height/5+1, eye_width, eye_height);
		
		g.setColor(Color.RED);
		// Smile
		g.drawArc(x+head_width/3-1, y+head_height/3, head_width/2, head_height/2, 0, -180);
		
		g.setColor(Color.BLACK);
		// Body
		g.drawLine(x+(head_width/2), y+head_height, x+(head_width/2), y+head_height+body_height);
		
		// Arms
		g.drawLine(x+(head_width/2)-arm_width, y+head_height+shoulder_height, x+(head_width/2)+arm_width, y+head_height+shoulder_height);
		
		// Left leg
		g.drawLine(x+(head_width/2), y+head_height+body_height, x, y+head_height+body_height+leg_height);
		
		// Right leg
		g.drawLine(x+(head_width/2), y+head_height+body_height, x+head_width, y+head_height+body_height+leg_height);
	}

}