package model;

import java.util.ArrayList;

import controller.CraneStateListener;
import model.Coord;

public class Crane {
	public final static int round = 300;
	public final static int forbiden_round = 50;

	
	public static Coord position; 
	private int heightHook;
	private State state;
	private ArrayList<CraneStateListener> cslList = new ArrayList<>();
	
	public Crane(Coord position, int heightHook) {
		Crane.position = position;
		this.heightHook = heightHook;
	}
	
	public Coord getCurrentPosition() {
		return position;
	}
	
	public void setCurrentPosition(Coord position) {
		Crane.position = position;
	}
	
	public int getHeighHook() {
		return heightHook;
	}
	
	public void setState(State state) {
		this.state = state;
		for (CraneStateListener csl : cslList) {
			csl.changed(state);
		}
	}
	
	public State getState() {
		return state;
	}
	
	public void addCraneStateListener(CraneStateListener csl) {
		this.cslList.add(csl);
	}
}
