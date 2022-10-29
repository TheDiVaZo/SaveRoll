package saveroll.saveroll.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import saveroll.logging.Logger;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.chat.ChatManager;

@CommandAlias("dice")
public class UserCommand extends BaseCommand {

    @Default
    @CommandCompletion("@rollList")
    @Description("Кидать кубик")
    public void onDice(Player player, String rollName) {
        SaveRoll saveRoll = SaveRoll.getInstance();
        Logger.info(saveRoll.getRollManager().calculateRoll(rollName, player));
        int roll = saveRoll.getRollManager().calculateRoll(rollName, player) + saveRoll.getDateBaseManager().getRollForPlayer(player.getUniqueId(), rollName);
        ChatManager.sendChatMessage(player, saveRoll.getRollManager().getPlaceholderText(rollName).replace("%roll%", String.valueOf(roll)), 15);
        Logger.info("Команда выполнена.");
    }
}
