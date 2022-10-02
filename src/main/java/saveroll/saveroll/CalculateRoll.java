package saveroll.saveroll;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CalculateRoll extends PlaceholderExpansion {

    private ConfigManager configManager;

    public CalculateRoll(ConfigManager configManager) {
        this.configManager = configManager;
    }

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
        int[] plusRolls = configManager.getSqlManager().getRollFromUser(player.getName());
        if(params.equalsIgnoreCase("attack"))
            return String.valueOf(plusRolls[0] + configManager.attackRoll.calculateRoll(player));
        else if(params.equalsIgnoreCase("defend"))
            return String.valueOf(plusRolls[1] + configManager.defendRoll.calculateRoll(player));
        else if (params.equalsIgnoreCase("escape"))
            String.valueOf(plusRolls[2] + configManager.escapeRoll.calculateRoll(player));
        return params;
    }
}