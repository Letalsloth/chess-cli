package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private final ChessMatch match;

    public Pawn(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
    }

    public ChessMatch getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] grid = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position pos = new Position(0, 0);

        if (getColor() == Color.WHITE) {
            pos.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            pos.setValues(position.getRow() - 2, position.getColumn());
            Position pos2 = new Position(position.getRow() - 1, position.getColumn());
            boolean pos2Valid = getBoard().positionExists(pos2) && !getBoard().pieceInPosition(pos2);
            if (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos) &&
            getMoveCount() == 0 && pos2Valid) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            //Capturing pieces
            pos.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            pos.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            //white en passant
            if (position.getRow() == 3) {
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                boolean checkRightPositionAndPiece = getBoard().positionExists(right) && isThereOpponentPiece(right);
                boolean rightPieceIsVulnerable = getBoard().piece(right) == match.getEnPassantVulnerable();

                Position left = new Position(position.getRow(), position.getColumn() - 1);
                boolean checkLeftPositionAndPiece = getBoard().positionExists(left) && isThereOpponentPiece(left);
                boolean leftPieceIsVulnerable = getBoard().piece(left) == match.getEnPassantVulnerable();

                if (checkRightPositionAndPiece && rightPieceIsVulnerable) {
                    grid[right.getRow() - 1][right.getColumn()] = true;
                }

                if (checkLeftPositionAndPiece && leftPieceIsVulnerable) {
                    grid[left.getRow() - 1][left.getColumn()] = true;
                }
            }
        }
        else {
            pos.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            pos.setValues(position.getRow() + 2, position.getColumn());
            Position pos2 = new Position(position.getRow() + 1, position.getColumn());
            boolean pos2Valid = getBoard().positionExists(pos2) && !getBoard().pieceInPosition(pos2);
            if (getBoard().positionExists(pos) && !getBoard().pieceInPosition(pos) &&
                    getMoveCount() == 0 && pos2Valid) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            //Capturing pieces
            pos.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            pos.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
                grid[pos.getRow()][pos.getColumn()] = true;
            }

            //black en passant
            if (position.getRow() == 4) {
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                boolean checkRightPositionAndPiece = getBoard().positionExists(right) && isThereOpponentPiece(right);
                boolean rightPieceIsVulnerable = getBoard().piece(right) == match.getEnPassantVulnerable();

                Position left = new Position(position.getRow(), position.getColumn() - 1);
                boolean checkLeftPositionAndPiece = getBoard().positionExists(left) && isThereOpponentPiece(left);
                boolean leftPieceIsVulnerable = getBoard().piece(left) == match.getEnPassantVulnerable();

                if (checkRightPositionAndPiece && rightPieceIsVulnerable) {
                    grid[right.getRow() + 1][right.getColumn()] = true;
                }

                if (checkLeftPositionAndPiece && leftPieceIsVulnerable) {
                    grid[left.getRow() + 1][left.getColumn()] = true;
                }
            }
        }

        return grid;
    }
}
