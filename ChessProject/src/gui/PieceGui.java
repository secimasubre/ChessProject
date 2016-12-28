package gui;

import java.awt.Image;

import model.Piece;

public class PieceGui {

	private Image image;
	private int x;
	private int y;
	private Piece piece;
	
	public PieceGui(Image image, Piece piece){
		this.image = image;
		this.piece = piece;
		
		this.resetToUnderlyingPosition();
	}
	
	
	public void resetToUnderlyingPosition(){
		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
	}


	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public Piece getPiece() {
		return piece;
	}


	public void setPiece(Piece piece) {
		this.piece = piece;
	}


	public int getWidth() {
		// TODO Auto-generated method stub
		return image.getWidth(null);
	}
	public int getHeight(){
		return image.getHeight(null);
	}
	
}
