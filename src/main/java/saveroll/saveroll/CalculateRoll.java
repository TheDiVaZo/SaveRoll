package saveroll.saveroll;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CalculateRoll extends PlaceholderExpansion {


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
        if(Objects.isNull(player)) return params;
        int[] plusRolls = SaveRoll.getInstance().getConfigManager().getSqlManager().getRollFromUser(player.getName());
        if(params.equalsIgnoreCase("attack")) {
            int rollFinal = plusRolls[0] + SaveRoll.getInstance().getConfigManager().attackRoll.calculateRoll(player);
            String sign = rollFinal >= 0 ? "+":"";
            return sign + rollFinal;
        }
        else if(params.equalsIgnoreCase("defend")) {
            int rollFinal = plusRolls[1] + SaveRoll.getInstance().getConfigManager().defendRoll.calculateRoll(player);
            String sign = rollFinal >= 0 ? "+":"";
            return sign + rollFinal;
        }
        else if (params.equalsIgnoreCase("escape")) {
            int rollFinal = plusRolls[2] + SaveRoll.getInstance().getConfigManager().escapeRoll.calculateRoll(player);
            String sign = rollFinal >= 0 ? "+":"";
            return sign + rollFinal;
        }
        return params;
    }
}