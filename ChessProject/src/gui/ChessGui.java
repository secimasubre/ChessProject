package gui;

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
import model.Piece;

public class ChessGui extends JPanel{

	public static enum COLORS {WHITE, BLACK};
	public static enum TYPE {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN};
	public static enum GAMESTATE {WHITE_STATE, BLACK_STATE};
	
	private int boardXstart = 301;
	private int boardYstart = 51;
	private int boardXoffset = 50;
	private int boardYoffset = 50;
	
	private Image background;
	private JLabel lblGameState;
	
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private GAMESTATE gameState = GAMESTATE.WHITE_STATE;
	
	public ChessGui(){
		
		this.setLayout(null);
		
		URL urlBackground = getClass().getResource("/images/board.png");
		this.background = new ImageIcon(urlBackground).getImage();
		
		
		createPiece(COLORS.WHITE, TYPE.ROOK, boardXstart + boardXoffset * 0, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.KNIGHT, boardXstart + boardXoffset * 1, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.BISHOP, boardXstart + boardXoffset * 2, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.KING, boardXstart + boardXoffset * 3, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.QUEEN, boardXstart + boardXoffset * 4, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.BISHOP, boardXstart + boardXoffset * 5, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.KNIGHT, boardXstart + boardXoffset * 6, 
				boardYstart + boardYoffset * 7);
		createPiece(COLORS.WHITE, TYPE.ROOK, boardXstart + boardXoffset * 7, 
				boardYstart + boardYoffset * 7);
		
		for(int i = 0;i<8;i++){
			createPiece(COLORS.WHITE, TYPE.PAWN, boardXstart + boardXoffset * i, 
				boardYstart + boardYoffset * 6);
		}
		
		createPiece(COLORS.BLACK, TYPE.ROOK, boardXstart + boardXoffset * 0, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.KNIGHT, boardXstart + boardXoffset * 1, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.BISHOP, boardXstart + boardXoffset * 2, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.KING, boardXstart + boardXoffset * 3, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.QUEEN, boardXstart + boardXoffset * 4, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.BISHOP, boardXstart + boardXoffset * 5, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.KNIGHT, boardXstart + boardXoffset * 6, 
				boardYstart + boardYoffset * 0);
		createPiece(COLORS.BLACK, TYPE.ROOK, boardXstart + boardXoffset * 7, 
				boardYstart + boardYoffset * 0);
		
		for(int i = 0;i<8;i++){
			createPiece(COLORS.BLACK, TYPE.PAWN, boardXstart + boardXoffset * i, 
				boardYstart + boardYoffset * 1);
		}
		
		
		//Adding listeners for drag and drop
		PieceDragDropListener listener = new PieceDragDropListener(this.pieces);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		//Button to change states
		JButton btnChangeGameState = new JButton("Change State");
		btnChangeGameState
				.addActionListener(new ChangeGameStateButtonActionListener(this));
		btnChangeGameState.setBounds(0, 0, 150, 30);
		this.add(btnChangeGameState);

		//Label for displaying current game state
		String labelText = this.getGameStateAsText();
		this.lblGameState = new JLabel(labelText);
		lblGameState.setBounds(0, 30, 80, 30);
		lblGameState.setForeground(Color.WHITE);
		this.add(lblGameState);
		
		//Setting main frame of the game
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(this.background.getWidth(null), this.background.getHeight(null));
		frame.setLocationRelativeTo(null);
		
	}
	
	//Game states functions
	 private String getGameStateAsText() {
	        return (this.gameState == GAMESTATE.BLACK_STATE ? "black" : "white");
	    }
	 
	 public void changeGameState() {
	        switch (this.gameState) {
	            case BLACK_STATE:
	                this.gameState = GAMESTATE.WHITE_STATE;
	                break;
	            case WHITE_STATE:
	                this.gameState = GAMESTATE.BLACK_STATE;
	                break;
	            default:
	                throw new IllegalStateException("unknown game state: " + this.gameState);
	        }
	        this.lblGameState.setText(this.getGameStateAsText());
	    }
	 	
	public GAMESTATE getGameState() {
		return gameState;
	}

	public void setGameState(GAMESTATE gameState) {
		this.gameState = gameState;
	}
	 
	 /////////

	private Image getPieceImage(COLORS color, TYPE type){
		
	
		String name="";
		
		if(color == COLORS.WHITE){
			name+="w";
		}else{ 
			name+="b";
		}
	
		switch(type){
			case KING:
				name+="k";
				break;
			case QUEEN:
				name+="q";
				break;
			case PAWN:
				name+="p";
				break;
			case ROOK:
				name+="r";
				break;
			case BISHOP:
				name+="b";
				break;
			case KNIGHT:
				name+="n";
				break;
		}
		
		name+=".png";
		URL urlPieceImage = getClass().getResource("/images/" + name);
		return new ImageIcon(urlPieceImage).getImage();		
	}
	
	private void createPiece(COLORS color, TYPE type, int x, int y){
		Image img = this.getPieceImage(color, type);
		Piece piece = new Piece(img, x, y, color, type);
		this.pieces.add(piece);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.background, 0, 0, null);
		for(Piece piece: this.pieces){
			g.drawImage(piece.getImage(), piece.getX(), piece.getY(), null);
		}
	}
	
	
}
