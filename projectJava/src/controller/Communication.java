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
		synchronized (Communication.lock) {
			while(is.available() == 0) {
				tried++;
				if (tried >= MAX_TRIED) {
					throw new IOException("Przekroczona ilosc prob");
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String receive = new String();
			char c;
			while ((c = (char)(is.read())) != 13) {
				receive += c;
			}
			System.out.println(receive);
			return receive;
		}
	}
	
	public void close() {
		try {
			sendStop();
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
			}
		} catch (Exception e) {
			
		}
	}
	
	public void sendStop() throws IOException {
		try {
			this.sendMessage("s");
		} catch (Exception e) {
			
		}
	}
}
