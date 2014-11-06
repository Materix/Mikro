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
import model.State;
import model.Transfer;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import controller.Communication;
import controller.CraneStateListener;

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
		setLayout(new MigLayout("", "[grow][][][][]", "[][][][][][][][][grow]"));
		
		final CranePanel cranePanel = new CranePanel();
		add(cranePanel, "cell 0 0 1 9,grow");
		
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
		JLabel czer = new JLabel(new ImageIcon("img/czer.png"));
		
		add(czer, "cell 1 5,alignx right");
		JLabel nieb = new JLabel(new ImageIcon("img/nieb.png"));
		add(nieb, "cell 1 6,alignx right");
		JLabel ziel = new JLabel(new ImageIcon("img/ziel.png"));
		add(ziel, "cell 1 7,alignx right");
		
		final JButton start = new JButton("Start");
		add(start, "cell 2 8,growx,aligny bottom");
		
		JButton stop = new JButton("Stop");
		add(stop, "cell 4 8,growx,aligny bottom");
		JLabel polozenie = new JLabel("Po³o¿enie aktualne");
		add(polozenie, "cell 2 5 3 1");
		add(from, "cell 2 6 3 1");
		add(to, "cell 2 7 3 1");
		
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{transfer, move, fromX, fromY, toX, toY, start, stop, from, fromXLabel, fromYLabel, to, toXLabel, toYLabel}));
		
		move.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fromX.setEditable(false);
				fromY.setEditable(false);
				fromX.setBackground(new Color(240, 240, 240));
				fromY.setBackground(new Color(240, 240, 240));
			}
		});
		transfer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fromX.setEditable(true);
				fromY.setEditable(true);
				fromX.setBackground(Color.WHITE);
				fromY.setBackground(Color.WHITE);
			}
		});
		CaretListener checkField = new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				JTextField source = (JTextField)arg0.getSource();
				String text = source.getText();
				if (!text.matches("-?\\d+")) {
					if (source.isEditable()) {
						source.setBackground(new Color(255, 100, 100));
					}
				} else {
					int war = Integer.parseInt(text);
					String name = source.getName();
					JTextField source2 = null;
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
						if (source.isEditable()) {
							source.setBackground(Color.white);
						} else if (source2.isEditable()) {
							source2.setBackground(Color.white);
						}
					}
					for (MouseListener ml : cranePanel.getMouseListeners()) {
						Point p = CranePanel.cartesianToPanel(new Coord(war, war2));
						if (name.equals("toX") || name.equals("toY")) {
							ml.mousePressed(new MouseEvent(toX, 0, 0, 0, p.x, p.y, 0, 0, 0, false, MouseEvent.BUTTON3));
						} else {
							ml.mousePressed(new MouseEvent(toX, 0, 0, 0, p.x, p.y, 0, 0, 0, false, MouseEvent.BUTTON1));
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
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Urz¹dzenie nie odpowiada", "Brak odpowiedzi",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Proszê o uzupe³nienie wszystkich pól", "",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Application.getCommunication().sendStop();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Urz¹dzenie nie odpowiada", "Brak odpowiedzi",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		if (Application.getCrane().getState() != State.READY) {
			start.setEnabled(false);
		}
		Application.getCrane().addCraneStateListener(new CraneStateListener() {
			
			@Override
			public void changed(State newState) {
				if (newState == State.INIT || newState == State.MOVE || newState == State.TRANSFER) {
					start.setEnabled(false);
				} else if (newState == State.READY) {
					start.setEnabled(true);
				}
				cranePanel.repaint();
			}
		});
	}

}
