package observers;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderUpdateObserver implements ChangeListener {

	private JLabel label;
	
	public SliderUpdateObserver(JLabel label) {
		this.label = label;
	}
	
	public void stateChanged(ChangeEvent e) {
		JSlider js = (JSlider) e.getSource();
		this.label.setText(Integer.toString(js.getValue()));
	}

}