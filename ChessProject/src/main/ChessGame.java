package main;

import gamelogic.IPlayerHandler;
import gamelogic.Move;
import gamelogic.MoveValidator;

import java.util.ArrayList;

import model.Piece;


/**
 * table
 *   a  b  c  d  e  f  g  h  
 *  +--+--+--+--+--+--+--+--+
 * 8|BR|BN|BB|BQ|BK|BB|BN|BR|8
 * +--+--+--+--+--+--+--+--+
 * 7|BP|BP|BP|BP|BP|BP|BP|BP|7
 *  +--+--+--+--+--+--+--+--+
 * ..
 * 2|WP|WP|WP|WP|WP|WP|WP|WP|2
 *  +--+--+--+--+--+--+--+--+
 * 1|WR|WN|WB|WQ|WK|WB|WN|WR|1
 *  +--+--+--+--+--+--+--+--+
 *   a  b  c  d  e  f  g  h  
 *
 */

public class ChessGame implements Runnable{

	public static enum GAMESTATE {
		WHITE_STATE, BLACK_STATE, GAME_END_STATE_BLACK_WON, GAME_END_STATE_WHITE_WON
	};
	private GAMESTATE gameState = GAMESTATE.WHITE_STATE;
	
	private MoveValidator moveValidator;
	
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Piece> capturedPieces = new ArrayList<Piece>();
	
