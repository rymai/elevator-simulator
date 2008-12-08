/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views.graphics;

import java.awt.Graphics;

public class PersonneGraphique extends AnimatedObject {

    public int nbFrames = 20;
    public int currentFrame = 0;
    private int imageAdessiner;

    public PersonneGraphique(MyFrame f, int x, int y) {
        super(f, x, y);
        speedX = 10;
        speedY = 2;
    }

    public void updateState() {
        x += speedX;
        y += speedY;

        if ((x > 640) || (x < 0)) {
            speedX = -speedX;
        }
        if ((y > 480) || (y < 0)) {
            speedY = -speedY;
        }
    }

    public void drawYourself(Graphics g) {
        if (g != null) {
            currentFrame++;

            if (currentFrame == nbFrames) {
                imageAdessiner++;
                if(imageAdessiner == 2) imageAdessiner = 0;
                currentFrame = 0;
            }

            if (imageAdessiner == 0) {
                g.drawOval(x, y, 10, 10);

            } else if (imageAdessiner == 1) {
                g.drawRect(x, y, 10, 10);

            }
           
        }
    }
}


