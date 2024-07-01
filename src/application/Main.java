package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ChessMatch match = new ChessMatch();
        Scanner scanner = new Scanner(System.in);
        List<ChessPiece> capturedPieces = new ArrayList<>();

        while (!match.getCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(match, capturedPieces);
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

                if (capturedPiece != null) {
                    capturedPieces.add(capturedPiece);
                }

                if (match.getPromoted() != null) {
                    System.out.println("Enter piece for promotion (Q/N/R/B): ");
                    String type = scanner.next();

                    boolean wrongType = type.equalsIgnoreCase("q") &&
                            type.equalsIgnoreCase("n") &&
                            type.equalsIgnoreCase("r") &&
                            type.equalsIgnoreCase("b");
                    if (wrongType) {
                        System.out.println("Invalid value! Enter piece for promotion (Q/N/R/B): ");
                        type = scanner.next();
                    }
                    match.replacePromotedPiece(type);
                }
            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(match, capturedPieces);
    }
}
