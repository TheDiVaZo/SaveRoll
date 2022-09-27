package saveroll.saveroll.config.params;

import org.bukkit.entity.Player;

public class CommandParam extends CalcParam{
    @Override
    public float calcValue(Player player) {
        return 0;
    }

    static public String getName() {
        return "COMMAND";
    }
}
