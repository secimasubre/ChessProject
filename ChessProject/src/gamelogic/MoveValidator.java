package gamelogic;

import main.ChessGame;
import model.Piece;

public class MoveValidator {

	private ChessGame chessGame;
	private Piece sourcePiece;
	private Piece targetPiece;

	public MoveValidator(ChessGame chessGame) {
		this.chessGame = chessGame;
	}

	public boolean isMoveValid(Move move) {
		int sourceRow = move.sourceRow;
		int sourceColumn = move.sourceColumn;
		int targetRow = move.targetRow;
		int targetColumn = move.targetColumn;

		sourcePiece = chessGame.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
		targetPiece = chessGame.getNonCapturedPieceAtLocation(targetRow, targetColumn);

		if (sourcePiece == null)
			return false;
		if (sourcePiece.getColor() == Piece.COLORS.WHITE
				&& this.chessGame.getGameState() == ChessGame.GAMESTATE.WHITE_STATE) {
			// ok
		} else if (sourcePiece.getColor() == Piece.COLORS.BLACK
				&& this.chessGame.getGameState() == ChessGame.GAMESTATE.BLACK_STATE) {
			// ok
		} else {
			// nije tvoj red
			return false;
		}

		// polje koje gadja van opsega
		if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8 || targetColumn < Piece.COLUMN_A
				|| targetColumn > Piece.COLUMN_H)
			return false;

		boolean validPieceMove = false;
		switch (sourcePiece.getType()) {
		case BISHOP:
			validPieceMove = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		case KING:
			validPieceMove = isValidKingMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		case KNIGHT:
			validPieceMove = isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		case PAWN:
			validPieceMove = isValidPawnMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		case QUEEN:
			validPieceMove = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		case ROOK:
			validPieceMove = isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
			break;
		default:
			break;
		}
		if (!validPieceMove) {
			return false;
		} else {
			// ok
		}
		return true;
	}

	// da li moze da jede
	public boolean isTargetCapturable() {
		if (targetPiece == null)
			return false;
		else if (targetPiece.getColor() != sourcePiece.getColor())
			return true;
		else
			return false;
	}

	private boolean isTargetLocationFree() {
		return targetPiece == null;
	}

	public boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

		if (isTargetLocationFree() || isTargetCapturable()) {
			// ovo je ok
		} else {
			return false;
		}

		boolean isValid = false;

		int difRow = targetRow - sourceRow;
		int difColumn = targetColumn - sourceColumn;

		if (difRow == difColumn && difColumn > 0) {
			// gore desno dijagonalno
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, +1);

		} else if (difRow == -difColumn && difColumn > 0) {
			// dole desno dijagonalno
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, +1);

		} else if (difRow == difColumn && difColumn < 0) {
			// dole levo dijagonalno
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, -1);

		} else if (difRow == -difColumn && difColumn < 0) {
			// gore levo dijagonalno
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, -1);

		} else {
			// ne krece se dobro
			isValid = false;
		}

		return isValid;
	}

	public boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		boolean result = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
		result |= isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
		return result;
	}

	/*public boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn){
		boolean isValid = false;
		
		if(isTargetLocationFree()){
			if(sourceColumn == targetColumn){
				if(sourcePiece.getColor() == Piece.COLORS.WHITE){
					if(sourceRow+1==targetRow){
						isValid = true;
					}else if(sourceRow ==1 && sourceRow+2 == targetRow){
						isValid = true;
					}
					else{
						isValid = false;
					}
				}else{
					if(sourceRow-1==targetRow){
						isValid = true;
					}else if(sourceRow==6 && sourceRow-2 == targetRow){
						isValid = true;
					}else{
						isValid = false;
					}
				}
			}else{
				//nisu u istoj koloni
				isValid = false;
			}
		}else if(isTargetCapturable()){
			if(sourceColumn+1 == targetColumn || sourceColumn-1 == targetColumn){
				//ide kolonu levo ili desno
				if(sourcePiece.getColor() == Piece.COLORS.WHITE){
					if(sourceRow+1 == targetRow){
						isValid = true;
					}else{
						isValid = false;
					}
				}else{
					if(sourceRow-1 == targetRow){
						isValid = true;
					}else{
						isValid = false;
					}
				}
			}else{
				isValid = false;
			}
		}
		//treba dodati da na prvom pomeranju moze dva polja da se pomeri
		return isValid;
	}*/

	
