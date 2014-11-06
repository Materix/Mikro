package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.*;

import javax.swing.JPanel;

import model.Application;
import model.Coord;
import model.Crane;

public class CranePanel extends JPanel {
	private Crane crane;

	private static final long serialVersionUID = -8011038091559755461L;
	private static final int w = 501;
	private static final int h = 501;
	private static final int scale = 15;
	
	private Point from = new Point(0,0);
	private Point to = new Point(0,0);

	public CranePanel() {
		super();
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(w, h));
		crane = Application.getCrane();
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					from = e.getPoint();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					to = e.getPoint();
				}
				repaint();
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		Ellipse2D zasieg = new Ellipse2D.Double((w - scale * Crane.round / 10) / 2, (h - scale * Crane.round / 10) / 2, scale * Crane.round / 10, scale * Crane.round / 10);
		g2.setColor(Color.DARK_GRAY);
		g2.fillOval((w - scale * Crane.forbiden_round / 10) / 2, (h - scale * Crane.forbiden_round / 10) / 2, scale * Crane.forbiden_round / 10, scale * Crane.forbiden_round / 10);
		Line2D line = new Line2D.Double(new Point(w / 2, h / 2), cartesianToPanel(crane.getCurrentPosition()));
		g2.setColor(Color.LIGHT_GRAY);
		g2.draw(zasieg);
		g2.setColor(Color.BLACK);
		g2.draw(line);
		g2.setColor(Color.BLUE);
		g2.fillOval(from.x - 5, from.y - 5,	10, 10);
		g2.setColor(Color.GREEN);
		g2.fillOval(to.x - 5, to.y - 5,	10, 10);
		g2.setColor(Color.RED);
		g2.fillOval(cartesianToPanel(crane.getCurrentPosition()).x - 5, cartesianToPanel(crane.getCurrentPosition()).y - 5,	10, 10);
    }
	
	public static Point cartesianToPanel(Coord coord) {
		return new Point((coord.x * scale) / 20 + 250, (-coord.y * scale) / 20 + 250);
	}
	
	public static Coord panelToCartesian(Point point) {
		return new Coord((point.x - 250) * 2 * 10 / scale, (-point.y + 250) * 2 * 10 / scale);
	}
}
