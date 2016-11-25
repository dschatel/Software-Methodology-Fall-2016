package chess;

/**
 * This abstract class represents the generic construction for a Piece on the board.
 * All specific pieces inherit from this class.
 * 
 * @author Derek Schatel
 *
 */
public abstract class Piece {
	
	private String color;
	private String rank;
	private boolean isFirstMove;
	
	public Piece (String color) {
		this.color = color;		
		isFirstMove = true;
	}
	
	/**
	 * Getter method for retrieving color value for the Piece.
	 * @return Color value
	 */
	public String getColor() {
		return color;
	}
	
	/** 
	 * Getter method for retrieving full Piece value for display.
	 * @return Piece display value
	 */
	public String getPiece() {
		return color + rank;
	}
	
	/**
	 * Getter method for retrieving Piece's rank
	 * @return Rank value
	 */
	public String getRank() {
		return rank;
	}
	
	/**
	 * Getter method for retrieving whether it is this Piece's
	 * first move
	 * @return First move value
	 */
	public Boolean getTurn () {
		return isFirstMove;
	}
	
	/** 
	 * Setter method for setting first turn value
	 * @param turn first turn value
	 */
	public void setTurn (boolean turn) {
		isFirstMove = turn;
	}
	
	/**
	 * Abstract method representing the legal move set of a Piece.
	 * @param oldRow Current row of piece
	 * @param oldCol Current column of piece
	 * @param newRow Destination row of piece
	 * @param newCol Destination column of piece
	 * @return boolean value for whether the requested move is legal for that Piece
	 */
	public abstract boolean moveSet(int oldRow, int oldCol, int newRow, int newCol);

}
