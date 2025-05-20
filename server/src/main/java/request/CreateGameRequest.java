package request;

import com.google.gson.annotations.Expose;

public record CreateGameRequest(@Expose String gameName, @Expose String authToken) {
}
