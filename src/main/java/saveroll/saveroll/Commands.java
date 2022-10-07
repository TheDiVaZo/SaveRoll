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

    }

    @Subcommand("reload")
    @CommandPermission("rollplus.reload")
    public void onCommand(CommandSender commandSender) {

    }
}
