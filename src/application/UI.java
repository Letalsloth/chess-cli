package application;

import chess.ChessPiece;

public class UI {

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
            System.out.print(piece);
        }
        System.out.print(" ");
    }
}
