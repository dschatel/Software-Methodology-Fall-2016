package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


/**
 * This class drives the game logic for a Chess Match.
 * It repeatedly redraws the game board and asks for input
 * Until a checkmate condition is found.
 * 
 * @author Derek Schatel
 *
 */
public class Chess {

	private static boolean isWhiteTurn = true;
	private static String playerInput;
	private static int oldRow;
	private static int oldCol;
	private static int newRow;
	private static int newCol;
	private static GameBoard gameBoard;
	private static boolean  gameRunning;
	private static boolean drawRequested;
	private static String promotion;
	private static boolean enPassantAllowed = false;

	
	
	/**
	 * This method terates over the GameBoard object and draws the
	 * visual elements.
	 */
	public static void drawBoard() {

		for (int i = 0; i < gameBoard.getBoard().length; i++) {
			for (int j = 0; j < gameBoard.getBoard()[i].length; j++) {

				if (gameBoard.getBoard()[i][j] == null) {
					if ((i %2 == 0 && j %2 == 1) || (i %2 == 1 && j %2 == 0))
						System.out.print("## ");
					else if ((i % 2 == 0 && j % 2 == 0) || (i %2 == 1 && j % 2 == 1))
						System.out.print("   ");
					}
				else
					System.out.print(gameBoard.getBoard()[i][j].getPiece() + " ");

			}
			System.out.println(gameBoard.getBoard().length - i);
		}

		System.out.println(" a  b  c  d  e  f  g  h");
	}

	/**
	 * This method breaks down the user's input and handles it based on how many tokens it finds.
	 * If the tokens are valid RankFile tokens, it will retrieve their integer equivalents.
	 */
	public static void tokenizeInput() {

		StringTokenizer st = new StringTokenizer(playerInput.toLowerCase().trim(), " ");
		String[] tokens = new String[st.countTokens()];
		int tokenCount = 0;

		while (st.hasMoreTokens()) {

			tokens[tokenCount++] = st.nextToken();

		}

		if (tokens.length > 0 && tokens.length < 4) {

			if (tokens.length == 1) {
				if(tokens[0].equals("resign")) {
					if(isWhiteTurn) {
						System.out.println("\nBlack wins!");
						gameRunning = false;
					}
					else {
						System.out.println("\nWhite wins!");
						gameRunning = false;
					}
				}
				else if (tokens[0].equals("draw")) {

					if (drawRequested) {
						System.out.println("Draw!");
						gameRunning = false;
					}
					else {
						System.out.println("\nOpposing player must request a draw first.\n");
					}
				}
				else
					System.out.println("\nInvalid input. Please try again.\n");
			}
			else if (tokens.length == 2) {
				if (tokens[0].length() == 2 && tokens[1].length() == 2) {
					obtainCoords(tokens);
				}
				else
					System.out.println("\nInvalid input. Please try again.\n");
			}
			else if (tokens.length == 3) {
				if (tokens[0].length() == 2 && tokens[1].length() == 2) {
					obtainCoords(tokens);

					if(tokens[2].equals("draw?"))
						drawRequested = true;
					else if(tokens[2].equals("q") || tokens[2].equals("n") || tokens[2].equals("b") || tokens[2].equals("r"))
						promotion = tokens[2];
					else
						System.out.println("\nInvalid input, please try again.\n");

				}
				else
					System.out.println("\nInvalid input. Please try again.\n");
			}
		}
		else
			System.out.println("\nInvalid input. Please try again.\n");

	}

	/**
	 * This is an umbrella method for obtaining the matrix row and
	 * column values based on the user's input
	 * 
	 * @param tokens Array of Strings representing individual tokens 
	 */
	private static void obtainCoords(String[] tokens) {
		oldCol = alphaToNum(tokens[0].substring(0, 1).toLowerCase());
		newCol = alphaToNum(tokens[1].substring(0, 1).toLowerCase());

		if (validateNum(tokens[0].substring(1)) && validateNum(tokens[1].substring(1))) {
		oldRow = Math.abs(Integer.parseInt(tokens[0].substring(1) ) -8);
		newRow = Math.abs(Integer.parseInt(tokens[1].substring(1) ) -8);
		}
		else
			System.out.println("\nInvalid input. Please try again.\n");

		if (oldCol == -1 || newCol == -1)
			System.out.println("\nInvalid input. Please try again.\n");
	}

