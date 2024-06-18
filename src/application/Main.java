package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ChessMatch match = new ChessMatch();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                UI.clearScreen();
                UI.printBoard(match.getPieces());
                System.out.println();
                System.out.print("Origin: ");
                ChessPosition currentPosition = UI.readChessPosition(scanner);

                boolean[][] possibleMoves = match.possibleMoves(currentPosition);
                UI.clearScreen();
                UI.printBoard(match.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition targetPosition = UI.readChessPosition(scanner);

                ChessPiece capturedPiece = match.makeMove(currentPosition, targetPosition);
            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }


    }
}
