package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner scanner) {
        try {
            String s = scanner.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));

            return new ChessPosition(column, row);
        }
        catch (RuntimeException e ) {
            throw new InputMismatchException("Error reading ChessPosition. Use values from a1 to h8");
        }
    }

    public static void printMatch(ChessMatch match, List<ChessPiece> capturedPieces) {
        printBoard(match.getPieces());
        System.out.println();
        printCapturedPieces(capturedPieces);
        System.out.println();
        System.out.println("Turn: " + match.getTurn());

        if (!match.getCheckMate()) {
            System.out.println("Waiting player: " + match.getCurrentPlayer());

            if (match.getCheck()) {
                System.out.println("CHECK!");
            }
        }
        else {
            System.out.println("CHECKMATE");
            System.out.println("WINNER: " + match.getCurrentPlayer());
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        System.out.println();
        for (int row = 0; row < pieces.length; row ++) {
            System.out.print((8 - row) + " ");

            for (int col = 0; col < pieces[row].length; col ++) {
                printPiece(pieces[row][col], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        System.out.println();
        for (int row = 0; row < pieces.length; row ++) {
            System.out.print((8 - row) + " ");

            for (int col = 0; col < pieces[row].length; col ++) {
                printPiece(pieces[row][col], possibleMoves[row][col]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_PURPLE_BACKGROUND);
        }

        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            String text_color;
            if (piece.getColor() == Color.WHITE) {
                text_color = ANSI_WHITE;
            }
            else {
                text_color = ANSI_YELLOW;
            }

            System.out.print(text_color + piece + ANSI_RESET);
        }
        System.out.print(" ");
    }

    private static void printCapturedPieces(List<ChessPiece> capturedPieces) {
        List<ChessPiece> whitePieces = capturedPieces.stream().filter(
                x -> x.getColor() == Color.WHITE).toList();
        List<ChessPiece> blackPieces = capturedPieces.stream().filter(
                x -> x.getColor() == Color.BLACK).toList();

        System.out.println("Captured pieces:");

        System.out.print(ANSI_WHITE);
        System.out.print("White: ");
        System.out.println(Arrays.toString(whitePieces.toArray()));
        System.out.print(ANSI_RESET);

        System.out.print(ANSI_YELLOW);
        System.out.print("Black: ");
        System.out.println(Arrays.toString(blackPieces.toArray()));
        System.out.print(ANSI_RESET);
    }
}
