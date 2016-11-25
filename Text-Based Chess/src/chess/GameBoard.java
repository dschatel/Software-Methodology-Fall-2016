package chess;

/**
 * This class represents the GameBoard as a 2D array of Piece
 * Objects.
 * 
 * @author Derek Schatel
 *
 */
public class GameBoard {

	private Piece [][] gameBoard;

	public GameBoard() {
		gameBoard = new Piece[8][8];

		initialize();
	}

	/**
	 * Initializes the gameboard to its starting state, filling
	 * the starting positions with pointers to Piece objects of 
	 * the appropriate type.
	 */
	public void initialize() {
		gameBoard[0][0] = new Rook("b");
		gameBoard[0][1] = new Knight("b");
		gameBoard[0][2] = new Bishop("b");
		gameBoard[0][3] = new Queen("b");
		gameBoard[0][4] = new King("b");
		gameBoard[0][5] = new Bishop("b");
		gameBoard[0][6] = new Knight("b");
		gameBoard[0][7] = new Rook("b");

		for (int i = 0; i < 8; i++)
			gameBoard[1][i] = new Pawn("b");


		gameBoard[7][0] = new Rook("w");
		gameBoard[7][1] = new Knight("w");
		gameBoard[7][2] = new Bishop("w");
		gameBoard[7][3] = new Queen("w");
		gameBoard[7][4] = new King("w");
		gameBoard[7][5] = new Bishop("w");
		gameBoard[7][6] = new Knight("w");
		gameBoard[7][7] = new Rook("w");

		for (int i = 0; i < 8; i++)
			gameBoard[6][i] = new Pawn("w");

	}

	/**
	 * Checks to see if the piece to be moved has an unobstructed path
	 * to its destination.
	 * 
	 * @param oldRow Starting row value
	 * @param oldCol Starting column value
	 * @param newRow Destination row value
	 * @param newCol Destination column value
	 * @return boolean designating whether the path is clear
	 */
	public boolean checkPath(int oldRow, int oldCol, int newRow, int newCol) {

		int diffCol = newCol - oldCol;
		int diffRow = newRow - oldRow;
		int colDirection = 0;
		int rowDirection = 0;
		
		if (gameBoard[oldRow][oldCol].getPiece().equals("wN") || gameBoard[oldRow][oldCol].getPiece().equals("bN"))
			return true;
		else if ((gameBoard[oldRow][oldCol].getPiece().equals("wK") || gameBoard[oldRow][oldCol].getPiece().equals("bK")) && Math.abs(diffCol) <= 1)
			return true;

		/*
		 * 1. Check what direction it's going in - north, south, east or west.
		 * 2. If only moving in a cardinal direction, check just in that direction.
		 * 3. If moving in a diagonal, check in a diagonal direction.
		 */


		if (diffCol > 0)
			colDirection = 1;
		else if (diffCol < 0)
			colDirection = -1;

		if (diffRow > 0)
			rowDirection = 1;
		else if (diffRow < 0)
			rowDirection = -1;

		if (diffCol == 0) {
			oldRow+=rowDirection;
			for (int i = 0; i < Math.abs(diffRow)-1; i++) {
				if (gameBoard[oldRow][oldCol] != null)
					return false;

				oldRow+=rowDirection;
			}

		}

		if (diffRow == 0) {
			oldCol+=colDirection;
			for (int i = 0; i < Math.abs(diffCol)-1; i++) {
				if (gameBoard[oldRow][oldCol] != null)
					return false;
				oldCol+=colDirection;
			}
		}

		if (diffRow != 0 && diffCol != 0) {
			oldRow+=rowDirection;
			oldCol+=colDirection;
			for (int i =0; i < Math.abs(diffRow)-1; i++) {
				if (gameBoard[oldRow][oldCol] != null)
					return false;
				oldRow+=rowDirection;
				oldCol+=colDirection;
			}
		}


		return true;
	}

	/**
	 * Getter method to retrieve the game board instance.
	 * 
	 * @return gameBoard
	 */
	public Piece[][] getBoard() {
		return gameBoard;
	}

	/**
	 * Setter method for moving pointer references of Piece objects.
	 * 
	 * @param row Row location
	 * @param col Column location
	 * @param value Piece value to set location to
	 */
	public void execute (int row, int col, Piece value) {
		gameBoard[row][col] = value;
	}



