package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Application;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8720432252745326845L;
	private JPanel contentPane;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO zmieniæ na roz³¹czanie
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if (Application.getCommunication().connected) {
					Application.getCommunication().close();
				}
				System.exit(0);
			}
		});


		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle("¯uraw");
		setJPanel(new ConnectView());
	}
	
	public void setJPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		setMinimumSize(this.contentPane.getMinimumSize());
		setResizable(false);
		setLocationRelativeTo(null);
		setContentPane(this.contentPane);
		pack();
	}

}
