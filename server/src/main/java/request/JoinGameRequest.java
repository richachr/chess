package request;

import com.google.gson.annotations.Expose;

public record JoinGameRequest(@Expose Integer gameID, @Expose String playerColor, @Expose String authToken) {
}
