package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ServerMessageTypeAdapter extends TypeAdapter<ServerMessage> {
    @Override
    public void write(JsonWriter jsonWriter, ServerMessage serverMessage) throws IOException {
        Gson gson = new Gson();

        switch(serverMessage.getServerMessageType()) {
            case LOAD_GAME -> gson.getAdapter(LoadGameMessage.class).write(jsonWriter, (LoadGameMessage) serverMessage);
            case ERROR -> gson.getAdapter(ErrorMessage.class).write(jsonWriter, (ErrorMessage) serverMessage);
            case NOTIFICATION -> gson.getAdapter(NotificationMessage.class).write(jsonWriter, (NotificationMessage) serverMessage);
        }
    }

    @Override
    public ServerMessage read(JsonReader jsonReader) throws IOException {
        ServerMessage.ServerMessageType serverMessageType = null;
        String message = null;
        String errorMessage = null;
        ChessGame game = null;

        jsonReader.beginObject();

        while(jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "serverMessageType" -> serverMessageType = ServerMessage.ServerMessageType.valueOf(jsonReader.nextString());
                case "message" -> message = jsonReader.nextString();
                case "errorMessage" -> errorMessage = jsonReader.nextString();
                case "game" -> game = new Gson().fromJson(jsonReader.nextString(), ChessGame.class);
            }
        }

        jsonReader.endObject();

        if(serverMessageType == null) {
            return null;
        } else {
            return switch(serverMessageType) {
                case ERROR -> new ErrorMessage(errorMessage);
                case LOAD_GAME -> new LoadGameMessage(game);
                case NOTIFICATION -> new NotificationMessage(message);
            };
        }
    }
}
