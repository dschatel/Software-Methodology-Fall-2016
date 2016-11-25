package chess;

/**
 * This class represents the values and move logic for a Knight.
 * 
 * @author Derek Schatel
 *
 */
public class Knight extends Piece {
	
	private String rank;
	
	public Knight (String color) {
		super(color);
		rank =  "N";		
		
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
		
		if(diffRow == 2 && diffCol == 1)
			return true;
		else if (diffRow == 1 && diffCol == 2)
			return true;
		
		return false;
		
	}

}
