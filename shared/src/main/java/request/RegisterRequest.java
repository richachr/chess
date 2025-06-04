package request;

import com.google.gson.annotations.Expose;

public record RegisterRequest(@Expose String username, @Expose String password, @Expose String email) implements Request {
    @Override
    public String getAuthToken() {
        return "";
    }
}