	/**
	 * This method ensures that the File number passed from input is valid for the game board.
	 * 
	 * @param alpha File number as a string
	 * @return boolean value for whether File number is valid
	 */
	private static boolean validateNum(String alpha) {

		int num = -1;

		try {

			num = Integer.parseInt(alpha);

		} catch (NumberFormatException nfe) {
			return false;
		}

		if (num < 0 || num > 8)
			return false;
		else
			return true;

	}

	/**
	 * This method parses the Rank value passed from input.
	 * 
	 * @param alpha String representing the Rank
	 * @return corresponding integer value for 2D array
	 */
	private static int alphaToNum(String alpha) {
		switch(alpha) {
		case "a": return 0;
		case "b": return 1;
		case "c": return 2;
		case "d": return 3;
		case "e": return 4;
		case "f": return 5;
		case "g": return 6;
		case "h": return 7;
		default: return -1;
		}
	}

	/**
	 * This method handles pawn promotion. If a pawn has found its way into the outer rows,
	 * the method checks when the requested promotion type is and replaced the pawn with
	 * a new Object. Defaults to Queen if no promotion type selected.
	 */
	public static void promote() {
		
		if (promotion == null)
			promotion = "Q";
		
		if (newRow == 0) {
			// check to make sure the space being moved into is on opposite side of board depending on color
			// for white
			// we should be using [oldRow][oldCol] right? since in the current state the piece is originally there
			if (gameBoard.getBoard()[newRow][newCol].getPiece().equals("wP")) {
				// ok to promote now?
				// promotion string should just specific ONLY the piece itself
				// perform case insensitive check in case some doofus decides to type in all caps or something
					
				switch (promotion.toUpperCase()) {
				case "R":
					gameBoard.getBoard()[newRow][newCol] = new Rook("w");
					break;
				case "N":
					gameBoard.getBoard()[newRow][newCol] = new Knight("w");
					break;
				case "B":
					gameBoard.getBoard()[newRow][newCol] = new Bishop("w");
					break;
				case "Q":
					gameBoard.getBoard()[newRow][newCol] = new Queen("w");
				default:
					gameBoard.getBoard()[newRow][newCol] = new Queen("w");
					break;
				}
			}
		}
		else if (newRow == 7) {
			// for black
			if (gameBoard.getBoard()[newRow][newCol].getPiece().equals("bP")) {
				// ok to promote now?
				// promotion string should just specific ONLY the piece itself
				// perform case insensitive check in case some doofus decides to type in all caps or something
				switch (promotion.toUpperCase()) {
				case "R":
					gameBoard.getBoard()[newRow][newCol] = new Rook("b");
					break;
				case "N":
					gameBoard.getBoard()[newRow][newCol] = new Knight("b");
					break;
				case "B":
					gameBoard.getBoard()[newRow][newCol] = new Bishop("b");
					break;
				case "Q":
					gameBoard.getBoard()[newRow][newCol] = new Queen("b");
				default:
					gameBoard.getBoard()[newRow][newCol] = new Queen("b");
					break;
				}
			}
		}

	}

