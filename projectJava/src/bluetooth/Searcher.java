package bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.*;

public class Searcher {

	static Object lock = new Object();
	private static boolean searched = false;
	public static List<RemoteDevice> bluetoothDevice = new ArrayList<RemoteDevice>();
	
	public static void search() throws BluetoothStateException {
		try{
			
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();         
			agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
			try {
				synchronized(lock){
					lock.wait();
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			searched = true;
		}
		catch (Exception e) {
			if (e.getLocalizedMessage().equals("BluetoothStack not detected")) {
				throw new BluetoothStateException();
			} else {
				e.printStackTrace();
			}
		}
	}
	
	public static String[] getNames() throws BluetoothStateException {
		if (!searched) {
			Searcher.search();
		}
		String[] names = new String[Searcher.bluetoothDevice.size()];
		int i = 0;
		for (RemoteDevice rd : Searcher.bluetoothDevice) {
			try {
				names[i] = rd.getFriendlyName(false);
			} catch (IOException e) {
				names[i] = rd.getBluetoothAddress();
			}
			i++;
		}
		return names;
	}
	
	public static void searchForService(RemoteDevice device) {
		try {
			System.out.println("Search service for " + device.getFriendlyName(false));
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();   
			UUID[] uuidSet = new UUID[1];
			uuidSet[0]=new UUID(0x1101); //Serial port service
			agent.searchServices(null, uuidSet, device, new MyDiscoveryListener());
			try {
				synchronized(lock){
					lock.wait();
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		} catch (BluetoothStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
