package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import gui.ChessGui;
import main.Main;
import model.Piece;

public class PieceDragDropListener implements MouseListener, MouseMotionListener {

	private ArrayList<Piece> pieces;	
	
	private Piece dragPiece;
	private int xOffsetDrag;
	private int yOffsetDrag;
	
	public PieceDragDropListener(ArrayList<Piece> pieces){
		this.pieces = pieces;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		for(int i = this.pieces.size()-1;i>=0;i--){
			Piece piece = this.pieces.get(i);
			if(mouseOverPiece(piece, x, y)){
				
				//Check the game state first before you let drag
				if( (Main.chess.getGameState() == ChessGui.GAMESTATE.WHITE_STATE && piece.getColor() == ChessGui.COLORS.WHITE) 
					|| (Main.chess.getGameState() == ChessGui.GAMESTATE.BLACK_STATE && piece.getColor() == ChessGui.COLORS.BLACK)){
				
				this.xOffsetDrag = x - piece.getX();
				this.yOffsetDrag = y - piece.getY();
				this.dragPiece = piece;
				
				}
			}
		}
		
		if(this.dragPiece !=null){
			this.pieces.remove(this.dragPiece);
			this.pieces.add(this.dragPiece);
		}
		
	}

	
	private boolean mouseOverPiece(Piece piece, int x, int y){
		return piece.getX() <= x && piece.getX() + piece.getWidth() >= x && 
				piece.getY()<=y && piece.getY() + piece.getHeight() >=y;
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.dragPiece != null){
			this.dragPiece.setX(e.getX() - this.xOffsetDrag);
			this.dragPiece.setY(e.getY() - this.yOffsetDrag);
			Main.chess.repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//prsoxgfbxffxb


	@Override
	public void mouseReleased(MouseEvent e) {
		this.dragPiece = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