	/**
	 * Handles all logic related to moving pieces around the board. If the piece can legally move
	 * and if it can reach its destination, moves the piece, nulls out the original location's reference
	 * and swaps player turns.
	 */
	public static void movePiece () {

		//Checks if "piece" trying to be moved is an empty space
		if (gameBoard.getBoard()[oldRow][oldCol] == null)
			System.out.println("\nThere is no piece at that location.\n");

		//Checks if the piece trying to be moved is owned by the player
		else if ((isWhiteTurn && gameBoard.getBoard()[oldRow][oldCol].getColor().equals("w")) ||
				(!isWhiteTurn && gameBoard.getBoard()[oldRow][oldCol].getColor().equals("b")))	{

			if (gameBoard.castling(oldRow, oldCol, newRow, newCol)) {
				isWhiteTurn = !isWhiteTurn;
			}
			//Checks if the piece trying to be moved has a clear path to its destination
			else if (gameBoard.checkPath(oldRow, oldCol, newRow, newCol)) {

				//Checks if the piece trying to be moved is legally allowed to do so within its moveset
				if(gameBoard.getBoard()[oldRow][oldCol].moveSet(oldRow, oldCol, newRow, newCol)) {

					//Moves the piece if the target space is empty
					if(gameBoard.getBoard()[newRow][newCol] == null) {
						//Add conditional check for Pawns

						// if piece is a pawn and it's trying to move into a diagonal space that's empty
						if((gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("wP")
								|| gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("bP")) && Math.abs(oldCol-newCol) == 1) {

							if (enPassantAllowed) {


								if(newRow == 2) {
									if(gameBoard.getBoard()[newRow+1][newCol] == null || !gameBoard.getBoard()[newRow+1][newCol].getPiece().equals("bP")) {
										System.out.println("\nInvalid move, try again.\n");
									}

									else {
										gameBoard.execute(newRow, newCol, gameBoard.getBoard()[oldRow][oldCol]);
										gameBoard.execute(oldRow, oldCol, null);
										gameBoard.execute(newRow+1, newCol, null);
										enPassantAllowed = false;
										isWhiteTurn = !isWhiteTurn;

									}

								}
								else if (newRow == 5){
									if (gameBoard.getBoard()[newRow-1][newCol] == null || !gameBoard.getBoard()[newRow-1][newCol].getPiece().equals("wP")) {
										System.out.println("\nInvalid move, try again.\n");
									}
									else {
										gameBoard.execute(newRow, newCol, gameBoard.getBoard()[oldRow][oldCol]);
										gameBoard.execute(oldRow, oldCol, null);
										gameBoard.execute(newRow-1, newCol, null);
										enPassantAllowed = false;
										isWhiteTurn = !isWhiteTurn;

									}
								}
							}
							else
								System.out.println("\nInvalid move for that piece.\n");
						}
						else {
							if(gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("bP")
									|| gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("wP")) {
								enPassantAllowed = gameBoard.enPassant(oldRow, oldCol, newRow, newCol);
							}
							gameBoard.execute(newRow, newCol, gameBoard.getBoard()[oldRow][oldCol]);
							gameBoard.execute(oldRow, oldCol, null);
							isWhiteTurn = !isWhiteTurn;
						}
					}

					//Moves the piece if the target space has a piece of the opposing color
					else if (gameBoard.getBoard()[newRow][newCol] != null &&
							!gameBoard.getBoard()[newRow][newCol].getColor().equals(gameBoard.getBoard()[oldRow][oldCol].getColor())) {

						if((gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("wP")
								|| gameBoard.getBoard()[oldRow][oldCol].getPiece().equals("bP")) && Math.abs(oldCol-newCol) == 0) {
							System.out.println("\nThere is another piece in the way.\n");
						}
						else {
						gameBoard.execute(newRow, newCol, gameBoard.getBoard()[oldRow][oldCol]);
						gameBoard.execute(oldRow,  oldCol,  null);
						isWhiteTurn = !isWhiteTurn;
						}
					}
				}
				else
					System.out.println("\nMove is not a valid move for that piece.\n");
			}
			else
				System.out.println("\nCannot move - there is another piece in the way.\n");
			}
			else
				System.out.println("\nThat is not your piece.\n");
	}

	public static void main(String[] args) {

		gameBoard = new GameBoard();
		gameRunning = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(gameRunning) {
			drawBoard();

			if (gameBoard.isCheck(isWhiteTurn) && isWhiteTurn)
				System.out.println("White King is in Check.");
			else if(gameBoard.isCheck(isWhiteTurn) && !isWhiteTurn)
				System.out.println("Black King is in Check.");


			try {
			if(isWhiteTurn)
				System.out.print("\nWhite player's turn: ");
			else
				System.out.print("\nBlack player's turn: ");

				playerInput = br.readLine();
				tokenizeInput();

				if (gameRunning) {
					movePiece();
					promote();
				}

				if (gameBoard.isCheck(!isWhiteTurn) && !isWhiteTurn) {
					System.out.println("Checkmate, Black Wins.");
					gameRunning = false;
				}
				else if(gameBoard.isCheck(!isWhiteTurn) && isWhiteTurn) {
					System.out.println("Checkmate, White Wins.");
					gameRunning = false;
				}

			} catch (IOException e) { System.out.println("Invalid input. Please try again."); };


		}

	}

}