	private IPlayerHandler blackPlayerHandler;
	private IPlayerHandler whitePlayerHandler;
	private IPlayerHandler activePlayerHandler;
	
	
	public ChessGame() {

		moveValidator = new MoveValidator(this);
		
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.ROOK, Piece.ROW_1, Piece.COLUMN_A);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.KNIGHT, Piece.ROW_1, Piece.COLUMN_B);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.BISHOP, Piece.ROW_1, Piece.COLUMN_C);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.KING, Piece.ROW_1, Piece.COLUMN_D);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.QUEEN, Piece.ROW_1, Piece.COLUMN_E);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.BISHOP, Piece.ROW_1, Piece.COLUMN_F);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.KNIGHT, Piece.ROW_1, Piece.COLUMN_G);
		createPiece(Piece.COLORS.WHITE, Piece.TYPE.ROOK, Piece.ROW_1, Piece.COLUMN_H);

		int currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(Piece.COLORS.WHITE, Piece.TYPE.PAWN, Piece.ROW_2, currentColumn);
			currentColumn++;
		}

		createPiece(Piece.COLORS.BLACK, Piece.TYPE.ROOK, Piece.ROW_8, Piece.COLUMN_A);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.KNIGHT, Piece.ROW_8, Piece.COLUMN_B);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.BISHOP, Piece.ROW_8, Piece.COLUMN_C);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.KING, Piece.ROW_8, Piece.COLUMN_D);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.QUEEN, Piece.ROW_8, Piece.COLUMN_E);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.BISHOP, Piece.ROW_8, Piece.COLUMN_F);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.KNIGHT, Piece.ROW_8, Piece.COLUMN_G);
		createPiece(Piece.COLORS.BLACK, Piece.TYPE.ROOK, Piece.ROW_8, Piece.COLUMN_H);

		currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(Piece.COLORS.BLACK, Piece.TYPE.PAWN, Piece.ROW_7, currentColumn);
			currentColumn++;
		}
	}

	private void createPiece(Piece.COLORS color, Piece.TYPE type, int row, int column) {
		// Image img = this.getPieceImage(color, type, row, column);
		Piece piece = new Piece(color, type, column, row);
		this.pieces.add(piece);
		//this.movePiece(piece, row, column);
	}

	public void setPlayer(Piece.COLORS pieceColor, IPlayerHandler playerHandler){
		switch(pieceColor){
			case WHITE: this.whitePlayerHandler = playerHandler; break;
			case BLACK: this.blackPlayerHandler = playerHandler; break;
			default: throw new IllegalArgumentException("Invalid pieceColor: "+pieceColor);
		}
	}
	
	public void startGame(){
		while(this.blackPlayerHandler == null || this.whitePlayerHandler == null){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				
			}
		}
		
		this.activePlayerHandler = whitePlayerHandler;
		while(!isEndOfGame()){
			waitForMove();
			swapPlayers();
		}
	}
	
	private void swapPlayers(){
		
		if(this.activePlayerHandler == this.blackPlayerHandler){
			this.activePlayerHandler = this.whitePlayerHandler;
		}else{
			this.activePlayerHandler = this.blackPlayerHandler;
		}
		
		this.changeGameState();
	}
	
	private void waitForMove(){
		Move move = null;
		
		do{
			move = this.activePlayerHandler.getMove();
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(move == null || !this.moveValidator.isMoveValid(move));
		
		boolean success = this.movePiece(move);
		if(success){
			this.blackPlayerHandler.moveSuccessfullyExecuted(move);
			this.whitePlayerHandler.moveSuccessfullyExecuted(move);
		}else{
			throw new IllegalStateException("move was valid, but failed to execute it");
		}
	}
	
	
	
	public boolean movePiece(Move move) {
			
		Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);
		
		Piece.COLORS opponentColor = (piece.getColor() == Piece.COLORS.BLACK ? Piece.COLORS.WHITE : Piece.COLORS.BLACK);
		if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
			Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
			opponentPiece.setCaptured(true);
			this.pieces.remove(opponentPiece);
			this.capturedPieces.add(opponentPiece);
		}
		piece.setRow(move.targetRow);
		piece.setColumn(move.targetColumn);
		return true;
	}

	public Piece getNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column && piece.isCaptured() == false) {
				return piece;
			}
		}
		return null;
	}

	public	 boolean isNonCapturedPieceAtLocation(Piece.COLORS color, int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column && piece.isCaptured() == false
					&& piece.getColor() == color) {
				return true;
			}
		}
		return false;
	}

	public String getGameStateAsText() {
		return (this.gameState == GAMESTATE.BLACK_STATE ? "black" : "white");
	}
	
	public boolean isNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column
					&& piece.isCaptured() == false) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isEndOfGame(){
		for(Piece piece : capturedPieces){
			if(piece.getType() == Piece.TYPE.KING && piece.isCaptured()){
				return true;
			}
		}
		return false;
	}

	public void changeGameState() {
		
		if(isEndOfGame()){
			if(this.gameState == ChessGame.GAMESTATE.BLACK_STATE)
				this.gameState = ChessGame.GAMESTATE.GAME_END_STATE_BLACK_WON;
			else if(this.gameState == ChessGame.GAMESTATE.WHITE_STATE)
				this.gameState = ChessGame.GAMESTATE.GAME_END_STATE_WHITE_WON;
			
			return;
		}
		
		
		switch (this.gameState) {
		case BLACK_STATE:
			this.gameState = GAMESTATE.WHITE_STATE;
			break;
		case WHITE_STATE:
			this.gameState = GAMESTATE.BLACK_STATE;
			break;
		case GAME_END_STATE_BLACK_WON:
			break;
		case GAME_END_STATE_WHITE_WON:
			break;
		default:
			throw new IllegalStateException("unknown game state:" + this.gameState);
		}

	}
	
	//vracanje jednog poteza unazad uz promenu stanja
	public void undoMove(Move move){
		Piece piece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
		
		piece.setRow(move.sourceRow);
		piece.setColumn(move.sourceColumn);
		
		if(move.capturedPiece != null){
			move.capturedPiece.setRow(move.targetRow);
			move.capturedPiece.setColumn(move.targetColumn);
			move.capturedPiece.isCaptured(false);
			this.capturedPieces.remove(move.capturedPiece);
			this.pieces.add(move.capturedPiece);
		}
		
		if(piece.getColor() == Piece.COLORS.BLACK){
			this.gameState = ChessGame.GAMESTATE.BLACK_STATE;
		}else{
			this.gameState = ChessGame.GAMESTATE.WHITE_STATE;
		}
	}

	public GAMESTATE getGameState() {
		return gameState;
	}

	public void setGameState(GAMESTATE gameState) {
		this.gameState = gameState;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public MoveValidator getMoveValidator() {
		return moveValidator;
	}

	public void setMoveValidator(MoveValidator moveValidator) {
		this.moveValidator = moveValidator;
	}

	@Override
	public void run() {
		this.startGame();
		
	}
}
