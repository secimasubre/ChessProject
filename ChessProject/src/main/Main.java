package main;

import gui.ChessGui;
import model.Piece;
import ailogic.SimpleAiPlayerHandler;

public class Main {

	public static ChessGui chess;
	
	public static void main(String[] args) {
		
		chess = new ChessGui();
		
		//ai igraci
		SimpleAiPlayerHandler ai1 = new SimpleAiPlayerHandler(chess.getChessGame());
		SimpleAiPlayerHandler ai2 = new SimpleAiPlayerHandler(chess.getChessGame());
		
		//razlicite "tezine"
		ai1.maxDepth = 1;
		ai2.maxDepth = 2;
		
		//podesavanje igraca
		chess.getChessGame().setPlayer(Piece.COLORS.WHITE, chess);
		chess.getChessGame().setPlayer(Piece.COLORS.BLACK, ai1);

        new Thread(chess.getChessGame()).start();
	}
	
	
}
