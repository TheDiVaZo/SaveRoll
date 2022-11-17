package saveroll.saveroll.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatManager {

    public static void sendChatMessage(Player player, String message, double distance) {
        Bukkit.getOnlinePlayers().stream().filter(otherPlayer->otherPlayer.getWorld().equals(player.getWorld())).filter(otherPlayer -> otherPlayer.getLocation().distance(player.getLocation()) < distance).forEach(p->p.sendMessage(message));
    }

    public static void sendPrivateMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
    }
}
