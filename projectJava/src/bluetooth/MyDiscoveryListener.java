package bluetooth;


import javax.bluetooth.*;
public class MyDiscoveryListener implements DiscoveryListener{
	
	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
		Searcher.bluetoothDevice.add(btDevice);
	}

	@Override
	public void inquiryCompleted(int arg0) {
		synchronized(Searcher.lock){
			Searcher.lock.notify();
		}
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		synchronized(Searcher.lock){
			Searcher.lock.notify();
		}
	}

	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		for(int i = 0; i < arg1.length; i++) {
			//TODO w jakiœ magiczny sposób wybraæ odpowiedni serwis
		}
	}

}