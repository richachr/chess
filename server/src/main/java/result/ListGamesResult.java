package result;

import com.google.gson.annotations.Expose;
import model.GameData;

import java.util.Collection;

public record ListGamesResult(@Expose GameData[] games) {
}
