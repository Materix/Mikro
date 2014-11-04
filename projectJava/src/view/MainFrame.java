package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8720432252745326845L;
	private JPanel contentPane;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO zmieniæ na roz³¹czanie
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setJPanel(new ConnectView());
	}
	
	public void setJPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		setMinimumSize(this.contentPane.getMinimumSize());
		setResizable(false);
		setLocationRelativeTo(null);
		setContentPane(this.contentPane);
	}

}
