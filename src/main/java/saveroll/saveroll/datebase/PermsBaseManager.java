package saveroll.saveroll.datebase;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.Flag;
import org.bukkit.Bukkit;
import saveroll.logging.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PermsBaseManager extends DateBaseManager {

    private final String BONUS_NAME_FORMAT = "[bonusName]";
    private final String BONUS_ROLL_FORMAT = "[bonusRoll]";
    private final String PERMISSION_FORMAT = "rollplus.bonus."+BONUS_NAME_FORMAT+"."+BONUS_ROLL_FORMAT;
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("rollplus\\.bonus\\.([a-zA-Z0-9]+)\\.([0-9]+)");
    LuckPerms api = LuckPermsProvider.get();

    private String insertPermissionData(String bonusName, String bonusRoll) {
        return PERMISSION_FORMAT.replace(BONUS_NAME_FORMAT, bonusName).replace(BONUS_ROLL_FORMAT, bonusRoll);
    }

    interface ParsedRoll {
        String getRollName();
        int getBonusRoll();
    }

    public static ParsedRoll parseRollFromPermission(String permissionRoll) {
        Matcher matcher = PERMISSION_PATTERN.matcher(permissionRoll);
        if(matcher.find()) {
            try {
                String group = matcher.group();
                int roll = Integer.parseInt(matcher.group());
                return new ParsedRoll() {
                    @Override
                    public String getRollName() {
                        return group;
                    }

                    @Override
                    public int getBonusRoll() {
                        return roll;
                    }
                };
            } catch (NumberFormatException | IllegalStateException e) {
                Logger.warn("При выдаче ролла произошла ошибка в парсинке группы. Не удалость получить ролл.");
            }
        }
        return null;
    }

    @Override
    public void setRollForPlayer(UUID player, String bonusName, String bonusRoll) {
        User user = api.getUserManager().getUser(player);
        String permission = insertPermissionData(bonusName, bonusRoll);
        if(user == null) {
            Logger.warn("Право "+permission+" небыло установлено игроку "+ Bukkit.getOfflinePlayer(player).getName() + " из за неизвестной ошибки");
            return;
        }
        user.getNodes(NodeType.PERMISSION).stream().filter(node->PERMISSION_PATTERN.matcher(node.getPermission()).matches()).toList().forEach(node->user.data().remove(node));
        user.data().add(PermissionNode.builder(insertPermissionData(bonusName, bonusRoll)).build());
        api.getUserManager().saveUser(user);

    }

    public static ParsedRoll getMaxLvlRollFromCollection(Collection<PermissionNode> permissionRollCollection) {
        ArrayList<ParsedRoll> parsedRolls = new ArrayList<>();
        for (PermissionNode permissionNode : permissionRollCollection) {
            ParsedRoll parsedRoll = parseRollFromPermission(permissionNode.getPermission());
            if(parsedRoll != null) parsedRolls.add(parsedRoll);
        }
        parsedRolls.sort(Comparator.comparingInt(ParsedRoll::getBonusRoll).reversed());
        if(!parsedRolls.isEmpty() && parsedRolls.get(0) != null) {
            return parsedRolls.get(0);
        }

        return null;
    }

    @Override
    public int getRollForPlayer(UUID player, String bonusName) {
        User user = api.getUserManager().getUser(player);
        if(user == null) {
            Logger.warn("Игрок не найден");
            return 0;
        }
        for (Group inheritedGroup : user.getInheritedGroups(user.getQueryOptions())) {
            Set<PermissionNode> collection = inheritedGroup.getNodes(NodeType.PERMISSION).stream().filter(node -> node.getPermission().matches(insertPermissionData(bonusName, "([0-9]+)"))).collect(Collectors.toSet());
            ParsedRoll parsedRoll = getMaxLvlRollFromCollection(collection);
            if(parsedRoll != null) return parsedRoll.getBonusRoll();
        }
        ParsedRoll parsedRoll = getMaxLvlRollFromCollection(user.getNodes(NodeType.PERMISSION));
        if(parsedRoll != null) return parsedRoll.getBonusRoll();
        return 0;
    }
}
