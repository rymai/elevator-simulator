package views.graphics;

import java.awt.*;

import javax.swing.*;

import observers.*;

public class JFrameConfigView extends JFrame implements InterfaceSwing {

	private static final long serialVersionUID = 8164118974463460991L;
	private static final String POLICE = "Monaco";
	private static final int MIN_FLOOR_COUNT = 2;
	public static final int MAX_FLOOR_COUNT = 16;
	private static final int MIN_ELEVATOR_COUNT = 1;
	public static final int MAX_ELEVATOR_COUNT = 10;
	private static final int MIN_PERSON_COUNT = 1;
	private static final int MAX_PERSON_COUNT = 100;
	private static final int MIN_GROUP_COUNT = 0;
	private static final int MAX_GROUP_COUNT = 10;
	
	// |Nb etage: <slider>
	// |Nb ascenseur: <slider>
	// |Nb individu: <slider>
	// |Nb groupes: <slider>
	//
	private JPanel jpanel_principal;
	
		private JPanel jpanel_floor_count;
			private JSlider jslider_floor_count;
		private JPanel jpanel_elevator_count;
			private JSlider jslider_elevator_count;
		private JPanel jpanel_person_count;
			private JSlider jslider_person_count;
		private JPanel jpanel_group_count;
			private JSlider jslider_group_count;
		private JPanel jpanel_start_simulation;

	public JFrameConfigView() {
		this.setLocationByPlatform(true);
		this.setSize(600, 600);
		this.setTitle("Projet Java 2008 : Simulation de comportement d'ascenseurs - Tic/Tac/Viet");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container content_pane = this.getContentPane();

		this.jpanel_principal = new JPanel();
		this.jpanel_principal.setLayout(new BoxLayout(jpanel_principal, BoxLayout.PAGE_AXIS));
		{			
			// Cadre du choix du nombre d'etages
			this.jpanel_floor_count = new JPanel(new GridLayout(1,3));
			{	
				JLabel choose_floor_count = new JLabel("Nombre d'Žtages");
				choose_floor_count.setFont(new Font(POLICE, Font.BOLD, 15));
				jpanel_floor_count.add(choose_floor_count);
				
				JLabel jlabel_floor_count = new JLabel(Integer.toString((int)MAX_FLOOR_COUNT/2));
				jlabel_floor_count.setFont(new Font(POLICE, Font.BOLD, 15));
				
				jslider_floor_count = new JSlider(MIN_FLOOR_COUNT, MAX_FLOOR_COUNT);
				jslider_floor_count.setValue((int)MAX_FLOOR_COUNT/2);
				jslider_floor_count.addChangeListener(new SliderUpdateObserver(jlabel_floor_count));
				jpanel_floor_count.add(jslider_floor_count);
				jpanel_floor_count.add(jlabel_floor_count);
			}
			this.jpanel_principal.add(jpanel_floor_count);
			
			// Cadre du choix du nombre d'ascenseur
			this.jpanel_elevator_count = new JPanel(new GridLayout(1,3));
			{	
				JLabel choose_elevator_count = new JLabel("Nombre d'ascenseur");
				choose_elevator_count.setFont(new Font(POLICE, Font.BOLD,15));
				jpanel_elevator_count.add(choose_elevator_count);
				
				JLabel jlabel_elevator_count = new JLabel(Integer.toString((int)MAX_ELEVATOR_COUNT/2));
				jlabel_elevator_count.setFont(new Font(POLICE, Font.BOLD, 15));
				
				jslider_elevator_count = new JSlider(MIN_ELEVATOR_COUNT, MAX_ELEVATOR_COUNT);
				jslider_elevator_count.setValue((int)MAX_ELEVATOR_COUNT/2);
				jslider_elevator_count.addChangeListener(new SliderUpdateObserver(jlabel_elevator_count));
				jpanel_elevator_count.add(jslider_elevator_count);
				jpanel_elevator_count.add(jlabel_elevator_count);
			}
			this.jpanel_principal.add(jpanel_elevator_count);
			
			// Cadre du choix du nombre d'individu
			this.jpanel_person_count = new JPanel(new GridLayout(1,3));
			{	
				JLabel choose_person_count = new JLabel("Nombre d'individus");
				choose_person_count.setFont(new Font(POLICE, Font.BOLD,15));
				jpanel_person_count.add(choose_person_count);
				
				JLabel jlabel_person_count = new JLabel(Integer.toString((int)MAX_PERSON_COUNT/2));
				jlabel_person_count.setFont(new Font(POLICE, Font.BOLD, 15));
				
				jslider_person_count = new JSlider(MIN_PERSON_COUNT, MAX_PERSON_COUNT);
				jslider_person_count.setValue((int)MAX_PERSON_COUNT/2);
				jslider_person_count.addChangeListener(new SliderUpdateObserver(jlabel_person_count));
				jpanel_person_count.add(jslider_person_count);
				jpanel_person_count.add(jlabel_person_count);
			}
			this.jpanel_principal.add(jpanel_person_count);
			
			// Cadre du choix du nombre de groupes
			this.jpanel_group_count = new JPanel(new GridLayout(1,3));
			{	
				JLabel choose_group_count = new JLabel("Nombre de groupes");
				choose_group_count.setFont(new Font(POLICE, Font.BOLD,15));
				jpanel_group_count.add(choose_group_count);
				
				JLabel jlabel_group_count = new JLabel(Integer.toString((int)MAX_GROUP_COUNT/2));
				jlabel_group_count.setFont(new Font(POLICE, Font.BOLD, 15));
				
				jslider_group_count = new JSlider(MIN_GROUP_COUNT, MAX_GROUP_COUNT);
				jslider_group_count.setValue((int)MAX_GROUP_COUNT/2);
				jslider_group_count.addChangeListener(new SliderUpdateObserver(jlabel_group_count));
				jpanel_group_count.add(jslider_group_count);
				jpanel_group_count.add(jlabel_group_count);
			}
			this.jpanel_principal.add(jpanel_group_count);
			
			// Cadre du bouton "start simulation"
			this.jpanel_start_simulation = new JPanel();
			this.jpanel_start_simulation.setLayout(new BoxLayout(jpanel_start_simulation,BoxLayout.Y_AXIS));
			{
				JButton jbutton_start_simulation = new JButton("Lancer la simulation", new ImageIcon("src/10.png"));
				jbutton_start_simulation.setAlignmentX(CENTER_ALIGNMENT);
				jbutton_start_simulation.setFont(new Font(POLICE, Font.BOLD, 20));
				jbutton_start_simulation.addActionListener(new StartSimulationObserver(this));
				this.jpanel_start_simulation.add(jbutton_start_simulation);
			}
			this.jpanel_principal.add(jpanel_start_simulation);
		}
		content_pane.add(jpanel_principal);
		
		this.setResizable(true);
		this.setVisible(true);
	}

	public int get_floor_count() {
		return jslider_floor_count.getValue();
	}

	public int get_elevator_count() {
		return jslider_elevator_count.getValue();
	}

	public int get_person_count() {
		return jslider_person_count.getValue();
	}

	public int get_group_count() {
		return jslider_group_count.getValue();
	}

	public Component getComponent() {
		return jpanel_principal;
	}
	
}