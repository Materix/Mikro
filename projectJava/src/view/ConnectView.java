package view;

import javax.bluetooth.RemoteDevice;
import javax.swing.JPanel;

import model.Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import jssc.SerialPortList;

import javax.swing.JLabel;

import controller.Communication;
import bluetooth.Searcher;

public class ConnectView extends JPanel {
	private static final long serialVersionUID = -3713321785875878557L;

	public ConnectView() {
		setPreferredSize(new Dimension(300, 125));
		setMaximumSize(new Dimension(300, 125));
		setMinimumSize(new Dimension(300, 125));
		setLayout(new MigLayout("", "[grow]", "[20][][]"));
		
		JLabel wybierzPortLabel = new JLabel("Wybierz port");
		add(wybierzPortLabel, "cell 0 0,alignx center,aligny bottom");
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(Searcher.getNames()));
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
				Application.getCommunication().tryConnect(selectedDevice);
				Application.getCommunication().close();
//				if (Application.getCommunication().open()) {
//					System.out.println("Hura");
//					Application.getCommunication().close();
//				} else {
//					JOptionPane.showMessageDialog(Application.getMaintFrame(),"Z³y port","Z³y port",JOptionPane.ERROR_MESSAGE);
//				}
//				Searcher.sendMessageToDevice("dupa");
			}
		});
		add(btnPocz, "cell 0 2,alignx center,aligny center");

	}

}
