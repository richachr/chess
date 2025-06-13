package websocket;

import chess.ChessPosition;

public class CoordinateHandler {
    public static ChessPosition getPosition(String coordinate) throws Exception {
        char[] coordinateArray = coordinate.toCharArray();
        int row = 0;
        int col = 0;
        if(coordinateArray.length != 2) {
            throw new Exception("Incorrect coordinate length. Please try again.");
        }
        for(char coord : coordinateArray) {
            int coordInt = coord;
            if(coordInt >= 49 && coordInt <= 56) {
                row = coordInt - 48;
            } else if (coordInt >= 65 && coordInt <= 72) {
                col = coordInt - 64;
            } else if (coordInt >= 97 && coordInt <= 104) {
                col = coordInt - 96;
            } else {
                throw new Exception("Incorrect coordinate entered. Please try again.");
            }
        }
        assert(row != 0 && col != 0);
        return new ChessPosition(row, col);
    }

    public static String getCoordinate(ChessPosition position) {
        StringBuilder builder = new StringBuilder();
        builder.append((char) (position.getColumn() + 96));
        builder.append(position.getRow());
        return builder.toString();
    }
}