// OVDE SAM DODAO STARI PawnMove jer mi na taj nacin bar odradi jedan potez ai
private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		boolean isValid = false;
		// The pawn may move forward to the unoccupied square immediately in front
		// of it on the same file, or on its first move it may advance two squares
		// along the same file provided both squares are unoccupied
		if( isTargetLocationFree() ){
			if( sourceColumn == targetColumn){
				// same column
				if(  sourcePiece.getColor() == Piece.COLORS.WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// move one up
						isValid = true;
					}else{
						//not moving one up
						isValid = false;
					}
				}else{
					// black
					if( sourceRow-1 == targetRow ){
						// move one down
						isValid = true;
					}else{
						//not moving one down
						isValid =  false;
					}
				}
			}else{
				// not staying in the same column
				isValid = false;
			}
			
		// or it may move
		// to a square occupied by an opponent’s piece, which is diagonally in front
		// of it on an adjacent file, capturing that piece. 
		}else if( isTargetCapturable() ){
			
			if( sourceColumn+1 == targetColumn || sourceColumn-1 == targetColumn){
				// one column to the right or left
				if(  sourcePiece.getColor() == Piece.COLORS.WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// move one up
						isValid = true;
					}else{
						//not moving one up
						isValid = false;
					}
				}else{
					// black
					if( sourceRow-1 == targetRow ){
						// move one down
						isValid = true;
					}else{
						//not moving one down
						isValid = false;
					}
				}
			}else{
				// note one column to the left or right
				isValid = false;
			}
		}
		
		// on its first move it may advance two squares
		// ..
		
		// The pawn has two special
		// moves, the en passant capture, and pawn promotion.
		
		// en passant
		// ..
		return isValid;
	}

	private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

		if (isTargetLocationFree() || isTargetCapturable()) {
			// ovo je ok
		} else {
			return false;
		}
		if (sourceRow + 2 == targetRow && sourceColumn + 1 == targetColumn) {
			// gore gore desno
			return true;
		} else if (sourceRow + 1 == targetRow && sourceColumn + 2 == targetColumn) {
			// gore desno desno
			return true;
		} else if (sourceRow - 1 == targetRow && sourceColumn + 2 == targetColumn) {
			// dole desno desno
			return true;
		} else if (sourceRow - 2 == targetRow && sourceColumn + 1 == targetColumn) {
			// dole dole desno
			return true;
		} else if (sourceRow - 2 == targetRow && sourceColumn - 1 == targetColumn) {
			// dole dole levo
			return true;
		} else if (sourceRow - 1 == targetRow && sourceColumn - 2 == targetColumn) {
			// dole levo levo
			return true;
		} else if (sourceRow + 1 == targetRow && sourceColumn - 2 == targetColumn) {
			// gore levo levo
			return true;
		} else if (sourceRow + 2 == targetRow && sourceColumn - 1 == targetColumn) {
			// gore gore levo
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		if (isTargetCapturable() || isTargetLocationFree()) {
			// ovo je ok
		} else {
			return false;
		}

		if (sourceRow + 1 == targetRow && sourceColumn + 1 == targetColumn) {
			// gore
			return true;
		} else if (sourceRow + 1 == targetRow && sourceColumn + 1 == targetColumn) {
			// gore desno
			return true;
		} else if (sourceRow == targetRow && sourceColumn + 1 == targetColumn) {
			// desno
			return true;
		} else if (sourceRow - 1 == targetRow && sourceColumn + 1 == targetColumn) {
			// dole desno
			return true;
		} else if (sourceRow - 1 == targetRow && sourceColumn == targetColumn) {
			// dole
			return true;
		} else if (sourceRow - 1 == targetRow && sourceColumn - 1 == targetColumn) {
			// dole levo
			return true;
		} else if (sourceRow == targetRow && sourceColumn - 1 == targetColumn) {
			// levo
			return true;
		} else if (sourceRow + 1 == targetRow && sourceColumn - 1 == targetColumn) {
			// gore levo
			return true;
		} else {
			return false;
		}

	}

	public boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

		if (isTargetLocationFree() || isTargetCapturable()) {
			// ovo je ok
		} else {
			return false;
		}

		boolean isValid = false;

		int diffRow = targetRow - sourceRow;
		int diffColumn = targetColumn - sourceColumn;

		if (diffRow == 0 && diffColumn > 0) {
			// desno
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 0, +1);

		} else if (diffRow == 0 && diffColumn < 0) {
			// levo
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 0, -1);

		} else if (diffRow > 0 && diffColumn == 0) {
			// gore
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, +1, 0);

		} else if (diffRow < 0 && diffColumn == 0) {
			// dole
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, 0);

		} else {
			// ne moze dijagonala
			isValid = false;
		}

		return isValid;
	}

	private boolean arePiecesBetweenSourceAndTarget(int sourceRow, int sourceColumn, int targetRow, int targetColumn,
			int rowIncrementPerStep, int columnIncrementPerStep) {

		int currentRow = sourceRow + rowIncrementPerStep;
		int currentColumn = sourceColumn + columnIncrementPerStep;
		while (true) {
			if (currentRow == targetRow && currentColumn == targetColumn) {
				break;
			}
			if (currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8 || currentColumn < Piece.COLUMN_A
					|| currentColumn > Piece.COLUMN_H) {
				break;
			}

			if (this.chessGame.isNonCapturedPieceAtLocation(currentRow, currentColumn)) {
				return true;
			}

			currentRow += rowIncrementPerStep;
			currentColumn += columnIncrementPerStep;
		}
		return false;
	}

}
