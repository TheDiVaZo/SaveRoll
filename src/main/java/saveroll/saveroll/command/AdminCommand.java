package saveroll.saveroll.command;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import com.mysql.jdbc.log.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import saveroll.errors.NotExistMaterialException;
import saveroll.errors.NotMatchPatternException;
import saveroll.logging.Logger;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.chat.ChatManager;
import saveroll.saveroll.config.ConfigManager;
import saveroll.saveroll.datebase.DateBaseManager;
import saveroll.saveroll.roll.Roll;
import saveroll.saveroll.roll.RollManager;

import static saveroll.saveroll.chat.ChatManager.sendPrivateMessage;

@CommandAlias("adice|admindice")
public class AdminCommand extends BaseCommand {
    private static SaveRoll saveRoll = SaveRoll.getInstance();
    private static RollManager rollManager = saveRoll.getRollManager();
    private static DateBaseManager dateBaseManager = saveRoll.getDateBaseManager();

    private static String getTextDescFromRoll(Roll roll, Player player) {
        return "&eТекущий ролл:"
                + "\n     &cКому рассчитан ролл: &5" + player.getName()
                + "\n     &cСистм. имя ролла: &5" + roll.getSystemName()
                + "\n     &cИгрвое имя ролла: &5" + roll.getName()
                + "\n     &cПлюс за предметы: &5" + roll.getEquipmentBonus().calculateRoll(player)
                + "\n     &cПлюс за наложенные зелья: &5" + roll.getPotionBonus().calculateRoll(player)
                + "\n     &cПлюс за транспорт: &5" + roll.getRiderBonus().calculateRoll(player)
                + "\n     &cУстановленный плюс к роллу: &5" + dateBaseManager.getRollForPlayer(player.getUniqueId(), roll.getSystemName())
                + "\n     &cОбщий плюс к роллу: &5" + rollManager.calculateRoll(roll,player)
                + "\n     &c/dice "+roll.getSystemName()+" -> : &5" + (rollManager.calculateRoll(roll,player) + Math.round(Math.random() * 11 + 1)) + " из 12ти";
    }

    @Subcommand("reload")
    @Description("Перезагрузка конфига плагина")
    @CommandPermission("saveroll.reload")
    public static void onReload(Player player) {
        try {
            saveRoll.reloadPlugin();
            sendPrivateMessage(player,"&eКонфиг был перезагружен.");
        } catch (NotExistMaterialException | NotMatchPatternException e) {
            sendPrivateMessage(player,"&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Subcommand("set")
    @CommandPermission("saveroll.set")
    @CommandCompletion("@rollList <number> @players")
    public static void setRoll(Player player, String rollName, int roll, OfflinePlayer otherPlayer) {
        try {
            SaveRoll.getInstance().getDateBaseManager().setRollForPlayer(otherPlayer.getUniqueId(), rollName, String.valueOf(roll));
            sendPrivateMessage(player, "&eБонус к роллу &c" + rollName + "&e успешно установлен игроку " + otherPlayer.getName());
            if(otherPlayer.isOnline() && otherPlayer.getPlayer() != null) {
                sendPrivateMessage(otherPlayer.getPlayer(), "&eИгрок &c"+player.getName()+" &eустановил вам бонус к роллу &c" + rollName + "&e. Теперь у вас &c+"+roll+"&e" );
            }
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(player, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Subcommand("getAll")
    @Description("получить все роллы")
    @CommandPermission("saveroll.getall")
    @CommandCompletion("@players")
    public static void getAllRoll(Player player, OfflinePlayer otherPlayer) {
        if(!otherPlayer.isOnline()) {
            sendPrivateMessage(player, "&cИгрок должен быть в сети.");
            return;
        }
        try {
            String text = "";
            for(Roll roll: SaveRoll.getInstance().getRollManager().getRolls().values()) {
                text += getTextDescFromRoll(roll, otherPlayer.getPlayer()) + "\n";
            }
            sendPrivateMessage(player, text);
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(player, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Subcommand("get")
    @Description("получить свой ролл или ролл игрока")
    @CommandPermission("saveroll.get")
    @CommandCompletion("@rollList @players")
    public static void getRoll(Player player, String rollName, OfflinePlayer otherPlayer) {
        if(!otherPlayer.isOnline() && otherPlayer.getPlayer() != null) {
            sendPrivateMessage(player, "&cИгрок должен бть в сети.");
            return;
        }
        try {
            if(!rollManager.hasRoll(rollName)) {
                sendPrivateMessage(player,"&cТакого типа ролла не существует!");
                return;
            }
            Roll roll = rollManager.getRolls().get(rollName);
            sendPrivateMessage(player,getTextDescFromRoll(roll, otherPlayer.getPlayer()));
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(player, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
