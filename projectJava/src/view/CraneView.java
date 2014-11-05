package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import model.Application;
import model.Coord;
import model.Crane;
import model.Move;
import model.Transfer;
import net.miginfocom.swing.MigLayout;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import controller.Communication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.io.IOException;

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
		
		final CranePanel cranePanel = new CranePanel();
		add(cranePanel, "cell 0 0 1 6,grow");
		
		final JRadioButton move = new JRadioButton("Obr\u00F3\u0107");
		add(move, "cell 4 0");
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(move);
		
		final JRadioButton transfer = new JRadioButton("Przenie\u015B");
		transfer.setSelected(true);
		add(transfer, "cell 2 0");
		radioGroup.add(transfer);
		
		JLabel from = new JLabel("Po\u0142o\u017Cenie pocz\u0105tkowe");
		add(from, "cell 1 1 4 1,alignx center");
		
		JLabel fromXLabel = new JLabel("x=");
		add(fromXLabel, "cell 1 2,alignx trailing");
		
		fromX = new JTextField("0");
		fromX.setName("fromX");
		add(fromX, "cell 2 2,alignx center");
		fromX.setColumns(10);
		
		JLabel fromYLabel = new JLabel("y=");
		add(fromYLabel, "cell 3 2,alignx trailing");
		
		fromY = new JTextField("0");
		fromY.setName("fromY");
		add(fromY, "cell 4 2,growx");
		fromY.setColumns(10);
		
		JLabel to = new JLabel("Po\u0142o\u017Cenie ko\u0144cowe");
		add(to, "cell 1 3 4 1,alignx center");
		
		JLabel toXLabel = new JLabel("x=");
		add(toXLabel, "cell 1 4,alignx trailing,aligny top");
		
		toX = new JTextField("0");
		toX.setName("toX");
		add(toX, "cell 2 4,growx,aligny top");
		toX.setColumns(10);
		
		JLabel toYLabel = new JLabel("y=");
		add(toYLabel, "cell 3 4,alignx trailing,aligny top");
		
		toY = new JTextField("0");
		toY.setName("toY");
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
		CaretListener checkField = new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				JTextField source = (JTextField)arg0.getSource();
				String text = source.getText();
				if (!text.matches("-?\\d+")) {
					source.setBackground(new Color(255, 100, 100));
				} else {
					int war = Integer.parseInt(text);
					String name = source.getName();
					Component source2 = null;
					int war2 = 0;
					if (name.equals("fromX")) {
						try {
							war2 = Integer.parseInt(fromY.getText());
						} catch (NumberFormatException e1) {
							war2 = 0;
						}
						source2 = fromY;
					} else if (name.equals("fromY")) {
						try {
							war2 = war;
							war = Integer.parseInt(fromX.getText());
						} catch (NumberFormatException e1) {
							war2 = 0;
						}
						source2 = fromX;
					} else if (name.equals("toX")) {
						try {
							war2 = Integer.parseInt(toY.getText());
						} catch (NumberFormatException e1) {
							war2 = 0;
						}
						source2 = toY;
					} else if (name.equals("toY")) {
						try {
							war2 = war;
							war = Integer.parseInt(toX.getText());
						} catch (NumberFormatException e1) {
							war2 = 0;
						}
						source2 = toX;
					} else {
						source2 = source;
					}
					if (war2 * war2 + war * war > Crane.round * Crane.round || war2 * war2 + war * war < Crane.forbiden_round * Crane.forbiden_round) {
						source.setBackground(new Color(255, 100, 100));
						source2.setBackground(new Color(255, 100, 100));
					} else {
						source.setBackground(Color.white);
						source2.setBackground(Color.white);
						Point p = CranePanel.cartesianToPanel(new Coord(war, -war2));
						for (MouseListener ml : cranePanel.getMouseListeners()) {
							if (name.equals("toX") || name.equals("toY")) {
								ml.mousePressed(new MouseEvent(toX, 0, 0, 0, p.x, p.y, 0, 0, 0, false, MouseEvent.BUTTON3));
							} else {
								ml.mousePressed(new MouseEvent(toX, 0, 0, 0, p.x, p.y, 0, 0, 0, false, MouseEvent.BUTTON1));
							}
						}
					}
				}
			}
		};
		toX.addCaretListener(checkField);
		toY.addCaretListener(checkField);
		fromX.addCaretListener(checkField);
		fromY.addCaretListener(checkField);
		
		cranePanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (!e.getSource().equals(toX)) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						fromX.setText(Integer.toString(CranePanel.panelToCartesian(e.getPoint()).x));
						fromY.setText(Integer.toString(CranePanel.panelToCartesian(e.getPoint()).y));
					} else if (e.getButton() == MouseEvent.BUTTON3) {
						toX.setText(Integer.toString(CranePanel.panelToCartesian(e.getPoint()).x));
						toY.setText(Integer.toString(CranePanel.panelToCartesian(e.getPoint()).y));	
					}
				}
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
				
				
			}
		});
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Communication communication = Application.getCommunication();
				try {
					if (move.isSelected()) {
						communication.sendMove(new Move(new Coord(Integer.parseInt(toX.getText()), Integer.parseInt(toY.getText()))));
					} else {
						communication.sendTranser(new Transfer(new Coord(Integer.parseInt(fromX.getText()), Integer.parseInt(fromY.getText())), new Coord(Integer.parseInt(toX.getText()), Integer.parseInt(toY.getText()))));
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Urz�dzenie nie odpowiada", "Brak odpowiedzi",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Prosz� o uzupe�nienie wszystkich p�l", "",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Application.getCommunication().sendStop();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Urz�dzenie nie odpowiada", "Brak odpowiedzi",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

}
