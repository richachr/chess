package result;

import com.google.gson.annotations.Expose;

public record RegisterResult(@Expose String username, @Expose String authToken) {
}
