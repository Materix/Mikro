package model;

public class Coord {
	int x;
	int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//czy inty wysy³aæ jako string?
	public String toSend() {
		return "cx" + this.x + "y" + this.y;
	}
}
