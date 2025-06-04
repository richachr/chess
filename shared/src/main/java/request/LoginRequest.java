package request;

import com.google.gson.annotations.Expose;

public record LoginRequest(@Expose String username, @Expose String password) implements Request {
    @Override
    public String getAuthToken() {
        return "";
    }
}
