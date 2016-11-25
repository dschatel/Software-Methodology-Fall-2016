package chess;


/**
 * This class represents the values and move logic for a Pawn.
 * 
 * @author Derek Schatel
 *
 */
public class Pawn extends Piece {
	
	private String rank;
	
	public Pawn (String color) {
		super(color);
		rank =  "P";		
		
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#getPiece()
	 */
	public String getPiece() {
		return this.getColor() + rank;
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#getRank()
	 */
	public String getRank() {
		return rank;
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#moveSet(int, int, int, int)
	 */
	public boolean moveSet(int oldRow, int oldCol, int newRow, int newCol) {
		
		int diffCol = Math.abs(oldCol - newCol);
		int diffRow;
		
		if (this.getColor().equals("w"))
			diffRow = -(newRow - oldRow);
		else
			diffRow = newRow - oldRow;
		
		if (diffCol == 0 && diffRow == 1)
			return true;
		else if (diffCol == 1 && diffRow == 1)
			return true;
		else if (diffCol == 0 && diffRow == 2 && this.getTurn()) {
			this.setTurn(false);
			return true;
		}
		
		return false;
		
	}

}
