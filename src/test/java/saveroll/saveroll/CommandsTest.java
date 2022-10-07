package saveroll.saveroll;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import saveroll.saveroll.datebase.SQLManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {

    public static SQLManager sqlManager = new SQLManager();
    static {
        sqlManager.setUrl("jdbc:mysql://vh368.timeweb.ru/cl11741_testplug");
        sqlManager.setRootNickname("cl11741_testplug");
        sqlManager.setPassword("7vx4KCBz");
    }

    @Test
    void onCommand() throws SQLException {
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName()).thenReturn("AllahMallah");

        OfflinePlayer otherPlayer = Mockito.mock(Player.class);
        Mockito.when(otherPlayer.getName()).thenReturn("AhmedBD");

        sqlManager.addUser(otherPlayer.getName());
        sqlManager.setRoll(otherPlayer.getName(), "attack", 3);
    }
}