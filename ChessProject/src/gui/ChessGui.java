package gui;

import gamelogic.IPlayerHandler;
import gamelogic.Move;
import gamelogic.MoveValidator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import listeners.ChangeGameStateButtonActionListener;
import listeners.PieceDragDropListener;
import main.ChessGame;
import model.Piece;
import model.Piece.COLORS;
import model.Piece.TYPE;

public class ChessGui extends JPanel implements IPlayerHandler{

	private static final int START_X_BOARD = 299;
	private static final int START_Y_BOARD = 49;

	private static final int SQUARE_WIDTH = 50;
	private static final int SQUARE_HEIGHT = 50;

	private static final int PIECE_WIDTH = 48;
	private static final int PIECE_HEIGHT = 48;

	private static final int PIECES_START_X = START_X_BOARD + (int) (SQUARE_WIDTH / 2.0 - PIECE_WIDTH / 2.0);
	private static final int PIECES_START_Y = START_Y_BOARD + (int) (SQUARE_HEIGHT / 2.0 - PIECE_HEIGHT / 2.0);

	private static final int DRAG_TARGET_SQUARE_START_X = START_X_BOARD - (int) (PIECE_WIDTH / 2.0);
	private static final int DRAG_TARGET_SQUARE_START_Y = START_Y_BOARD - (int) (PIECE_HEIGHT / 2.0);

	private Image background;
	private JLabel lblGameState;

	private ChessGame chessGame;
	private ArrayList<PieceGui> guiPieces = new ArrayList<PieceGui>();

	private Move lastMove;
	private Move currentMove;
	
	private PieceGui dragPiece;
	private boolean draggingGamePiecesEnabled;
	
	public ChessGui() {

		this.setLayout(null);
		this.chessGame = new ChessGame();

		for (Piece piece : this.chessGame.getPieces()) {
			createGuiPiece(piece);
		}
		URL urlBackground = getClass().getResource("/images/board.png");
		this.background = new ImageIcon(urlBackground).getImage();

		// Adding listeners for drag and drop
		PieceDragDropListener listener = new PieceDragDropListener(this.guiPieces, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		// Button to change states
		JButton btnChangeGameState = new JButton("Change State");
		btnChangeGameState.addActionListener(new ChangeGameStateButtonActionListener(this));
		btnChangeGameState.setBounds(0, 0, 150, 30);
		this.add(btnChangeGameState);

		// Label for displaying current game state
		String labelText = this.chessGame.getGameStateAsText();
		this.lblGameState = new JLabel(labelText);
		lblGameState.setBounds(0, 30, 80, 30);
		lblGameState.setForeground(Color.WHITE);
		this.add(lblGameState);

		// Setting main frame of the game
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(this.background.getWidth(null), this.background.getHeight(null));
		frame.setLocationRelativeTo(null);

	}

	private void createGuiPiece(Piece piece) {
		Image image = this.getPieceImage(piece.getColor(), piece.getType());
		PieceGui pieceGui = new PieceGui(image, piece);
		this.guiPieces.add(pieceGui);
	}

	public PieceGui getDragPiece() {
		return dragPiece;
	}

	public void changeGameState() {
		this.chessGame.changeGameState();
		
	}

	public ChessGame getChessGame() {
		return chessGame;
	}

	public boolean isDraggingGamePiecesEnabled() {
		return draggingGamePiecesEnabled;
	}

	public void setDraggingGamePiecesEnabled(boolean draggingGamePiecesEnabled) {
		this.draggingGamePiecesEnabled = draggingGamePiecesEnabled;
	}

	private Image getPieceImage(COLORS color, TYPE type) {
		String name = "";
		if (color == COLORS.WHITE) {
			name += "w";
		} else {
			name += "b";
		}

		switch (type) {
		case KING:
			name += "k";
			break;
		case QUEEN:
			name += "q";
			break;
		case PAWN:
			name += "p";
			break;
		case ROOK:
			name += "r";
			break;
		case BISHOP:
			name += "b";
			break;
		case KNIGHT:
			name += "n";
			break;
		}

		name += ".png";
		URL urlPieceImage = getClass().getResource("/images/" + name);
		return new ImageIcon(urlPieceImage).getImage();
	}

	public void setNewPieceLocation(PieceGui dragPiece, int x, int y) {
		int row = convertYToRow(y);
		int column = convertXToColumn(x);

//		if (row < Piece.ROW_1 || row > Piece.ROW_8 || column < Piece.COLUMN_A || column > Piece.COLUMN_H) {
//			dragPiece.resetToUnderlyingPosition();
//		} else {
//			System.out.println("moving piece to " + row + "/" + column);
			Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), row, column);
			//boolean wasMoveSuccessfull = this.chessGame.movePiece(move);
			if(this.chessGame.getMoveValidator().isMoveValid(move)){
				this.currentMove = move;
			}else{
				dragPiece.resetToUnderlyingPosition();
			}
//			if (wasMoveSuccessfull) {
//				this.lastMove = move;
//			}

//			dragPiece.resetToUnderlyingPosition();
		//}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.background, 0, 0, null);
		for (PieceGui piece : this.guiPieces) {
			if (!piece.getPiece().isCaptured()) {
				g.drawImage(piece.getImage(), piece.getX(), piece.getY(), null);
			}
		}

