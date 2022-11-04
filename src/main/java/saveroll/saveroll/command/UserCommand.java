package saveroll.saveroll.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import saveroll.logging.Logger;
import saveroll.saveroll.SaveRoll;
import saveroll.saveroll.chat.ChatManager;
import saveroll.saveroll.config.ConfigManager;
import saveroll.saveroll.roll.Roll;
import saveroll.saveroll.roll.RollManager;

import java.util.regex.Pattern;

@CommandAlias("dice")
public class UserCommand extends BaseCommand {
    private static SaveRoll saveRoll = SaveRoll.getInstance();
    private static ConfigManager configManager = saveRoll.getConfigManager();
    private static RollManager rollManager = saveRoll.getRollManager();

    public static final Pattern ROLL_NAME_REGEX = Pattern.compile(Pattern.quote("[roll-name]"));
    public static final Pattern ROLL_NAME_SYSTEM_REGEX = Pattern.compile(Pattern.quote("[roll-name-system]"));
    public static final Pattern ROLL_COUNT_REGEX = Pattern.compile(Pattern.quote("[roll-count]"));
    public static final Pattern ROLL_COUNT_RANDOM_REGEX = Pattern.compile(Pattern.quote("[roll-count-random]"));

    public static String replaceTextRoll(Player player, Roll roll) {
        return configManager.getTextFromDiceCommand()
                .replaceAll(ROLL_NAME_REGEX.pattern(), roll.getName())
                .replaceAll(ROLL_NAME_SYSTEM_REGEX.pattern(), roll.getSystemName())
                .replaceAll(ROLL_COUNT_REGEX.pattern(), String.valueOf(rollManager.calculateRoll(roll, player)))
                .replaceAll(ROLL_COUNT_RANDOM_REGEX.pattern(), String.valueOf(rollManager.calculateRoll(roll, player) + Math.round(Math.random() * 12)));

    }

    @Default
    @Description("Кидать ролл")
    @CommandCompletion("@rollList")
    public static void onDice(Player player, String rollName) {
        try {


            if (!rollManager.hasRoll(rollName)) {
                ChatManager.sendPrivateMessage(player, "&cТакого типа ролла не существует!");
                return;
            }
            Roll roll = rollManager.getRolls().get(rollName);
            ChatManager.sendChatMessage(player, PlaceholderAPI.setPlaceholders(player, replaceTextRoll(player, roll)), configManager.getDistanceVisibleRoll());
        } catch (Exception e) {
            ChatManager.sendPrivateMessage(player, "&cОшибка при выполнении команды: &e"+e.getMessage());
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
