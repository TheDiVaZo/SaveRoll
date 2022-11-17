package saveroll.saveroll.command;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import saveroll.errors.NotExistMaterialException;
import saveroll.errors.NotMatchPatternException;
import saveroll.logging.Logger;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.chat.ChatManager;
import saveroll.saveroll.datebase.DateBaseManager;
import saveroll.saveroll.roll.Roll;
import saveroll.saveroll.roll.RollManager;

import java.util.Map;
import java.util.Objects;

import static saveroll.saveroll.chat.ChatManager.sendPrivateMessage;

@CommandAlias("adice|admindice")
public class AdminCommand extends BaseCommand {
    private static SaveRoll saveRoll = SaveRoll.getInstance();
    private static RollManager rollManager = saveRoll.getRollManager();
    private static DateBaseManager dateBaseManager = saveRoll.getDateBaseManager();

    private static String getTextDescFromRoll(Map<String, Roll> rolls, OfflinePlayer otherPlayer) {
        StringBuilder text = new StringBuilder();
        for (Roll roll : rolls.values()) {
            text.append(getTextDescFromRoll(roll, otherPlayer)).append("\n");
        }
        return text.toString();
    }

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

    private static String getTextDescFromRoll(Roll roll, OfflinePlayer player) {
        if(Objects.nonNull(player.getPlayer())) return getTextDescFromRoll(roll, player.getPlayer());
        else return "&eТекущий ролл:"
                + "\n     &cКому рассчитан ролл: &5" + player.getName() + " &e(Offline)"
                + "\n     &cСистм. имя ролла: &5" + roll.getSystemName()
                + "\n     &cИгрвое имя ролла: &5" + roll.getName()
                + "\n     &cУстановленный плюс к роллу: &5" + dateBaseManager.getRollForPlayer(player.getUniqueId(), roll.getSystemName());
    }

    @Subcommand("reload")
    @Description("Перезагрузка конфига плагина")
    @CommandPermission("saveroll.reload")
    public static void onReload(CommandSender player) {
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
    @CommandCompletion("@rollList <number> *|@players")
    public static void setRoll(CommandSender commandSender, String rollName, int roll, @Optional @Single OfflinePlayer otherPlayer) {
        try {
            if(commandSender instanceof Player player) {
                OfflinePlayer targetPlayer = player;
                if(Objects.nonNull(otherPlayer)) targetPlayer = otherPlayer;
                SaveRoll.getInstance().getDateBaseManager().setRollForPlayer(targetPlayer.getUniqueId(), rollName, String.valueOf(roll));
                sendPrivateMessage(player, "&eБонус к роллу &c" + rollName + "&e успешно установлен игроку &c" + targetPlayer.getName());
                if(targetPlayer.isOnline() && targetPlayer.getPlayer() != null) {
                    sendPrivateMessage(targetPlayer.getPlayer(), "&eИгрок &c"+player.getName()+" &eустановил вам бонус к роллу &c" + rollName + "&e. Теперь у вас &c+"+roll+"&e" );
                }
            }
            else {
                if(Objects.isNull(otherPlayer)) {
                    sendPrivateMessage(commandSender, "&cНе указано имя игрока");
                    return;
                }
                SaveRoll.getInstance().getDateBaseManager().setRollForPlayer(otherPlayer.getUniqueId(), rollName, String.valueOf(roll));
                sendPrivateMessage(commandSender, "&eБонус к роллу &c" + rollName + "&e успешно установлен игроку &c" + otherPlayer.getName());
                if(otherPlayer.isOnline() && otherPlayer.getPlayer() != null) {
                    sendPrivateMessage(otherPlayer.getPlayer(), "&eИгрок &c"+commandSender.getName()+" &eустановил вам бонус к роллу &c" + rollName + "&e. Теперь у вас &c+"+roll+"&e" );
                }
            }
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(commandSender, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Subcommand("getAll")
    @Description("получить все роллы")
    @CommandPermission("saveroll.getall")
    @CommandCompletion("*|@players")
    public static void getAllRoll(CommandSender commandSender, @Optional @Single OfflinePlayer otherPlayer) {
        try {
            if(commandSender instanceof Player player) {
                OfflinePlayer targetPlayer = player;
                if(Objects.nonNull(otherPlayer)) targetPlayer = otherPlayer;

                sendPrivateMessage(player,getTextDescFromRoll(rollManager.getRolls(), targetPlayer));
            }
            else {
                if(Objects.isNull(otherPlayer)) {
                    sendPrivateMessage(commandSender, "&cНе указано имя игрока");
                    return;
                }
                sendPrivateMessage(commandSender,getTextDescFromRoll(rollManager.getRolls(), otherPlayer));
            }

        } catch (Exception e) {
            ChatManager.sendPrivateMessage(commandSender, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Subcommand("get")
    @Description("получить свой ролл или ролл игрока")
    @CommandPermission("saveroll.get")
    @CommandCompletion("@rollList *|@players")
    public static void getRoll(CommandSender commandSender, String rollName, @Optional @Single OfflinePlayer otherPlayer) {
        try {
            if(commandSender instanceof Player player) {
                OfflinePlayer targetPlayer = player;
                if(Objects.nonNull(otherPlayer)) targetPlayer = otherPlayer;
                if(!rollManager.hasRoll(rollName)) {
                    sendPrivateMessage(player,"&cТакого типа ролла не существует!");
                    return;
                }
                Roll roll = rollManager.getRolls().get(rollName);
                sendPrivateMessage(player,getTextDescFromRoll(roll, targetPlayer));
            }
            else {
                if(Objects.isNull(otherPlayer)) {
                    sendPrivateMessage(commandSender, "&cНе указано имя игрока");
                    return;
                }
                Roll roll = rollManager.getRolls().get(rollName);
                sendPrivateMessage(commandSender,getTextDescFromRoll(roll, otherPlayer));

            }

        } catch (Exception e) {
            ChatManager.sendPrivateMessage(commandSender, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
