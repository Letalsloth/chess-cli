package application;

import chess.ChessPiece;
import chess.Color;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void printBoard(ChessPiece[][] pieces) {
        System.out.println();
        for (int line = 0; line < pieces.length; line ++) {
            System.out.print((8 - line) + " ");

            for (int col = 0; col < pieces[line].length; col ++) {
                printPiece(pieces[line][col]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print("-");
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
}
