package saveroll.saveroll.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.roll.RollManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RollExpansion extends PlaceholderExpansion {

    public static final String NAME_ROLL_REGEX = "([a-zA-Z0-9]+)";

    public static final Pattern COUNT_PLACEHOLDER = Pattern.compile("rollplus_"+NAME_ROLL_REGEX+"_count");
    public static final Pattern COUNT_RANDOM_PLACEHOLDER = Pattern.compile("rollplus_"+NAME_ROLL_REGEX+"_count_random");
    public static final Pattern NAME_PLACEHOLDER = Pattern.compile("rollplus_"+NAME_ROLL_REGEX+"_name");
    public static final Pattern NAME_SYSTEM_PLACEHOLDER = Pattern.compile("rollplus_"+NAME_ROLL_REGEX+"_name_system");

    public RollExpansion(){};

    @Override
    public String getAuthor() {
        return "TheDiVaZo";
    }

    @Override
    public String getIdentifier() {
        return "rollplus";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        RollManager rollManager = SaveRoll.getInstance().getRollManager();
        if(COUNT_PLACEHOLDER.matcher(params).matches()) {
            return String.valueOf(rollManager.calculateRoll(params, player));
        }
        else if(COUNT_RANDOM_PLACEHOLDER.matcher(params).matches()) {
            return String.valueOf(rollManager.calculateRoll(params, player) + Math.round(Math.random() * 12));
        }
        else if(NAME_PLACEHOLDER.matcher(params).matches()) {
            return rollManager.getNameRoll(params);
        }
        else if(NAME_SYSTEM_PLACEHOLDER.matcher(params).matches()) {
            return rollManager.getSystemNameRoll(params);
        }
        else return null;
    }
}
