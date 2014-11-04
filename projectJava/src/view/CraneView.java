package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Color;
import java.awt.Component;

public class CraneView extends JPanel {

	private static final long serialVersionUID = -1064785835344149993L;
	private JTextField fromX;
	private JTextField fromY;
	private JTextField toX;
	private JTextField toY;

	/**
	 * Create the panel.
	 */
	public CraneView() {
		setLayout(new MigLayout("", "[grow][][][][]", "[][][][][grow][]"));
		
		CranePanel cranePanel = new CranePanel();
		add(cranePanel, "cell 0 0 1 6,grow");
		
		final JRadioButton move = new JRadioButton("Obr\u00F3\u0107");
		add(move, "cell 4 0");
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(move);
		
		JRadioButton transfer = new JRadioButton("Przenie\u015B");
		transfer.setSelected(true);
		add(transfer, "cell 2 0");
		radioGroup.add(transfer);
		
		JLabel from = new JLabel("Po\u0142o\u017Cenie pocz\u0105tkowe");
		add(from, "cell 1 1 4 1,alignx center");
		
		JLabel fromXLabel = new JLabel("x=");
		add(fromXLabel, "cell 1 2,alignx trailing");
		
		fromX = new JTextField();
		add(fromX, "cell 2 2,alignx center");
		fromX.setColumns(10);
		
		JLabel fromYLabel = new JLabel("y=");
		add(fromYLabel, "cell 3 2,alignx trailing");
		
		fromY = new JTextField();
		add(fromY, "cell 4 2,growx");
		fromY.setColumns(10);
		
		JLabel to = new JLabel("Po\u0142o\u017Cenie ko\u0144cowe");
		add(to, "cell 1 3 4 1,alignx center");
		
		JLabel toXLabel = new JLabel("x=");
		add(toXLabel, "cell 1 4,alignx trailing,aligny top");
		
		toX = new JTextField();
		add(toX, "cell 2 4,growx,aligny top");
		toX.setColumns(10);
		
		JLabel toYLabel = new JLabel("y=");
		add(toYLabel, "cell 3 4,alignx trailing,aligny top");
		
		toY = new JTextField();
		add(toY, "cell 4 4,growx,aligny top");
		toY.setColumns(10);
		
		JButton start = new JButton("Start");
		add(start, "cell 2 5,growx,aligny bottom");
		
		JButton stop = new JButton("Stop");
		add(stop, "cell 4 5,growx,aligny bottom");
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{transfer, move, fromX, fromY, toX, toY, start, stop, from, fromXLabel, fromYLabel, to, toXLabel, toYLabel}));
		
		move.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fromX.setEditable(false);
				fromY.setEditable(false);
			}
		});
		transfer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fromX.setEditable(true);
				fromY.setEditable(true);
			}
		});
		KeyListener checkField = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				JTextField source = (JTextField)e.getSource();
				String text = source.getText();
				if (e.getKeyChar() >= ' ' && e.getKeyChar() <= 167) {
					text += e.getKeyChar();
				}
				if (text.matches("\\d*\\D+\\d*")) {
					source.setBackground(new Color(255, 100, 100));
				} else {
					source.setBackground(Color.white);
				}
			}
			
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
		toX.addKeyListener(checkField);
		toY.addKeyListener(checkField);
		fromX.addKeyListener(checkField);
		fromY.addKeyListener(checkField);
		
		cranePanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
				if (move.isSelected()) {
					toX.setText(Integer.toString((int)e.getPoint().getX()));
					toY.setText(Integer.toString((int)e.getPoint().getY()));
				}
				
			}
		});
	}
	

}
