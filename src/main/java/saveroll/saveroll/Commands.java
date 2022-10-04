package saveroll.saveroll;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandAlias("rollplus")
public class Commands extends BaseCommand {
    @Subcommand("set")
    @Syntax("<число> <attack/defend/escape> <ник_игрока>")
    @CommandPermission("rollplus.set")
    @CommandCompletion("[0-9]+ @rollList @players")
    public void onCommand(Player player, int roll, String nameRoll, OfflinePlayer otherPlayer) {
        try {
            SaveRoll.getInstance().getConfigManager().getSqlManager().addUser(player.getName());
            SaveRoll.getInstance().getConfigManager().getSqlManager().setRoll(otherPlayer.getName(), nameRoll, roll);
            player.sendMessage("Ролл успешно установлен!");
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Произошла ошибка! Загляните в консоль.");
        }
    }

    @Subcommand("reload")
    @CommandPermission("rollplus.reload")
    public void onCommand(CommandSender commandSender) {
        SaveRoll.getInstance().reloadPlugin();
        commandSender.sendMessage("Config has been reloaded");
    }
}
