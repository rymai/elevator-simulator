package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import models.Elevator;

/**
 * Classe AnimatedElevator dessine l'ascenseur
 * @author x_nem
 */
public class AnimatedElevator extends AnimatedObject {

    public static int ELEVATOR_WIDTH = 100;
    public static int ELEVATOR_HEIGHT = 50;
    public static final int BETWEEN_2_FLOORS_DURATION = 10;
    private Elevator elevator;
    private int animationStep;

    /**
     * Constructeur 
     * @param fenetre
     * @param e     Elevator
     * @param x     placement en x
     * @param y     placement en y
     */
    public AnimatedElevator(MyFrame fenetre, Elevator e, int x, int y) {
        super(fenetre, x, y);
        elevator = e;
        speedX = 0;
        speedY = ELEVATOR_HEIGHT / BETWEEN_2_FLOORS_DURATION;
        animationStep = BETWEEN_2_FLOORS_DURATION;
        System.out.println("y : " + y);
    }

    /**
     * Méthode mettant à jour l'état graphique et signale au modèle le déplacement l'arrivé d'un étage
     * et passe la main au modèle pour qu'il agisse
     */
    public void updateState() {
        // L'animation est arrive a un etage, on avertir l'elevator
        // On reset l'animationStep si l'elevator veut repartir
//    	System.out.println(animationStep);
        if (animationStep == BETWEEN_2_FLOORS_DURATION) {
            if (elevator.getMoving()) {
                // L'ascenseur pense encore qu'il bouge alors qu'il est arrive,
                // on lui dit alors de mettre a jour son etage
                elevator.setToNextFloor();
            }

            // On lui dit d'agir (il peut tres bien decider de repartir tout de suite en placant
            // la variable 'moving' a true).
            elevator.acts();

            // L'ascenseur veut repartir
            if (elevator.getMoving()) {
                animationStep = 0;
            }
        }

        // Tant qu'on est pas arrive a un etage, on bouge
        if (elevator.getMoving() && (animationStep != BETWEEN_2_FLOORS_DURATION)) {
            animationStep++; // On est entre 2 etages, on se deplace
            y -= elevator.getStep() * speedY;
        }
    }

    public void drawYourself(Graphics g) {
        if (g != null) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, ELEVATOR_WIDTH, ELEVATOR_HEIGHT);
            g.drawString(elevator.getCurrentWeight() + "kg", x + (ELEVATOR_WIDTH / 2) - 30, y + (ELEVATOR_HEIGHT / 2) - 10);
        }
    }
}