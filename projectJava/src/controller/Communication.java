package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.*;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;
import javax.tools.ToolProvider;

import bluetooth.Searcher;

public class Communication {
	private RemoteDevice device;
	private String URL;
	StreamConnection session;
	
	public Communication() {
	}
	
	public boolean tryConnect(RemoteDevice rm) {
		this.device = rm;
		this.URL = "btspp://" + this.device.getBluetoothAddress() + ":1";
		try {
			session = (StreamConnection) Connector.open(this.URL, Connector.READ_WRITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sendMessage("ZURAW");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (this.receiveString().equals("OK")) {
			System.out.println("Sie uda³o");
			return true;
		} else {
			System.out.println("Dupa");
			this.close();
			return false;
		}
	}
	
	public void sendMessage(String msg){
		try{
			OutputStream os = session.openOutputStream();
			os.write(msg.getBytes());
			os.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String receiveString() {
		InputStream is;
		try {
			is = session.openInputStream();
			String receive = new String();
			char c;
			while ((c = (char)(is.read())) != 13) {
				System.out.println((int)c);
				receive += c;
			}
			System.out.println("Odebra³em wiadomoœæ " + receive + " Koniec. ");
			return receive;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String();
	}
	
	public void close() {
		try {
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendCoord(Coord coord) {
		this.sendMessage(coord.toSend());
	}
	
	public void sendTranser(Transfer transer) {
		this.sendMessage(transer.toSend());
	}
}
