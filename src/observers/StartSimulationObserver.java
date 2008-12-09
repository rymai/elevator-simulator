package observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.graphics.ConfigView;

import controllers.MainController;
/**
 * 
 * @author x_nem
 */
public class StartSimulationObserver implements ActionListener {

	private ConfigView window;
	
	public StartSimulationObserver(ConfigView f) {
		this.window = f;
	}
	
	public void actionPerformed(ActionEvent e) {
		window.setVisible(false);
		try {
			MainController.getInstance().startSimulation(window.get_floor_count(), 
					window.get_elevator_count(), window.get_person_per_elevator_count(), 
					window.get_person_count(), window.get_group_count(), window.getElevatorStrategy());
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
