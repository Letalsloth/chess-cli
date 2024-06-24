package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] grid = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position pos = new Position(0, 0);

        // up
        pos.setValues(position.getRow() - 1, position.getColumn());
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setRow(pos.getRow() - 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // left
        pos.setValues(position.getRow(), position.getColumn() - 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setColumn(pos.getColumn() - 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // right
        pos.setValues(position.getRow(), position.getColumn() + 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setColumn(pos.getColumn() + 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // down
        pos.setValues(position.getRow() + 1, position.getColumn());
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setRow(pos.getRow() + 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // nw
        pos.setValues(position.getRow() - 1, position.getColumn() - 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setValues(pos.getRow() - 1, pos.getColumn() - 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // ne
        pos.setValues(position.getRow() - 1, position.getColumn() + 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setValues(pos.getRow() - 1, pos.getColumn() + 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // sw
        pos.setValues(position.getRow() + 1, position.getColumn() - 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setValues(pos.getRow() + 1, pos.getColumn() - 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        // se
        pos.setValues(position.getRow() + 1, position.getColumn() + 1);
        while (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
            pos.setValues(pos.getRow() + 1, pos.getColumn() + 1);
        }
        if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
            grid[pos.getRow()][pos.getColumn()] = true;
        }

        return grid;
    }
}
