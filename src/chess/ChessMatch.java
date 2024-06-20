package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private final Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public Board getBoard() {
        return  board;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Piece> getPiecesOnBoard() {
        return piecesOnBoard;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
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

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnBoard.add(piece);
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateCurrentPosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece makeMove(ChessPosition currentPosition, ChessPosition targetPosition) {
        Position current = currentPosition.toPosition();
        Position target = targetPosition.toPosition();
        validateCurrentPosition(current);
        validateTargetPosition(current, target);
        Piece capturedPiece = movePiece(current, target);
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    private void validateCurrentPosition(Position position) {
        if (!board.pieceInPosition(position)) {
            throw new ChessException("No piece in currentPosition");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).anyPossibleMove()) {
            throw new ChessException("There are no possible moves for this piece");
        }
    }

    private void validateTargetPosition(Position currentPosition, Position targetPosition) {
        if (!board.piece(currentPosition).possibleMove(targetPosition)) {
            throw new ChessException("This piece cannot move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece movePiece(Position currentPosition, Position targetPosition) {
        Piece pieceToMove = board.removePiece(currentPosition);
        Piece capturedPiece = board.removePiece(targetPosition);
        board.placePiece(pieceToMove, targetPosition);

        if (capturedPiece != null) {
            piecesOnBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void initialSetup() {
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));

        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
    }
}
