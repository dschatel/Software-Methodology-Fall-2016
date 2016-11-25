package chess;

/**
 * This class represents the values and move logic for a King.
 * 
 * @author Derek Schatel
 *
 */
public class King extends Piece {
	
	private String rank;
	
	public King (String color) {
		super(color);
		rank =  "K";		
		
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#getRank()
	 */
	public String getRank() {
		return rank;
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#getPiece()
	 */
	public String getPiece() {
		return this.getColor() + rank;
	}
	
	/* (non-Javadoc)
	 * @see chess.Piece#moveSet(int, int, int, int)
	 */
	public boolean moveSet(int oldRow, int oldCol, int newRow, int newCol) {
		
		int diffRow = Math.abs(oldRow - newRow);
		int diffCol = Math.abs(oldCol - newCol);
		
		if (diffRow == 1 && diffCol == 1 && diffRow == diffCol)
			return true;
		else if (diffRow == 1 && diffCol == 0)
			return true;
		else if (diffRow == 0 && diffCol == 1)
			return true;
		
		return false;
		
	}

}
