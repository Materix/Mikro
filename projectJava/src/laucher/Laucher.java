package laucher;

import java.awt.EventQueue;

import bluetooth.Searcher;
import controller.Communication;
import model.Application;
import view.MainFrame;

public class Laucher {
	public static void main(String[] args) {
		Application.setCommunication(new Communication());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
