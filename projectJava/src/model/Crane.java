package model;

import model.Coord;

public class Crane {
	private Coord position; 
	private int heightHook;
	
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
}
