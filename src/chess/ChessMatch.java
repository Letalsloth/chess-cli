package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private final Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private final List<Piece> piecesOnBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        check = false;
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

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
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

        if (isKingInCheck(currentPlayer)) {
            undoMove(current, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            boolean whiteCondition = movedPiece.getColor() == Color.WHITE && target.getRow() == 0;
            boolean blackCondition = movedPiece.getColor() == Color.BLACK && target.getRow() == 7;

            if (whiteCondition || blackCondition) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = isKingInCheck(opponent(currentPlayer));

        if (isCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        //en passant
        boolean whitePawnMoved = target.getRow() == current.getRow() - 2;
        boolean blackPawnMoved = target.getRow() == current.getRow() + 2;

        if (movedPiece instanceof Pawn && (whitePawnMoved || blackPawnMoved)) {
            enPassantVulnerable = movedPiece;
        }
        else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to promote");
        }
        boolean wrongType = type.equalsIgnoreCase("q") &&
                type.equalsIgnoreCase("n") &&
                type.equalsIgnoreCase("r") &&
                type.equalsIgnoreCase("b");
        if (wrongType) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece replaced = board.removePiece(pos);
        piecesOnBoard.remove(replaced);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equalsIgnoreCase("q")) return new Queen(board, color);
        if (type.equalsIgnoreCase("n")) return new Knight(board, color);
        if (type.equalsIgnoreCase("r")) return new Rook(board, color);
        return new Bishop(board, color);
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
        ChessPiece pieceToMove = (ChessPiece) board.removePiece(currentPosition);
        pieceToMove.increaseMoveCount();
        Piece capturedPiece = board.removePiece(targetPosition);
        board.placePiece(pieceToMove, targetPosition);

        if (capturedPiece != null) {
            piecesOnBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // castling king side rook
        if (pieceToMove instanceof King && targetPosition.getColumn() ==
                currentPosition.getColumn() + 2) {
            Position currentTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() + 3);
            Position targetTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(currentTowerPosition);
            board.placePiece(rook, targetTowerPosition);
            rook.increaseMoveCount();
        }

        // castling queen side rook
        if (pieceToMove instanceof King && targetPosition.getColumn() ==
                currentPosition.getColumn() - 2) {
            Position currentTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() - 4);
            Position targetTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(currentTowerPosition);
            board.placePiece(rook, targetTowerPosition);
            rook.increaseMoveCount();
        }

        // en passant
        if (pieceToMove instanceof Pawn) {
            boolean enPassantMovement = currentPosition.getColumn() != targetPosition.getColumn() && capturedPiece == null;
            if (enPassantMovement) {
                Position pawnPosition;
                if (pieceToMove.getColor() == Color.WHITE) {
                    pawnPosition = new Position(targetPosition.getRow() + 1, targetPosition.getColumn());
                }
                else {
                    pawnPosition = new Position(targetPosition.getRow() - 1, targetPosition.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position currentPosition, Position targetPosition, Piece capturedPiece) {
        ChessPiece pieceToMove = (ChessPiece) board.removePiece(targetPosition);
        pieceToMove.decreaseMoveCount();
        board.placePiece(pieceToMove, currentPosition);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, targetPosition);
            capturedPieces.remove(capturedPiece);
            piecesOnBoard.add(capturedPiece);
        }

        // reverse king side castling
        if (pieceToMove instanceof King && targetPosition.getColumn() ==
                currentPosition.getColumn() + 2) {
            Position currentTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() + 3);
            Position targetTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getRow() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetTowerPosition);
            board.placePiece(rook, currentTowerPosition);
            rook.decreaseMoveCount();
        }

        // reverse queen side castling
        if (pieceToMove instanceof King && targetPosition.getColumn() ==
                currentPosition.getColumn() - 2) {
            Position currentTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getColumn() - 4);
            Position targetTowerPosition = new Position(currentPosition.getRow(),
                    currentPosition.getRow() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetTowerPosition);
            board.placePiece(rook, currentTowerPosition);
            rook.decreaseMoveCount();
        }

        // en passant
        if (pieceToMove instanceof Pawn) {
            boolean enPassantMovement = currentPosition.getColumn() != targetPosition.getColumn() &&
                    capturedPiece == enPassantVulnerable;
            ChessPiece pawn = (ChessPiece) board.removePiece(targetPosition);

            if (enPassantMovement) {
                Position pawnPosition;
                if (pieceToMove.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, targetPosition.getColumn());
                } else {
                    pawnPosition = new Position(4, targetPosition.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece findKing(Color color) {
        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor()
                == color).toList();
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on chessboard");
    }

    private boolean isKingInCheck(Color color) {
        Position kingPosition = findKing(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor()
                == opponent(color)).toList();

        for (Piece o : opponentPieces) {
            boolean[][] grid = o.possibleMoves();
            if (grid[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean isCheckMate(Color color) {
        if (!isKingInCheck(color)) {
            return false;
        }

        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor()
                == color).toList();
        for (Piece p : list) {
            boolean[][] grid = p.possibleMoves();
            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getColumns(); col++) {
                    if (grid[row][col]) {
                        //Simulating the piece movement to see if the king check is removed
                        Position current = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(row, col);
                        Piece capturedPiece = movePiece(current, target);
                        boolean isCheck = isKingInCheck(color);
                        undoMove(current, target, capturedPiece);

                        if (!isCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void initialSetup() {
        for (int i = 0; i < 8; i++) {
            placeNewPiece((char) (i + 'a'), 2, new Pawn(board, Color.WHITE, this));
        }
        for (int i = 0; i < 8; i++) {
            placeNewPiece((char) (i + 'a'), 7, new Pawn(board, Color.BLACK, this));
        }

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));

        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));

        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));

        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));

        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
    }
}
