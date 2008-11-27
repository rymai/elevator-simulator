package views;

import controllers.MainController;

public abstract class PassengerView implements InterfaceView {
	
	protected MainController controller = null;
	public final MainController getController(){
		return controller;
	}
	
	public PassengerView(MainController controller){		
		this.controller = controller;
	}
	
}