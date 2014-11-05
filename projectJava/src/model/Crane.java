package model;

import model.Coord;

public class Crane {
	public final static int round = 300;
	public final static int forbiden_round = 50;

	
	public static Coord position; 
	private int heightHook;
	private State state;
	
	public Crane(Coord position, int heightHook) {
		Crane.position = position;
		this.heightHook = heightHook;
	}
	
	public Coord getCurrentPosition() {
		return position;
	}
	
	public int getHeighHook() {
		return heightHook;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
}
