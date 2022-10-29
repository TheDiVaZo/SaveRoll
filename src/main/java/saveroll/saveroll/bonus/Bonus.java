package saveroll.saveroll.bonus;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Bonus {
    protected final String name;

    protected interface ConfigParam {}

    protected Bonus(String name) {
        this.name = name;
    }

    public abstract int calculateRoll(Player player);



    public String getName() {
        return name;
    }
}
