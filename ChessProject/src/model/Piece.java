package model;

public class Piece {

	public static enum COLORS {
		WHITE, BLACK
	};

	public static enum TYPE {
		KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
	};

	public static final int ROW_1 = 0;
	public static final int ROW_2 = 1;
	public static final int ROW_3 = 2;
	public static final int ROW_4 = 3;
	public static final int ROW_5 = 4;
	public static final int ROW_6 = 5;
	public static final int ROW_7 = 6;
	public static final int ROW_8 = 7;

	public static final int COLUMN_A = 0;
	public static final int COLUMN_B = 1;
	public static final int COLUMN_C = 2;
	public static final int COLUMN_D = 3;
	public static final int COLUMN_E = 4;
	public static final int COLUMN_F = 5;
	public static final int COLUMN_G = 6;
	public static final int COLUMN_H = 7;
	
	private COLORS color;
	private TYPE type;

	private int column;
	private int row;
	private boolean isCaptured = false;

	public Piece(COLORS color, TYPE type, int column, int row) {

		this.color = color;
		this.type = type;
		this.column = column;
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
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

	public boolean isCaptured() {
		return isCaptured;
	}
	
	public void isCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	@Override
	public String toString() {
		String strColor = (this.color == Piece.COLORS.WHITE ? "white" : "black");
		String strType = "unknown";
		switch (this.type) {
		case BISHOP:
			strType = "B";
			break;
		case KING:
			strType = "K";
			break;
		case KNIGHT:
			strType = "N";
			break;
		case PAWN:
			strType = "P";
			break;
		case QUEEN:
			strType = "Q";
			break;
		case ROOK:
			strType = "R";
			break;
		}
		
		String strRow = getRowString(this.row);
		String strColumn = getColumnString(this.column);
		
		 return strColor+" "+strType+" "+strRow+"/"+strColumn;
	}
	
	public static String getRowString(int row){
		String strRow = "unknown";
		switch (row) {
			case ROW_1: strRow = "1";break;
			case ROW_2: strRow = "2";break;
			case ROW_3: strRow = "3";break;
			case ROW_4: strRow = "4";break;
			case ROW_5: strRow = "5";break;
			case ROW_6: strRow = "6";break;
			case ROW_7: strRow = "7";break;
			case ROW_8: strRow = "8";break;
		}
		return strRow;
	}
	
	public static String getColumnString(int column){
		String strColumn = "unknown";
		switch (column) {
			case COLUMN_A: strColumn = "A";break;
			case COLUMN_B: strColumn = "B";break;
			case COLUMN_C: strColumn = "C";break;
			case COLUMN_D: strColumn = "D";break;
			case COLUMN_E: strColumn = "E";break;
			case COLUMN_F: strColumn = "F";break;
			case COLUMN_G: strColumn = "G";break;
			case COLUMN_H: strColumn = "H";break;
		}
		return strColumn;
	}
}
