package result;

import com.google.gson.annotations.Expose;
import model.GameData;

public record ListGamesResult(@Expose GameData[] games) {
}
