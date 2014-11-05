package laucher;

import java.awt.EventQueue;

import controller.Communication;
import model.Application;
import model.Coord;
import model.Crane;
import view.MainFrame;

public class Laucher {
	public static void main(String[] args) {
		Application.setCommunication(new Communication());
		Application.setCrane(new Crane(new Coord(Crane.round, 0), 0));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					Application.setMainFrame(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
