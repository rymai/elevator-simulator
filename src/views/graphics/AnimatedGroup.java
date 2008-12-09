package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;

import models.Group;
import models.Person;

import controllers.MainController;

public class AnimatedGroup extends AnimatedObject {

	private Group group;
	private LinkedList<AnimatedPerson> animatedPersons = new LinkedList<AnimatedPerson>();
	
	public AnimatedGroup(MyFrame frame, Group group, int x, int y) {
		super(frame, x, y);
		this.group = group;
	}

	@Override
	public void updateState() {
		if(group.isInTheElevator()) {
			x = group.getElevator().getAnimatedElevator().getX()+AnimatedElevator.ELEVATOR_WIDTH-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*group.getElevator().getPassengerIndex(group));
			y = group.getElevator().getAnimatedElevator().getY()+AnimatedElevator.ELEVATOR_HEIGHT-AnimatedPerson.PERSON_HEIGHT;
		}
		else {
			if(group.isArrived()) { // Is arrived
				x = MyFrame.frame_width-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*MainController.getInstance().getBuilding().getPassengerIndexAtHisFloor(group));
			}
			else { // Is waiting
				x = FixedFloor.FLOOR_WIDTH-AnimatedPerson.PERSON_WIDTH-(AnimatedPerson.PERSON_WIDTH*MainController.getInstance().getBuilding().getPassengerIndexAtHisFloor(group));
			}
		}
	}

	@Override
	public void drawYourself(Graphics g) {
		if(group.getMood() == 1)
			g.setColor(Color.GREEN);
		else if(group.getMood() == 2)
			g.setColor(Color.ORANGE);
		else if(group.getMood() == 3)
			g.setColor(Color.RED);
		// Head
		g.fillOval(x, y, AnimatedPerson.head_width, AnimatedPerson.head_height);
		
		// Body
		g.drawLine(x+(AnimatedPerson.head_width/2), y+AnimatedPerson.head_height, x+(AnimatedPerson.head_width/2), y+AnimatedPerson.head_height+AnimatedPerson.body_height);
		
		// Arms
		g.drawLine(x+(AnimatedPerson.head_width/2)-AnimatedPerson.arm_width, y+AnimatedPerson.head_height+AnimatedPerson.shoulder_height, x+(AnimatedPerson.head_width/2)+AnimatedPerson.arm_width, y+AnimatedPerson.head_height+AnimatedPerson.shoulder_height);
		
		// Left leg
		g.drawLine(x+(AnimatedPerson.head_width/2), y+AnimatedPerson.head_height+AnimatedPerson.body_height, x, y+AnimatedPerson.head_height+AnimatedPerson.body_height+AnimatedPerson.leg_height);
		
		// Right leg
		g.drawLine(x+(AnimatedPerson.head_width/2), y+AnimatedPerson.head_height+AnimatedPerson.body_height, x+AnimatedPerson.head_width, y+AnimatedPerson.head_height+AnimatedPerson.body_height+AnimatedPerson.leg_height);
		
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(group.getPersonCount()), x+AnimatedPerson.head_width/3-1, y+AnimatedPerson.head_height-1);
	}

}
