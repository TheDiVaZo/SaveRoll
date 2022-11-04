package saveroll.saveroll.command;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import com.mysql.jdbc.log.Log;
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
                + "\n     &c/dice "+roll.getSystemName()+" -> : &5" + (rollManager.calculateRoll(roll,player) + Math.round(Math.random()*12)) + " из 12ти";
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
    public static void setRoll(Player player, String rollName, int roll, Player otherPlayer) {
        try {
            SaveRoll.getInstance().getDateBaseManager().setRollForPlayer(otherPlayer.getUniqueId(), rollName, String.valueOf(roll));
            sendPrivateMessage(player, "&eРолл &c" + rollName + "&e успешно установлен игроку " + otherPlayer.getName());
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
    public static void getAllRoll(Player player, Player otherPlayer) {
        try {
            String text = "";
            for(Roll roll: SaveRoll.getInstance().getRollManager().getRolls().values()) {
                text += getTextDescFromRoll(roll, otherPlayer) + "\n";
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
    public static void getRoll(Player player, String rollName, Player otherPlayer) {
        try {
            if(!rollManager.hasRoll(rollName)) {
                sendPrivateMessage(player,"&cТакого типа ролла не существует!");
                return;
            }
            Roll roll = rollManager.getRolls().get(rollName);
            sendPrivateMessage(player,getTextDescFromRoll(roll, otherPlayer));
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(player, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
