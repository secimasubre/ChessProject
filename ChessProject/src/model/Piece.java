package model;

import gui.ChessGui;
import gui.ChessGui.COLORS;
import gui.ChessGui.TYPE;

import java.awt.Image;

public class Piece {

	
	private Image image;
	private int x;
	private int y;
	private COLORS color;
	private TYPE type;
	
	public Piece(Image img, int x, int y, COLORS color, TYPE type){
		this.image = img;
		this.x = x;
		this.y = y;
		this.color = color;
		this.type = type;
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
	
	public COLORS getColor() {
		return color;
	}

	public void setColor(COLORS color) {
		this.color = color;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public int getWidth(){
		return image.getHeight(null);
	}
	
	public int getHeight(){
		return image.getHeight(null);
	}
	
	@Override
	public String toString() {
		String strColor = (this.color==ChessGui.COLORS.WHITE?"white":"black");
		String strType = "unknown";
		switch (this.type) {
			case BISHOP: strType = "B";break;
			case KING: strType = "K";break;
			case KNIGHT: strType = "N";break;
			case PAWN: strType = "P";break;
			case QUEEN: strType = "Q";break;
			case ROOK: strType = "R";break;
		}
		return strColor+" "+strType+" "+x+"/"+y;
	}
}
