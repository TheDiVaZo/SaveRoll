package saveroll.saveroll.command;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import org.bukkit.entity.Player;
import saveroll.logging.Logger;
import saveroll.saveroll.SaveRoll;

@CommandAlias("adice|admindice")
public class AdminCommand extends BaseCommand {

    @Subcommand("reload")
    @Description("Перезагрузка конфига плагина")
    @CommandPermission("rollplus.reload")
    public static void onReload(Player player) {
        SaveRoll.getInstance().reloadPlugin();
        Logger.info("Команда введена.");
    }

    @Subcommand("set")
    @CommandPermission("rollplus.set")
    @CommandCompletion("@rollList <number> @players")
    public static void setRoll(Player player, String rollName, int roll, Player otherPlayer) {
        SaveRoll.getInstance().getDateBaseManager().setRollForPlayer(otherPlayer.getUniqueId(), rollName, String.valueOf(roll));
        player.sendMessage("&eРолл &c"+rollName+"&e успешно установлен игроку "+otherPlayer.getName());
    }
}
