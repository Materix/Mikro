package controller;

import java.io.IOException;
import java.io.InputStream;

import model.Crane;
import model.State;

public class ReceiveListener implements Runnable {
	private InputStream listenedStream;
	private Crane crane;

	public ReceiveListener(Crane crane) {
		this.crane = crane;
	}

	@Override
	public void run() {
		listen();
		
	}
	
	public void setListenerStream(InputStream listenedStream) {
		this.listenedStream = listenedStream;
	}
	
	public void removeListenerStream() {
		this.listenedStream = null;
	}
	
	public void listen() {
		while(true) {
			try {
				synchronized (Communication.lock) {
					Communication.lock.wait(); //waiting for notify
				}
				if (this.listenedStream != null) {
					synchronized(Communication.lock) {
						while(listenedStream.available() == 0) {
						}
						String receive = new String();
						char c;
						while ((c = (char)(listenedStream.read())) != 13) {
							receive += c;
						}
						listenedStream.read();
						System.out.println(receive);
						if (receive.equals("OK")) {
							
						}
						crane.setState(State.READY);
					}
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
