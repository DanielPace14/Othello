package Othello;

public abstract class Position {

	private char piece;
	
	public abstract boolean canPlay();
	
	final static char EMPTY = '-';
	final static char WHITE = 'W';
	final static char BLACK = 'B';
	public void setPiece(char symb){
		piece = symb;
	}
	public char getPiece() {
		return piece;
	}

}

class PlayablePosition extends Position{

	
	public PlayablePosition(char s) {
		setPiece(s);
	}
	@Override
	//This will return true only if the position is empty
	public boolean canPlay() {
		if(this.getPiece() == EMPTY) {
			return true;
		}
		else{return false;}
	}
	
}
class UnplayablePosition extends Position{

	final static char UNPLAYABLE = '*';
	
	public UnplayablePosition() {
		setPiece(UNPLAYABLE);
	}
	@Override
	public boolean canPlay() {
		// TODO Auto-generated method stub
		return false;
	}
	
}