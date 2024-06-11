package chess;

import boardgame.Board;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
    }

    public Board getBoard() {
        return  board;
    }

    // Down casting the Piece 2d array to chessPiece 2d array
    public ChessPiece[][] getPieces() {
        ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col ++) {
                chessPieces[row][col] = (ChessPiece) board.piece(row, col);
            }
        }

        return chessPieces;
    }
}
