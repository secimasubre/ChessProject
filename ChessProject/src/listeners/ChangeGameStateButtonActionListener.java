package listeners;

import gui.ChessGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Main;

public class ChangeGameStateButtonActionListener implements ActionListener {

	public ChangeGameStateButtonActionListener(ChessGui chessGui) {
		Main.chess = chessGui;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Main.chess.changeGameState();
	}
}
