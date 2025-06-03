package request;

import com.google.gson.annotations.Expose;

public record ListGamesRequest(@Expose String authToken) {
}
