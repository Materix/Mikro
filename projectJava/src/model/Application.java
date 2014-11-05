package model;

import view.MainFrame;
import controller.Communication;

public class Application {
	private static MainFrame mainFrame;
	private static Communication communication;
	private static Crane crane;

	public static MainFrame getMaintFrame() {
		return mainFrame;
	}

	public static void setMainFrame(MainFrame mainFrame) {
		Application.mainFrame = mainFrame;
	}
	
	public static Communication getCommunication() {
		return communication;
	}

	public static void setCommunication(Communication communication) {
		Application.communication = communication;
	}

	public static Crane getCrane() {
		return crane;
	}
	
	public static void setCrane(Crane crane) {
		Application.crane = crane;
	}
}
