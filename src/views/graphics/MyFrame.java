package views.graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

	public static int frame_width;
	public static int frame_height;
	private SimulationPanel panel;
	
    public MyFrame(int elevator_count, int floor_count) {
    	frame_width = (elevator_count*AnimatedElevator.ELEVATOR_WIDTH) + (2*FixedFloor.FLOOR_WIDTH);
    	frame_height = (floor_count)*AnimatedElevator.ELEVATOR_HEIGHT;
//    	System.out.println(frame_width+"x"+frame_height);
        setSize(frame_width, frame_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new SimulationPanel(this, getWidth(), getHeight());
        add(panel);
        pack();
        setResizable(false);
        setVisible(true);
        
        addKeyListener(new KeyListener() {
		
			@Override
			public void keyTyped(KeyEvent e) {
			}
		
			@Override
			public void keyReleased(KeyEvent e) {
			}
		
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					panel.pause();
				}
			}
		});
        
//        Insets insets = getInsets(); 
//		FRAME_WIDTH = getWidth()+insets.left+insets.right;
//		FRAME_HEIGHT = getHeight()+insets.bottom+insets.top;
//		setSize(FRAME_WIDTH, FRAME_HEIGHT);
//		System.out.println(FRAME_WIDTH+"x"+FRAME_HEIGHT);
    }
    
    public void addAnimatedObject(AnimatedObject o) {
    	panel.addAnimatedObject(o);
    }
    
    public void addFixedObject(FixedObject o) {
    	panel.addFixedObject(o);
    }
    
}