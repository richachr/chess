package request;

import com.google.gson.annotations.Expose;

public record LogoutRequest(@Expose String authToken) implements Request {
    @Override
    public String getAuthToken() {
        return authToken;
    }
}
