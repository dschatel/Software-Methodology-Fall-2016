package chess;

/**
 * This class represents the values and move logic for a Bishop.
 * 
 * @author Derek Schatel
 *
 */
public class Bishop extends Piece {
	
	private String rank;
	
	public Bishop (String color) {
		super(color);
		rank = "B";		
		
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
		
		if (diffRow == diffCol)
			return true;
		
		return false;
		
	}

}
