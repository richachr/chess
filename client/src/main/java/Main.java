import chess.*;
import ui.InputLoop;

public class Main {
    public static void main(String[] args) {
        String url = "http://localhost:8080";
        if(args.length > 0) {
            url = args[0];
        }

        new InputLoop().run();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}