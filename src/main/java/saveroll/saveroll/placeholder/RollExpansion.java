package saveroll.saveroll.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.roll.RollManager;

import java.util.HashMap;
import java.util.Map;

public class RollExpansion extends PlaceholderExpansion {

    public static final String ROLL_PLACEHOLDER = "%roll%";

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
        int roll = rollManager.calculateRoll(params, player);
        String text = rollManager.getPlaceholderText(params);
        if(roll == 0) return "";
        else return text.replace(ROLL_PLACEHOLDER, String.valueOf(roll + SaveRoll.getInstance().getDateBaseManager().getRollForPlayer(player.getUniqueId(),params)));
    }
}