	/**
	 * This method tests whether castling is possible. If it is, it
	 * performs a castling manuever.
	 * 
	 * @param oldRow Row of king to be castled
	 * @param oldCol Column of king to be castled
	 * @param newRow Row destination for castling
	 * @param newCol Column destination for castling
	 * @return boolean value for whether castling occurred
	 */
	public boolean castling(int oldRow, int oldCol, int newRow, int newCol) {

		int diffCol = newCol - oldCol;
		
		if (gameBoard[oldRow][oldCol] != null) {
			if (gameBoard[oldRow][oldCol].getRank().equals("K") && gameBoard[oldRow][oldCol].getTurn()) {
				if (diffCol == 2) {
					if (gameBoard[oldRow][7] != null) {
						if (gameBoard[oldRow][7].getRank().equals("R") && gameBoard[oldRow][7].getTurn()) {
							if (checkPath(oldRow, oldCol, newRow, 7)) {
								gameBoard[newRow][newCol] = gameBoard[oldRow][oldCol];
								gameBoard[oldRow][oldCol] = null;
								gameBoard[newRow][newCol].setTurn(false);
								gameBoard[newRow][5] = gameBoard[newRow][7];
								gameBoard[newRow][7] = null;
								gameBoard[newRow][5].setTurn(false);
								return true;
							}
						}
					}
				}
				else if (diffCol == -2) {
					if (gameBoard[oldRow][0] != null) {
						if (gameBoard[oldRow][0].getRank().equals("R") && gameBoard[oldRow][7].getTurn()) {
							if (checkPath(oldRow, oldCol, newRow, 0)) {
								gameBoard[newRow][newCol] = gameBoard[oldRow][oldCol];
								gameBoard[oldRow][oldCol] = null;
								gameBoard[newRow][newCol].setTurn(false);
								gameBoard[newRow][3] = gameBoard[newRow][0];
								gameBoard[newRow][0] = null;
								gameBoard[newRow][3].setTurn(false);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method tests whether the pawn being moved is capable of being captured 
	 * via the enPassant move.
	 * 
	 * @param oldRow Old pawn row
	 * @param oldCol Old pawn column
	 * @param newRow new Pawn row
	 * @param newCol new Pawn column
	 * @return boolean value for whether enPassant capture is capable on this pawn
	 */
	public boolean enPassant(int oldRow, int oldCol, int newRow, int newCol) {
		// first check that pawn is in a valid starting position and end row is valid
		// for white
		if (gameBoard[oldRow][oldCol].getPiece().equals("wP")) {
			if (oldRow == 6 && newRow == 4) {
				// piece is moving 2 spaces, check that there is an enemy pawn in correct position
				// check first for pawns in col 1 or 8
				
				if (newCol == 0) {
					if (gameBoard[newRow][newCol+1] == null)
						return false;
					if (gameBoard[newRow][newCol+1].getPiece().equals("bP")) {
						return true;
					}
				}
				else if (newCol == 7) {
					if (gameBoard[newRow][newCol-1] == null)
						return false;
					if (gameBoard[newRow][newCol-1].getPiece().equals("bP")) {
						return true;
					}
				}
				else {
					
						//If both adjacent colums are empty, no enPassant
					 if (gameBoard[newRow][newCol+1] == null && gameBoard[newRow][newCol-1] == null)
						 return false;
					 
					 if (gameBoard[newRow][newCol+1] != null)
						 if (gameBoard[newRow][newCol+1].getPiece().equals("bP"))
							 return true;
					 
					 if(gameBoard[newRow][newCol-1] != null)
						 if (gameBoard[newRow][newCol-1].getPiece().equals("bP"))
							 return true;
					 
					 return false;
				}
			}
			return false;
		}
		else if (gameBoard[oldRow][oldCol].getPiece().equals("bP")) {
			if (oldRow == 1 && newRow == 3) {
				// piece is moving 2 spaces, check that there is an enemy pawn in correct position
				// check first for pawns in col 1 or 8
				if (newCol == 0) {
					if (gameBoard[newRow][newCol+1] == null)
						return false;
					if (gameBoard[newRow][newCol+1].getPiece().equals("wP")) {
						return true;
					}
				}
				else if (newCol == 7) {
					if (gameBoard[newRow][newCol-1] == null)
						return false;
					if (gameBoard[newRow][newCol-1].getPiece().equals("wP")) {
						return true;
					}
				}
				else {
					 if (gameBoard[newRow][newCol+1] == null && gameBoard[newRow][newCol-1] == null)
						 return false;
					 
					 if (gameBoard[newRow][newCol+1] != null)
						 if (gameBoard[newRow][newCol+1].getPiece().equals("bP"))
							 return true;
					 
					 if(gameBoard[newRow][newCol-1] != null)
						 if (gameBoard[newRow][newCol-1].getPiece().equals("bP"))
							 return true;
					 
					 return false;
				}
			}
			return false;
		}
		else return false;
	}

	/**
	 * This method tests whether a King is currently in Check.
	 * 
	 * @param isWhiteTurn the current turn
	 * @return boolean value for whether a King is in Check.
	 */
	public boolean isCheck(boolean isWhiteTurn) {
		
		int kingRow = 0;
		int kingCol = 0;
		
		//If black just moved, find location of white King
		if (isWhiteTurn) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j< 8; j++) {
					if(gameBoard[i][j] != null) {
						if(gameBoard[i][j].getPiece().equals("wK")) {
							kingRow = i;
							kingCol = j;
							break;
						}
					}
				}
			}
		
		
		
		//Find black pieces, see if they have a clear path and legal move to the white king. If so, white king is in check
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (gameBoard[i][j] != null) {
					if (gameBoard[i][j].getColor().equals("b")){
						if (gameBoard[i][j].moveSet(i, j, kingRow, kingCol))
							if (checkPath(i, j, kingRow, kingCol)) {
							return true;
						}
					}
				}
			}
		}
	}
		
		//If white just moved, find location of black King
		else if (!isWhiteTurn) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j< 8; j++) {
					if (gameBoard[i][j] != null) {
						if(gameBoard[i][j].getPiece().equals("bK")) {
							kingRow = i;
							kingCol = j;
							break;
						}
					}
				}
			}
		
		
		
		//Find white pieces, see if they have a clear path and legal move to the black king. If so, black king is in check
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (gameBoard[i][j] != null) {
					if (gameBoard[i][j].getColor().equals("w")){
						if (gameBoard[i][j].moveSet(i, j, kingRow, kingCol))
							if (checkPath(i, j, kingRow, kingCol)) {
								return true;
						}
					}
				}
			}
		}
	}
		
		return false;
		
	}

}
