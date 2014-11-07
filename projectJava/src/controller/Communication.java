package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import model.*;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Communication {
	public static final Object lock = new Object();
	private static final int MAX_TRIED = 15;
	private RemoteDevice device;
	private String URL;
	StreamConnection session;
	OutputStream os;
	InputStream is;
	private ReceiveListener listener;
	public boolean connected = false;
	
	public Communication() {
		this.listener = new ReceiveListener(Application.getCrane());
		(new Thread(listener)).start();
	}
	
	public boolean tryConnect(RemoteDevice rm) throws IOException {
		this.device = rm;
		this.URL = "btspp://" + this.device.getBluetoothAddress() + ":1";
		session = (StreamConnection) Connector.open(this.URL, Connector.READ_WRITE);
		os = session.openOutputStream();
		is = session.openInputStream();
		this.sendMessage("c");
		if (this.receiveString().equals("OK")) {
			Application.getCrane().setState(State.INIT);
			connected = true;
			listener.setListenerStream(is);
			synchronized (Communication.lock) {
				Communication.lock.notifyAll();
			}
			return true;
		} else {
			this.close();
			return false;
		}
	}
	
	public void sendMessage(String msg) throws IOException {
		msg += "$";
		os.write(msg.getBytes());
	}
	
	public String receiveString() throws IOException {
		int tried = 0;
		String receive;
		synchronized (Communication.lock) {
			while(is.available() == 0) {
				tried++;
				System.out.println(tried);
				if (tried >= MAX_TRIED) {
					throw new IOException("Przekroczona ilosc prob");
				}
				try {
					System.out.println("ete");
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			receive = new String();
			char c;
			while ((c = (char)(is.read())) != 13) {
				receive += c;
			}
			is.read();
		}
		return receive;
	}
	
	public void close() {
		try {
			sendMessage("d");
			is.close();
			os.close();
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMove(Move move) throws IOException {
		this.sendMessage(move.toSend());
		try {
			if (!this.receiveString().equals("OK")) {
				throw new IOException();
			} else {
				synchronized (Communication.lock) {
					Communication.lock.notifyAll();
				}
				Application.getCrane().setCurrentPosition(move.getTo());
				Application.getCrane().setState(State.MOVE);
			}
		} catch (Exception e) {
			
		}
	}
	
	public void sendTranser(Transfer transer) throws IOException {
		this.sendMessage(transer.toSend());
		try {
			if (!this.receiveString().equals("OK")) {
				throw new IOException();
			} else {
				synchronized (Communication.lock) {
					Communication.lock.notifyAll();
				}
				Application.getCrane().setState(State.TRANSFER);
				Application.getCrane().setCurrentPosition(transer.getTo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendStop() throws IOException {
		try {
			this.sendMessage("s");
		} catch (Exception e) {
			
		}
	}
}
