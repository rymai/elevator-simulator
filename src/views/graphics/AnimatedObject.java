package views.graphics;

abstract public class AnimatedObject extends GraphicObject {
    
    protected int speedX, speedY;

    public AnimatedObject(int x, int y) {
    	super(x, y);
    }
    
    public int getSpeedX() {
        return speedX;
    }
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
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
    
    public abstract void updateState();
    
}