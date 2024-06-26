package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private final ChessMatch match;

    public King(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
    }

    public ChessMatch getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece piece = (ChessPiece) getBoard().piece(position);
        return piece == null || piece.getColor() != getColor();
    }

    private boolean testCastling(Position position) {
        ChessPiece piece = (ChessPiece) getBoard().piece(position);

        return piece instanceof Rook && piece.getColor() == getColor()
                && piece.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] grid = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position pos = new Position(0, 0);

        // up
        pos.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // down
        pos.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // left
        pos.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // right
        pos.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // nw
        pos.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // ne
        pos.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // sw
        pos.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // se
        pos.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(pos) && canMove(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        //special moves
        // castling
        if (getMoveCount() == 0 && !match.getCheck()) {
            // king side rook
            Position rook1 = new Position(position.getRow(), position.getColumn() + 3);
            if (testCastling(rook1)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    grid[position.getRow()][position.getColumn() + 2] = true;
                }
            }

            // queen side rook
            Position rook2 = new Position(position.getRow(), position.getColumn() - 4);
            if (testCastling(rook2)) {
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece
                        (p3)  == null) {
                    grid[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }

        return grid;
    }
}
