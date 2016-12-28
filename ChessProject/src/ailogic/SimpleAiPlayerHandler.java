package ailogic;

import gamelogic.IPlayerHandler;
import gamelogic.Move;
import gamelogic.MoveValidator;

import java.util.ArrayList;
import java.util.List;

import main.ChessGame;
import main.ChessGame.GAMESTATE;
import model.Piece;
import model.Piece.COLORS;
import model.Piece.TYPE;

public class SimpleAiPlayerHandler implements IPlayerHandler{
	
	private ChessGame chessGame;
	private MoveValidator validator;
	
	//dubina u koju ai ide tokom vrsenja pretrage buducih poteza
	public int maxDepth = 2;
	
	public SimpleAiPlayerHandler(ChessGame chessGame) {
		this.chessGame = chessGame;
		this.validator = this.chessGame.getMoveValidator();
	}

	@Override
	public Move getMove() {
		return getBestMove();
	}
	
	private Move getBestMove() {
		System.out.println("getting best move");
		System.out.println("thinking...");
		
		List<Move> validMoves = generateMoves(false);
		int bestResult = Integer.MIN_VALUE;
		Move bestMove = null;
		
		for (Move move : validMoves) {
			executeMove(move);
			int evaluationResult = -1 * negaMax(this.maxDepth,"");
			undoMove(move);
			if( evaluationResult > bestResult){
				bestResult = evaluationResult;
				bestMove = move;
			}
		}
		System.out.println("done thinking! best move is: "+bestMove);
		return bestMove;
	}

	@Override
	public void moveSuccessfullyExecuted(Move move) {
		System.out.println("executed: "+move);
	}
	
	//algoritam nega max za procenu poteza
	private int negaMax(int depth, String indent) {

		if (depth <= 0
			|| this.chessGame.getGameState() == ChessGame.GAMESTATE.GAME_END_STATE_WHITE_WON
			|| this.chessGame.getGameState() == ChessGame.GAMESTATE.GAME_END_STATE_BLACK_WON){
			
			return evaluateState();
		}
		
		List<Move> moves = generateMoves(false);
		int currentMax = Integer.MIN_VALUE;
		
		for(Move currentMove : moves){
			
			executeMove(currentMove);
			int score = -1 * negaMax(depth - 1, indent+" ");
			undoMove(currentMove);
			
			if( score > currentMax){
				currentMax = score;
			}
		}
		return currentMax;
	}
	
	//funckija za vracanje poteza (zajedno sa stanjem) unazad
	private void undoMove(Move move) {
		this.chessGame.undoMove(move);
	}
	
	//funkcija za izvrsavanje poteza
	private void executeMove(Move move) {
		this.chessGame.movePiece(move);
		this.chessGame.changeGameState();
	}
	
	private int evaluateState() {

		//racunanje rezultata za sve figure
		int scoreWhite = 0;
		int scoreBlack = 0;
		
		for (Piece piece : this.chessGame.getPieces()) {
			if(piece.getColor() == Piece.COLORS.BLACK){
				scoreBlack +=
					getScoreForPieceType(piece.getType());
				scoreBlack +=
					getScoreForPiecePosition(piece.getRow(),piece.getColumn());
			}else if( piece.getColor() == Piece.COLORS.WHITE){
				scoreWhite +=
					getScoreForPieceType(piece.getType());
				scoreWhite +=
					getScoreForPiecePosition(piece.getRow(),piece.getColumn());
			}else{
				throw new IllegalStateException(
						"unknown piece color found: "+piece.getColor());
			}
		}
		
		//evaluacija vraca rezultat u zavisnosti na koga je red da igra
		GAMESTATE gameState = this.chessGame.getGameState();
		
		if( gameState == ChessGame.GAMESTATE.BLACK_STATE){
			return scoreBlack - scoreWhite;
		
		}else if(gameState == ChessGame.GAMESTATE.WHITE_STATE){
			return scoreWhite - scoreBlack;
		
		}else if(gameState == ChessGame.GAMESTATE.GAME_END_STATE_WHITE_WON
				|| gameState == ChessGame.GAMESTATE.GAME_END_STATE_BLACK_WON){
			return Integer.MIN_VALUE + 1;
		
		}else{
			throw new IllegalStateException("unknown game state: "+gameState);
		}
	}
	
	//funckija za generisanje svih poteza koje igrac na potezu moze da izvede, uz njihovo smestanje u listu
	private List<Move> generateMoves(boolean debug) {

		List<Piece> pieces = this.chessGame.getPieces();
		List<Move> validMoves = new ArrayList<Move>();
		Move testMove = new Move(0,0,0,0);
		
		COLORS pieceColor = (this.chessGame.getGameState()==ChessGame.GAMESTATE.WHITE_STATE
			?COLORS.WHITE
			:COLORS.BLACK);

		for (Piece piece : pieces) {

			// gledamo samo za figure igraca koji je trenutno na potezu
			if (pieceColor == piece.getColor()) {
				// pocetak generisanja poteza
				testMove.sourceRow = piece.getRow();
				testMove.sourceColumn = piece.getColumn();

				// pretrazi sve kolone i vrste
				for (int targetRow = Piece.ROW_1; targetRow <= Piece.ROW_8; targetRow++) {
					for (int targetColumn = Piece.COLUMN_A; targetColumn <= Piece.COLUMN_H; targetColumn++) {

						// zavrsi generisanje poteza
						testMove.targetRow = targetRow;
						testMove.targetColumn = targetColumn;

						// proveri da li je generisani potez validan
						if (this.validator.isMoveValid(testMove)) {
							// ako jeste, dodaj ga u listu
							validMoves.add(testMove.clone());
						} else {
							// nije dobar potez, preskacemo ga
						}
					}
				}

			}
		}
		return validMoves;
	}
	
	//postavljanje vrednosti poljima table tako da su polja bliza centru bolje ocenjena
	private int getScoreForPiecePosition(int row, int column) {
		byte[][] positionWeight =
		{ {1,1,1,1,1,1,1,1}
		 ,{2,2,2,2,2,2,2,2}
		 ,{2,2,3,3,3,3,2,2}
		 ,{2,2,3,4,4,3,2,2}
		 ,{2,2,3,4,4,3,2,2}
		 ,{2,2,3,3,3,3,2,2}
		 ,{2,2,2,2,2,2,2,2}
		 ,{1,1,1,1,1,1,1,1}
		 };
		return positionWeight[row][column];
	}
	
	//postavljanje vrednosti svake figure
	private int getScoreForPieceType(TYPE type){
		switch (type) {
			case BISHOP: return 30;
			case KING: return 99999;
			case KNIGHT: return 30;
			case PAWN: return 10;
			case QUEEN: return 90;
			case ROOK: return 50;
			default: throw new IllegalArgumentException("unknown piece type: "+type);
		}
	}
}
