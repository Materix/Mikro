package model;

public class Move {
	Coord to;
	
	public Move(Coord to) {
		this.to = to;
	}
	
	public String toSend() {
		return "m" + to.toSend();
	}
}