		if (!isUserDraggingPiece() && this.lastMove != null) {
			int highlightSourceX = convertColumnToX(this.lastMove.sourceColumn);
			int highlightSourceY = convertRowToY(this.lastMove.sourceRow);
			int highlightTargetX = convertColumnToX(this.lastMove.targetColumn);
			int highlightTargetY = convertRowToY(this.lastMove.targetRow);

			g.setColor(Color.YELLOW);
			g.drawRoundRect(highlightSourceX + 4, highlightSourceY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
			g.drawRoundRect(highlightTargetX + 4, highlightTargetY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
		}

		if (isUserDraggingPiece()) {
			MoveValidator moveValidator = this.chessGame.getMoveValidator();

			// iteriraj kroz celu tablu i proveri koja su polja validni koraci
			for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++) {
				for (int row = Piece.ROW_1; row <= Piece.ROW_8; row++) {
					int sourceRow = this.dragPiece.getPiece().getRow();
					int sourceColumn = this.dragPiece.getPiece().getColumn();

					if (moveValidator.isMoveValid(new Move(sourceRow, sourceColumn, row, column))) {

						int highlightX = convertColumnToX(column);
						int highlightY = convertRowToY(row);

						g.setColor(Color.BLACK);
						g.drawRoundRect(highlightX + 5, highlightY + 5, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);

						g.setColor(Color.GREEN);
						g.drawRoundRect(highlightX + 4, highlightY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
					}
				}
			}
		}
		
		this.lblGameState.setText(this.chessGame.getGameStateAsText());

	}

	private boolean isUserDraggingPiece() {
		return this.dragPiece != null;
	}

	public void setDragPiece(PieceGui guiPiece) {
		this.dragPiece = guiPiece;
	}

	public static int convertColumnToX(int column) {
		return PIECES_START_X + SQUARE_WIDTH * column;
	}

	public static int convertRowToY(int row) {
		return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
	}

	public static int convertXToColumn(int x) {
		return (x - DRAG_TARGET_SQUARE_START_X) / SQUARE_WIDTH;
	}

	public static int convertYToRow(int y) {
		return Piece.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
	}

	@Override
	public Move getMove() {
		this.draggingGamePiecesEnabled = true; 
		Move moveForExecution = this.currentMove;
		this.currentMove = null;
		return moveForExecution;
	}

	@Override
	public void moveSuccessfullyExecuted(Move move) {
		PieceGui guiPiece = this.getGuiPieceAt(move.targetRow, move.targetColumn);
		if( guiPiece == null){
			throw new IllegalStateException("no guiPiece at "+move.targetRow+"/"+move.targetColumn);
		}
		guiPiece.resetToUnderlyingPosition();

		this.lastMove = move;
		this.draggingGamePiecesEnabled = false;
		this.repaint();
		
	}
	
	private PieceGui getGuiPieceAt(int row, int column) {
		for (PieceGui guiPiece : this.guiPieces) {
			if( guiPiece.getPiece().getRow() == row
					&& guiPiece.getPiece().getColumn() == column
					&& guiPiece.getPiece().isCaptured() == false){
				return guiPiece;
			}
		}
		return null;
	}

}
