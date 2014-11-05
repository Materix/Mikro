package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.JPanel;

public class CranePanel extends JPanel {

	private static final long serialVersionUID = -8011038091559755461L;

	public CranePanel() {
		super();
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(501, 501));
	}
	
	@Override	
	public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        Ellipse2D zasieg = new Ellipse2D.Double(0, 0, 500, 500);
        Rectangle2D podstawa = new Rectangle2D.Double(225, 225, 50, 50);
        g2.draw(podstawa);
        g2.draw(zasieg);
     }

}
