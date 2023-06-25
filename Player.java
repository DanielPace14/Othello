package Othello;

public class Player {

	private String name= null;
	
	private char colour;
	
	public Player(String name, char s) {
		
		this.name = name;
		colour = s;
	}
	public String getName() {
		
		return name;
	}
	public char getColour() {
		return colour;
	}
	
}
