package model;

import model.Coord;

public class Crane {
	private Coord position; 
	private int heightHook;
	private State state;
	
	public Crane(Coord position, int heightHook) {
		this.position = position;
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
