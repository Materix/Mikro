package model;


public class Transfer {
	private Coord from;
	private Coord to;
	
	public Transfer(Coord from, Coord to) {
		this.from = from;
		this.to = to;
	}
	
	public String toSend() {
		return "tf" + from.toSend() + "t" + to.toSend();
	}
	
	public Coord getTo() {
		return to;
	}
}
