package request;

import com.google.gson.annotations.Expose;

public record CreateGameRequest(@Expose String gameName, @Expose String authToken) implements Request {
    @Override
    public String getAuthToken() {
        return authToken;
    }
}
