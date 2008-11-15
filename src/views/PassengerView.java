package views;

import controllers.MainController;

public abstract class PassengerView {
	private MainController controller = null;
	
	public PassengerView(MainController controller){
		super();
		
		this.controller = controller;
	}
	
	public final MainController getController(){
		return controller;
	}
	
	public abstract void display();
	public abstract void close();
}
