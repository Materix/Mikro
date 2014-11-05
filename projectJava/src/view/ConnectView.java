package view;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.swing.JPanel;

import model.Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

import bluetooth.Searcher;

public class ConnectView extends JPanel {
	private static final long serialVersionUID = -3713321785875878557L;

	public ConnectView() {
		setPreferredSize(new Dimension(300, 100));
		setMaximumSize(new Dimension(300, 100));
		setMinimumSize(new Dimension(300, 100));
		setLayout(new MigLayout("", "[grow]", "[20][][]"));
		
		JLabel wybierzPortLabel = new JLabel("Wybierz urz¹dzenie");
		add(wybierzPortLabel, "cell 0 0,alignx center,aligny bottom");
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		try {
			comboBox.setModel(new DefaultComboBoxModel<String>(Searcher.getNames()));
		} catch (BluetoothStateException e1) {
			JOptionPane.showMessageDialog(Application.getMaintFrame(),"Nie zosta³o znalezione urz¹dzenie bluetooth", "Brak bluetooth",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		add(comboBox, "cell 0 1,grow");
		
		JButton btnPocz = new JButton("Po\u0142\u0105cz");
		btnPocz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RemoteDevice selectedDevice = null;
				for (RemoteDevice rm : Searcher.bluetoothDevice) {
					try {
						if (rm.getFriendlyName(false).equals((String)comboBox.getSelectedItem())) {
							selectedDevice = rm;
						}
					} catch (IOException e) {
						if (rm.getBluetoothAddress().equals((String)comboBox.getSelectedItem())) {
							selectedDevice = rm;
						}
					}
				}
				try {
					if (Application.getCommunication().tryConnect(selectedDevice)) {
						Application.getMaintFrame().setJPanel(new CraneView());
					} else {
						JOptionPane.showMessageDialog(Application.getMaintFrame(), "Zosta³o wybrane z³e urz¹dzenie lub urz¹dzenie nie odpowiada", "Z³e urz¹dzenie", JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException | IOException e) {
					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Zosta³o wybrane z³e urz¹dzenie lub urz¹dzenie nie odpowiada", "Z³e urz¹dzenie", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(btnPocz, "cell 0 2,alignx center,aligny center");
	}

}
