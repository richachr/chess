package result;

import com.google.gson.annotations.Expose;

public record ErrorResult(@Expose String message) {
    public ErrorResult(String message) {
        this.message = "Error: " + message;
    }
}
