package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import gui.ChessGui;
import gui.PieceGui;
import main.ChessGame;
import main.Main;
import model.Piece;

public class PieceDragDropListener implements MouseListener, MouseMotionListener {

	private ArrayList<PieceGui> pieces;
	private ChessGui chessGui;
	private int xOffsetDrag;
	private int yOffsetDrag;

	public PieceDragDropListener(ArrayList<PieceGui> pieces, ChessGui chessGui) {
		this.pieces = pieces;
		this.chessGui = chessGui;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if (!this.chessGui.isDraggingGamePiecesEnabled()) {
			return;
		}
		
		int x = e.getX();
		int y = e.getY();

		for (int i = this.pieces.size() - 1; i >= 0; i--) {
			PieceGui piece = this.pieces.get(i);
			if (piece.getPiece().isCaptured())
				continue;

			if (mouseOverPiece(piece, x, y)) {

				// Check the game state first before you let drag

				if ((this.chessGui.getChessGame().getGameState() == ChessGame.GAMESTATE.WHITE_STATE
						&& piece.getPiece().getColor() == Piece.COLORS.WHITE)
						|| (this.chessGui.getChessGame().getGameState() == ChessGame.GAMESTATE.BLACK_STATE
								&& piece.getPiece().getColor() == Piece.COLORS.BLACK)) {

					this.xOffsetDrag = x - piece.getX();
					this.yOffsetDrag = y - piece.getY();
					this.chessGui.setDragPiece(piece);
					this.chessGui.repaint();
					break;
				}
			}
		}
		if (this.chessGui.getDragPiece() != null) {
			this.pieces.remove(this.chessGui.getDragPiece());
			this.pieces.add(this.chessGui.getDragPiece());
		}
	}

	private boolean mouseOverPiece(PieceGui piece, int x, int y) {
		return piece.getX() <= x && piece.getX() + piece.getWidth() >= x && piece.getY() <= y
				&& piece.getY() + piece.getHeight() >= y;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.chessGui.getDragPiece() != null){
			int x = e.getPoint().x - this.xOffsetDrag;
			int y = e.getPoint().y - this.yOffsetDrag;
			this.chessGui.getDragPiece().setX(x);
			this.chessGui.getDragPiece().setY(y);
			this.chessGui.repaint();
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

	// prsoxgfbxffxb

	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.chessGui.getDragPiece() != null){
			int x = e.getPoint().x - this.xOffsetDrag;
			int y = e.getPoint().y - this.yOffsetDrag;
			
			this.chessGui.setNewPieceLocation(this.chessGui.getDragPiece(), x, y);
			this.chessGui.repaint();
			this.chessGui.setDragPiece(null);
		}
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
